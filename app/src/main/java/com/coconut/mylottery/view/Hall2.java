package com.coconut.mylottery.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.coconut.mylottery.util.PromptManager;
import com.coconut.mylottery.view.manager.BaseUI;

import org.apache.commons.lang3.StringUtils;

/**
 * 购彩大厅
 * Created by Administrator on 2016/7/2 0002.
 */
public class Hall2 extends BaseUI {
    private static final String TAG = "Hall";
    private ListView categoryList;
    private CategoryAdapter adapter = null;

    public Hall2(Context context) {
        super(context);
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        hallTask.executeProxy(ConstantValue.LOTTERYID_SSQ);
        hallTask.executeProxy(ConstantValue.LOTTERYID_3D);
        hallTask.executeProxy(ConstantValue.LOTTERYID_7LC);
        super.onResume();
    }

    public void init() {
        this.middleView = (ViewGroup) View.inflate(this.context, R.layout.il_hall, null);
        categoryList = (ListView) findViewById(R.id.ii_hall_lottery_list);
        adapter = new CategoryAdapter();
        categoryList.setAdapter(adapter);

    }

//    private List<TextView> needUpdate = new ArrayList<>();

    /**
     * 改变Hall大厅的当前期消息通知
     *
     * @param issueElement 服务器返回的message中的body部分Element
     */
    private void changeNotice(CurrentIssueElement issueElement) {
        int lotteryID = Integer.parseInt(issueElement.getLotteryid().getTagValue());
        String resultInfo = getSummaryFromElement(issueElement);
        switch (lotteryID) {
            case ConstantValue.LOTTERYID_SSQ:
//                TextView textView1 = needUpdate.get(0);
                TextView textView1 = (TextView) categoryList.findViewWithTag(0);

                if (textView1 != null) {
                    textView1.setText(resultInfo);
//                ssqIssue.setText(resultInfo);
                }

                break;
            case ConstantValue.LOTTERYID_3D:
                //修改3D玩法的信息
//                TextView textView2 = needUpdate.get(1);
                TextView textView2 = (TextView) categoryList.findViewWithTag(1);
                if (textView2 != null) {
                    textView2.setText(resultInfo);
                }
                break;
            case ConstantValue.LOTTERYID_7LC:
                //修改7乐彩玩法的信息
//                TextView textView3 = needUpdate.get(2);
                TextView textView3 = (TextView) categoryList.findViewWithTag(2);
                if (textView3 != null) {
                    textView3.setText(resultInfo);
                }
                break;
            default:

                break;
        }

    }

    private String getSummaryFromElement(CurrentIssueElement issueElement) {
        String issue = issueElement.getIssue().getTagValue();
        String lasttime = getLasttime(issueElement.getLasttime().getTagValue());
        String commonInfo = context.getResources().getString(R.string.is_hall_common_summary);
        return StringUtils.replaceEach(commonInfo, new String[]{"ISSUE", "TIME"},
                new String[]{issue, lasttime});
    }

    @Override
    public void onClick(View v) {


        super.onClick(v);
    }

    protected void setListener() {

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


    MyAsyncTask<Integer> hallTask = new MyAsyncTask<Integer>() {
        @Override
        protected Message doInBackground(Integer... params) {
            CommonInfoEngine impl = BeanFactory.newInstance(context).getImpl(CommonInfoEngine.class);
            return impl.getCommonInfo(params[0]);
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
                Log.i(TAG, "onPostExecute: 返回的数据为空message");
            }


            super.onPostExecute(message);
        }
    };

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

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.logo.setImageResource(logoResIds[position]);
            holder.title.setText(titleResIds[position]);

//            holder.summary.setTag(position);


            return convertView;
        }
    }

    private class ViewHolder {
        ImageView logo;
        TextView title;
        TextView summary;
        ImageView bet;
    }

}
