package top.tjsanshao.bilibili.bilibilirequest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.data.UserInfo;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * user check
 *
 * @author TjSanshao
 * @date 2020-12-29 17:28
 */
@Slf4j
@Component
public class UserCheck {
    @Resource
    private BilibiliRequestClient client;

    public void userStatus() {
        JsonObject loginInfo = client.get(APIList.LOGIN);
        if (Objects.isNull(loginInfo)) {
            log.info("用户信息请求失败，如果是412错误，请更换UA，412问题仅影响用户信息确认，不影响任务");
        } else {
            if (loginInfo.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
                if (loginInfo.get(BilibiliResponseConstant.DATA).getAsJsonObject().get(BilibiliResponseConstant.IS_LOGIN).getAsBoolean()) {
                    // 登录有效
                    CurrentUser.userInfo = new Gson().fromJson(loginInfo.get(BilibiliResponseConstant.DATA), UserInfo.class);
                    log.info("登录有效！");
                    log.info("当前用户：{}", CurrentUser.userInfo);
                }
            } else {
                log.info(loginInfo.getAsString());
                log.warn("Cookies可能失效了,请仔细检查Github Secrets中DEDEUSERID SESSDATA BILI_JCT三项的值是否正确、过期");
            }
        }
    }
}
