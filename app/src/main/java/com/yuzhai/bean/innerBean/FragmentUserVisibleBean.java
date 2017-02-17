package com.yuzhai.bean.innerBean;

/**
 * Created by 35429 on 2017/2/15.
 */

public class FragmentUserVisibleBean {
    private boolean isVisible;

    public FragmentUserVisibleBean(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
