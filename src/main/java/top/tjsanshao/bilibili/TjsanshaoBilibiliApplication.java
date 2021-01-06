package top.tjsanshao.bilibili;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author TjSanshao
 */
@EnableScheduling
@SpringBootApplication
public class TjsanshaoBilibiliApplication {

    public static void main(String[] args) {
        SpringApplication.run(TjsanshaoBilibiliApplication.class, args);
    }

}
