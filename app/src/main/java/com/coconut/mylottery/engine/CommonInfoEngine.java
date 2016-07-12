package com.coconut.mylottery.engine;

import com.coconut.mylottery.net.protocol.Message;

/**
 * Hall页面上显示的双色球信息的获取engine接口.
 * Created by Administrator on 2016/7/3 0003.
 */
public interface CommonInfoEngine {

    /**
     * 获取当期双色球的信息.
     * @return 信息Message.
     */
    Message getCommonInfo(Integer lotteyId);
}
