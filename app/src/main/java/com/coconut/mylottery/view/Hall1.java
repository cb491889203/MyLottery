package com.coconut.mylottery.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class Hall1 extends BaseUI {
    private static final String TAG = "Hall";
    /**
     * 双色球期数信息
     */
    private TextView ssqIssue;
    /**
     * 双色球图片
     */
    private ImageView ssqBet;

    public Hall1(Context context) {
        super(context);
    }


    public void init() {
        this.middleView = (ViewGroup) View.inflate(this.context, R.layout.il_hall, null);


        ssqIssue = (TextView) findViewById(R.id.ii_hall_ssq_summary);
        ssqBet = (ImageView) findViewById(R.id.ii_hall_ssq_bet);

        hallTask.executeProxy(ConstantValue.LOTTERYID_SSQ);
        hallTask.executeProxy(ConstantValue.LOTTERYID_3D);
        hallTask.executeProxy(ConstantValue.LOTTERYID_7LC);
    }


    /**
     * 改变Hall大厅的当前期消息通知
     *
     * @param issueElement
     */
    private void changeNotice(CurrentIssueElement issueElement) {
        int lotteryID = Integer.parseInt(issueElement.getLotteryid().getTagValue());
        switch (lotteryID) {
            case ConstantValue.LOTTERYID_SSQ:
                String issue = issueElement.getIssue().getTagValue();
                String lasttime = getLasttime(issueElement.getLasttime().getTagValue());
                String commonInfo = context.getResources().getString(R.string.is_hall_common_summary);
                String resultInfo = StringUtils.replaceEach(commonInfo, new String[]{"ISSUE", "TIME"},
                        new String[]{});
                ssqIssue.setText(resultInfo);
                break;
            case ConstantValue.LOTTERYID_3D:
                //修改3D玩法的信息
                break;
            case ConstantValue.LOTTERYID_7LC:
                //修改7乐彩玩法的信息
                break;
            default:

                break;
        }

    }

    @Override
    public void onClick(View v) {


        super.onClick(v);
    }

    protected void setListener() {
        ssqBet.setOnClickListener(this);
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

}
