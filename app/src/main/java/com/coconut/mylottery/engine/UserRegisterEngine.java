package com.coconut.mylottery.engine;

import com.coconut.mylottery.bean.User;
import com.coconut.mylottery.net.protocol.Message;

/**
 * 用户注册业务
 * Created by Administrator on 2016/7/5 0005.
 */
public interface UserRegisterEngine {

    /**
     * 用户注册的方法,将用户添的个人信息封装到user中,上传到服务器再返回数据,以Message的形式返回. 其中已经对数据进行解析.并将数据封装在message中返回.
     * @param user 封装了所有数据的user,需要设置user中的属性的值.
     * @return 已经解析好了的message.
     */
   Message registerUser(User user);
}
