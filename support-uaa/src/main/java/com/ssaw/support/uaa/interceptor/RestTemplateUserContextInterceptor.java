package com.ssaw.support.uaa.interceptor;

import com.ssaw.support.uaa.holder.UserContextHolder;
import com.ssaw.support.uaa.model.User;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author HuSen
 * @date 2019/4/27 15:11
 */
public class RestTemplateUserContextInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        User user = UserContextHolder.currentUser();
        request.getHeaders().add("x-user-id", user.getId().toString());
        request.getHeaders().add("x-user-name", user.getUsername());
        request.getHeaders().add("x-user-serviceName", request.getURI().getHost());
        return execution.execute(request, body);
    }
}