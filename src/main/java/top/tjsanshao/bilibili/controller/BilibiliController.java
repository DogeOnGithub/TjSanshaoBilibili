package top.tjsanshao.bilibili.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tjsanshao.bilibili.action.TaskStatus;
import top.tjsanshao.bilibili.action.UserCheck;
import top.tjsanshao.bilibili.api.OftenAPI;
import top.tjsanshao.bilibili.current.CurrentUser;

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
    private OftenAPI oftenAPI;

    @Resource
    private UserCheck userCheck;
    @Resource
    private TaskStatus taskStatus;

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

    @RequestMapping("/coinBalance")
    public String coinBalance() {
        double balance = oftenAPI.getCoinBalance();
        return String.valueOf(balance);
    }
}
