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
import android.widget.TextView;

import com.coconut.mylottery.R;
import com.coconut.mylottery.bean.ShoppingBasket;
import com.coconut.mylottery.bean.ShoppingCart;
import com.coconut.mylottery.bean.Ticket;
import com.coconut.mylottery.bean.User;
import com.coconut.mylottery.engine.UserEngine;
import com.coconut.mylottery.factory.BeanFactory;
import com.coconut.mylottery.net.protocol.Message;
import com.coconut.mylottery.net.protocol.Oelement;
import com.coconut.mylottery.net.protocol.element.BetElement;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.util.GlobalParams;
import com.coconut.mylottery.util.PromptManager;
import com.coconut.mylottery.view.manager.BaseUI;
import com.coconut.mylottery.view.manager.MiddleManager;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 追期和倍投页面
 * Created by Administrator on 2016/7/8 0008.
 */
public class PreBet extends BaseUI {

    private static final String TAG = "PreBet";
    // 通用三步

    // ①填充ListView
    // ②提示信息处理
    // ③倍投和追期的设置
    // ④立即购买

    private TextView bettingNum;// 注数
    private TextView bettingMoney;// 金额

    private Button subAppnumbers;// 减少倍投
    private TextView appnumbersInfo;// 倍数
    private Button addAppnumbers;// 增加倍投

    private Button subIssueflagNum;// 减少追期
    private TextView issueflagNumInfo;// 追期
    private Button addIssueflagNum;// 增加追期

    private ImageButton lotteryPurchase;// 投注
    private ListView shoppingList;// 购物车展示

    private ShoppingAdapter adapter;

    public PreBet(Context context) {
        super(context);
    }

    @Override
    public int getId() {
        return ConstantValue.VIEW_PREBET;
    }

    @Override
    public void init() {
        middleView = (ViewGroup) View.inflate(context, R.layout.il_play_prefectbetting, null);

        bettingNum = (TextView) findViewById(R.id.ii_shopping_list_betting_num);
        bettingMoney = (TextView) findViewById(R.id.ii_shopping_list_betting_money);

        subAppnumbers = (Button) findViewById(R.id.ii_sub_appnumbers);
        appnumbersInfo = (TextView) findViewById(R.id.ii_appnumbers);
        addAppnumbers = (Button) findViewById(R.id.ii_add_appnumbers);

        subIssueflagNum = (Button) findViewById(R.id.ii_sub_issueflagNum);
        issueflagNumInfo = (TextView) findViewById(R.id.ii_issueflagNum);
        addIssueflagNum = (Button) findViewById(R.id.ii_add_issueflagNum);

        lotteryPurchase = (ImageButton) findViewById(R.id.ii_lottery_purchase);
        shoppingList = (ListView) findViewById(R.id.ii_lottery_shopping_list);

        adapter = new ShoppingAdapter();
        shoppingList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        changeNotice();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    protected void setListener() {
        // 倍数
        addAppnumbers.setOnClickListener(this);
        subAppnumbers.setOnClickListener(this);
        // 追期
        addIssueflagNum.setOnClickListener(this);
        subIssueflagNum.setOnClickListener(this);
        // 投注
        lotteryPurchase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ii_add_appnumbers:
                // 增加倍数
                boolean result = ShoppingCart.newInstance().addAppnumbers(true);
                if (result) {
                    Log.i(TAG, "onClick: 点击增加倍数");
                    changeNotice();
                }
                break;
            case R.id.ii_sub_appnumbers:
                // 减少倍数
                if (ShoppingCart.newInstance().addAppnumbers(false)) {
                    changeNotice();
                }
                break;
            case R.id.ii_add_issueflagNum:
                // 增加追期
                if (ShoppingCart.newInstance().addIssuesnumbers(true)) {
                    changeNotice();
                }
                break;
            case R.id.ii_sub_issueflagNum:
                // 减少追期
                if (ShoppingCart.newInstance().addIssuesnumbers(false)) {
                    changeNotice();
                }
                break;

            case R.id.ii_lottery_purchase:
                // 投注请求
                User user = new User();
                user.setUsername(GlobalParams.USERNAME);
                new MyAsyncTask<User>() {
                    @Override
                    protected void onPreExecute() {
                        PromptManager.showProgressDialog(context);
                        super.onPreExecute();
                    }

                    @Override
                    protected Message doInBackground(User... params) {
                        UserEngine engine = BeanFactory.newInstance(context).getImpl(UserEngine.class);
                        return engine.bet(params[0]);
                    }

                    protected void onPostExecute(Message result) {
                        PromptManager.closeProgressDialog();
                        if (result != null) {
                            Oelement oelement = result.getBody().getOelement();
                            if (ConstantValue.SUCCESS.equals(oelement.getErrorcode())) {
                                BetElement element = (BetElement) result.getBody().getElements().get(0);
                                // 修改用户的余额信息
                                GlobalParams.MONEY = Float.parseFloat(element.getActvalue());
                                // 清理返回键
                                MiddleManager.getInstance().clear();
                                // 跳转到购彩大厅，提示对话框
                                MiddleManager.getInstance().changeUI(Hall.class);
                                PromptManager.showErrorDialog(context, "投注成功！剩余余额:" + GlobalParams.MONEY +
                                        "元");

                                List<Ticket> tickets = ShoppingCart.newInstance().getTickets();
                                int counts = ShoppingCart.newInstance().getAppnumbers() * ShoppingCart
                                        .newInstance().getIssuesnumbers();
                                for (Ticket t : tickets) {
                                    t.setNum(t.getNum() * counts);
                                    t.setTime(System.currentTimeMillis());
                                    t.setIssue(ShoppingCart.newInstance().getIssue());
                                    ShoppingBasket.tickets.add(t);
                                }
                                // 清空购物车
                                ShoppingCart.newInstance().clear();
                            }
                        } else {
                            Log.i(TAG, "onPostExecute: result为空!");
                        }


                    }
                }.executeProxy(user);
                break;
        }
    }


    private void changeNotice() {
        Integer lotterynumber = ShoppingCart.newInstance().getLotterynumber();
        Integer lotteryvalue = ShoppingCart.newInstance().getLotteryvalue();
        // GlobalParams.MONEY;

        String number = context.getResources().getString(R.string.is_shopping_list_betting_num);
        String money = context.getResources().getString(R.string.is_shopping_list_betting_money);

        number = StringUtils.replace(number, "NUM", lotterynumber.toString());
        money = StringUtils.replaceEach(money, new String[]{"MONEY1", "MONEY2"}, new String[]{lotteryvalue
                .toString(), String.valueOf(GlobalParams.MONEY)});

        // private TextView bettingNum;// 注数
        // private TextView bettingMoney;// 金额
        bettingNum.setText(Html.fromHtml(number));
        bettingMoney.setText(Html.fromHtml(money));


        // 修改倍数和追期
        // private TextView appnumbersInfo;// 倍数
        // private TextView issueflagNumInfo;// 追期
        appnumbersInfo.setText(String.valueOf(ShoppingCart.newInstance().getAppnumbers()));
        issueflagNumInfo.setText(String.valueOf(ShoppingCart.newInstance().getIssuesnumbers()));

    }


    private class ShoppingAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ShoppingCart.newInstance().getTickets().size();
        }

        @Override
        public Object getItem(int position) {
            return ShoppingCart.newInstance().getTickets().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.il_play_prefectbetting_row, null);

                holder.redNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_reds);
                holder.blueNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_blues);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Ticket ticket = ShoppingCart.newInstance().getTickets().get(position);
            holder.redNum.setText(ticket.getRedNum());
            holder.blueNum.setText(ticket.getBlueNum());

            return convertView;
        }

        class ViewHolder {

            TextView redNum;
            TextView blueNum;
        }
    }
}
