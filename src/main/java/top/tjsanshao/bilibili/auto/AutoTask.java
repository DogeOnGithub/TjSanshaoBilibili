package top.tjsanshao.bilibili.auto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.action.Action;
import top.tjsanshao.bilibili.current.ActionResult;
import top.tjsanshao.bilibili.current.CurrentUser;

import java.util.Objects;

/**
 * auto task
 *
 * @author TjSanshao
 * @date 2021-01-06 17:11
 */
@Slf4j
@Component
public class AutoTask implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AutoTask.applicationContext = applicationContext;
        log.info("ApplicationContext init success...");
    }

    public static void run() {
        String[] actionNames = AutoTask.applicationContext.getBeanNamesForType(Action.class);
        for (String actionName : actionNames) {
            Action action = AutoTask.applicationContext.getBean(actionName, Action.class);
            if (Objects.nonNull(action)) {
                try {
                    action.act();
                } catch (Exception e) {
                    ActionResult ar = new ActionResult();
                    ar.setAction(action.actionName());
                    ar.setActionResultCode(-1);
                    ar.setActionResultMessage(e.getMessage());
                    CurrentUser.actionResult.put(action.resultKey(), ar);
                }
            }
        }
        Notice notice = AutoTask.applicationContext.getBean("notice", Notice.class);
        if (Objects.nonNull(notice)) {
            notice.send();
        }
    }
}
