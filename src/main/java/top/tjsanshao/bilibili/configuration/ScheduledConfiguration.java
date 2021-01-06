package top.tjsanshao.bilibili.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import top.tjsanshao.bilibili.auto.AutoTask;

/**
 * Scheduled configuration
 *
 * @author TjSanshao
 * @date 2021-01-06 18:08
 */
@Configuration
public class ScheduledConfiguration {
    @Scheduled(cron = "0 0 9 * * *")
    public void bilibiliAutoTask() {
        AutoTask.run();
    }
}
