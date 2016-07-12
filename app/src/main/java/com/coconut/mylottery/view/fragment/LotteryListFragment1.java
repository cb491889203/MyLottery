package com.coconut.mylottery.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
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
import com.coconut.mylottery.view.PlaySSQ;
import com.coconut.mylottery.view.manager.MiddleManager;
import com.coconut.mylottery.view.manager.MyAsyncTask;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class LotteryListFragment1 extends ListFragment {

    private Context context;
    private Bundle bundle;
    private MyHallTask<Integer> ssqTask;
    private MyHallTask<Integer> d3Task;
    private MyHallTask<Integer> lc7Task;
    private ExecutorService executorService;
    private ListView fcListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = this.getActivity();
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        } else {
            bundle = new Bundle();
        }
        executorService = Executors.newFixedThreadPool(5);
        setListAdapter(new LotteryFragementAdapter());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        fcListView = this.getListView();
        ssqTask = new MyHallTask<>(context);
        d3Task = new MyHallTask<>(context);
        lc7Task = new MyHallTask<>(context);
        ssqTask.executeOnExecutor(executorService, ConstantValue.LOTTERYID_SSQ);
        d3Task.executeOnExecutor(executorService, ConstantValue.LOTTERYID_3D);
        lc7Task.executeOnExecutor(executorService, ConstantValue.LOTTERYID_7LC);
        super.onResume();
    }


    @Override
    public void onStop() {

        ssqTask.cancel(true);
        d3Task.cancel(true);
        lc7Task.cancel(true);

        super.onStop();
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
                textView1 = (TextView) fcListView.findViewWithTag(0);
                if (textView1 != null) {
                    textView1.setText(resultInfo);
                    //ssqIssue.setText(resultInfo);
                }
                bundle.putString("ssqIssue", issue);
                break;
            case ConstantValue.LOTTERYID_3D:
                //修改3D玩法的信息
                //TextView textView2 = needUpdate.get(1);
                textView1 = (TextView) fcListView.findViewWithTag(1);
                if (textView1 != null) {
                    textView1.setText(resultInfo);
                    //ssqIssue.setText(resultInfo);
                }
                bundle.putString("3dIssue", issue);
                break;
            case ConstantValue.LOTTERYID_7LC:
                //修改7乐彩玩法的信息
                // TextView textView3 = needUpdate.get(2);
                textView1 = (TextView) fcListView.findViewWithTag(2);
                if (textView1 != null) {
                    textView1.setText(resultInfo);
                    //ssqIssue.setText(resultInfo);
                }
                bundle.putString("7lcIssue", issue);
                break;
        }

    }

    private String getSummaryFromElement(CurrentIssueElement issueElement) {
        String issue = issueElement.getIssue().getTagValue();
        String lasttime = getLasttime(issueElement.getLasttime().getTagValue());
        String commonInfo = this.getActivity().getResources().getString(R.string.is_hall_common_summary);
        return StringUtils.replaceEach(commonInfo, new String[]{"ISSUE", "TIME"}, new String[]{issue,
                lasttime});
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

    class LotteryFragementAdapter extends BaseAdapter {

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
        private int[] logoResIds = new int[]{R.drawable.id_dlt, R.drawable.id_a3 , R.drawable.id_qxc};

        private int[] titleResIds = new int[]{R.string.is_hall_ssq_title, R.string.is_hall_a3_title, R
                .string.is_hall_qxc_title};

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

    public class MyHallTask<Integer> extends MyAsyncTask<Integer> {

        public MyHallTask(Context context) {
            super(context);
        }

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
