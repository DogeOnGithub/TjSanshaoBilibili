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
 * live check in
 *
 * @author TjSanshao
 * @date 2020-12-30 14:51
 */
@Slf4j
@Component
public class LiveCheckIn implements Action {
    @Resource
    private BilibiliRequestClient client;

    @Override
    public void act() {
        if (!CurrentUser.liveCheckIn) {
            log.warn("直播打卡功能未开启！");
        }
        JsonObject response = client.get(APIList.LIVE_CHECK_IN);
        if (response.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
            JsonObject data = response.getAsJsonObject(BilibiliResponseConstant.DATA);
            String text = data.get(BilibiliResponseConstant.TEXT).getAsString();
            String specialText = data.get(BilibiliResponseConstant.SPECIAL_TEXT).getAsString();
            log.info("直播打卡成功啦！这次拿到了{}，{}", text, specialText);
        } else {
            log.warn("直播打卡失败了...居然是因为{}", response.get(BilibiliResponseConstant.MESSAGE).getAsString());
        }
    }
}
