package top.tjsanshao.bilibili.action;

/**
 * @author action
 * @date 2020-12-29 17:02
 */
public interface Action {
    /**
     * 获取Action中文名称
     * @return 名称
     */
    String actionName();

    /**
     * 获取结果集key
     * @return key
     */
    String resultKey();

    /**
     * 用户动作
     */
    void act();
}
