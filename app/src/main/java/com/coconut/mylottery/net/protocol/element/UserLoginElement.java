package com.coconut.mylottery.net.protocol.element;

import com.coconut.mylottery.net.protocol.Element;
import com.coconut.mylottery.net.protocol.Leaf;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * 	用户登陆密码验证(14001) , 内部只有一个特殊Leaf:actpassword.
 * Created by Administrator on 2016/6/23 0023.
 */
public class UserLoginElement extends Element{


    private Leaf actpassword = new Leaf("actpassword");
    @Override
    public void serializerElement(XmlSerializer serializer) {
        try {
            serializer.startTag(null, "element");
            actpassword.serializerLeaf(serializer);
            serializer.endTag(null, "element");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 当前元素的请求标识码 14001
     */
    @Override
    public String getTransactionType() {
        return "14001";
    }

    public Leaf getActpassword() {
        return actpassword;
    }
}
