package com.yuzhai.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuzhai.db.CategoryMode;
import com.yuzhai.db.OnCategoryDataChanged;
import com.yuzhai.ui.CategoryActivity;
import com.yuzhai.yuzhaiwork.R;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/26.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> implements OnItemTouchListener,
        OnCategoryDataChanged {

    private Context mContext;
    private CategoryMode mCategoryMode;
    private List<Map<String, Object>> mCategory;

    public static final String TITLE = "title";

    public RecyclerViewAdapter(Context context) {
        this.mContext = context;
        initCategoryData();
    }

    private void initCategoryData() {
        mCategoryMode = CategoryMode.getInstance(mContext);
        mCategoryMode.setOnCategoryDataChangedListener(this);
        mCategory = mCategoryMode.getCategoryData();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mCategory, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mCategory.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder holder = new RecyclerViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.home_category_cell_layout, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.mImageView.setBackgroundResource((Integer) mCategory.get(position).get(CategoryMode.CATEGORY_IMAGE));
        holder.mTextView.setText((String) mCategory.get(position).get(CategoryMode.CATEGORY_TEXT));
        holder.mFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mTextView.getText() != mCategory.get(mCategory.size() - 1).get(CategoryMode.CATEGORY_TEXT)) {
                    Intent category = new Intent(mContext, CategoryActivity.class);
                    category.putExtra(RecyclerViewAdapter.TITLE, holder.mTextView.getText());
                    mContext.startActivity(category);
                } else {
//                    UnRepeatToast.showToast(mContext, "敬请期待");
                    mCategoryMode.addCategory(R.drawable.it, CategoryMode.IT);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    @Override
    public void onCategoryDataInserted(List<Map<String, Object>> categoryData) {
        mCategory = categoryData;
        notifyItemInserted(mCategory.size() - 2);
        Log.i("change", mCategory.toString());
        Log.i("size", mCategory.size() + "");
    }

    @Override
    public void OnCategoryDataRemove(List<Map<String, String>> categoryData, int removeIndex) {

    }
}