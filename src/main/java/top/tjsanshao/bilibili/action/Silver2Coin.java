package top.tjsanshao.bilibili.action;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.current.ActionResult;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.current.Init;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;
import top.tjsanshao.bilibili.util.TjSanshaoDateUtil;

import javax.annotation.Resource;

/**
 * sliver to coin
 *
 * @author TjSanshao
 * @date 2020-12-30 14:30
 */
@Slf4j
@Component
public class Silver2Coin implements Action {
    @Resource
    private BilibiliRequestClient client;
    @Resource
    private Init init;

    @Override
    public void act() {
        ActionResult ar = new ActionResult();
        ar.setAction("银瓜子换硬币");

        if (!CurrentUser.silver2Coin) {
            log.warn("银瓜子换硬币功能未开启！");
            ar.setActionResultCode(0);
            ar.setActionResultMessage("没开银瓜子换硬币...");
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
            CurrentUser.actionResult.put("Silver2Coin", ar);
            return;
        }
        JsonObject response = client.get(APIList.SILVER_TO_COIN);
        int code = response.get(BilibiliResponseConstant.CODE).getAsInt();
        if (code == BilibiliResponseConstant.CODE_SUCCESS) {
            log.info("银瓜子换硬币成功了额...");

            JsonObject statusResponse = client.get(APIList.SILVER_TO_COIN_STATUS);
            int sliver = statusResponse.getAsJsonObject(BilibiliResponseConstant.DATA).get(BilibiliResponseConstant.SILVER).getAsInt();
            log.info("现在还剩 {} 个银瓜子了...", sliver);
            init.refresh();
            log.info("现在的硬币还剩 {} 个哦", CurrentUser.userInfo.getMoney());

            ar.setActionResultCode(0);
            ar.setBilibiliCode(0);
            String arMsg = String.format("现在还剩 %d 个银瓜子了...但是换了硬币之后硬币居然有 %f 个了，银瓜子换硬币成功了额...冲冲冲！！！", sliver, CurrentUser.userInfo.getMoney());
            ar.setActionResultMessage(arMsg);
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
        } else {
            String bilibiliMsg = response.get(BilibiliResponseConstant.MESSAGE).getAsString();
            log.warn("银瓜子换硬币换失败了额，因为{}", bilibiliMsg);
            ar.setActionResultCode(code);
            ar.setBilibiliCode(code);
            String arMsg = String.format("银瓜子换硬币换失败了额，因为%s", bilibiliMsg);
            ar.setActionResultMessage(arMsg);
            ar.setBilibiliMessage(bilibiliMsg);
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
        }
        CurrentUser.actionResult.put("Silver2Coin", ar);
    }
}
