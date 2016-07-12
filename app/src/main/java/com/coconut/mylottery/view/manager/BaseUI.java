package com.coconut.mylottery.view.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.coconut.mylottery.net.NetUtil;
import com.coconut.mylottery.net.protocol.Message;
import com.coconut.mylottery.util.PromptManager;

/**
 * middleContainer中的通用ui类.其中有获得该容器的子view的getChild方法.
 * 直接继承的属性protected Context context 上下文
 * 直接继承的属性 protected ViewGroup middleView 是中间容器的对象,使用View.inflate(this.context,R.id...,null),加载相应的layout文件
 * 直接继承的方法 findViewById(int id) ,  利用middleView查找里面的子View.
 * Created by Administrator on 2016/6/30 0030.
 */
public abstract class BaseUI implements View.OnClickListener {
    private static final String TAG = "BaseUI";
    protected Context context;
    /**
     * 存放数据,如果切换页面时需要传送数据时使用.
     */
    protected Bundle bundle;
    /**
     * 现在在中间容器中的ViewGroup
     */
    protected ViewGroup middleView;

    public BaseUI(Context context) {
        this.context = context;
        init();
        setListener();
    }

    /**
     * 设置监听,所有子类的监听都在这里设置后,会在构造方法中自动调用完成设置.需要手动重写onClick方法.
     */
    protected abstract void setListener();

    /**
     * 每个页面必须有自己特有的页面标识码,以区别标题栏和底部导航栏的样式.
     *
     * @return 标识码, 如VIEW_FIRST, VIEW_SECOND 等.
     */
    public abstract int getId();

    /**
     * 获取在中间容器中需要加载的子view
     *
     * @return 需要加载的子view
     */
    public View getChild() {
        if (middleView.getLayoutParams() == null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            middleView.setLayoutParams(params);
        }
        return middleView;
    }

    /**
     * 初始化方法,在其中加载xml布局文件并直接赋值给middleView=View.inflate(this.context,R.layout.$name$,null).
     * 如果middleVIew布局文件中有其他子控件,
     * 也在这个方法中利用方法findViewById(int id)找到..
     */
    public abstract void init();

    @Override
    public void onClick(View v) {

    }

    /**
     * 在baseUI暂停,也就是被移除中间容器前(changeUI和goBack方法时),执行的方法.
     */
    public void onPause(){

    }

    /**
     * 在BaseUI恢复,也就是被添加进中间容器后(changeUI和goBack方法时),执行的方法.一般用于更新界面信息.从服务器获取消息的耗时操作.
     */
    public void onResume(){

    }

    /**
     * 利用middleView.findViewById(id)查找view,并返回. 在子类中调用这个方法可以简省操作步骤.
     * @param id 资源文件的id
     * @return 找到的view
     */
    public final View findViewById(int id) {
       return middleView.findViewById(id);
    }

    /**
     * 如果有数据需要保存,并传入到这个页面,使用这个方法.
     * @param bundle 封装的数据.
     */
    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }


    /**
     * 继承这个抽象类,实现doInBackground方法. 然后在抽象类对象上调用executeProxy方法,开始执行异步任务.
     * @param <Params>
     */
    public abstract class MyAsyncTask<Params> extends AsyncTask<Params, Void, Message> {

        /**
         * 这个方法代替调用AsyncTask中的execute的方法.
         * @param params 执行的彩种玩法lottery_ID
         * @return 返回一个封装了服务器返回的数据的message,并已经经过解析.
         */
        public final AsyncTask<Params, Void, Message> executeProxy(Params... params) {
            if (NetUtil.checkNet(BaseUI.this.context)) {
                return execute(params);
            } else {
                PromptManager.showNoNetWork(BaseUI.this.context);
            }

            return null;
        }

    }
}
