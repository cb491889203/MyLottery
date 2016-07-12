package com.coconut.mylottery;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coconut.mylottery.util.AnimUtils;
import com.coconut.mylottery.view.FirstUI;
import com.coconut.mylottery.view.Hall;
import com.coconut.mylottery.view.SecondUI;
import com.coconut.mylottery.view.manager.BaseUI;
import com.coconut.mylottery.view.manager.BottomManager;
import com.coconut.mylottery.view.manager.MiddleManager;
import com.coconut.mylottery.view.manager.TitleManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RelativeLayout middleContainer;
    private FragmentManager fragmentManager;
    private android.support.v4.app.FragmentManager supportFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.tv);

        fragmentManager = getFragmentManager();
        supportFragmentManager = getSupportFragmentManager();
        Log.i(TAG, "onCreate:获取 supportFragmentManager============="+ supportFragmentManager);
        init();
    }


    public void init() {

        TitleManager titleManager = TitleManager.getInstance();
        titleManager.initView(this);
        titleManager.showUnLoginTitle();

        BottomManager bottomManager = BottomManager.getInstrance();
        bottomManager.init(this);
//        bottomManager.showCommonBottom();
//        Window window = getWindow();
//        View decorView = window.getDecorView().getRootView();
//        RelativeLayout rootView = (RelativeLayout)findViewById(android.R.id.content).getRootView();
//        RelativeLayout mainView = (RelativeLayout) findViewById(R.id.Rl_main);

//        RelativeLayout middleContainer1 = MiddleManager.getInstance().getMiddleContainer();
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
//                .MATCH_PARENT,ViewGroup.LayoutParams
//                .MATCH_PARENT);
//        params.addRule(RelativeLayout.BELOW,R.id.include_title);
//        params.addRule(RelativeLayout.ABOVE,R.id.include_bottom);
//        middleContainer1.setLayoutParams(params);
//        rootView.addView(middleContainer1);


        //利用中间容器动态切换页面.注册观察者
        this.middleContainer = (RelativeLayout) findViewById(R.id.middle_container);
        MiddleManager middleManager = MiddleManager.getInstance();
        middleManager.setMiddleContainer(this.middleContainer);
        middleManager.setFragmentManager(supportFragmentManager);
        middleManager.addObserver(titleManager);
        middleManager.addObserver(bottomManager);
//        middleManager.changeUI(FirstUI.class);
        middleManager.changeUI(Hall.class);

//        handler.sendEmptyMessageDelayed(10, 2000);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MiddleManager.getInstance().goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //****************************以下是测试用的方法*****************************//


//    };

    //***********************简单的切换页面方法**********************//
    public void changeUI1(BaseUI ui) {
        AnimUtils.fadeOut(firstChild, 200);
        View child = ui.getChild();
        middleContainer.addView(child);
//        child.setAnimation(AnimationUtils.loadAnimation(this,R.anim.fadein));
        AnimUtils.fadeIn(child, 200, 200);
    }

    View firstChild;

    private void loadFirstUI() {
        FirstUI firstUI = new FirstUI(this);
        firstChild = firstUI.getChild();
        middleContainer.addView(firstChild);
    }

    private void loadSecondUI() {
        SecondUI secondUI = new SecondUI(this);
        View child = secondUI.getChild();
        middleContainer.addView(child);
        child.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein));
    }

    /**
     * 先删除前面一个view,再添加新的view
     */
    protected void changeUI() {

        middleContainer.removeAllViews();
        loadSecondUI();
    }



}
