package com.coconut.mylottery.engine.Impl;

import com.coconut.mylottery.bean.User;
import com.coconut.mylottery.engine.BaseEngine;
import com.coconut.mylottery.engine.UserRegisterEngine;
import com.coconut.mylottery.net.protocol.Message;
import com.coconut.mylottery.net.protocol.element.UserRegisterElement;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class UserRegisterEngineImpl extends BaseEngine implements UserRegisterEngine{
    @Override
    public Message registerUser(User user) {
        UserRegisterElement element = new UserRegisterElement();
        element.getActpassword().setTagValue(user.getPassword());
        element.getNickname().setTagValue(user.getNickname());
        element.getMail().setTagValue(user.getMail());
        element.getPhone().setTagValue(user.getPhone());
        element.getRecommentuser().setTagValue(user.getRecommentuser());
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUsername());
        String xml = message.getXml(element);
        Message result = getResult(xml);
        if (result != null) {
            //TODO 第四步;解密DES body部分
        }
        return null;
    }
}
