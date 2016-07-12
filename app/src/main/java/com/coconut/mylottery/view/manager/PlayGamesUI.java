package com.coconut.mylottery.view.manager;

/**
 * 所有彩种的选号界面需要继承的接口,实现其中的clear()方法,清空已选好的号码.
 * Created by Administrator on 2016/7/7 0007.
 */
public interface PlayGamesUI {

    /**
     * 清空当前彩票选号界面的所有已选号码.
     */
    void clear();

    /**
     * 已经选好号码,提交到购物车.
     */
    void done();
}
