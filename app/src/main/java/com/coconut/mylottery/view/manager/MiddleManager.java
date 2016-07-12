package com.coconut.mylottery.view.manager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.coconut.mylottery.util.AnimUtils;
import com.coconut.mylottery.util.AnimationController;
import com.coconut.mylottery.util.PromptManager;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;

/**
 * 页面中间部分的管理类.添加子view,切换动画等.
 * Created by Administrator on 2016/6/30 0030.
 */
public class MiddleManager extends Observable {

    private static final String TAG = "MiddleManager";
    private BaseUI currentUI = null;
    private FragmentManager fragmentManager;
    /**
     * 中间容器自身一个RelativeLayout实例
     */
    private RelativeLayout middleContainer;
    private static MiddleManager instance = new MiddleManager();

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        Log.i(TAG, "setFragmentManager: 设置fragmentManager ==========="+ fragmentManager);
        this.fragmentManager = fragmentManager;
    }

    private MiddleManager() {
    }

    /**
     * 获取当前正在显示的页面. currentUI
     * @return 当前正在显示的页面.
     */
    public BaseUI getCurrentUI() {
        return currentUI;
    }

    /**
     * 单例模式获取中间页面的管理者对象
     *
     * @return 管理者对象MiddleManager.
     */
    public static MiddleManager getInstance() {
        return instance;
    }

    /**
     * 获取中间容器RelativeLayout实例
     *
     * @return RelativeLayout实例
     */
    public RelativeLayout getMiddleContainer() {
        return middleContainer;
    }


    /**
     * 设置一个中间容器. 因为每个页面的容器类型可能不一样,所以每次都手动设置一个容器进来.
     *
     * @param middleContainer 中间容器
     */
    public void setMiddleContainer(RelativeLayout middleContainer) {
        this.middleContainer = middleContainer;
    }


    /**
     * 用于保存之前创建过的界面BaseUI. key为BaseUI的子类简单名称.value为该子类的一个对象.
     */
    private Map<String, BaseUI> viewCache = new HashMap<>();

    /**
     * 存的是当前返回栈中的UI的类简单名key为string类型..这个栈永远是添加在第一个.
     */
    private LinkedList<String> historyView = new LinkedList<>();




    /**
     * 切换界面:解决问题“在标题容器中每次点击都在创建一个目标界面”
     * 解决问题“中间容器中，每次切换没有判断当前正在展示和需要切换的目标是不是同一个”
     * 改变中间容器的子view,会先删除原来所有的view,再添加这个新的view.
     * <p/>
     * 观察者模式与上下容器联动实现更新样式.
     *
     * @param targetClazz 新子view的类型.如FirstUI,SecondUI.
     *
     */
    public void changeUI(Class<? extends BaseUI> targetClazz) {
        Log.i(TAG, "changeUI: middleContainer的fragmentManager=========="+ fragmentManager);
        //  解决问题“中间容器中，每次切换没有判断当前正在展示和需要切换的目标是不是同一个”
        // 判断当前界面是否与之前的界面相同,如果相同,就复用.
        if (currentUI != null && currentUI.getClass() == targetClazz) {
            Log.i(TAG, "changeUI: 跟上一个view相同,界面保持不变.");
            return;
        }


        //解决问题“在标题容器中每次点击都在创建一个目标界面”
        //判断当前界面是否创建过.
        String key = targetClazz.getSimpleName();
        BaseUI childUI = null;

        if (viewCache.containsKey(key)) {
            //如果创建过,就复用.
            childUI = viewCache.get(key);
        } else {
            //没有创建,就创建新的,并添加进viewCache中.
            try {
                Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
                childUI = constructor.newInstance(middleContainer.getContext());
                viewCache.put(key, childUI);
            } catch (Exception e) {
                throw new RuntimeException("创建目标baseUI的实例出错.");
            }
        }
        Log.i(TAG, "changeUI: childui:" + childUI);
        if (currentUI != null) {
            currentUI.onPause();
            AnimationController.slideOut(currentUI.getChild(), 200, 0);
            //        middleContainer.removeAllViews();
        }
        final View child = childUI.getChild();

        //不在这里删除这个child时,当最小化程序,再进来时,点击切换页面会报错说这个child已经有父容器了.需要清空它.
        ViewGroup parent = (ViewGroup) child.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        middleContainer.addView(child);
        //        AnimUtils.fadeIn(child, 1000, 200);
        AnimationController.slideIn(child, 200, 200);
        childUI.onResume();
        currentUI = childUI;
        historyView.addFirst(key);

        changeTitleAndBottom();
    }

    /**
     * 需要传递数据时用这个方法,将数据放入bundle中传递.
     * @param targetClazz 要切换页面的类.
     * @param bundle 存放数据的bundle
     */
    public void changeUI(Class<? extends BaseUI> targetClazz, Bundle bundle) {
        //  解决问题“中间容器中，每次切换没有判断当前正在展示和需要切换的目标是不是同一个”
        // 判断当前界面是否与之前的界面相同,如果相同,就复用.
        if (currentUI != null && currentUI.getClass() == targetClazz) {
            Log.i(TAG, "changeUI: 跟上一个view相同,界面保持不变.");
            return;
        }


        //解决问题“在标题容器中每次点击都在创建一个目标界面”
        //判断当前界面是否创建过.
        String key = targetClazz.getSimpleName();
        BaseUI childUI = null;

        if (viewCache.containsKey(key)) {
            //如果创建过,就复用.
            childUI = viewCache.get(key);
        } else {
            //没有创建,就创建新的,并添加进viewCache中.
            try {
                Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
                childUI = constructor.newInstance(middleContainer.getContext());
                viewCache.put(key, childUI);
            } catch (Exception e) {
                throw new RuntimeException("创建目标baseUI的实例出错.");
            }
        }
        childUI.setBundle(bundle);
        Log.i(TAG, "changeUI: childui:" + childUI);
        if (currentUI != null) {
            currentUI.onPause();
            AnimationController.slideOut(currentUI.getChild(), 200, 0);
            //        middleContainer.removeAllViews();
        }
        final View child = childUI.getChild();

        //不在这里删除这个child时,当最小化程序,再进来时,点击切换页面会报错说这个child已经有父容器了.需要清空它.
        //        ViewGroup parent = (ViewGroup) child.getParent();
        //        if (parent != null) {
        //            parent.removeAllViews();
        //        }

        middleContainer.addView(child);
        //        AnimUtils.fadeIn(child, 1000, 200);
        AnimationController.slideIn(child, 200, 200);
        childUI.onResume();
        currentUI = childUI;
        historyView.addFirst(key);

        changeTitleAndBottom();
    }

    /**
     * 改变标题和底部栏的样式.以观察者的方式通知他们.
     */
    private void changeTitleAndBottom() {
        int id = currentUI.getId();
        setChanged();
        notifyObservers(id);
    }


    /**
     * 进行返回键操作,如果不能返回就提示
     */
    public void goBack() {
        //判断当前栈顶的UI类与页面显示的当前UI是否一致,如果一致的时候才能返回.
        //这个判断是解决可能出现的BUG.即返回键和切换同时按时,可能会出现线程问题.
        //这个问题目前没有解决好!!!!!
        BaseUI topUI = viewCache.get(historyView.getFirst());
        if (historyView.size() > 1 && currentUI.getClass() == topUI.getClass()) {
            //表示返回栈中有上一个页面.需要先删除当前页面.
            if (currentUI != null) {
                currentUI.onPause();
                AnimationController.slideOut(currentUI.getChild(), 200, 0);
                //            middleContainer.removeAllViews();
            }
            historyView.removeFirst();
            String first = historyView.getFirst();
            BaseUI baseUI = viewCache.get(first);
            ViewGroup parent = (ViewGroup) baseUI.getChild().getParent();
//
            Log.i(TAG, "goBack: baseUI的parent是-------------" + parent);
            if (parent != null) {
                parent.removeAllViews();
            }
            middleContainer.addView(baseUI.getChild());
            AnimationController.slideIn(baseUI.getChild(), 200, 200);
            baseUI.onResume();
            currentUI = baseUI;
            changeTitleAndBottom();
        } else {
            //表示当前页面就是最后一个页面,再返回就是退出程序了.提示
//            Toast.makeText(middleContainer.getContext(), "是否退出系统", Toast.LENGTH_SHORT).show();
            PromptManager.showExitSystem(middleContainer.getContext());

        }
    }


    //********************************************************//

    /**
     * 最原始的方法.
     *
     * @param ui
     */
    public void changeUI1(BaseUI ui) {
        View child = ui.getChild();
        //        if (currentUI != null && currentUI == child) {
        //            return;
        //        }
        //        AnimUtils.fadeOut(firstChild,200);
        middleContainer.removeAllViews();

        middleContainer.addView(child);
        //        child.setAnimation(AnimationUtils.loadAnimation(this,R.anim.fadein));
        AnimUtils.fadeIn(child, 200, 200);
        //        currentUI = child;
    }

    /**
     * 清空历史记录
     */
    public void clear() {
        historyView.clear();
    }
}
