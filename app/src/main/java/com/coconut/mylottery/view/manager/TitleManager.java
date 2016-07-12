package com.coconut.mylottery.view.manager;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coconut.mylottery.R;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.util.GlobalParams;
import com.coconut.mylottery.view.Hall;
import com.coconut.mylottery.view.PlaySSQ;
import com.coconut.mylottery.view.Shopping;
import com.coconut.mylottery.view.UserLogin;

import org.apache.commons.lang3.StringUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * 标题栏管理类. 设置标题栏信息, 改变标题栏样式.调用单例模式后,需要立即手动调用initView()方法.
 * Created by Administrator on 2016/6/24 0024.
 */
public class TitleManager implements Observer {

    private static final String TAG = "TitleManager";
    private static TitleManager newInstance;
    private static int viewID;

    private TitleManager() {

    }

    private RelativeLayout commonContainer;
    private RelativeLayout unLoginContainer;
    private RelativeLayout loginContainer;

    private ImageView goback;// 返回
    private ImageView help;// 帮助
    private ImageView login;// 登录

    private TextView titleContent;// 标题内容
    private TextView userInfo;// 用户信息

    /**
     * 单例模式,获取管理者对象.
     *
     * @return 管理者对象.
     */
    public synchronized static TitleManager getInstance() {
        if (newInstance == null) {
            return newInstance = new TitleManager();
        } else {
            return newInstance;
        }


    }

    /**
     * 初始化TilteManager, 在这个方法中 查找布局文件中的所有控件,并为控件赋值和设置监听器.
     *
     * @param activity
     */
    public void initView(Activity activity) {

        commonContainer = (RelativeLayout) activity.findViewById(R.id.ii_common_title);
        unLoginContainer = (RelativeLayout) activity.findViewById(R.id.ii_unlogin_title);
        loginContainer = (RelativeLayout) activity.findViewById(R.id.ii_login_title);

        goback = (ImageView) activity.findViewById(R.id.ii_title_goback);
        help = (ImageView) activity.findViewById(R.id.ii_title_help);
        login = (ImageView) activity.findViewById(R.id.ii_title_login);

        titleContent = (TextView) activity.findViewById(R.id.ii_title_content);
        userInfo = (TextView) activity.findViewById(R.id.ii_top_user_info);

        setListener();

    }

    /**
     * 修改当前标题栏内容的值.
     *
     * @param title 需要显示的内容.
     */
    public void changeTitleContent(String title) {
        titleContent.setTextSize(16);
        titleContent.setText(title);
    }

    /**
     * 显示普通标题栏
     */
    public void showCommonTitle() {
        initTitle();
        commonContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 显示未登录标题栏
     */
    public void showUnLoginTitle() {
        initTitle();
        unLoginContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 显示已登录标题栏
     */
    public void showLoginTitle() {
        initTitle();
        loginContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化标题栏,让3中样式的标题栏都设为不可见 GONE.
     */
    private void initTitle() {
        commonContainer.setVisibility(View.GONE);
        unLoginContainer.setVisibility(View.GONE);
        loginContainer.setVisibility(View.GONE);

    }

    /**
     * 为标题栏中的按钮设置监听事件.
     */
    private void setListener() {
        goback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (viewID) {
                    case ConstantValue.VIEW_SSQ:
                    case ConstantValue.VIEW_MYLOTTERY:
                        MiddleManager.getInstance().changeUI(Hall.class);
                        break;
                    case ConstantValue.VIEW_SHOPPING:
                        MiddleManager.getInstance().changeUI(PlaySSQ.class);
                        break;
                    case ConstantValue.VIEW_PREBET:
                        MiddleManager.getInstance().changeUI(Shopping.class);
                        break;
                }
            }
        });
        help.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("help");

            }
        });
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MiddleManager.getInstance().changeUI(UserLogin.class);
                // SecondUI secondUI = new
                //                 SecondUI(MiddleManager.getInstance().getContext());
                //                MiddleManager.getInstance().changeUI(UserLogin.class);//
                // changeUI需要修改，不能传递对象，但是明确目标
            }
        });
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null && StringUtils.isNumeric(data.toString())) {
            viewID = Integer.parseInt(data.toString());
            switch (viewID) {
                case ConstantValue.VIEW_FIRST:

                case ConstantValue.VIEW_LOGIN:
                    this.showUnLoginTitle();
                    break;
                case ConstantValue.VIEW_SSQ:
                case ConstantValue.VIEW_SHOPPING:
                case ConstantValue.VIEW_PREBET:
                    this.showCommonTitle();
                    break;
                case ConstantValue.VIEW_HALL:
                case ConstantValue.VIEW_MYLOTTERY:
                    if (GlobalParams.isLogin) {
                        showLoginTitle();
                        String info = "您好:" + GlobalParams.USERNAME + "\r\n" + "余额" + GlobalParams.MONEY +
                                "元";
                        userInfo.setText(info);

                    } else {
                        showUnLoginTitle();
                    }
                    break;
            }
        } else {
            Log.e(TAG, "update: 被观察者传来的data有误!");
        }
    }
}
