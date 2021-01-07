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
import top.tjsanshao.bilibili.login.PassCheck;
import top.tjsanshao.bilibili.bilibilirequest.VideoPullRequest;
import top.tjsanshao.bilibili.util.TjSanshaoDateUtil;

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
    private BilibiliRequestClient client;
    @Resource
    private VideoPullRequest videoPullRequest;
    @Resource
    private PassCheck passCheck;

    @Override
    public String actionName() {
        return "视频分享";
    }

    @Override
    public String resultKey() {
        return "VideoShare";
    }

    @Override
    public void act() {
        ActionResult ar = new ActionResult();
        ar.setAction(this.actionName());

        if (!CurrentUser.videoShare) {
            log.warn("自动视频分享功能未开启！");
            ar.setActionResultCode(0);
            ar.setActionResultMessage("视频自己滚去手动分享...");
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
            CurrentUser.actionResult.put(this.resultKey(), ar);
            return;
        }
        String randomVideo = videoPullRequest.randomVideo();
        if (!CurrentUser.taskStatus.isShare()) {
            String postBody = "bvid=" + randomVideo + "&csrf=" + passCheck.getBiliJct();
            JsonObject response = client.post(APIList.AV_SHARE, postBody);
            String title = often.videoTitle(randomVideo);
            int code = response.get(BilibiliResponseConstant.CODE).getAsInt();
            if (code == BilibiliResponseConstant.CODE_SUCCESS) {
                log.info("视频【{}】分享成功！", title);
                ar.setActionResultCode(0);
                ar.setBilibiliCode(0);
                String arMsg = String.format("视频【%s】已经分享给大佬们看啦！", title);
                ar.setActionResultMessage(arMsg);
                ar.setActionFinishedTime(TjSanshaoDateUtil.now());
            } else {
                String bilibiliMsg = response.get(BilibiliResponseConstant.MESSAGE).getAsString();
                log.warn("视频【{}】分享失败，失败原因:{}", title, bilibiliMsg);
                ar.setActionResultCode(code);
                ar.setBilibiliCode(code);
                String arMsg = String.format("视频【%s】分享不了，因为%s", title, bilibiliMsg);
                ar.setActionResultMessage(arMsg);
                ar.setBilibiliMessage(bilibiliMsg);
                ar.setActionFinishedTime(TjSanshaoDateUtil.now());
            }
        } else {
            log.info("已完成每日分享视频任务！");
            ar.setActionResultCode(999);
            ar.setActionResultMessage("早已分享，无需多言");
            ar.setActionFinishedTime(TjSanshaoDateUtil.now());
        }
        CurrentUser.actionResult.put(this.resultKey(), ar);
    }
}
