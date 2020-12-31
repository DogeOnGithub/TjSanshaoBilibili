package top.tjsanshao.bilibili.current;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.request.TaskStatus;
import top.tjsanshao.bilibili.request.UserCheck;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * init current user info
 *
 * @author TjSanshao
 * @date 2020-12-30 11:22
 */
@Slf4j
@Component
public class Init {
    @Resource
    private UserCheck userCheck;
    @Resource
    private TaskStatus taskStatus;

    @PostConstruct
    public void init() {
        userCheck.userStatus();
        taskStatus.taskStatus();
        log.info("current user init successfully...");
    }

    public void refresh() {
        userCheck.userStatus();
        taskStatus.taskStatus();
        log.info("current user refresh successfully...");
    }
}
