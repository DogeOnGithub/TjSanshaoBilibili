package top.tjsanshao.bilibili.auto;

import cn.hutool.core.date.DateUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * notice
 *
 * @author TjSanshao
 * @date 2021-01-07 10:25
 */
@Slf4j
@Component
public class Notice {
    @Value("${wechat.pushSecret}")
    private String secret;

    private static String pushUrl;

    @Resource
    private BilibiliRequestClient client;

    @PostConstruct
    public void init() {
        Notice.pushUrl = APIList.SERVER_PUSH + secret + ".send";
    }

    public void send(String title, String content) {
        String requestBody = String.format("text=%s&desp=%s", title, content);
        JsonObject response = client.post(Notice.pushUrl, requestBody);
        log.info("push to wechat...{}", response.toString());
    }

    public void send() {
        String title = String.format("%s Bilibili Auto Task Result", DateUtil.today());
        String content = new Gson().toJson(CurrentUser.actionResult);
        this.send(title, content);
    }
}
