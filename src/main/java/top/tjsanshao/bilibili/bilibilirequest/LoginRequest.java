package top.tjsanshao.bilibili.bilibilirequest;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;
import top.tjsanshao.bilibili.login.PassCheck;

import javax.annotation.Resource;

/**
 * bilibili login request
 *
 * @author TjSanshao
 * @date 2021-01-13 16:00
 */
@Slf4j
@Component
public class LoginRequest {
    @Resource
    private BilibiliRequestClient client;
    @Resource
    private PassCheck passCheck;

    public String loginCode() {
        JsonObject response = client.get("https://passport.bilibili.com/qrcode/getLoginUrl");
        return new Gson().toJson(response);
    }

    public String loginInfo(String oauthKey) {
        String postBody = "oauthKey=" + oauthKey;
        JsonObject response = client.post("https://passport.bilibili.com/qrcode/getLoginInfo", postBody);
        boolean status = response.get(BilibiliResponseConstant.STATUS).getAsBoolean();
        if (status) {
            String url = response.get(BilibiliResponseConstant.DATA).getAsJsonObject().get("url").getAsString();
            String[] cookiesArray = url.split("\\?")[1].split("&");
            String SESSDATA = "";
            String bili_jct = "";
            for (String c : cookiesArray) {
                if ("SESSDATA".equals(c.split("=")[0])) {
                    SESSDATA = c.split("=")[1];
                }
                if ("bili_jct".equals(c.split("=")[0])) {
                    bili_jct = c.split("=")[1];
                }
            }
            if (StrUtil.isEmpty(SESSDATA) || StrUtil.isEmpty(bili_jct)) {
                return "login info error!";
            }
            passCheck.updatePass(SESSDATA, bili_jct);
            return "success";
        } else {
            return response.get(BilibiliResponseConstant.MESSAGE).getAsString();
        }
    }
}
