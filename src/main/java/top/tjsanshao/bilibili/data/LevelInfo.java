package top.tjsanshao.bilibili.data;

import lombok.Data;

/**
 * level info
 *
 * @author TjSanshao
 * @date 2020-12-29 17:33
 */
@Data
public class LevelInfo {
    private int current_level;
    private int current_min;
    private int current_exp;
    private String next_exp;

    public int getNext_exp_asInt() {
        if ("--".equals(next_exp)) {
            return current_exp;
        } else {
            return Integer.parseInt(next_exp);
        }
    }
}
