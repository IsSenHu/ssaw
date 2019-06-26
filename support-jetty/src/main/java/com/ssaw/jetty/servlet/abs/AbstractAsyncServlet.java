package com.ssaw.jetty.servlet.abs;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author HuSen
 * create on 2019/6/25 12:54
 */
@Slf4j
public abstract class AbstractAsyncServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final AsyncContext asyncContext = req.startAsync();
        asyncContext.addListener(new AsyncListener() {
            /**
             * 这里处理正常结束的逻辑
             *
             * @param asyncEvent AsyncEvent
             */
            @Override
            public void onComplete(AsyncEvent asyncEvent) {
                log.info("请求完成了...");
            }

            /**
             * 这里处理超时的逻辑
             *
             * @param asyncEvent AsyncEvent
             */
            @Override
            public void onTimeout(AsyncEvent asyncEvent) {
                log.error("超时了:", asyncEvent.getThrowable());
            }

            /**
             * 这里处理出错的逻辑
             *
             * @param asyncEvent AsyncEvent
             */
            @Override
            public void onError(AsyncEvent asyncEvent) {
                log.error("出错了:", asyncEvent.getThrowable());
            }

            /**
             * 这里处理开始异步线程的逻辑
             *
             * @param asyncEvent AsyncEvent
             */
            @Override
            public void onStartAsync(AsyncEvent asyncEvent) {
                log.info("开始处理异步线程逻辑...");
            }
        });
        // 设置超时的时间，到了时间以后，会回调onTimeout的方法
        asyncContext.setTimeout(10000L);
        // 在这里启动，传入一个Runnable对象，服务器会把此Runnable对象放在线程池里面执行
        asyncContext.start(() -> {
            // 在这里做耗时的操作，如果做完，则调用complete方法通知回调，异步处理结束了
            try {
                exec(asyncContext, req, resp);
            } catch (IOException e) {
                log.error("", e);
            } finally {
                asyncContext.complete();
            }
        });
    }

    /**
     * 执行
     *
     * @param asyncContext AsyncContext
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException IOException
     */
    protected abstract void exec(AsyncContext asyncContext, HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
