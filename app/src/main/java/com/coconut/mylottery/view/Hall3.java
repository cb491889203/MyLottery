package com.coconut.mylottery.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.coconut.mylottery.R;
import com.coconut.mylottery.engine.CommonInfoEngine;
import com.coconut.mylottery.factory.BeanFactory;
import com.coconut.mylottery.net.protocol.Message;
import com.coconut.mylottery.net.protocol.element.CurrentIssueElement;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.util.GlobalParams;
import com.coconut.mylottery.util.PromptManager;
import com.coconut.mylottery.view.manager.BaseUI;
import com.coconut.mylottery.view.manager.MiddleManager;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 购彩大厅
 * Created by Administrator on 2016/7/2 0002.
 */
public class Hall3 extends BaseUI {

    private static final String TAG = "Hall";
    /**
     * 用于显示在viewPager页面中的ListView,其中放的是3个彩种的入口
     */
    private ListView categoryList;
    private ListView categoryList1;
    private ListView categoryList2;
    /**
     * ListView的适配器
     */
    private CategoryAdapter adapter = null;
    /**
     * viewPager对象
     */
    private ViewPager viewPager;
    /**
     * 存储viewPager页面的集合.目前放3个页面进入. 福彩 / 体彩/ 高频彩
     */
    private List<View> viewPagerList;

    private MyViewPagerAdapter pagerAdapter;
    private ImageView underline;
    private TextView fcTitle;
    private TextView tcTitle;
    private TextView gpcTitle;
    private int offset;
    private Bitmap bitmap;
    private MyHallTask<Integer> ssqTask;
    private MyHallTask<Integer> d3Task;
    private MyHallTask<Integer> lc7Task;


    public Hall3(Context context) {
        super(context);
    }

    @Override
    public void onPause() {
        if (ssqTask != null) {
            ssqTask.cancel(true);
        }
        if (d3Task != null) {
            d3Task.cancel(true);
        }
        if (lc7Task != null) {
            lc7Task.cancel(true);
        }
        super.onPause();
    }

    @Override
    public void onResume() {

        TranslateAnimation ta = new TranslateAnimation(0, prePosition * GlobalParams.screenWidth / 3, 0, 0);
        ta.setDuration(0);
        ta.setFillAfter(true);
        underline.setAnimation(ta);
        ssqTask = new MyHallTask<Integer>();
        d3Task = new MyHallTask<Integer>();
        lc7Task = new MyHallTask<Integer>();

        ssqTask.executeProxy(ConstantValue.LOTTERYID_SSQ);
        d3Task.executeProxy(ConstantValue.LOTTERYID_3D);
        lc7Task.executeProxy(ConstantValue.LOTTERYID_7LC);
        super.onResume();
    }

    public void init() {
        this.middleView = (ViewGroup) View.inflate(this.context, R.layout.il_hall, null);
        categoryList = new ListView(context);
        categoryList1 = new ListView(context);
        categoryList2 = new ListView(context);
        adapter = new CategoryAdapter();
        categoryList.setAdapter(adapter);
        categoryList1.setAdapter(adapter);
        categoryList2.setAdapter(adapter);

        //viewPager的初始化
        initPagers();
        pagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(pagerAdapter);

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
        viewPagerList = new ArrayList<>();
        viewPagerList.add(categoryList);
        viewPagerList.add(categoryList1); //测试
        viewPagerList.add(categoryList2); //测试

        //        TextView textView1 = new TextView(context);
        //        textView1.setText("这是体彩的界面!");
        //        textView1.setTextSize(30);
        //        viewPagerList.add(textView1);
        //
        //        TextView textView2 = new TextView(context);
        //        textView2.setText("这是高频彩的界面!");
        //        textView2.setTextSize(30);
        //        viewPagerList.add(textView2);
    }


    private class MyViewPagerAdapter extends PagerAdapter {

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

    }

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

                new MyHallTask<Integer>().executeProxy(ConstantValue.LOTTERYID_SSQ);
                new MyHallTask<Integer>().executeProxy(ConstantValue.LOTTERYID_3D);
                new MyHallTask<Integer>().executeProxy(ConstantValue.LOTTERYID_7LC);

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

    /**
     * 改变Hall大厅的当前期消息通知
     *
     * @param issueElement 服务器返回的message中的body部分Element
     */
    private void changeNotice(CurrentIssueElement issueElement) {
        String issue = issueElement.getIssue().getTagValue();
        int lotteryID = Integer.parseInt(issueElement.getLotteryid().getTagValue());
        String resultInfo = getSummaryFromElement(issueElement);
        TextView textView1 = null;
        TextView textView2 = null;
        TextView textView3 = null;
        switch (lotteryID) {
            case ConstantValue.LOTTERYID_SSQ:
                //TextView textView1 = needUpdate.get(0);
                textView1 = (TextView) categoryList.findViewWithTag(0);
                textView2 = (TextView) categoryList1.findViewWithTag(0);
                textView3 = (TextView) categoryList2.findViewWithTag(0);
                if (textView1 != null) {
                    textView1.setText(resultInfo);
                    //ssqIssue.setText(resultInfo);
                }
                if (textView2 != null) {
                    textView2.setText(resultInfo);
                    //ssqIssue.setText(resultInfo);
                }
                if (textView3 != null) {
                    textView3.setText(resultInfo);
                    //ssqIssue.setText(resultInfo);
                }
                bundle = new Bundle();

                bundle.putString("ssqIssue", issue);
                break;
            case ConstantValue.LOTTERYID_3D:
                //修改3D玩法的信息
                //TextView textView2 = needUpdate.get(1);
                textView1 = (TextView) categoryList.findViewWithTag(1);
                textView2 = (TextView) categoryList1.findViewWithTag(1);
                textView3 = (TextView) categoryList2.findViewWithTag(1);
                if (textView1 != null) {
                    textView1.setText(resultInfo);
                    //ssqIssue.setText(resultInfo);
                }
                if (textView2 != null) {
                    textView2.setText(resultInfo);
                    //ssqIssue.setText(resultInfo);
                }
                if (textView3 != null) {
                    textView3.setText(resultInfo);
                    //ssqIssue.setText(resultInfo);
                }
                //bundle.putString("3dIssue", issue);
                break;
            case ConstantValue.LOTTERYID_7LC:
                //修改7乐彩玩法的信息
                // TextView textView3 = needUpdate.get(2);
                textView1 = (TextView) categoryList.findViewWithTag(2);
                textView2 = (TextView) categoryList1.findViewWithTag(2);
                textView3 = (TextView) categoryList2.findViewWithTag(2);
                if (textView1 != null) {
                    textView1.setText(resultInfo);
                    //ssqIssue.setText(resultInfo);
                }
                if (textView2 != null) {
                    textView2.setText(resultInfo);
                    //ssqIssue.setText(resultInfo);
                }
                if (textView3 != null) {
                    textView3.setText(resultInfo);
                    //ssqIssue.setText(resultInfo);
                }
                //bundle.putString("7lcIssue", issue);
                break;
            default:

                break;
        }

    }

    private String getSummaryFromElement(CurrentIssueElement issueElement) {
        String issue = issueElement.getIssue().getTagValue();
        String lasttime = getLasttime(issueElement.getLasttime().getTagValue());
        String commonInfo = context.getResources().getString(R.string.is_hall_common_summary);
        return StringUtils.replaceEach(commonInfo, new String[]{"ISSUE", "TIME"}, new String[]{issue,
                lasttime});
    }

    @Override
    public void onClick(View v) {


        super.onClick(v);
    }

    @Override
    public int getId() {
        return ConstantValue.VIEW_HALL;
    }


    /**
     * 将秒时间转换成日时分格式
     *
     * @param lasttime
     * @return
     */
    public String getLasttime(String lasttime) {
        StringBuffer result = new StringBuffer();
        if (StringUtils.isNumericSpace(lasttime)) {
            int time = Integer.parseInt(lasttime);
            int day = time / (24 * 60 * 60);
            result.append(day).append("天");
            if (day > 0) {
                time = time - day * 24 * 60 * 60;
            }
            int hour = time / 3600;
            result.append(hour).append("时");
            if (hour > 0) {
                time = time - hour * 60 * 60;
            }
            int minute = time / 60;
            result.append(minute).append("分");
        }
        return result.toString();
    }

    private class CategoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        // 资源信息
        private int[] logoResIds = new int[]{R.drawable.id_ssq, R.drawable.id_3d, R.drawable.id_qlc};

        private int[] titleResIds = new int[]{R.string.is_hall_ssq_title, R.string.is_hall_3d_title, R
                .string.is_hall_qlc_title};

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.il_hall_lottery_item, null);
                holder.logo = (ImageView) convertView.findViewById(R.id.ii_hall_lottery_logo);
                holder.title = (TextView) convertView.findViewById(R.id.ii_hall_lottery_title);
                holder.summary = (TextView) convertView.findViewById(R.id.ii_hall_lottery_summary);

                //                needUpdate.add(position, holder.summary);
                // 给 holder.summary 设置标签,方便后面取出.
                holder.summary.setTag(position);
                holder.bet = (ImageView) convertView.findViewById(R.id.ii_hall_lottery_bet);
                holder.bet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MiddleManager.getInstance().changeUI(PlaySSQ.class, bundle);
                    }
                });

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.logo.setImageResource(logoResIds[position]);
            holder.title.setText(titleResIds[position]);


            return convertView;
        }

    }

    private class ViewHolder {

        ImageView logo;
        TextView title;
        TextView summary;
        ImageView bet;


    }

    private class MyHallTask<Integer> extends MyAsyncTask<Integer> {

        @Override
        protected Message doInBackground(Integer... params) {
            CommonInfoEngine impl = BeanFactory.newInstance(context).getImpl(CommonInfoEngine.class);
            return impl.getCommonInfo((java.lang.Integer) params[0]);
        }

        @Override
        protected void onPostExecute(Message message) {
            if (message != null) {
                if (message.getBody().getOelement().getErrorcode().equals(ConstantValue.SUCCESS)) {
                    CurrentIssueElement issueElement = (CurrentIssueElement) message.getBody().getElements
                            ().get(0);
                    changeNotice(issueElement);

                } else {
                    PromptManager.showToast(context, message.getBody().getOelement().getErrormsg());
                }
            } else {
                PromptManager.showToast(context, "返回的数据为空message");
            }


            super.onPostExecute(message);
        }

    }
}

