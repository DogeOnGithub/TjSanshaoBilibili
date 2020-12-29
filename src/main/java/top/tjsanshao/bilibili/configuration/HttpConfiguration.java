package top.tjsanshao.bilibili.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * http client configuration
 *
 * @author TjSanshao
 * @date 2020-12-29 15:04
 */
@Data
@Configuration("HttpConfiguration")
@ConfigurationProperties(prefix = "http.client.config")
public class HttpConfiguration {
    private int connectTimeout;
    private int connectionRequestTimeout;
    private int socketTimeout;
}
