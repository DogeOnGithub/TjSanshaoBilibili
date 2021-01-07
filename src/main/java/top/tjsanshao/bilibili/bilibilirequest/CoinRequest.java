package top.tjsanshao.bilibili.bilibilirequest;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;

import javax.annotation.Resource;

/**
 * daily coin exp
 *
 * @author TjSanshao
 * @date 2020-12-30 17:08
 */
@Slf4j
@Component
public class CoinRequest {
    @Resource
    private BilibiliRequestClient client;

    public int dailyCoinExp() {
        JsonObject response = client.get(APIList.COIN_EXP_NEW);
        if (response.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
            return response.get(BilibiliResponseConstant.DATA).getAsInt();
        } else {
            return 0;
        }
    }

    public int dailyCoinUsed() {
        return this.dailyCoinExp() / 10;
    }

    public boolean isCoin(String av) {
        String urlParameter = "?bvid=" + av;
        JsonObject response = client.get(APIList.IS_COIN + urlParameter);
        if (response.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
            int coins = response.getAsJsonObject(BilibiliResponseConstant.DATA).get(BilibiliResponseConstant.MULTIPLY).getAsInt();
            return coins > 0;
        }
        return false;
    }
}
