package com.coconut.mylottery.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.coconut.mylottery.R;
import com.coconut.mylottery.bean.ShoppingCart;
import com.coconut.mylottery.bean.Ticket;
import com.coconut.mylottery.engine.CommonInfoEngine;
import com.coconut.mylottery.factory.BeanFactory;
import com.coconut.mylottery.net.protocol.Message;
import com.coconut.mylottery.net.protocol.Oelement;
import com.coconut.mylottery.net.protocol.element.CurrentIssueElement;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.util.PromptManager;
import com.coconut.mylottery.view.custom.MyGridView;
import com.coconut.mylottery.view.manager.BaseUI;
import com.coconut.mylottery.view.manager.BottomManager;
import com.coconut.mylottery.view.manager.MiddleManager;
import com.coconut.mylottery.view.manager.PlayGamesUI;
import com.coconut.mylottery.view.manager.TitleManager;
import com.coconut.mylottery.view.sensor.ShakeListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/7/6 0006.
 */
public class PlaySSQ extends BaseUI implements PlayGamesUI {

    private static final String TAG = "PlaySSQ";
    private MyGridView redContainer;
    private GridView blueContainer;
    private Button randomRed;
    private Button randomBlue;
    /**
     * 红球已选中的号码.
     */
    private ArrayList<Integer> redNums;
    /**
     * 篮球已选中的号码
     */
    private ArrayList<Integer> blueNums;
    private PoolAdapter redAdapter;
    private PoolAdapter blueAdapter;

    private SensorManager manager;
    private ShakeListener listener;

    /**
     * 窗体
     */
    //    private ScrollView scrollView;
    public PlaySSQ(Context context) {
        super(context);
    }

    @Override
    public void init() {
        middleView = (ViewGroup) View.inflate(context, R.layout.il_playssq, null);
        //        scrollView = (ScrollView)findViewById(R.id.scrollview);
        redContainer = (MyGridView) findViewById(R.id.ii_ssq_red_number_container);
        blueContainer = (GridView) findViewById(R.id.ii_ssq_blue_number_container);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.id_defalut_ball);
        int width = (int) (bitmap.getWidth() * 0.8);
        redContainer.setColumnWidth(width);
        blueContainer.setColumnWidth(width);
        //        redContainer.setHorizontalSpacing(DensityUtil.dip2px(context, 5));
        //        redContainer.setVerticalSpacing(DensityUtil.dip2px(context, 5));
        //        blueContainer.setHorizontalSpacing(DensityUtil.dip2px(context, 5));
        //        blueContainer.setVerticalSpacing(DensityUtil.dip2px(context, 5));
        randomRed = (Button) findViewById(R.id.ii_ssq_random_red);
        randomBlue = (Button) findViewById(R.id.ii_ssq_random_blue);
        redNums = new ArrayList<Integer>();
        blueNums = new ArrayList<Integer>();

        redAdapter = new PoolAdapter(33, redNums, R.drawable.id_redball);
        blueAdapter = new PoolAdapter(16, blueNums, R.drawable.id_blueball);

        redContainer.setAdapter(redAdapter);
        blueContainer.setAdapter(blueAdapter);

        //传感器监听
        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    protected void setListener() {
        randomBlue.setOnClickListener(this);
        randomRed.setOnClickListener(this);

        redContainer.setOnActionUpListener(new MyGridView.OnActionUpListener() {
            @Override
            public void onActionUp(View view, int position) {
                // 判断当前点击的item是否被选中了
                if (!redNums.contains(position + 1)) {
                    // 如果没有被选中
                    // 背景图片切换到红色
                    view.setBackgroundResource(R.drawable.id_redball);
                    view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ia_ball_shake));
                    redNums.add(position + 1);
                    changeNotice();
                } else {
                    // 被选中
                    // 还原背景图片
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    redNums.remove((Object) (position + 1));
                }
                changeNotice();

            }
        });
        blueContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 判断当前点击的item是否被选中了
                if (!blueNums.contains(position + 1)) {
                    // 如果没有被选中
                    // 背景图片切换到红色
                    view.setBackgroundResource(R.drawable.id_blueball);
                    // 摇晃的动画
                    view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ia_ball_shake));
                    blueNums.add(position + 1);
                } else {
                    // 被选中
                    // 还原背景图片
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    blueNums.remove((Object) (position + 1));
                }

                changeNotice();
            }
        });
        blueContainer.setOnTouchListener(new GridView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return MotionEvent.ACTION_MOVE == event.getAction() ? blueContainer.onTouchEvent(event) :
                        false;
            }
        });


    }

    @Override
    public int getId() {
        return ConstantValue.VIEW_SSQ;
    }

    /**
     * 修改标题栏的信息.从bundle中拿取Hall大厅保存的从服务器获取的信息.
     */
    private void changeTitle() {
        if (bundle != null) {
            String ssqIssue = bundle.getString("ssqIssue");
            if (ssqIssue != null) {
              String  titleInfo = "双色球第" + ssqIssue + "期";
                TitleManager.getInstance().changeTitleContent(titleInfo);
            } else {
                TitleManager.getInstance().changeTitleContent("选号页面");
                Log.i(TAG, "changeTitle: bundle中没有issue信息.!!使用默认信息.");
            }

        } else {
            TitleManager.getInstance().changeTitleContent("选号页面");
            Log.i(TAG, "changeTitle: bundle为空,切换页面没有传入bundle!!使用默认信息");
        }
    }

    @Override
    public void onPause() {
        manager.unregisterListener(listener);
        super.onPause();
    }

    @Override
    public void onResume() {
        changeTitle();
        clear();
        listener = new ShakeListener(context) {
            @Override
            public void randomCure() {
                randomSSQ();

            }
        };
        manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }

    /**
     * 机选红蓝球
     */
    private void randomSSQ() {
        Random random = new Random();
        // 机选红球
        redNums.clear();
        blueNums.clear();
        while (redNums.size() < 6) {
            int num = random.nextInt(33) + 1;
            if (redNums.contains(num)) {
                continue;
            }
            redNums.add(num);
        }
        int num = random.nextInt(16) + 1;
        blueNums.add(num);

        // 处理展示
        redAdapter.notifyDataSetChanged();
        blueAdapter.notifyDataSetChanged();
        changeNotice();
    }

    @Override
    public void onClick(View v) {
        Random random = new Random();
        switch (v.getId()) {
            case R.id.ii_ssq_random_red:
                // 机选红球
                redNums.clear();
                while (redNums.size() < 6) {
                    int num = random.nextInt(33) + 1;

                    if (redNums.contains(num)) {
                        continue;
                    }
                    redNums.add(num);
                }

                // 处理展示
                redAdapter.notifyDataSetChanged();
                changeNotice();
                break;
            case R.id.ii_ssq_random_blue:
                blueNums.clear();
                int num = random.nextInt(16) + 1;
                blueNums.add(num);

                blueAdapter.notifyDataSetChanged();
                changeNotice();
                break;
        }
        super.onClick(v);

    }

    private class PoolAdapter extends BaseAdapter {

        List<Integer> slectedNums;
        int nums;
        int slectedBgResId;

        public PoolAdapter(int nums, List<Integer> slectedNums, int resId) {
            this.nums = nums;
            this.slectedNums = slectedNums;
            this.slectedBgResId = resId;
        }

        @Override
        public int getCount() {
            return nums;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TextView ball = new TextView(context);

            ball.setBackgroundResource(R.drawable.id_defalut_ball);
            DecimalFormat format = new DecimalFormat("00");
            ball.setTextSize(16);
            TextPaint paint = ball.getPaint();
            paint.setFakeBoldText(true);
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.id_defalut_ball);
            int width = (int) (bitmap.getWidth() * 0.8);
            ball.setLayoutParams(new ViewGroup.LayoutParams(width, width));
            //            int px1 = DensityUtil.dip2px(context, 5);
            //            ball.setWidth(50);
            //            ball.setHeight(48);
            //            Log.i(TAG, "getView: ball的宽:高值-----" + ball.getWidth() + "------" + ball
            // .getHeight());
            ball.setGravity(Gravity.CENTER);
            ball.setText(format.format(position + 1));

            // 获取到用户已选号码的集合，判读集合中有，背景图片修改为红色
            if (slectedNums.contains(position + 1)) {
                ball.setBackgroundResource(slectedBgResId);
                ball.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ia_ball_shake));

            } else {
                ball.setBackgroundResource(R.drawable.id_defalut_ball);
            }

            return ball;
        }
    }

    private void changeNotice() {
        String notice = "";
        // 以一注为分割
        if (redNums.size() < 6) {
            notice = "您还需要选择" + (6 - redNums.size()) + "个红球";
        } else if (blueNums.size() == 0) {
            notice = "您还需要选择" + 1 + "个蓝球";
        } else if (redNums.size()>18||blueNums.size()>6) {
            notice = "您选的注数太多了";
        } else {
            notice = "共 " + calc() + " 注 " + calc() * 2 + " 元";
        }
        BottomManager.getInstrance().changeGameBottomNotice(notice);
    }

    /**
     * 计算注数
     *
     * @return
     */
    private int calc() {
        int redC = (int) (factorial(redNums.size()) / (factorial(6) * factorial(redNums.size() - 6)));
        int blueC = blueNums.size();
        return redC * blueC;
    }

    /**
     * 计算一个数的阶乘
     *
     * @param num
     * @return
     */
    private long factorial(int num) {
        // num=7 7*6*5...*1

        if (num > 1) {
            return num * factorial(num - 1);
        } else if (num == 1 || num == 0) {
            return 1;
        } else {
            throw new IllegalArgumentException("num >= 0");
        }
    }

    @Override
    public void clear() {
        blueNums.clear();
        redNums.clear();

        changeNotice();
        redAdapter.notifyDataSetChanged();
        blueAdapter.notifyDataSetChanged();

    }

    @Override
    public void done() {
        if ((redNums.size() >= 6 && blueNums.size() >= 1)&&redNums.size() <= 18&& blueNums.size() <= 6) {

            if (bundle != null) {
                Ticket ticket = new Ticket();
                DecimalFormat decimalFormat = new DecimalFormat("00");
                StringBuffer redBuffer = new StringBuffer();
                for (Integer i : redNums) {
                    redBuffer.append(" ").append(decimalFormat.format(i));
                }
                ticket.setRedNum(redBuffer.substring(1));
                StringBuffer blueBuffer = new StringBuffer();
                for (Integer i : blueNums) {
                    blueBuffer.append(" ").append(decimalFormat.format(i));
                }
                ticket.setBlueNum(blueBuffer.substring(1));

                ticket.setNum(calc());

                ShoppingCart cart = ShoppingCart.newInstance();
                cart.getTickets().add(ticket);
                cart.setIssue(bundle.getString("ssqIssue"));
                cart.setLotteryId(ConstantValue.LOTTERYID_SSQ);

                // ⑥界面跳转——购物车展示
                MiddleManager.getInstance().changeUI(Shopping.class, bundle);
            } else {
                //重新获取期次
                getCurrentIssueInfoAgain();

            }
        } else {
            String notice = "";
            if (redNums.size() < 6) {
                notice = "您还需要选择" + (6 - redNums.size()) + "个红球";
            } else if (blueNums.size() == 0) {
                notice = "您还需要选择" + 1 + "个蓝球";
            } else if (redNums.size() >= 18 || blueNums.size() >= 6) {
                notice = "请选择合理的注数";
            } else {
                notice = "共 " + calc() + " 注 " + calc() * 2 + " 元";
            }
            PromptManager.showErrorDialog(context, notice);
        }

    }
    private void getCurrentIssueInfoAgain() {
        new MyAsyncTask<Integer>() {
            protected void onPreExecute() {
                // 显示滚动条
                PromptManager.showProgressDialog(context);
            }

            @Override
            protected Message doInBackground(Integer... params) {
                // 获取数据——业务的调用
                CommonInfoEngine engine = BeanFactory.newInstance(context).getImpl(CommonInfoEngine.class);
                return engine.getCommonInfo(params[0]);
            }

            @Override
            protected void onPostExecute(Message result) {
                PromptManager.closeProgressDialog();
                // 更新界面
                if (result != null) {
                    Oelement oelement = result.getBody().getOelement();

                    if (ConstantValue.SUCCESS.equals(oelement.getErrorcode())) {
                        CurrentIssueElement element = (CurrentIssueElement) result.getBody().getElements()
                                .get(0);
                        // 创建bundle
                        if (bundle != null) {
                            bundle.putString("ssqIssue", element.getIssue().getTagValue());
                        } else {
                            bundle = new Bundle();
                            bundle.putString("ssqIssue", element.getIssue().getTagValue());
                        }
                        changeTitle();
                        //重新封装数据
                        Ticket ticket = new Ticket();
                        DecimalFormat decimalFormat = new DecimalFormat("00");
                        StringBuffer redBuffer = new StringBuffer();
                        for (Integer i : redNums) {
                            redBuffer.append(" ").append(decimalFormat.format(i));
                        }
                        ticket.setRedNum(redBuffer.substring(1));
                        StringBuffer blueBuffer = new StringBuffer();
                        for (Integer i : blueNums) {
                            blueBuffer.append(" ").append(decimalFormat.format(i));
                        }
                        ticket.setBlueNum(blueBuffer.substring(1));

                        ticket.setNum(calc());

                        ShoppingCart cart = ShoppingCart.newInstance();
                        cart.getTickets().add(ticket);
                        cart.setIssue(bundle.getString("ssqIssue"));
                        cart.setLotteryId(ConstantValue.LOTTERYID_SSQ);

                        // ⑥界面跳转——购物车展示
                        MiddleManager.getInstance().changeUI(Shopping.class, bundle);
                    } else {
                        PromptManager.showToast(context, oelement.getErrormsg());
                    }
                } else {
                    // 可能：网络不通、权限、服务器出错、非法数据……
                    // 如何提示用户
                    PromptManager.showToast(context, "服务器忙，请稍后重试……");
                }

                super.onPostExecute(result);
            }
        }.executeProxy(ConstantValue.LOTTERYID_SSQ);
    }
    private void getCurrentIssueInfo() {
        new MyAsyncTask<Integer>() {
            protected void onPreExecute() {
                // 显示滚动条
                PromptManager.showProgressDialog(context);
            }

            @Override
            protected Message doInBackground(Integer... params) {
                // 获取数据——业务的调用
                CommonInfoEngine engine = BeanFactory.newInstance(context).getImpl(CommonInfoEngine.class);
                return engine.getCommonInfo(params[0]);
            }

            @Override
            protected void onPostExecute(Message result) {
                PromptManager.closeProgressDialog();
                // 更新界面
                if (result != null) {
                    Oelement oelement = result.getBody().getOelement();

                    if (ConstantValue.SUCCESS.equals(oelement.getErrorcode())) {
                        CurrentIssueElement element = (CurrentIssueElement) result.getBody().getElements()
                                .get(0);
                        // 创建bundle
                        if (bundle != null) {
                            bundle.putString("ssqIssue", element.getIssue().getTagValue());
                        } else {
                            bundle = new Bundle();
                            bundle.putString("ssqIssue", element.getIssue().getTagValue());
                        }
                        changeTitle();
                    } else {
                        PromptManager.showToast(context, oelement.getErrormsg());
                    }
                } else {
                    // 可能：网络不通、权限、服务器出错、非法数据……
                    // 如何提示用户
                    PromptManager.showToast(context, "服务器忙，请稍后重试……");
                }

                super.onPostExecute(result);
            }
        }.executeProxy(ConstantValue.LOTTERYID_SSQ);
    }

}
