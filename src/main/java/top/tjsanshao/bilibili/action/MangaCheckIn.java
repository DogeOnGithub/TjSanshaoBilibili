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
    public String actionName() {
        return "漫画打卡";
    }

    @Override
    public String resultKey() {
        return "MangaCheckIn";
    }

    @Override
    public void act() {
        ActionResult ar = new ActionResult();
        ar.setAction(this.actionName());

        if (!CurrentUser.mangaCheckIn) {
            log.warn("漫画打卡功能未开启！");
            ar.setActionResultCode(0);
            ar.setActionResultMessage("没开漫画打卡...");
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
            CurrentUser.actionResult.put(this.resultKey(), ar);
            return;
        }
        String requestBody = "platform=ios";
        JsonObject response = client.post(APIList.MANAGE, requestBody);
        if (response.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
            log.info("漫画签到成功啦！注意签到的是IOS平台噢！");
            ar.setActionResultCode(0);
            ar.setBilibiliCode(0);
            ar.setActionResultMessage("漫画签到成功啦！注意签到的是IOS平台噢！");
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
        } else {
            log.warn("漫画签到竟然失败了...居然是因为{}", response.get(BilibiliResponseConstant.MESSAGE).getAsString());
            int code = response.get(BilibiliResponseConstant.CODE).getAsInt();
            ar.setActionResultCode(code);
            ar.setBilibiliCode(code);
            String arMsg = String.format("漫画不让打卡...竟然是因为%s", response.get(BilibiliResponseConstant.MESSAGE).getAsString());
            ar.setActionResultMessage(arMsg);
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
        }
        CurrentUser.actionResult.put(this.resultKey(), ar);
    }
}
