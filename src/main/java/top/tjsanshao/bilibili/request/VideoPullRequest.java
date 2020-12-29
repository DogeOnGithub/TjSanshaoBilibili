package top.tjsanshao.bilibili.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.api.APIList;
import top.tjsanshao.bilibili.constant.BilibiliResponseConstant;
import top.tjsanshao.bilibili.http.BilibiliRequestClient;
import top.tjsanshao.bilibili.login.PassCheck;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * pull video
 *
 * @author TjSanshao
 * @date 2020-12-29 18:03
 */
@Component
public class VideoPullRequest {
    @Resource
    private PassCheck passCheck;
    @Resource
    private BilibiliRequestClient client;

    @Getter
    private ArrayList<String> followUpVideoList;
    @Getter
    private ArrayList<String> rankVideoList;

    @PostConstruct
    public void init() {
        followUpVideoList = this.pullUpDynamicVideo();
        rankVideoList = this.regionRanking(this.randomRegion(), 7);
    }

    /**
     * 重载
     * @param rid 分区
     * @param day 排行榜
     */
    public void reload(int rid, int day) {
        followUpVideoList = this.pullUpDynamicVideo();
        rankVideoList = this.regionRanking(rid, day);
    }

    /**
     * 随机返回一个视频
     * @return bvid
     */
    public String randomVideo() {
        ArrayList<String> allVideos = new ArrayList<>(this.followUpVideoList);
        allVideos.addAll(this.rankVideoList);
        return allVideos.get(new Random().nextInt(allVideos.size()));
    }

    /**
     * 获取up主动态视频
     * @return up主动态视频id
     */
    private ArrayList<String> pullUpDynamicVideo() {
        ArrayList<String> bvidList = new ArrayList<>();
        String urlParameter = "?uid=" + passCheck.getUserId() + "&type_list=8" + "&from=" + "&platform=web";
        JsonObject dynamicResponse = client.get(APIList.QUERY_DYNAMIC_NEW + urlParameter);
        if (dynamicResponse.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
            JsonObject data = dynamicResponse.get(BilibiliResponseConstant.DATA).getAsJsonObject();
            if (Objects.nonNull(data)) {
                JsonArray cards = data.getAsJsonArray(BilibiliResponseConstant.CARDS);
                if (Objects.nonNull(cards)) {
                    cards.forEach(v -> {
                        String bvid = v.getAsJsonObject().getAsJsonObject(BilibiliResponseConstant.DESC).get(BilibiliResponseConstant.BVID).getAsString();
                        bvidList.add(bvid);
                    });
                }
            }
        }
        return bvidList;
    }

    /**
     * 从分区排行榜获取视频id
     * @param rid 分区id
     * @param day 1，3，7榜
     * @return 视频id集合
     */
    private ArrayList<String> regionRanking(int rid, int day) {
        ArrayList<String> bvidList = new ArrayList<>();
        String urlParameter = "?rid=" + rid + "&day=" + day;
        JsonObject regionRanking = client.get(APIList.REGION_RANKING + urlParameter);
        if (regionRanking.get(BilibiliResponseConstant.CODE).getAsInt() == BilibiliResponseConstant.CODE_SUCCESS) {
            JsonArray data = regionRanking.get(BilibiliResponseConstant.DATA).getAsJsonArray();
            if (Objects.nonNull(data)) {
                data.forEach(v -> {
                    String bvid = v.getAsJsonObject().get(BilibiliResponseConstant.BVID).getAsString();
                    bvidList.add(bvid);
                });
            }
        }
        return bvidList;
    }

    /**
     * 从有限分区中返回随机分区id
     * @return 随机分区id
     */
    private int randomRegion() {
        int[] arr = {1, 3, 4, 5, 160, 22, 119};
        return arr[(int) (Math.random() * arr.length)];
    }
}
