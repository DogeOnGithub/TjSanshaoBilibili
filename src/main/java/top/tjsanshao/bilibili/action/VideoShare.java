package top.tjsanshao.bilibili.action;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.api.OftenAPI;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;
import top.tjsanshao.bilibili.login.PassCheck;
import top.tjsanshao.bilibili.request.VideoPullRequest;

import javax.annotation.Resource;

/**
 * video share
 *
 * @author TjSanshao
 * @date 2020-12-30 11:14
 */
@Slf4j
@Component
public class VideoShare implements Action {
    @Resource
    private OftenAPI often;

    @Resource
    private CurrentUser currentUser;
    @Resource
    private BilibiliRequestClient client;
    @Resource
    private VideoPullRequest videoPullRequest;
    @Resource
    private PassCheck passCheck;

    @Override
    public void act() {
        String randomVideo = videoPullRequest.randomVideo();
        if (!currentUser.getTaskStatus().isShare()) {
            String postBody = "bvid=" + randomVideo + "&csrf=" + passCheck.getBiliJct();
            JsonObject response = client.post(APIList.AV_SHARE, postBody);
            String title = often.videoTitle(randomVideo);
            if (response.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
                log.info("视频【{}】分享成功！", title);
            } else {
                log.warn("视频【{}】分享失败，失败原因:{}", title, response.get(BilibiliResponseConstant.MESSAGE).getAsString());
            }
        } else {
            log.info("已完成每日分享视频任务！");
        }
    }
}
