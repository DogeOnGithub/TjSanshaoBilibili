package top.tjsanshao.bilibili.action;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.api.OftenAPI;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.current.ActionResult;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;
import top.tjsanshao.bilibili.login.PassCheck;
import top.tjsanshao.bilibili.request.CoinRequest;
import top.tjsanshao.bilibili.request.VideoPullRequest;
import top.tjsanshao.bilibili.util.TjSanshaoDateUtil;

import javax.annotation.Resource;
import java.util.Random;

/**
 * coin
 *
 * @author TjSanshao
 * @date 2020-12-30 17:27
 */
@Slf4j
@Component
public class Coin implements Action {
    @Resource
    private BilibiliRequestClient client;
    @Resource
    private PassCheck passCheck;
    @Resource
    private OftenAPI often;
    @Resource
    private CoinRequest coinRequest;
    @Resource
    private VideoPullRequest videoPullRequest;

    /**
     * 最大投币
     */
    private final static int MAX_AUTO_COIN = 5;

    @Override
    public void act() {
        ActionResult ar = new ActionResult();
        ar.setAction("自动投币");

        if (!CurrentUser.coin) {
            log.warn("自动投币功能未开启！");
            ar.setActionResultCode(0);
            ar.setActionResultMessage("尊重一下UP🐖，币要自己去投");
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
            CurrentUser.actionResult.put("Coin", ar);
            return;
        }

        int dailyCoinUsed = coinRequest.dailyCoinUsed();
        if (dailyCoinUsed >= MAX_AUTO_COIN) {
            log.warn("已经超过每天投币获取经验值上限！");
            ar.setActionResultCode(0);
            ar.setActionResultMessage("今天早就投超5个币了，还想投就自己手动投");
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
            CurrentUser.actionResult.put("Coin", ar);
            return;
        }
        int needCoins = MAX_AUTO_COIN - dailyCoinUsed;
        int coins = (int) CurrentUser.userInfo.getMoney();
        if (needCoins > coins) {
            // 硬币余额不足需要投币数的时候，改为投剩下的硬币
            needCoins = coins;
        }

        log.info("自动投币执行前剩余 {} 个硬币", CurrentUser.userInfo.getMoney());

        while (needCoins > 0 && needCoins <= MAX_AUTO_COIN) {
            String av = videoPullRequest.randomVideo();
            String title = often.videoTitle(av);
            boolean isCoin = coinRequest.isCoin(av);
            if (isCoin) {
                // 投过币
                log.info("这个视频 【{}】 已经投过币了额...", title);
                String msg = String.format("这个视频 【%s】 已经投过币了额...\n", title);
                ar.setActionResultMessage(ar.getActionResultMessage() + msg);
                continue;
            }
            String requestBody = "bvid=" + av
                    + "&multiply=" + 1
                    + "&select_like=" + 1
                    + "&cross_domain=" + "true"
                    + "&csrf=" + passCheck.getBiliJct();
            JsonObject response = client.post(APIList.COIN_ADD, requestBody);
            if (response.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
                // 投币成功
                log.info("给视频 【{}】 投了 {} 个币", title, 1);
                String msg = String.format("给视频 【%s】 投了 %d 个币\n", title, 1);
                ar.setActionResultMessage(ar.getActionResultMessage() + msg);
                needCoins--;
            } else {
                String errMsg = response.get(BilibiliResponseConstant.MESSAGE).getAsString();
                log.info("给视频 【{}】 投币失败了额，因为{}", title, errMsg);
                String msg = String.format("给视频 【%s】 投币失败了额，因为%s\n", title, errMsg);
                ar.setActionResultMessage(ar.getActionResultMessage() + msg);
            }
            try {
                Random random = new Random();
                int sleepTime = (int) ((random.nextDouble() + 0.5) * 3000);
                log.info("投币后随机暂停 {} ms", sleepTime);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                log.error("自动投币过程异常", e);
                ar.setActionResultCode(9999);
                ar.setActionResultMessage("自动投币过程异常");
            }
        }
        ar.setActionResultCode(0);
        ar.setBilibiliCode(0);
        ar.setActionResultMessage("今天已经自动投掉了5个币了");
        CurrentUser.actionResult.put("Coin", ar);
    }
}
