package com.coconut.mylottery.view;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coconut.mylottery.R;
import com.coconut.mylottery.bean.ShoppingCart;
import com.coconut.mylottery.bean.Ticket;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.util.GlobalParams;
import com.coconut.mylottery.util.PromptManager;
import com.coconut.mylottery.view.manager.BaseUI;
import com.coconut.mylottery.view.manager.MiddleManager;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/7/8 0008.
 */
public class Shopping extends BaseUI {

    private static final String TAG = "Shopping";
    private Button addOptional;
    private Button addRandom;
    private ImageButton shoppingListClear;
    private TextView notice;
    private Button buy;
    private ListView shoppingList;
    private ShoppingAdapter adapter;

    public Shopping(Context context) {
        super(context);
    }

    @Override
    public int getId() {
        return ConstantValue.VIEW_SHOPPING;
    }

    @Override
    public void init() {
        middleView = (RelativeLayout) View.inflate(context, R.layout.il_shopping, null);
        addOptional = (Button) findViewById(R.id.ii_add_optional);
        addRandom = (Button) findViewById(R.id.ii_add_random);
        shoppingListClear = (ImageButton) findViewById(R.id.ii_shopping_list_clear);
        notice = (TextView) findViewById(R.id.ii_shopping_lottery_notice);
        buy = (Button) findViewById(R.id.ii_lottery_shopping_buy);
        shoppingList = (ListView) findViewById(R.id.ii_shopping_list);

        adapter = new ShoppingAdapter();
        shoppingList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        changeNotice();
        super.onResume();
    }

    // ①填充购物车
    // ②添加自选+添加机选
    // ③清空购物车
    // ④高亮的提示信息处理
    // ⑤购买

    @Override
    protected void setListener() {
        addOptional.setOnClickListener(this);
        addRandom.setOnClickListener(this);
        shoppingListClear.setOnClickListener(this);
        buy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ii_add_optional:
                //返回选号页面自选.
                MiddleManager.getInstance().changeUI(PlaySSQ.class);
                break;
            case R.id.ii_add_random:
                //添加一注到当前页面.
                addRandom();
                changeNotice();
                break;
            case R.id.ii_shopping_list_clear:
                //清空购物车
                ShoppingCart.newInstance().getTickets().clear();
                adapter.notifyDataSetChanged();
                changeNotice();
                break;
            case R.id.ii_lottery_shopping_buy:
                //购买
                //1.购物车中是否有投注
                if (ShoppingCart.newInstance().getTickets().size() >= 1) {
                    if (GlobalParams.isLogin) {
                        //2.判断用户是否登录---被动登录
                        if (ShoppingCart.newInstance().getLotteryvalue() <= GlobalParams.MONEY) {
                            //3.判断用户的余额是否满足投注需求.
                            //4.界面跳转到追期和倍投设置界面.
                            MiddleManager.getInstance().changeUI(PreBet.class);
                            Log.i(TAG, "onClick: 点击购买键.");

                        } else {
                            //提示:充值模块,用户充值界面.
                            PromptManager.showToast(context,"余额不足");
                        }
                    } else {
                        //提示用户登录--跳转到登录界面
                        PromptManager.showToast(context, "请登录!");
                        MiddleManager.getInstance().changeUI(UserLogin.class);
                    }
                } else {
                    //提示需要选择一注
                    PromptManager.showToast(context, "需要至少投注一注!");
                }

                break;
        }
        super.onClick(v);
    }

    private void addRandom() {
        // 机选一注
        Random random = new Random();
        List<Integer> redNums = new ArrayList<Integer>();
        List<Integer> blueNums = new ArrayList<Integer>();

        // 机选红球
        while (redNums.size() < 6) {
            int num = random.nextInt(33) + 1;

            if (redNums.contains(num)) {
                continue;
            }
            redNums.add(num);
        }
        int num = random.nextInt(16) + 1;
        blueNums.add(num);

        // 封装Ticket
        Ticket ticket = new Ticket();
        DecimalFormat decimalFormat = new DecimalFormat("00");
        StringBuffer redBuffer = new StringBuffer();
        for (Integer item : redNums) {
            // redBuffer.append(decimalFormat.format(item)).append(" ");
            redBuffer.append(" ").append(decimalFormat.format(item));
        }
        ticket.setRedNum(redBuffer.substring(1));

        StringBuffer blueBuffer = new StringBuffer();
        for (Integer item : blueNums) {
            blueBuffer.append(" ").append(decimalFormat.format(item));
        }

        ticket.setBlueNum(blueBuffer.substring(1));

        ticket.setNum(1);
        // 添加到购物车
        ShoppingCart.newInstance().getTickets().add(ticket);
        // 更新界面
        adapter.notifyDataSetChanged();
    }

    /**
     * 改变总计的显示信息.
     */
    private void changeNotice() {
        Integer lotterynumber = ShoppingCart.newInstance().getLotterynumber();
        Integer lotteryvalue = ShoppingCart.newInstance().getLotteryvalue();
        //<![CDATA[注数：&#160;&#160;<font color="#ff0000">NUM</font>&#160;&#160;注&#160;&#160;金额：&#160;&#160;
        // <font color="#ff0000">MONEY</font>元]]>
        String noticeInfo = context.getResources().getString(R.string.is_shopping_list_notice);
        noticeInfo = StringUtils.replaceEach(noticeInfo, new String[]{"NUM", "MONEY"}, new
                String[]{lotterynumber.toString(), lotteryvalue.toString()});

        // Html.fromHtml(notice):将notice里面的内容转换
        notice.setText(Html.fromHtml(noticeInfo));
    }

    private class ShoppingAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ShoppingCart.newInstance().getTickets().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.il_shopping_row, null);

                holder.delete = (ImageButton) convertView.findViewById(R.id.ii_shopping_item_delete);
                holder.redNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_reds);
                holder.blueNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_blues);
                holder.num = (TextView) convertView.findViewById(R.id.ii_shopping_item_money);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Ticket ticket = ShoppingCart.newInstance().getTickets().get(position);
            holder.redNum.setText(ticket.getRedNum());
            holder.blueNum.setText(ticket.getBlueNum());
            holder.num.setText(String.valueOf(ticket.getNum()) + "注");

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCart.newInstance().getTickets().remove(position);
                    notifyDataSetChanged();
                    changeNotice();

                }
            });
            return convertView;
        }

        class ViewHolder {

            ImageButton delete;
            TextView redNum;
            TextView blueNum;
            TextView num;
        }
    }
}
