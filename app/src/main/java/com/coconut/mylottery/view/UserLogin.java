package com.coconut.mylottery.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.coconut.mylottery.R;
import com.coconut.mylottery.bean.User;
import com.coconut.mylottery.engine.UserEngine;
import com.coconut.mylottery.factory.BeanFactory;
import com.coconut.mylottery.net.protocol.Message;
import com.coconut.mylottery.net.protocol.Oelement;
import com.coconut.mylottery.net.protocol.element.BalanceElement;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.util.GlobalParams;
import com.coconut.mylottery.util.PromptManager;
import com.coconut.mylottery.view.manager.BaseUI;
import com.coconut.mylottery.view.manager.MiddleManager;

/**
 * 用户登录
 * Created by Administrator on 2016/7/8 0008.
 */
public class UserLogin extends BaseUI {

    private static final String TAG = "UserLogin";
    private EditText username;
    private ImageView clear;// 清空用户名
    private EditText password;
    private Button login;

    public UserLogin(Context context) {
        super(context);
    }

    @Override
    public int getId() {
        return ConstantValue.VIEW_LOGIN;
    }

    @Override
    public void init() {
        middleView = (ViewGroup) View.inflate(context, R.layout.il_user_login, null);

        username = (EditText) findViewById(R.id.ii_user_login_username);
        clear = (ImageView) findViewById(R.id.ii_clear);
        password = (EditText) findViewById(R.id.ii_user_login_password);
        login = (Button) findViewById(R.id.ii_user_login);
    }

    @Override
    protected void setListener() {
        username.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (username.getText().toString().length() > 0) {
                    clear.setVisibility(View.VISIBLE);
                }

            }
        });

        clear.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ii_clear:
                //清空输入框
                username.setText("");
                clear.setVisibility(View.INVISIBLE);
                break;
            case R.id.ii_user_login:
                //登录信息判断,字数等
                if (checkUserInfo()) {
                    User user = new User();
                    user.setUsername(username.getText().toString());
                    user.setPassword(password.getText().toString());
                    new MyAsyncTask<User>() {
                        @Override
                        protected void onPreExecute() {
                            PromptManager.showProgressDialog(context);
                            super.onPreExecute();
                        }

                        @Override
                        protected Message doInBackground(User... params) {
                            UserEngine userEngine = BeanFactory.newInstance(context).getImpl(UserEngine
                                    .class);
                            Message login = userEngine.login(params[0]);
                            if (login != null) {
                                Oelement oelement = login.getBody().getOelement();
                                if (oelement.getErrorcode().equals(ConstantValue.SUCCESS)) {
                                    //系统备注登录成功了.
                                    GlobalParams.isLogin = true;
                                    GlobalParams.USERNAME = params[0].getUsername();
                                    //获取余额
                                    Message balance = userEngine.getBalance(params[0]);
                                    if (balance != null) {
                                        oelement = balance.getBody().getOelement();
                                        if (ConstantValue.SUCCESS.equals(oelement.getErrorcode())) {
                                            BalanceElement element = (BalanceElement) balance.getBody()
                                                    .getElements().get(0);
                                            GlobalParams.MONEY = Float.parseFloat(element.getInvestvalues());
                                            return balance;
                                        }
                                    } else {
                                        PromptManager.showToast(context,
                                                "登录界面onclick:没有获取到balance的Message信息");
                                    }
                                }
                            } else {
                                PromptManager.showToast(context, "登录界面onclick,登录不成功");
                            }
                            return null;

                        }

                        @Override
                        protected void onPostExecute(Message message) {
                            PromptManager.closeProgressDialog();
                            if (message != null) {
                                // 界面跳转
                                PromptManager.showToast(context, "登录成功");
                                MiddleManager.getInstance().goBack();
                            } else {
                                PromptManager.showToast(context, "服务忙……");
                            }
                            super.onPostExecute(message);
                        }
                    }.executeProxy(user);
                }
                break;
        }

    }

    /**
     * 用户信息判断
     *
     * @return
     */

    private boolean checkUserInfo() {

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
