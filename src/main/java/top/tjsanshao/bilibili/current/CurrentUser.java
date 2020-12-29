package top.tjsanshao.bilibili.current;

import lombok.Data;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.data.TaskStatus;
import top.tjsanshao.bilibili.data.UserInfo;

/**
 * current user
 *
 * @author TjSanshao
 * @date 2020-12-29 17:46
 */
@Data
@Component
public class CurrentUser {
    private UserInfo userInfo;
    private TaskStatus taskStatus;
}
