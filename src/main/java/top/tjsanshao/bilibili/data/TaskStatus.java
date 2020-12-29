package top.tjsanshao.bilibili.data;

import lombok.Data;

/**
 * task status response
 *
 * @author TjSanshao
 * @date 2020-12-29 18:40
 */
@Data
public class TaskStatus {
    private int coins;
    private boolean email;
    private boolean identify_card;
    private boolean login;
    private boolean safe_question;
    private boolean share;
    private boolean tel;
    private boolean watch;
}
