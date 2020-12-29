package top.tjsanshao.bilibili.login;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * bilibili pass check
 *
 * @author TjSanshao
 * @date 2020-12-29 15:43
 */
@Data
@Component
public class PassCheck {
    @Value("bilibili.pass.userId")
    private String userId;

    @Value("bilibili.pass.sessData")
    private String sessData;

    @Value("bilibili.pass.biliJct")
    private String biliJct;

    public String getPass() {
        return "bili_jct=" + biliJct + ";SESSDATA=" + sessData + ";DedeUserID=" + userId + ";";
    }
}