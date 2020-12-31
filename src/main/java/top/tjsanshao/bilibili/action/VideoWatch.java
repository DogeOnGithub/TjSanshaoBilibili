package top.tjsanshao.bilibili.action;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.api.OftenAPI;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;
import top.tjsanshao.bilibili.request.VideoPullRequest;

import javax.annotation.Resource;
import java.util.Random;

/**
 * video watch
 *
 * @author TjSanshao
 * @date 2020-12-30 10:44
 */
@Slf4j
@Component
public class VideoWatch implements Action {
    @Resource
    private OftenAPI often;

    @Resource
    private BilibiliRequestClient client;
    @Resource
    private VideoPullRequest videoPullRequest;

    @Override
    public void act() {
        if (!CurrentUser.videoWatch) {
            log.warn("自动观看功能未开启！");
        }
        String randomVideo = videoPullRequest.randomVideo();
        if (!CurrentUser.taskStatus.isWatch()) {
            int playedTime = new Random().nextInt(90) + 1;
            String postBody = "bvid=" + randomVideo + "&played_time=" + playedTime;
            JsonObject response = client.post(APIList.VIDEO_HEART_BEAT, postBody);
            String title = often.videoTitle(randomVideo);
            if (response.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
                log.info("视频【{}】播放成功，已观看至第{}秒", title, playedTime);
            } else {
                log.warn("视频【{}】播放失败，失败原因:{}", title, response.get(BilibiliResponseConstant.MESSAGE).getAsString());
            }
        } else {
            log.info("已完成每日播放视频任务！");
        }
    }
}
