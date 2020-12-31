package top.tjsanshao.bilibili.action;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;

import javax.annotation.Resource;

/**
 * manga check in
 *
 * @author TjSanshao
 * @date 2020-12-30 14:15
 */
@Slf4j
@Component
public class MangaCheckIn implements Action {
    @Resource
    private BilibiliRequestClient client;

    @Override
    public void act() {
        if (!CurrentUser.mangaCheckIn) {
            log.warn("漫画打卡功能为开启！");
        }
        String requestBody = "platform=ios";
        JsonObject response = client.post(APIList.MANAGE, requestBody);
        if (response.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
            log.info("漫画签到成功啦！注意签到的是IOS平台噢！");
        } else {
            log.warn("漫画签到竟然失败了...居然是因为{}", response.get(BilibiliResponseConstant.MESSAGE).getAsString());
        }
    }
}
