package com.coconut.mylottery.view.manager;

/**
 * Created by Administrator on 2016/7/11 0011.
 */

import android.content.Context;
import android.os.AsyncTask;

import com.coconut.mylottery.net.NetUtil;
import com.coconut.mylottery.net.protocol.Message;
import com.coconut.mylottery.util.PromptManager;

/**
 * 继承这个抽象类,实现doInBackground方法. 然后在抽象类对象上调用executeProxy方法,开始执行异步任务.
 * @param <Params>
 */
public abstract class MyAsyncTask<Params> extends AsyncTask<Params, Void, Message> {
    Context context;
    public MyAsyncTask(Context context) {
        this.context = context;
    }

    /**
     * 这个方法代替调用AsyncTask中的execute的方法.
     * @param params 执行的彩种玩法lottery_ID
     * @return 返回一个封装了服务器返回的数据的message,并已经经过解析.
     */
    public final AsyncTask<Params, Void, Message> executeProxy(Params... params) {
        if (NetUtil.checkNet(context)) {
            return execute(params);
        } else {
            PromptManager.showNoNetWork(context);
        }

        return null;
    }

}
