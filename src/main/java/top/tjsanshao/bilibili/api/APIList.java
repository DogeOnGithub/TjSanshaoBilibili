package top.tjsanshao.bilibili.api;

/**
 * bilibili api list
 *
 * @author TjSanshao
 * @date 2020-12-29 14:46
 */
public class APIList {
    public final static String SERVER_PUSH = "https://sc.ftqq.com/";
    public final static String LOGIN = "https://api.bilibili.com/x/web-interface/nav";
    public final static String MANAGE = "https://manga.bilibili.com/twirp/activity.v1.Activity/ClockIn";
    public final static String AV_SHARE = "https://api.bilibili.com/x/web-interface/share/add";
    public final static String COIN_ADD = "https://api.bilibili.com/x/web-interface/coin/add";
    public final static String IS_COIN = "https://api.bilibili.com/x/web-interface/archive/coins";
    public final static String REGION_RANKING = "https://api.bilibili.com/x/web-interface/ranking/region";
    public final static String REWARD = "https://api.bilibili.com/x/member/web/exp/reward";

    /**
     * 查询获取已获取的投币经验
     */
    public final static String COIN_EXP = "https://www.bilibili.com/plus/account/exp.php";

    public final static String COIN_EXP_NEW = "https://api.bilibili.com/x/web-interface/coin/today/exp";

    /**
     * 硬币换银瓜子
     */
    public final static String SLIVER_TO_COIN = "https://api.live.bilibili.com/pay/v1/Exchange/silver2coin";

    /**
     * 查询银瓜子兑换状态
     */
    public final static String SLIVER_TO_COIN_STATUS = "https://api.live.bilibili.com/pay/v1/Exchange/getStatus";

    /**
     * 上报观看进度
     */
    public final static String VIDEO_HEART_BEAT = "https://api.bilibili.com/x/click-interface/web/heartbeat";

    /**
     * 查询主站硬币余额
     */
    public final static String COIN_BALANCE = "https://account.bilibili.com/site/getCoin";

    /**
     * 充电请求
     */
    public final static String CHARGE = "https://api.bilibili.com/x/ugcpay/web/v2/trade/elec/pay/quick";

    /**
     * 充电留言
     */
    public final static String CHARGE_COMMENT = "https://api.bilibili.com/x/ugcpay/trade/elec/message";

    public final static String CHARGE_QUERY = "https://api.bilibili.com/x/ugcpay/web/v2/trade/elec/panel";

    /**
     * 领取大会员福利
     */
    public final static String VIP_PRIVILEGE_RECEIVE = "https://api.bilibili.com/x/vip/privilege/receive";

    /**
     * 领取大会员漫画福利
     */
    public final static String VIP_REWARD_COMIC = "https://manga.bilibili.com/twirp/user.v1.User/GetVipReward";

    /**
     * 直播签到
     */
    public final static String LIVE_CHECK_IN = "https://api.live.bilibili.com/xlive/web-ucenter/v1/sign/DoSign";

    public final static String QUERY_DYNAMIC_NEW = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_new";

    public final static String VIDEO_VIEW = "https://api.bilibili.com/x/web-interface/view";
}
