package top.tjsanshao.bilibili.action;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.api.OftenAPI;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;
import top.tjsanshao.bilibili.login.PassCheck;
import top.tjsanshao.bilibili.request.CoinRequest;
import top.tjsanshao.bilibili.request.VideoPullRequest;

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
        int dailyCoinUsed = coinRequest.dailyCoinUsed();
        if (dailyCoinUsed >= MAX_AUTO_COIN) {
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
                needCoins--;
            } else {
                log.info("给视频 【{}】 投币失败了额，因为{}", title, response.get(BilibiliResponseConstant.MESSAGE).getAsString());
            }
            try {
                Random random = new Random();
                int sleepTime = (int) ((random.nextDouble() + 0.5) * 3000);
                log.info("投币后随机暂停 {} ms", sleepTime);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                log.error("自动投币过程异常", e);
            }
        }
    }
}
