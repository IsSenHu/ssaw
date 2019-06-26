package com.ssaw.jetty.servlet.impl;

import com.alibaba.fastjson.JSON;
import com.ssaw.jetty.servlet.abs.AbstractAsyncServlet;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2019/6/25 13:40
 */
public class TestServlet extends AbstractAsyncServlet {

    @Override
    protected void exec(AsyncContext asyncContext, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        Map<String, Object> result = new HashMap<>(2);
        result.put("code", 0);
        result.put("data", "你好! " + name);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(JSON.toJSONString(result));
    }
}
