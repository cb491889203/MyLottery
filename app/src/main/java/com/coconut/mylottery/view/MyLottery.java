package com.coconut.mylottery.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coconut.mylottery.R;
import com.coconut.mylottery.bean.ShoppingBasket;
import com.coconut.mylottery.bean.Ticket;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.view.manager.BaseUI;
import com.coconut.mylottery.view.manager.MiddleManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/9 0009.
 */
public class MyLottery extends BaseUI implements View.OnClickListener {

    /**
     * 购买键
     */
    private Button shoppingBuy;
    /**
     * ListView控件
     */
    private ListView shoppingList;
    /**
     * 空白显示栏
     */
    private RelativeLayout emptyNotice;
    private ShoppingListAdapter adapter;


    public MyLottery(Context context) {
        super(context);
    }

    @Override
    public int getId() {
        return ConstantValue.VIEW_MYLOTTERY;
    }

    @Override
    public void onResume() {
        if(ShoppingBasket.tickets.size()==0){
            shoppingList.setVisibility(View.INVISIBLE);
            emptyNotice.setVisibility(View.VISIBLE);
        }else{
            shoppingList.setVisibility(View.VISIBLE);
            emptyNotice.setVisibility(View.INVISIBLE);
        }

        super.onResume();
    }

    @Override
    public void init() {
        middleView = (RelativeLayout) View.inflate(context, R.layout.il_mylottery, null);
        shoppingBuy = (Button) findViewById(R.id.ii_lottery_shopping_buy);
        shoppingList = (ListView) findViewById(R.id.ii_shopping_list);
        emptyNotice = (RelativeLayout) findViewById(R.id.rl_empty);
        adapter = new ShoppingListAdapter();

        shoppingList.setAdapter(adapter);

    }

    @Override
    protected void setListener() {
        shoppingBuy.setOnClickListener(this);

    }

    class ShoppingListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return ShoppingBasket.tickets.size();
        }

        @Override
        public Object getItem(int position) {
            return ShoppingBasket.tickets.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder Holder = null;
            if (convertView == null) {
                Holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.il_mylottery_row, null);
                Holder.timeView = (TextView) convertView.findViewById(R.id.il_mylottery_time);
                Holder.numsView = (TextView) convertView.findViewById(R.id.il_mylottery_nums);
                Holder.valueView = (TextView) convertView.findViewById(R.id.il_mylottery_value);
                Holder.issueView = (TextView) convertView.findViewById(R.id.ii_mylottery_issue);
                Holder.redsView = (TextView) convertView.findViewById(R.id.ii_mylottery_reds);
                Holder.bluesView = (TextView) convertView.findViewById(R.id.ii_mylottery_blues);
                convertView.setTag(Holder);
            } else {
                Holder = (ViewHolder) convertView.getTag();
            }
            Ticket ticket = ShoppingBasket.tickets.get(position);
            long time = ticket.getTime();
            //SimpleDateFormat format = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance(DateFormat
            // .SHORT, DateFormat.SHORT);
            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
            Holder.timeView.setText("时间:"+format.format(new Date(time)));
            Holder.numsView.setText("注数:"+ticket.getNum()+"注");
            Holder.valueView.setText("金额:" + ticket.getValue() + "元");
            Holder.issueView.setText("第"+ticket.getIssue()+"期:");
            Holder.redsView.setText(ticket.getRedNum());
            Holder.bluesView.setText(ticket.getBlueNum());

            return convertView;
        }

        class ViewHolder {

            TextView timeView;
            TextView numsView;
            TextView valueView;
            TextView issueView;
            TextView redsView;
            TextView bluesView;


        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==shoppingBuy.getId()) {
            MiddleManager.getInstance().changeUI(PlaySSQ.class);
        }
    }
}
