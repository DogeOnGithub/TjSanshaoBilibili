package top.tjsanshao.bilibili.api;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.constant.BilibiliTypeConstant;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;
import top.tjsanshao.bilibili.login.PassCheck;

import javax.annotation.Resource;

/**
 * often api
 *
 * @author TjSanshao
 * @date 2020-12-29 15:55
 */
@Slf4j
@Component("often")
public class OftenAPI {
    @Resource
    private BilibiliRequestClient client;
    @Resource
    private PassCheck passCheck;

    /**
     * 获取硬币余额
     * url：https://account.bilibili.com/site/getCoin
     * 响应格式为：
     * {"code":0,"status":true,"data":{"money":4}}
     * @return 硬币余额
     */
    public double getCoinBalance() {
        JsonObject response = client.get(APIList.COIN_BALANCE);
        int code = response.get(BilibiliResponseConstant.CODE).getAsInt();
        if (code == BilibiliResponseConstant.CODE_SUCCESS) {
            JsonObject data = response.get(BilibiliResponseConstant.DATA).getAsJsonObject();
            if (data.get(BilibiliResponseConstant.MONEY).isJsonNull()) {
                return 0.0;
            } else {
                return data.get(BilibiliResponseConstant.MONEY).getAsDouble();
            }
        } else {
            return 0.0;
        }
    }

    /**
     * 根据bvid获取视频标题
     * @param bvid id
     * @return 标题
     */
    public String videoTitle(String bvid) {
        String urlParameter = "?bvid=" + bvid;
        JsonObject response = client.get(APIList.VIDEO_VIEW + urlParameter);
        if (response.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
            JsonObject data = response.getAsJsonObject(BilibiliResponseConstant.DATA);
            String up = data.getAsJsonObject(BilibiliResponseConstant.OWNER).get(BilibiliResponseConstant.NAME).getAsString();
            String title = data.get(BilibiliResponseConstant.TITLE).getAsString();
            return up + ":" + title;
        } else {
            log.warn("Warning! Can not get title! Bilibili Message:[{}]", response.get(BilibiliResponseConstant.MESSAGE));
            return "No title!";
        }
    }

    /**
     * 领取大会员福利
     * @param type 1=B币券，2=大会员福利
     */
    public void vipPrivilege(int type) {
        String body ="type=" + type + "&csrf=" + passCheck.getBiliJct();
        JsonObject response = client.post(APIList.VIP_PRIVILEGE_RECEIVE, body);
        int code = response.get(BilibiliResponseConstant.CODE).getAsInt();
        if (code == BilibiliResponseConstant.CODE_SUCCESS) {
            if (type == BilibiliTypeConstant.B_COIN) {
                log.info("领取年度大会员每月赠送的B币券成功！");
            } else if (type == BilibiliTypeConstant.VIP_REWARD) {
                log.info("领取大会员福利/权益成功！");
            }
        } else {
            log.error("领取年度大会员每月赠送的B币券/大会员福利失败，原因:{}", response.get("message").getAsString());
        }
    }
}
