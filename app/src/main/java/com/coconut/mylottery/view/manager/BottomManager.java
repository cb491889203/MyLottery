package com.coconut.mylottery.view.manager;

import android.app.Activity;
import android.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coconut.mylottery.R;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.view.Hall;
import com.coconut.mylottery.view.MyLottery;
import com.coconut.mylottery.view.PlaySSQ;
import com.coconut.mylottery.view.UserLogin;

import org.apache.commons.lang3.StringUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2016/6/24 0024.
 */
public class BottomManager implements Observer,View.OnClickListener{

    private static final String TAG = "BottomManager";
    private FragmentManager fragmentManager ;
    /*******************
     * 第一步：管理对象的创建(单例模式)
     ***************************************************/
    // 创建一个静态实例
    private static BottomManager instrance;
    private RelativeLayout rl;

    private BottomManager( ) {

    }

    /**
     * 单例模式获取管理器对象
     *
     * @return 管理器对象.
     */

    public static BottomManager getInstrance() {

        if (instrance == null) {
            instrance = new BottomManager();

        }
        return instrance;
    }
    /*********************************************************************************************/
    /******************* 第二步：初始化各个导航容器及相关控件设置监听 *********************************/

    public RelativeLayout getBottomMenuContainer() {
        return bottomMenuContainer;
    }

    /**********
     * 底部菜单容器
     **********/
    private RelativeLayout bottomMenuContainer;
    /************
     * 底部导航
     ************/
    private LinearLayout commonBottom;// 购彩通用导航
    private LinearLayout playBottom;// 购彩

    /***************** 导航按钮 ******************/

    /************
     * 购彩导航底部按钮及提示信息
     ************/
    private ImageButton cleanButton;
    private ImageButton addButton;

    private TextView playBottomNotice;

    /************
     * 通用导航底部按钮
     ************/
    private ImageButton homeButton;
    private ImageButton hallButton;
    private ImageButton rechargeButton;
    private ImageButton myselfButton;

    public void init(Activity activity) {
        bottomMenuContainer = (RelativeLayout) activity.findViewById(R.id.include_bottom);
        commonBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_common);
        playBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_game);

        playBottomNotice = (TextView) activity.findViewById(R.id.ii_bottom_game_choose_notice);
        cleanButton = (ImageButton) activity.findViewById(R.id.ii_bottom_game_choose_clean);
        addButton = (ImageButton) activity.findViewById(R.id.ii_bottom_game_choose_ok);

        homeButton = (ImageButton) activity.findViewById(R.id.ii_bottom_home);
        hallButton = (ImageButton) activity.findViewById(R.id.ii_bottom_lottery_hall);
        rechargeButton = (ImageButton) activity.findViewById(R.id.ii_bottom_recharge);
        myselfButton = (ImageButton) activity.findViewById(R.id.ii_bottom_lottery_myself);
        // 设置监听
        setListener();
        this.fragmentManager = activity.getFragmentManager();

    }


    private void setListener() {
        homeButton.setOnClickListener(this);
        hallButton.setOnClickListener(this);
        rechargeButton.setOnClickListener(this);
        myselfButton.setOnClickListener(this);

        // 清空按钮
        cleanButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                BaseUI currentUI = MiddleManager.getInstance().getCurrentUI();
                if(currentUI instanceof PlayGamesUI){
                    ((PlayGamesUI)currentUI).clear();
                }else{
                    Log.i(TAG, "onClick: 清空选号失败!---原因,没有获取到当前选号页面.");
                }
            }
        });
        // 选好按钮
        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i(TAG, "点击选好按钮");
                BaseUI current = MiddleManager.getInstance().getCurrentUI();
                if (current instanceof PlayGamesUI) {
                    ((PlayGamesUI) current).done();
                }
            }
        });
    }


    /*********************************************************************************************/
    /****************** 第三步：控制各个导航容器的显示和隐藏 *****************************************/
    /**
     * 转换到通用导航
     */
    public void showCommonBottom() {
        if (bottomMenuContainer.getVisibility() == View.GONE || bottomMenuContainer.getVisibility() == View
                .INVISIBLE) {
            bottomMenuContainer.setVisibility(View.VISIBLE);
        }
        commonBottom.setVisibility(View.VISIBLE);
        playBottom.setVisibility(View.INVISIBLE);
    }

    /**
     * 转换到购彩
     */
    public void showGameBottom() {
        if (bottomMenuContainer.getVisibility() == View.GONE || bottomMenuContainer.getVisibility() == View
                .INVISIBLE) {
            bottomMenuContainer.setVisibility(View.VISIBLE);
        }
        commonBottom.setVisibility(View.INVISIBLE);
        playBottom.setVisibility(View.VISIBLE);
    }

    /**
     * 改变底部导航容器显示情况
     */
    public void changeBottomVisiblity(int type) {
        if (bottomMenuContainer.getVisibility() != type)
            bottomMenuContainer.setVisibility(type);
    }

    /*********************************************************************************************/
    /*********************** 第四步：控制玩法导航内容显示 ********************************************/
    /**
     * 设置玩法底部提示信息
     *
     * @param notice
     */
    public void changeGameBottomNotice(String notice) {
        playBottomNotice.setText(notice);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null && StringUtils.isNumeric(data.toString())) {
            int id = Integer.parseInt(data.toString());
            switch (id) {
                case ConstantValue.VIEW_FIRST:
                case ConstantValue.VIEW_HALL:
                case ConstantValue.VIEW_LOGIN:
                    case ConstantValue.VIEW_MYLOTTERY:
                    showCommonBottom();
                    break;
                case ConstantValue.VIEW_SECOND:
                case ConstantValue.VIEW_SSQ:
                    showGameBottom();
                    break;
                case ConstantValue.VIEW_SHOPPING:
                case ConstantValue.VIEW_PREBET:
                    changeBottomVisiblity(View.GONE);
                    break;
            }
        } else {
            Log.e(TAG, "update: 被观察者传来的data有误!");
        }
    }


    @Override
    public void onClick(View v) {
        /*homeButton = (ImageButton) activity.findViewById(R.id.ii_bottom_home);
        hallButton = (ImageButton) activity.findViewById(R.id.ii_bottom_lottery_hall);
        rechargeButton = (ImageButton) activity.findViewById(R.id.ii_bottom_recharge);
        myselfButton = (ImageButton) activity.findViewById(R.id.ii_bottom_lottery_myself);*/
        //FragmentTransaction transaction1 = fragmentManager.beginTransaction();
        //UserLoginFragment fragment1 = (UserLoginFragment) fragmentManager.findFragmentById(R.id.middle_container);
        //
        //transaction1.remove(fragment1);
        switch(v.getId()){


            case R.id.ii_bottom_home:
                MiddleManager.getInstance().changeUI(Hall.class);

            break;
            case R.id.ii_bottom_lottery_hall:
                MiddleManager.getInstance().changeUI(PlaySSQ.class);
            break;
            case R.id.ii_bottom_recharge:
            MiddleManager.getInstance().changeUI(UserLogin.class);
            //    FragmentTransaction transaction = fragmentManager.beginTransaction();
            //    UserLoginFragment fragment = new UserLoginFragment();
            //    transaction.replace(R.id.middle_container,fragment);
            //    transaction.commit();
                break;
            case R.id.ii_bottom_lottery_myself:
                MiddleManager.getInstance().changeUI(MyLottery.class);
            break;
        }
    }
}


