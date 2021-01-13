package top.tjsanshao.bilibili.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tjsanshao.bilibili.action.ChargeMe;
import top.tjsanshao.bilibili.action.Coin;
import top.tjsanshao.bilibili.action.LiveCheckIn;
import top.tjsanshao.bilibili.action.MangaCheckIn;
import top.tjsanshao.bilibili.action.MangaVIPReward;
import top.tjsanshao.bilibili.action.Silver2Coin;
import top.tjsanshao.bilibili.auto.AutoTask;
import top.tjsanshao.bilibili.auto.Notice;
import top.tjsanshao.bilibili.bilibilirequest.LoginRequest;
import top.tjsanshao.bilibili.bilibilirequest.TaskStatus;
import top.tjsanshao.bilibili.bilibilirequest.UserCheck;
import top.tjsanshao.bilibili.action.VideoShare;
import top.tjsanshao.bilibili.action.VideoWatch;
import top.tjsanshao.bilibili.api.OftenAPI;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.bilibilirequest.CoinRequest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * controller
 *
 * @author TjSanshao
 * @date 2020-12-29 16:01
 */
@RestController
public class BilibiliController {
    @Resource
    private OftenAPI often;
    @Resource
    private CoinRequest coinRequest;
    @Resource
    private LoginRequest loginRequest;
    @Resource
    private UserCheck userCheck;
    @Resource
    private TaskStatus taskStatus;
    @Resource
    private VideoWatch videoWatch;
    @Resource
    private VideoShare videoShare;
    @Resource
    private MangaCheckIn mangaCheckIn;
    @Resource
    private Silver2Coin silver2Coin;
    @Resource
    private LiveCheckIn liveCheckIn;
    @Resource
    private MangaVIPReward mangaVIPReward;
    @Resource
    private Coin coin;
    @Resource
    private ChargeMe chargeMe;
    @Resource
    private Notice notice;

    @RequestMapping("/run")
    public String run() {
        AutoTask.run();
        return "success";
    }

    @RequestMapping("/login")
    public String login() {
        userCheck.userStatus();
        return "success";
    }

    @RequestMapping("/taskStatus")
    public String taskStatus() {
        taskStatus.taskStatus();
        return "success";
    }

    @RequestMapping("/current")
    public Map<String, Object> current() {
        userCheck.userStatus();
        taskStatus.taskStatus();
        HashMap<String, Object> info = new HashMap<>(10);
        info.put("userInfo", CurrentUser.userInfo);
        info.put("taskStatus", CurrentUser.taskStatus);
        info.put("chargeMe", CurrentUser.chargeMe);
        info.put("coin", CurrentUser.coin);
        info.put("liveCheckIn", CurrentUser.liveCheckIn);
        info.put("mangaCheckIn", CurrentUser.mangaCheckIn);
        info.put("mangaVIPReward", CurrentUser.mangaVIPReward);
        info.put("silver2Coin", CurrentUser.silver2Coin);
        info.put("videoShare", CurrentUser.videoShare);
        info.put("videoWatch", CurrentUser.videoWatch);
        info.put("actionResult", CurrentUser.actionResult);
        return info;
    }

    @RequestMapping("/videoWatch")
    public String videoWatch() {
        videoWatch.act();
        return "success";
    }

    @RequestMapping("/videoShare")
    public String videoShare() {
        videoShare.act();
        return "success";
    }

    @RequestMapping("/mangaCheck")
    public String mangaCheck() {
        mangaCheckIn.act();
        return "success";
    }

    @RequestMapping("/silver2Coin")
    public String sliver2Coin() {
        silver2Coin.act();
        return "success";
    }

    @RequestMapping("/liveCheck")
    public String liveCheck() {
        liveCheckIn.act();
        return "success";
    }

    @RequestMapping("/mangaReward")
    public String mangaReward() {
        mangaVIPReward.act();
        return "success";
    }

    @RequestMapping("/coin")
    public String coin() {
        coin.act();
        return "success";
    }

    @RequestMapping("/chargeMe")
    public String chargeMe() {
        chargeMe.act();
        return "success";
    }

    @RequestMapping("/coinBalance")
    public String coinBalance() {
        double balance = often.getCoinBalance();
        return String.valueOf(balance);
    }

    @RequestMapping("/dailyCoinExp")
    public String dailyCoinExp() {
        int exp = coinRequest.dailyCoinExp();
        return String.valueOf(exp);
    }

    @RequestMapping("/send")
    public String send() {
        notice.send();
        return "success";
    }

    @RequestMapping("/s/{t}/{c}")
    public String send(@PathVariable String t, @PathVariable String c) {
        notice.send(t, c);
        return "success";
    }

    @RequestMapping("/loginCode")
    public String loginCode() {
        return loginRequest.loginCode();
    }

    @RequestMapping("/loginInfo")
    public String loginInfo(String oauthKey) {
        return loginRequest.loginInfo(oauthKey);
    }
}
