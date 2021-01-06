package top.tjsanshao.bilibili.action;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.current.ActionResult;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;
import top.tjsanshao.bilibili.util.TjSanshaoDateUtil;

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
        ActionResult ar = new ActionResult();
        ar.setAction("直播打卡");

        if (!CurrentUser.liveCheckIn) {
            log.warn("直播打卡功能未开启！");
            ar.setActionResultCode(0);
            ar.setActionResultMessage("没开直播打卡...");
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
            CurrentUser.actionResult.put("LiveCheckIn", ar);
            return;
        }
        JsonObject response = client.get(APIList.LIVE_CHECK_IN);
        if (response.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
            JsonObject data = response.getAsJsonObject(BilibiliResponseConstant.DATA);
            String text = data.get(BilibiliResponseConstant.TEXT).getAsString();
            String specialText = data.get(BilibiliResponseConstant.SPECIAL_TEXT).getAsString();
            log.info("直播打卡成功啦！这次拿到了{}，{}", text, specialText);
            ar.setActionResultCode(0);
            ar.setBilibiliCode(0);
            String arMsg = String.format("赶上打卡，领到了：%s和%s", text, specialText);
            ar.setActionResultMessage(arMsg);
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
        } else {
            log.warn("直播打卡失败了...居然是因为{}", response.get(BilibiliResponseConstant.MESSAGE).getAsString());
            int code = response.get(BilibiliResponseConstant.CODE).getAsInt();
            ar.setActionResultCode(code);
            ar.setBilibiliCode(code);
            String arMsg = String.format("直播不让打卡，因为%s", response.get(BilibiliResponseConstant.MESSAGE).getAsString());
            ar.setActionResultMessage(arMsg);
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
        }
        CurrentUser.actionResult.put("LiveCheckIn", ar);
    }
}
