package com.hbs.hashbrownsys.locallinkers.customtab;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.SubCat_ViewPagerAdapter;

public class Sub_SlidingTabLayout extends HorizontalScrollView {

    public interface TabColorizer {
        int getIndicatorColor(int position);
    }

    private TabType tabType = TabType.TEXT;
    Typeface next_font;
    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;
    private int mTitleOffset;
    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private boolean mDistributeEvenly;
    private int mTabViewImageViewId;
    private ViewPager mViewPager;
    private SparseArray<String> mContentDescriptions = new SparseArray<String>();
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;
    private final Sub_SlidingTabStrip  mTabStrip;

    public Sub_SlidingTabLayout(Context context) {
        this(context, null);
    }

    public Sub_SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Sub_SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);
        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);
        mTabStrip = new Sub_SlidingTabStrip(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }


    public void setCustomTabColorizer(TabColorizer tabColorizer)
    {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    public void setDistributeEvenly(boolean distributeEvenly) {
        mDistributeEvenly = distributeEvenly;
    }


    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();
        mViewPager = viewPager;
        if (viewPager != null) {
            //setOffscreenPageLimit(12);
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }


    private void populateTabStrip()
    {
        final SubCat_ViewPagerAdapter adapter = (SubCat_ViewPagerAdapter) mViewPager.getAdapter();
        final View.OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++)
        {
            View tabView = null;
            tabView = LayoutInflater.from(getContext()).inflate(R.layout.sub_cat_layout, mTabStrip, false);
            TextView tabtext = (TextView) tabView.findViewById(R.id.tab_layout_txt);
            next_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/MyriadPro-Regular.otf");
            tabtext.setTypeface(next_font);
            ImageView iconImageView = (ImageView) tabView.findViewById(R.id.tab_layout_icon);
            //  iconImageView.setImageDrawable(getContext().getResources().getDrawable(getDrawableId(i)));
            tabtext.setText(adapter.getPageTitle(i));
            iconImageView.setImageDrawable(getContext().getResources().getDrawable(adapter.getDrawableId(i)));
            tabView.setOnClickListener(tabClickListener);
            mTabStrip.addView(tabView);
        }

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                Log.d("", "position in sliding tab mViewPagerPageChangeListener:-" + position);
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);
            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null) ? (int) (positionOffset * selectedTitle.getWidth()) : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }


        @Override
        public void onPageSelected(int position) {
            Log.d("", "position in sliding tab onPageSelected:-" + position);
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                Log.d("", "working:-" + position);
                mTabStrip.getChildAt(i).setSelected(false);
            }
            mTabStrip.getChildAt(position).setSelected(true);

            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                Log.d("", "SCROLL_STATE_IDLE:-" + position);
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }
    }

    private class TabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    Log.d("", "position in sliding tab:-" + i);
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    public enum TabType {
        TEXT, ICON;
    }

}

