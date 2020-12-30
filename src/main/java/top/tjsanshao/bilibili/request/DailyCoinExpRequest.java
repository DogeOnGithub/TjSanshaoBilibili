package top.tjsanshao.bilibili.request;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;
import top.tjsanshao.bilibili.login.PassCheck;

import javax.annotation.Resource;

/**
 * daily coin exp
 *
 * @author TjSanshao
 * @date 2020-12-30 17:08
 */
@Slf4j
@Component
public class DailyCoinExpRequest {
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
}
