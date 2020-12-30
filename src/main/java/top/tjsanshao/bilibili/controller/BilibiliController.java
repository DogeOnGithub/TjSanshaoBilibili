package top.tjsanshao.bilibili.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tjsanshao.bilibili.action.LiveCheckIn;
import top.tjsanshao.bilibili.action.MangaCheckIn;
import top.tjsanshao.bilibili.action.MangaVIPReward;
import top.tjsanshao.bilibili.action.Silver2Coin;
import top.tjsanshao.bilibili.action.TaskStatus;
import top.tjsanshao.bilibili.action.UserCheck;
import top.tjsanshao.bilibili.action.VideoShare;
import top.tjsanshao.bilibili.action.VideoWatch;
import top.tjsanshao.bilibili.api.OftenAPI;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.request.DailyCoinExpRequest;

import javax.annotation.Resource;

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
    private DailyCoinExpRequest dailyCoinExpRequest;
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
    private CurrentUser currentUser;

    @RequestMapping("/login")
    public String login() {
        userCheck.act();
        return "success";
    }

    @RequestMapping("/taskStatus")
    public String taskStatus() {
        taskStatus.act();
        return "success";
    }

    @RequestMapping("/current")
    public CurrentUser current() {
        return currentUser;
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

    @RequestMapping("/coinBalance")
    public String coinBalance() {
        double balance = often.getCoinBalance();
        return String.valueOf(balance);
    }

    @RequestMapping("/dailyCoinExp")
    public String dailyCoinExp() {
        int exp = dailyCoinExpRequest.dailyCoinExp();
        return String.valueOf(exp);
    }
}
