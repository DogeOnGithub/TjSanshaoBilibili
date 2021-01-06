package top.tjsanshao.bilibili.action;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.api.OftenAPI;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.current.ActionResult;
import top.tjsanshao.bilibili.current.CurrentUser;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;
import top.tjsanshao.bilibili.request.VideoPullRequest;
import top.tjsanshao.bilibili.util.TjSanshaoDateUtil;

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
    public String actionName() {
        return "自动观看";
    }

    @Override
    public String resultKey() {
        return "VideoWatch";
    }

    @Override
    public void act() {
        ActionResult ar = new ActionResult();
        ar.setAction(this.actionName());

        if (!CurrentUser.videoWatch) {
            log.warn("自动观看功能未开启！");
            ar.setActionResultCode(0);
            ar.setActionResultMessage("不会吧？不会真的有人不是每天上B站的吧？！？");
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
            CurrentUser.actionResult.put(this.resultKey(), ar);
            return;
        }
        String randomVideo = videoPullRequest.randomVideo();
        if (!CurrentUser.taskStatus.isWatch()) {
            int playedTime = new Random().nextInt(90) + 1;
            String postBody = "bvid=" + randomVideo + "&played_time=" + playedTime;
            JsonObject response = client.post(APIList.VIDEO_HEART_BEAT, postBody);
            String title = often.videoTitle(randomVideo);
            int code = response.get(BilibiliResponseConstant.CODE).getAsInt();
            if (code == BilibiliResponseConstant.CODE_SUCCESS) {
                log.info("视频【{}】播放成功，已观看至第{}秒", title, playedTime);
                ar.setActionResultCode(0);
                ar.setBilibiliCode(0);
                String arMsg = String.format("视频【%s】已经进行自动观看，已观看至第 %d 秒", title, playedTime);
                ar.setActionResultMessage(arMsg);
                ar.setActionFinishedTime(TjSanshaoDateUtil.now());
            } else {
                String bilibiliMsg = response.get(BilibiliResponseConstant.MESSAGE).getAsString();
                log.warn("视频【{}】播放失败，失败原因:{}", title, bilibiliMsg);
                ar.setActionResultCode(code);
                ar.setBilibiliCode(code);
                String arMsg = String.format("视频【%s】自动观看失败了，失败原因:%s", title, playedTime);
                ar.setActionResultMessage(arMsg);
                ar.setBilibiliMessage(bilibiliMsg);
                ar.setActionFinishedTime(TjSanshaoDateUtil.now());
            }
        } else {
            log.info("已完成每日播放视频任务！");
            ar.setActionResultCode(0);
            ar.setActionResultMessage("不会吧？？？还等自动任务？？昨晚刷完视频才睡的");
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
        }
        CurrentUser.actionResult.put(this.resultKey(), ar);
    }
}
