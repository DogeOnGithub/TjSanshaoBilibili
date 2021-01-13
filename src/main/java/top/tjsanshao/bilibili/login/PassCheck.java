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
    /**
     * DedeUserID
     */
    @Value("${bilibili.pass.userId}")
    private String userId;

    /**
     * SESSDATA
     */
    @Value("${bilibili.pass.sessData}")
    private String sessData;

    /**
     * bili_jct
     */
    @Value("${bilibili.pass.biliJct}")
    private String biliJct;

    public String getPass() {
        return "bili_jct=" + biliJct + ";SESSDATA=" + sessData + ";DedeUserID=" + userId + ";";
    }

    public void updatePass(String sessData, String biliJct) {
        this.sessData = sessData;
        this.biliJct = biliJct;
    }
}
