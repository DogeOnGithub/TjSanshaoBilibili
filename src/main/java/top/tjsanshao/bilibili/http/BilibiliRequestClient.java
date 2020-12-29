package top.tjsanshao.bilibili.http;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.configuration.HttpConfiguration;
import top.tjsanshao.bilibili.login.PassCheck;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

/**
 * bilibili request client
 *
 * @author TjSanshao
 * @date 2020-12-29 15:06
 */
@Slf4j
@Component
public class BilibiliRequestClient {
    @Resource(name = "HttpConfiguration")
    private HttpConfiguration configuration;
    @Resource
    private PassCheck passCheck;

    @Setter
    @Getter
    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";

    private static RequestConfig config;

    @PostConstruct
    public void init() {
        config = RequestConfig.custom()
                .setConnectTimeout(configuration.getConnectTimeout())
                .setConnectionRequestTimeout(configuration.getConnectionRequestTimeout())
                .setSocketTimeout(configuration.getSocketTimeout()).build();
    }

    public JsonObject post(String url, String body) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        if (body.startsWith("{")) {
            httpPost.setHeader("Content-Type", "application/json");
        } else {
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        httpPost.setHeader("Referer", "https://www.bilibili.com/");
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("User-Agent", userAgent);
        httpPost.setHeader("Cookie", passCheck.getPass());
        StringEntity stringEntity = new StringEntity(body, "utf-8");
        httpPost.setEntity(stringEntity);
        return executeRequest(httpPost);
    }

    public JsonObject get(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Referer", "https://www.bilibili.com/");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("User-Agent", userAgent);
        httpGet.setHeader("Cookie", passCheck.getPass());
        httpGet.setConfig(config);
        return executeRequest(httpGet);
    }

    private static JsonObject executeRequest(HttpRequestBase request) {
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        JsonObject result = null;
        try {
            response = client.execute(request);
            if (Objects.nonNull(response)) {
                result = parseResponse(response);
            }
        } catch (Exception e) {
            log.error("request bilibili error!", e);
        } finally {
            responseClose(response, client);
        }
        return result;
    }

    private static JsonObject parseResponse(CloseableHttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            String resString = EntityUtils.toString(entity);
            return JsonParser.parseString(resString).getAsJsonObject();
        } else {
            log.error("request bilibili error! statuCode=[{}]", statusCode);
            return null;
        }
    }

    private static void responseClose(CloseableHttpResponse response, CloseableHttpClient client) {
        if (Objects.nonNull(response)) {
            try {
                response.close();
            } catch (IOException e) {
                log.error("response close error!", e);
            }
        }
        if (Objects.nonNull(client)) {
            try {
                client.close();
            } catch (IOException e) {
                log.error("http client close error!", e);
            }
        }
    }
}
