package top.tjsanshao.bilibili.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tjsanshao.bilibili.api.OftenAPI;

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

    @RequestMapping("/coinBalance")
    public String coinBalance() {
        double balance = oftenAPI.getCoinBalance();
        return String.valueOf(balance);
    }
}
