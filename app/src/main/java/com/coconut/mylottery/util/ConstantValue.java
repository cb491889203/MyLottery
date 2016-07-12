package com.coconut.mylottery.util;

/**
 * Created by Administrator on 2016/6/18 0018.
 */
public interface ConstantValue {
    String ENCODING = "UTF-8";
    String AGENTERID = "123456";
    String SOURCE = "ivr";
    String COMPRESS = "DES";
    String AGENTER_PASSWORD = "9ab62a694d8bf6ced1fab6acd48d02f8";
    String DES_PASSWORD = "9b2648fcdfbad80f";

    //BaseUI页面的标识码
    int VIEW_FIRST = 1;
    int VIEW_SECOND = 2;
    /**
     * 购彩大厅页面,进入程序的第一个页面
     */
    int VIEW_HALL =10;
    /**
     * 双色球玩法页面.
     */
    int VIEW_SSQ =15;
    /**
     * 购物车
     */
    int VIEW_SHOPPING=20;
    /**
     * 追期和倍投的设置界面
     */
    int VIEW_PREBET=25;
    /**
     * 用户登录
     */
    int VIEW_LOGIN=30;

    int VIEW_MYLOTTERY = 35;

    //String LOTTERY_URI = "http://10.0.2.2:8080/ZCWService/Entrance";// 10.0.2.2模拟器如果需要跟PC机通信127.0.0.1
    String LOTTERY_URI = "http://192.168.1.100:8080/ZCWService/Entrance";// 10.0.2.2模拟器如果需要跟PC机通信127.0.0.1
    /**
     * 双色球标示
     */
    int LOTTERYID_SSQ =118;
    /**
     * 3D彩票ID
     */
    int LOTTERYID_3D =119;
    /**
     * 7乐彩ID
     */
    int LOTTERYID_7LC =120;
    /**
     * 服务器返回成功状态码
     */
    String SUCCESS="0";
}
