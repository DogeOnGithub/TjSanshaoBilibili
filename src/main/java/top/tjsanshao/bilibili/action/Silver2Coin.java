package top.tjsanshao.bilibili.action;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.current.Init;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;

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
    private CurrentUser currentUser;
    @Resource
    private Init init;

    @Override
    public void act() {
        JsonObject response = client.get(APIList.SILVER_TO_COIN);
        if (response.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
            log.info("银瓜子换硬币成功了额...");
        } else {
            log.warn("银瓜子换硬币换失败了额，因为{}", response.get(BilibiliResponseConstant.MESSAGE).getAsString());
        }
        JsonObject statusResponse = client.get(APIList.SILVER_TO_COIN_STATUS);
        int sliver = statusResponse.getAsJsonObject(BilibiliResponseConstant.DATA).get(BilibiliResponseConstant.SILVER).getAsInt();
        log.info("现在还剩 {} 个银瓜子了...", sliver);
        init.refresh();
        log.info("现在的硬币还剩 {} 个哦", currentUser.getUserInfo().getMoney());
    }
}
