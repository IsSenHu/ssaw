package com.ssaw.gateway.http.util;

import com.alibaba.fastjson.JSON;
import com.ssaw.gateway.http.entity.Resp;
import com.ssaw.gateway.http.handler.HttpStaticHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * @author HuSen
 * create on 2019/7/1 15:20
 */
@Slf4j
public class HttpUtils {

    private final static String INDEX = "/index";

    private final static String SPLIT = "/";

    private static String path;

    @SuppressWarnings("unused")
    private enum MimeType {
        //
        HTML("text/html;charset=utf-8"),
        JSON("application/json;charset=utf-8"),
        CSS("text/css"),
        JS("application/javascript"),
        PNG("image/png"),
        GIF("image/gif"),
        ICON("text/html"),
        JPEG("image/jpeg"),
        JPG("image/jpeg");

        private String value;

        MimeType(String value) {
            this.value = value;
        }
    }

    static
    {
        try {
            path = HttpStaticHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
            path = !path.contains("file:") ? path : path.substring(5);
            log.info("locate resource path: {}", path);
            log.info("now support mime type: {}", Arrays.toString(MimeType.values()));
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate resource path:", e);
        }
    }

    public static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    public static void load(FullHttpRequest request, ChannelHandlerContext ctx) {
        String uri = request.uri();
        File file;
        String reqPath = new QueryStringDecoder(uri).path();
        String type = StringUtils.substringAfter(reqPath, ".");
        boolean isIndex = StringUtils.equals(uri, SPLIT) || StringUtils.equals(uri, INDEX);
        if (StringUtils.isBlank(type) || isIndex) {
            file = new File(path + "index.html");
            type = MimeType.HTML.name();
        } else {
            file = new File(path + uri);
        }
        try {
            MimeType mimeType = MimeType.valueOf(type.toUpperCase());
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeType.value);
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
            returnStatus("this resource not found", HttpResponseStatus.NOT_FOUND, request, ctx);
        } catch (IOException e) {
            log.error("IOException: ", e);
            returnStatus("service error", HttpResponseStatus.INTERNAL_SERVER_ERROR, request, ctx);
        }
    }

    public static void returnStatus(String msg, HttpResponseStatus notFound, FullHttpRequest request, ChannelHandlerContext ctx) {
        HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), notFound);
        Resp<Object> resp = Resp.of(notFound.code(), msg, System.currentTimeMillis(), null);
        String json = JSON.toJSONString(resp, true);
        int length = json.length();
        response.headers().add(HttpHeaderNames.CONTENT_LENGTH, length);
        response.headers().add(HttpHeaderNames.CONTENT_TYPE, MimeType.JSON.value);
        ctx.write(response);
        HttpContent content = new DefaultHttpContent(ctx.alloc().buffer(length * 3).writeBytes(json.getBytes(CharsetUtil.UTF_8)));
        ctx.write(content);
        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    }
}
