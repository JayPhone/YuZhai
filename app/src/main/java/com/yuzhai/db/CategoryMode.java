package com.yuzhai.db;

import android.content.Context;

import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/1.
 */
public class CategoryMode {
    private static CategoryMode mInstance;
    private Context context;
    private OnCategoryDataChanged mOnCategoryDataChanged;
    private List<Integer> mCategoryImages;
    private List<String> mCategoryTexts;
    private List<Map<String, Object>> mCategory;
    public static final String CATEGORY_IMAGE = "categoryImage";
    public static final String CATEGORY_TEXT = "categoryText";

    public static final String IT = "软件IT";
    public static final String MUSIC = "音乐制作";
    public static final String DESIGN = "平面设计";
    public static final String MOVIE = "视频拍摄";
    public static final String GAME = "游戏研发";
    public static final String WRITE = "文案撰写";
    public static final String CALCULATE = "金融会计";
    public static final String PLUS = "添加类别";

    protected CategoryMode(Context context) {
        this.context = context;
        mCategory = initCategoryData();
    }

    public static synchronized CategoryMode getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CategoryMode(context);
        }
        return mInstance;
    }

    /**
     * 初始化类别数据
     *
     * @return
     */
    protected List<Map<String, Object>> initCategoryData() {
        mCategoryImages = new ArrayList<>();
        mCategoryImages.add(R.drawable.it);
        mCategoryImages.add(R.drawable.music);
        mCategoryImages.add(R.drawable.design);
        mCategoryImages.add(R.drawable.movie);
        mCategoryImages.add(R.drawable.game);
        mCategoryImages.add(R.drawable.write);
        mCategoryImages.add(R.drawable.calculate);
        mCategoryImages.add(R.drawable.plus);

        mCategoryTexts = new ArrayList<>();
        mCategoryTexts.add(IT);
        mCategoryTexts.add(MUSIC);
        mCategoryTexts.add(DESIGN);
        mCategoryTexts.add(MOVIE);
        mCategoryTexts.add(GAME);
        mCategoryTexts.add(WRITE);
        mCategoryTexts.add(CALCULATE);
        mCategoryTexts.add(PLUS);

        mCategory = new ArrayList<>();
        Map<String, Object> map;
        for (int i = 0; i < mCategoryImages.size(); i++) {
            map = new HashMap<>();
            map.put(CATEGORY_IMAGE, mCategoryImages.get(i));
            map.put(CATEGORY_TEXT, mCategoryTexts.get(i));
            mCategory.add(map);
        }
        return mCategory;
    }

    /**
     * 获取类别数据
     *
     * @return
     */
    public List<Map<String, Object>> getCategoryData() {
        return mCategory;
    }

    public void addCategory(int image, String text) {
        Map<String, Object> map = new HashMap<>();
        map.put(CATEGORY_IMAGE, image);
        map.put(CATEGORY_TEXT, text);
        mCategory.add(mCategory.size() - 1, map);
        if (mOnCategoryDataChanged != null) {
            mOnCategoryDataChanged.onCategoryDataInserted(mCategory);
        }
    }

    public void setOnCategoryDataChangedListener(OnCategoryDataChanged onCategoryDataChanged) {
        mOnCategoryDataChanged = onCategoryDataChanged;
    }
}
