package com.coconut.mylottery.engine;

import com.coconut.mylottery.bean.User;
import com.coconut.mylottery.net.protocol.Message;

/**
 * 抽象的业务接口. 每个业务需定义不同的接口.
 * 在工厂类BeanFactory中利用资源文件bean.properties加载所需的实现类.
 * Created by Administrator on 2016/6/24 0024.
 */
public interface UserEngine {
    /**
     * 登录的逻辑
     * @return 登陆后 ,服务器返回的回复信息.
     */
    Message login(User user);



    /**获取余额
     * @return 余额
     */
    Message getBalance(User user);

    Message bet(User user);
}
