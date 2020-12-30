package top.tjsanshao.bilibili.action;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.constant.BilibiliTypeConstant;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;

import javax.annotation.Resource;

/**
 * manga vip reward
 *
 * @author TjSanshao
 * @date 2020-12-30 16:46
 */
@Slf4j
@Component
public class MangaVIPReward implements Action {
    @Resource
    private BilibiliRequestClient client;
    @Resource
    private CurrentUser currentUser;

    @Override
    public void act() {
        if (currentUser.getUserInfo().getVipStatus() == BilibiliTypeConstant.VIP_EFFECT) {
            /*
             * 权益号，由https://api.bilibili.com/x/vip/privilege/my
             * 得到权益号数组，取值范围为数组中的整数
             * 为方便直接取1，为领取漫读劵，暂时不取其他的值
             */
            int reasonId = 1;
            // 参数为json格式
            String requestBody = "{\"reason_id\":" + reasonId + "}";
            JsonObject response = client.post(APIList.VIP_REWARD_COMIC, requestBody);
            if (response.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
                JsonObject data = response.getAsJsonObject(BilibiliResponseConstant.DATA);
                int amount = data.get(BilibiliResponseConstant.AMOUNT).getAsInt();
                log.info("大会员漫画特权领到啦！这次嫖到了 {} 张漫读券！", amount);
            } else {
                log.warn("大会员不中用了，没东西领，因为{}", response.get(BilibiliResponseConstant.MSG).getAsString());
            }
        } else {
            log.error("不会吧！这年头居然有人不是大会员？！？");
        }
    }
}
