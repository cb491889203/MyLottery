package com.coconut.mylottery.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.coconut.mylottery.R;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.util.GlobalParams;
import com.coconut.mylottery.view.fragment.LotteryListFragment;
import com.coconut.mylottery.view.fragment.LotteryListFragment1;
import com.coconut.mylottery.view.fragment.LotteryListFragment2;
import com.coconut.mylottery.view.manager.BaseUI;
import com.coconut.mylottery.view.manager.MiddleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 购彩大厅
 * Created by Administrator on 2016/7/2 0002.
 */
public class Hall extends BaseUI {

    private static final String TAG = "Hall";

    /**
     * viewPager对象
     */
    private ViewPager viewPager;
    /**
     * 存储viewPager页面的Fragment集合.目前放3个页面进入. 福彩 / 体彩/ 高频彩
     */
    private List<Fragment> fragmentPagerList;

    /**
     * ViewPager的适配器
     */
    private MyFragmentAdapter fragmentAdapter;
    /**
     * 下划线图片
     */
    private ImageView underline;
    private TextView fcTitle;
    private TextView tcTitle;
    private TextView gpcTitle;
    private int offset;
    private Bitmap bitmap;


    public Hall(Context context) {
        super(context);
    }


    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        TranslateAnimation ta = new TranslateAnimation(0, prePosition * GlobalParams.screenWidth / 3, 0, 0);
        ta.setDuration(0);
        ta.setFillAfter(true);
        underline.setAnimation(ta);


        super.onResume();
    }

    public void init() {
        this.middleView = (ViewGroup) View.inflate(this.context, R.layout.il_hall, null);

        //viewPager的初始化
        initPagers();

        //选项卡及三个选项卡标题的初始化
        underline = (ImageView) findViewById(R.id.ii_category_selector);
        fcTitle = (TextView) findViewById(R.id.ii_category_fc);
        tcTitle = (TextView) findViewById(R.id.ii_category_tc);
        gpcTitle = (TextView) findViewById(R.id.ii_category_gpc);
        fcTitle.setTextColor(context.getResources().getColor(R.color.red_slight));

        //下划线的图片,并获取下划线的位置差值offset
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.id_category_selector);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        GlobalParams.screenWidth = outMetrics.widthPixels;
        offset = (GlobalParams.screenWidth - 3 * bitmap.getWidth()) / 6;

        //设置下划线的初始位置
        Matrix matrix = new Matrix();
        matrix.setTranslate(offset, 0);
        //        matrix.postTranslate(offset, 0);
        underline.setImageMatrix(matrix);

    }

    /**
     * 将3个子页面添加进viewPager的List集合中.方便在viewPager的适配中去出.
     */
    private void initPagers() {
        viewPager = (ViewPager) findViewById(R.id.il_hall_viewpager);
        fragmentPagerList = new ArrayList<>();
        // 普通的fragment
        //FCFragment fcFragment = new FCFragment();
        //TCFragment tcFragment = new TCFragment();
        //QLCFragment qlcFragment = new QLCFragment();
        //
        //fragmentPagerList.add(fcFragment);
        //fragmentPagerList.add(tcFragment);
        //fragmentPagerList.add(qlcFragment);

        // ListFragment
        LotteryListFragment fragment = new LotteryListFragment();
        LotteryListFragment1 fragment1 = new LotteryListFragment1();
        LotteryListFragment2 fragment2 = new LotteryListFragment2();

        fragmentPagerList.add(fragment);
        fragmentPagerList.add(fragment1);
        fragmentPagerList.add(fragment2);



        FragmentManager fragmentManager = MiddleManager.getInstance().getFragmentManager();
        Log.i(TAG, "initPagers: fragmentManager====="+fragmentManager);
        fragmentAdapter = new MyFragmentAdapter(fragmentManager);
        viewPager.setAdapter(fragmentAdapter);


    }

    /**
     * 自定义的Fragment适配器.
     */
    private class MyFragmentAdapter extends FragmentPagerAdapter {


        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentPagerList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentPagerList.size();
        }
    }

   /* private class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = viewPagerList.get(position);
            //            ViewGroup parent = (ViewGroup) view.getParent();
            //            parent.removeAllViews();
            //            view = container.getChildAt(position);
            container.removeView(view);

        }

        @Override
        public int getCount() {
            if (viewPagerList != null) {
                return viewPagerList.size();
            } else {
                Log.i(TAG, "getCount: viewPagerList集合为空@!");
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if (viewPagerList != null) {
                view = viewPagerList.get(position);

                //让每个页面都显示福彩.
                //                view = viewPagerList.get(0);
                //                ViewGroup parent = (ViewGroup) view.getParent();
                //                if (parent != null) {
                //                    parent.removeAllViews();
                //                }
            } else {
                Log.i(TAG, "instantiateItem: viewPagerList集合为空@!");
            }

            container.addView(view);

            return view;
        }

    }*/

    //    private List<TextView> needUpdate = new ArrayList<>();

    private int prePosition;

    protected void setListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //这里注意translate动画是相对于imageview  underline 的初始位置的.而不是这个view里的图片的位置的.
                TranslateAnimation ta = new TranslateAnimation(prePosition * GlobalParams.screenWidth / 3,
                        position * GlobalParams.screenWidth / 3, 0, 0);
                ta.setDuration(200);
                ta.setFillAfter(true);
                underline.setAnimation(ta);

                fcTitle.setTextColor(Color.BLACK);
                tcTitle.setTextColor(Color.BLACK);
                gpcTitle.setTextColor(Color.BLACK);


                switch (position) {
                    case 0:
                        fcTitle.setTextColor(context.getResources().getColor(R.color.red_slight));
                        break;
                    case 1:
                        tcTitle.setTextColor(context.getResources().getColor(R.color.red_slight));
                        break;
                    case 2:
                        gpcTitle.setTextColor(context.getResources().getColor(R.color.red_slight));
                        break;
                }


                prePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fcTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        tcTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        gpcTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
    }

    @Override
    public int getId() {
        return ConstantValue.VIEW_HALL;
    }


}

