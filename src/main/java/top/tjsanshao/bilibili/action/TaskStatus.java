package top.tjsanshao.bilibili.action;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;

import javax.annotation.Resource;

/**
 * task status
 *
 * @author TjSanshao
 * @date 2020-12-29 18:39
 */
@Slf4j
@Component
public class TaskStatus implements Action {
    @Resource
    private CurrentUser currentUser;
    @Resource
    private BilibiliRequestClient client;

    @Override
    public void act() {
        JsonObject rewardResponse = client.get(APIList.REWARD);
        if (rewardResponse.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
            JsonObject data = rewardResponse.get(BilibiliResponseConstant.DATA).getAsJsonObject();
            top.tjsanshao.bilibili.data.TaskStatus taskStatus = new Gson().fromJson(data, top.tjsanshao.bilibili.data.TaskStatus.class);
            currentUser.setTaskStatus(taskStatus);
        }
    }
}
