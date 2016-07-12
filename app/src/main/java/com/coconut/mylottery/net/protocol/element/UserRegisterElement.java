package com.coconut.mylottery.net.protocol.element;

import com.coconut.mylottery.net.protocol.Element;
import com.coconut.mylottery.net.protocol.Leaf;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class UserRegisterElement extends Element {


    //************************发送给服务器的数据***************************//

    /**
     *用户妮称
     */
    private Leaf nickname = new Leaf("nickname");
    /**
     *电子邮件
     */
    private Leaf mail = new Leaf("mail");
    /**
     *联系电话
     */
    private Leaf phone = new Leaf("phone");
    /**
     *账户安全密码（提现需要）
     */
    private Leaf actpassword = new Leaf("actpassword");
    /**
     *推荐用户名
     */
    private Leaf recommentuser = new Leaf("recommentuser");

    public Leaf getRecommentuser() {
        return recommentuser;
    }

    public Leaf getActpassword() {
        return actpassword;
    }

    public Leaf getPhone() {
        return phone;
    }

    public Leaf getMail() {
        return mail;
    }

    public Leaf getNickname() {
        return nickname;
    }

    @Override
    public void serializerElement(XmlSerializer serializer) {
        try {
            serializer.startTag(null, "element");
            nickname.serializerLeaf(serializer);
            mail.serializerLeaf(serializer);
            phone.serializerLeaf(serializer);
            actpassword.serializerLeaf(serializer);
            recommentuser.serializerLeaf(serializer);
            serializer.endTag(null, "element");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTransactionType() {
        return "10001";
    }




    //************************服务器返回的数据***************************//

    private Leaf actvalue = new Leaf("actvalue");

    public Leaf getActvalue() {
        return actvalue;
    }


}
