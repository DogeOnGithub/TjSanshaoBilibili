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
        StringBuilder sb = new StringBuilder();
        sb.append("当前剩余硬币：").append(CurrentUser.userInfo.getMoney()).append(System.lineSeparator());
        sb.append("当前等级：").append(CurrentUser.userInfo.getLevel_info().getCurrent_level()).append(System.lineSeparator());
        sb.append("当前经验：").append(CurrentUser.userInfo.getLevel_info().getCurrent_exp()).append(System.lineSeparator());
        sb.append("距离6级大佬还差经验：").append(CurrentUser.userInfo.getLevel_info().getNext_exp_asInt() - CurrentUser.userInfo.getLevel_info().getCurrent_exp()).append(System.lineSeparator());
        CurrentUser.actionResult.forEach((n, a) -> {
            sb.append(n).append("--").append(a.getAction()).append("--").append(a.getActionResultCode()).append("--").append(a.getActionResultMessage()).append("--").append(a.getActionFinishedTime());
            sb.append(System.lineSeparator());
        });
        this.send(title, sb.toString());
    }
}
