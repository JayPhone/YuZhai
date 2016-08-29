package com.yuzhai.util;

import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/29.
 */
public class TypeUtil {

    /**
     * 生成文字和图片对应的Map
     *
     * @return 返回文字和图片对应的Map
     */
    public static Map<String, Integer> getTextImageMap() {
        Map<String, Integer> typeMap = new HashMap<>();
        typeMap.put("软件IT", R.drawable.it);
        typeMap.put("音乐制作", R.drawable.music);
        typeMap.put("平面设计", R.drawable.design);
        typeMap.put("视频拍摄", R.drawable.movie);
        typeMap.put("游戏研发", R.drawable.game);
        typeMap.put("文案撰写", R.drawable.write);
        typeMap.put("金融会计", R.drawable.calculate);
        return typeMap;
    }

    /**
     * 生成索引和文字对应的Map
     *
     * @return 返回索引和文字对应的Map
     */
    public static Map<Integer, String> getIndexTextMap() {
        Map<Integer, String> typeMap = new HashMap<>();
        typeMap.put(1, "软件IT");
        typeMap.put(2, "音乐制作");
        typeMap.put(3, "平面设计");
        typeMap.put(4, "视频拍摄");
        typeMap.put(5, "游戏研发");
        typeMap.put(6, "文案撰写");
        typeMap.put(7, "金融会计");
        return typeMap;
    }

    /**
     * 通过类型文字查询对应的图片
     *
     * @param typeContent 类型文字
     * @return 类型图片，查找不到返回-1
     */
    public static int getTypeImage(String typeContent) {
        Map<String, Integer> typeMap = getTextImageMap();
        if (typeMap.containsKey(typeContent)) {
            return typeMap.get(typeContent);
        }
        return -1;
    }

    /**
     * 通过类型索引查询对应的文本
     *
     * @param index 类型索引
     * @return 类型文字，查找不到返回-1
     */
    public static String getTypeText(int index) {
        Map<Integer, String> typeMap = getIndexTextMap();
        if (typeMap.containsKey(index)) {
            return typeMap.get(index);
        }
        return null;
    }

}
