package top.tjsanshao.bilibili.current;

import lombok.Data;

/**
 * action result
 *
 * @author TjSanshao
 * @date 2020-12-31 16:13
 */
@Data
public class ActionResult {
    private String action;
    private int actionResultCode;
    private String actionResultMessage;
    private String actionFinishedTime;
    private int bilibiliCode;
    private String bilibiliMessage;
}
