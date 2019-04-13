package com.ssaw.commons.util.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by HuSen on 2018/8/27 15:11.
 */
@Slf4j
@SuppressWarnings("all")
public final class HttpConnectionUtils {
    private static final int REQ_TIME_OUT = 30000;
    private static final int CON_TIME_OUT = 30000;
    private static final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(REQ_TIME_OUT).setConnectTimeout(CON_TIME_OUT).build();
    private static final String DEFAULT_CHARSET = "utf-8";
    private static final int DEFAULT_REPEAT_TIMES = 3;
    private static int repeatTime = 3;
    private static final String DEFAULT_STR_MARK = "Dds12312weqweq1412qwe1";

    private HttpConnectionUtils() {
    }

    public static String doGet(String httpUrl) throws IOException {
        return doGetUrlEncoding(httpUrl, (Map)null, true);
    }

    public static String doGetReturnHeaderCookies(String httpUrl, Map<String, String> parameters, boolean isRepeat) throws IOException {
        return doGetHttpRequest(httpUrl, parameters, isRepeat, true, new Header[0]);
    }

    public static String doGetUrlEncoding(String httpUrl, Map<String, String> parameters, boolean isRepeat) throws IOException {
        return doGetHttpRequest(httpUrl, parameters, isRepeat, false, new Header[0]);
    }

    public static String doGet(String httpUrl, Map<String, String> parameters, boolean isRepeat, Header... headers) throws IOException {
        return doGetHttpRequest(httpUrl, parameters, isRepeat, false, headers);
    }

    public static String doPost(String httpUrl, Map<String, String> parameters, boolean isRepeat) throws IOException {
        return doPost((CloseableHttpClient)null, httpUrl, parameters, isRepeat, new Header[0]);
    }

    public static String doPost(String httpUrl, String requestBody, Boolean isRepeat, Header... header) throws IOException {
        String returnDatas = doPostByRequestBody((CloseableHttpClient)null, httpUrl, requestBody, isRepeat, ContentType.APPLICATION_JSON, header);
        if(StringUtils.isBlank(returnDatas) && StringUtils.isBlank(returnDatas) && isRepeat.booleanValue()) {
            AtomicInteger integer = new AtomicInteger(0);

            while(repeatTime != integer.get()) {
                if (log.isDebugEnabled()) {
                    log.debug("正在重试第[{}]次", integer.incrementAndGet());
                }
                returnDatas = doPostByRequestBody((CloseableHttpClient)null, httpUrl, requestBody, isRepeat, ContentType.APPLICATION_JSON, header);
                if(StringUtils.equals(returnDatas, "Dds12312weqweq1412qwe1") || StringUtils.isNotBlank(returnDatas)) {
                    break;
                }
            }
        }

        return StringUtils.equals(returnDatas, "Dds12312weqweq1412qwe1")?null:returnDatas;
    }

    public static String doPost(String httpUrl, String requestBody, Boolean isRepeat, ContentType contentType, Header[] header) throws IOException {
        String returnDatas = doPostByRequestBody((CloseableHttpClient)null, httpUrl, requestBody, isRepeat, contentType, header);
        if(StringUtils.isBlank(returnDatas) && StringUtils.isBlank(returnDatas) && isRepeat.booleanValue()) {
            AtomicInteger integer = new AtomicInteger(0);

            while(repeatTime != integer.get()) {
                if (log.isDebugEnabled()) {
                    log.debug("正在重试第[{}]次", integer.incrementAndGet());
                }
                returnDatas = doPostByRequestBody((CloseableHttpClient)null, httpUrl, requestBody, isRepeat, contentType, header);
                if(StringUtils.equals(returnDatas, "Dds12312weqweq1412qwe1") || StringUtils.isNotBlank(returnDatas)) {
                    break;
                }
            }
        }

        return StringUtils.equals(returnDatas, "Dds12312weqweq1412qwe1")?null:returnDatas;
    }

    private static String doPostByRequestBody(CloseableHttpClient httpClient, String httpUrl, String requestBody, Boolean isRepeat, ContentType contentType, Header... header) throws IOException {
        if(httpClient == null) {
            httpClient = HttpClients.custom().build();
        }

        HttpPost httpPost = new HttpPost(httpUrl);
        httpPost.setConfig(requestConfig);
        if(header != null && header.length > 0) {
            httpPost.setHeaders(header);
        }

        httpPost.setEntity(new StringEntity(requestBody, contentType));
        CloseableHttpResponse response = null;
        String returnData = null;
        HttpEntity respEntity = null;

        try {
            response = httpClient.execute(httpPost);
            respEntity = response.getEntity();
            if(respEntity != null) {
                returnData = EntityUtils.toString(respEntity, "utf-8");
                if (log.isDebugEnabled()) {
                    log.info("HTTP 请求 Response == > {}", returnData);
                }
                if(response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {
                    if(StringUtils.isBlank(returnData)) {
                        returnData = "Dds12312weqweq1412qwe1";
                    }
                } else {
                    returnData = null;
                }
            }

            EntityUtils.consume(respEntity);
        } finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }

        return returnData;
    }

    public static String doPost(String httpUrl, Map<String, String> parameters, Header... header) throws IOException {
        return doPost((CloseableHttpClient)null, httpUrl, parameters, true, header);
    }

    public static String doPost(CloseableHttpClient httpClient, String httpUrl, Map<String, String> parameters, Header... header) throws IOException {
        return doPost(httpClient, httpUrl, parameters, false, header);
    }

    private static String doPost(CloseableHttpClient httpClient, String httpUrl, Map<String, String> parameters, boolean isRepeat, Header... header) throws IOException {
        String returnDatas = null;
        returnDatas = doPostHttpRequet(httpClient, httpUrl, parameters, header);
        if(StringUtils.isBlank(returnDatas) && isRepeat) {
            AtomicInteger integer = new AtomicInteger(0);

            while(repeatTime != integer.get()) {
                if (log.isDebugEnabled()) {
                    log.debug("正在重试第[{}]次", integer.incrementAndGet());
                }
                returnDatas = doPostHttpRequet(httpClient, httpUrl, parameters, header);
                if(StringUtils.equals(returnDatas, "Dds12312weqweq1412qwe1") || StringUtils.isNotBlank(returnDatas)) {
                    break;
                }
            }
        }

        return StringUtils.equals(returnDatas, "Dds12312weqweq1412qwe1")?null:returnDatas;
    }

    private static String doPostHttpRequet(CloseableHttpClient httpclient, String httpUrl, Map<String, String> parameters, Header... header) throws IOException {
        if(httpclient == null) {
            httpclient = HttpClients.custom().build();
        }

        HttpPost httpPost = new HttpPost(httpUrl);
        httpPost.setConfig(requestConfig);
        List<NameValuePair> valuePairs = new LinkedList();
        String returnData = null;
        if(MapUtils.isNotEmpty(parameters)) {
            Iterator var7 = parameters.entrySet().iterator();

            while(var7.hasNext()) {
                Entry<String, String> entry = (Entry)var7.next();
                valuePairs.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
            }
        }

        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(valuePairs, "utf-8");
        if(header != null && header.length > 0) {
            httpPost.setHeaders(header);
        }

        httpPost.setEntity(urlEncodedFormEntity);
        CloseableHttpResponse response = httpclient.execute(httpPost);

        try {
            HttpEntity respEntity = response.getEntity();
            if(respEntity != null) {
                returnData = EntityUtils.toString(respEntity, "utf-8");
                if (log.isDebugEnabled()) {
                    log.info("HTTP 请求 Response == > {}", returnData);
                }
                if(response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {
                    if(StringUtils.isBlank(returnData)) {
                        returnData = "Dds12312weqweq1412qwe1";
                    }
                } else {
                    returnData = null;
                }
            }

            EntityUtils.consume(respEntity);
        } finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpclient);
        }

        return returnData;
    }

    public static String multipartFormPost(String httpUrl, Map<String, String> parameters, InputStream fileInputStream, String filename, String boundary, List<Header> headers) {
        HttpPost httpPost = new HttpPost(httpUrl);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        if(MapUtils.isNotEmpty(parameters)) {
            Iterator var9 = parameters.entrySet().iterator();

            while(var9.hasNext()) {
                Entry<String, String> entry = (Entry)var9.next();
                reqEntity.addTextBody((String)entry.getKey(), (String)entry.getValue(), ContentType.create("text/plain", "utf-8"));
            }
        }

        reqEntity.setBoundary(boundary);
        HttpEntity httpEntity = reqEntity.addBinaryBody("file", fileInputStream, ContentType.create("multipart/form-data", "utf-8"), filename).build();
        if(headers != null && headers.size() > 0) {
            httpPost.setHeaders((Header[])headers.toArray(new Header[0]));
        }

        httpPost.setEntity(httpEntity);
        CloseableHttpResponse response = null;
        HttpEntity respHttpEntity = null;
        String returnUrl = null;

        try {
            response = closeableHttpClient.execute(httpPost);
            respHttpEntity = response.getEntity();
            if(respHttpEntity != null) {
                returnUrl = EntityUtils.toString(respHttpEntity);
            }

            EntityUtils.consume(respHttpEntity);
        } catch (ClientProtocolException var19) {
            var19.printStackTrace();
        } catch (ParseException var20) {
            var20.printStackTrace();
        } catch (IOException var21) {
            var21.printStackTrace();
        } finally {
            EntityUtils.consumeQuietly(respHttpEntity);
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(closeableHttpClient);
        }

        return returnUrl;
    }

    public static String multipartFormPost(String httpUrl, Map<String, String> parameters, InputStream fileInputStream, String fileInputName, String boundary, String filename, String charset, List<Header> headers) {
        HttpPost httpPost = new HttpPost(httpUrl);
        if(StringUtils.isBlank(charset)) {
            charset = "utf-8";
        }

        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        if(MapUtils.isNotEmpty(parameters)) {
            Iterator var11 = parameters.entrySet().iterator();

            while(var11.hasNext()) {
                Entry<String, String> entry = (Entry)var11.next();
                reqEntity.addTextBody((String)entry.getKey(), (String)entry.getValue(), ContentType.create("text/plain", charset));
            }
        }

        if(StringUtils.isNotBlank(boundary)) {
            reqEntity.setBoundary(boundary);
        }

        HttpEntity httpEntity = reqEntity.addBinaryBody(fileInputName, fileInputStream, ContentType.create("multipart/form-data", charset), filename).build();
        if(headers != null && headers.size() > 0) {
            httpPost.setHeaders((Header[])headers.toArray(new Header[0]));
        }

        httpPost.setEntity(httpEntity);
        CloseableHttpResponse response = null;
        HttpEntity respHttpEntity = null;
        String returnUrl = null;

        try {
            response = closeableHttpClient.execute(httpPost);
            respHttpEntity = response.getEntity();
            if(respHttpEntity != null) {
                returnUrl = EntityUtils.toString(respHttpEntity);
            }

            EntityUtils.consume(respHttpEntity);
        } catch (ClientProtocolException var21) {
            var21.printStackTrace();
        } catch (ParseException var22) {
            var22.printStackTrace();
        } catch (IOException var23) {
            var23.printStackTrace();
        } finally {
            EntityUtils.consumeQuietly(respHttpEntity);
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(closeableHttpClient);
        }

        return returnUrl;
    }

    private static String doGetHttpRequest(String httpUrl, Map<String, String> parameters, boolean isRepeat, boolean isReturnCookies, Header... headers) throws IOException {
        String appendParametersStr = buildQuery(parameters);
        if(StringUtils.isNotBlank(appendParametersStr)) {
            httpUrl = httpUrl + "?" + appendParametersStr;
        }

        String returnDatas = null;
        returnDatas = httpGetConnection(httpUrl, true, headers);
        if(StringUtils.isBlank(returnDatas) && isRepeat) {
            AtomicInteger integer = new AtomicInteger(0);

            while(repeatTime != integer.get()) {
                if (log.isDebugEnabled()) {
                    log.info("正在重试第[{}]次", integer.incrementAndGet());
                }
                returnDatas = httpGetConnection(httpUrl, true, headers);
                if(StringUtils.equals(returnDatas, "Dds12312weqweq1412qwe1") || StringUtils.isNotBlank(returnDatas)) {
                    break;
                }
            }
        }

        return StringUtils.equals(returnDatas, "Dds12312weqweq1412qwe1")?null:returnDatas;
    }

    public static CloseableHttpClient getHttpClient(String httpUrl, Map<String, String> parameters) throws ClientProtocolException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String appendParametersStr = buildQuery(parameters);
        if(StringUtils.isNotBlank(appendParametersStr)) {
            httpUrl = httpUrl + "?" + appendParametersStr;
        }

        HttpGet httpGet = new HttpGet(httpUrl);
        httpClient.execute(httpGet);
        return httpClient;
    }

    private static String httpGetConnection(String httpUrl, boolean isReturnCookies, Header[] headers) throws IOException {
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet httpGet = new HttpGet(httpUrl);
        httpGet.setConfig(requestConfig);
        String returnData = null;
        HttpEntity respEntity = null;
        CloseableHttpResponse responsere = null;
        if(headers != null && headers.length > 0) {
            httpGet.setHeaders(headers);
        }

        try {
            responsere = httpclient.execute(httpGet);
            respEntity = responsere.getEntity();
            if(respEntity != null) {
                returnData = EntityUtils.toString(respEntity, "utf-8");
                if (log.isDebugEnabled()) {
                    log.info("HTTP 请求 Response == > {}", returnData);
                }
                if(responsere.getStatusLine().getStatusCode() >= 200 && responsere.getStatusLine().getStatusCode() < 300) {
                    if(StringUtils.isBlank(returnData)) {
                        returnData = "Dds12312weqweq1412qwe1";
                    }
                } else {
                    returnData = null;
                }
            }

            EntityUtils.consume(respEntity);
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(responsere);
            HttpClientUtils.closeQuietly(httpclient);
        }

        return returnData;
    }

    public static String buildQuery(Map<String, String> params) throws IOException {
        return buildQuery(params, "utf-8", true);
    }

    public static String buildQuery(Map<String, String> params, String charset, boolean isEncoding) throws IOException {
        if(MapUtils.isEmpty(params)) {
            return null;
        } else {
            StringBuilder query = new StringBuilder();
            Set<Entry<String, String>> entries = params.entrySet();
            boolean hasParam = false;
            Iterator var6 = entries.iterator();

            while(var6.hasNext()) {
                Entry<String, String> entry = (Entry)var6.next();
                String name = (String)entry.getKey();
                String value = (String)entry.getValue();
                if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value)) {
                    if(hasParam) {
                        query.append("&");
                    } else {
                        hasParam = true;
                    }

                    query.append(name).append("=").append(isEncoding?URLEncoder.encode(value, charset):value);
                }
            }

            return query.toString();
        }
    }
}