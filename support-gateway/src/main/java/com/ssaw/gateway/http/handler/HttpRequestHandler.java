package com.ssaw.gateway.http.handler;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;

import static io.netty.handler.codec.http.HttpUtil.is100ContinueExpected;

/**
 * @author HuSen
 * create on 2019/6/28 13:53
 */
@Slf4j
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final static String INDEX = "/index";

    private final static String SPLIT = "/";

    private final static String CSS = "css";

    private final static String JS = "js";

    private static String path;

    static
    {
        try {
            path = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
            path = !path.contains("file:") ? path : path.substring(5);
            log.info("locate resource path: {}", path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate resource path:", e);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        // 处理100Continue请求以符合HTTP1.1规范
        if (is100ContinueExpected(request)) {
            send100Continue(ctx);
        }
        String uri = request.uri();
        File file;
        boolean isHtml = !StringUtils.contains(uri, CSS) && !StringUtils.contains(uri, JS);
        boolean isIndex = StringUtils.equals(uri, SPLIT) || StringUtils.startsWith(uri, INDEX);
        if (isIndex && isHtml) {
            file = new File(path + "index.html");
        } else {
            file = new File(path + uri);
        }
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=utf-8");
            boolean keepAlive = HttpUtil.isKeepAlive(request);
            if (keepAlive) {
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, raf.length());
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.write(response);
            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(raf.getChannel(), 0, file.length()));
            } else {
                ctx.write(new ChunkedNioFile(raf.getChannel()));
            }
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        } catch (FileNotFoundException e) {
            log.error("file {} is not exist", file);
        } catch (IOException e) {
            log.error("IOException: ", e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }
}
