package top.tjsanshao.bilibili.util;

import cn.hutool.core.date.DateUtil;

/**
 * date utils
 *
 * @author TjSanshao
 * @date 2020-12-31 16:30
 */
public class TjSanshaoDateUtil {
    public static String now() {
        return DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss");
    }
}
