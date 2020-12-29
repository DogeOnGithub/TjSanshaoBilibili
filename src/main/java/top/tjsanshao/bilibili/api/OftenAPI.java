package top.tjsanshao.bilibili.api;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;

import javax.annotation.Resource;

/**
 * often api
 *
 * @author TjSanshao
 * @date 2020-12-29 15:55
 */
@Component
public class OftenAPI {
    @Resource
    private BilibiliRequestClient client;

    private final static String MONEY = "money";

    /**
     * 获取硬币余额
     * url：https://account.bilibili.com/site/getCoin
     * 响应格式为：
     * {"code":0,"status":true,"data":{"money":4}}
     * @return 硬币余额
     */
    public double getCoinBalance() {
        JsonObject response = client.get(APIList.COIN_BALANCE);
        int code = response.get("code").getAsInt();
        if (code == 0) {
            JsonObject data = response.get("data").getAsJsonObject();
            if (data.get(MONEY).isJsonNull()) {
                return 0.0;
            } else {
                return data.get(MONEY).getAsDouble();
            }
        } else {
            return 0.0;
        }
    }
}
