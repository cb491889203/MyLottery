package com.coconut.mylottery.net.protocol;

import com.coconut.mylottery.util.ConstantValue;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2016/6/18 0018.
 * <p/>
 * 通过该类的对象调用serializerHeader()可以实现序列化header,但是在调用前需指明header中的8个Leaf的值
 * .其中transactiontype和username需要手动设置.
 */
public class Header {
    //    三个固定不变的常量信息头. 代理商id, 信息源, 加密方法.
    private Leaf agenterid = new Leaf("agenterid", ConstantValue.AGENTERID);
    private Leaf source = new Leaf("source", ConstantValue.SOURCE);
    private Leaf compress = new Leaf("compress", ConstantValue.COMPRESS);

    //    三个可以简单处理的信息头, messengerid,timestamp,digest.
    private Leaf messengerid = new Leaf("messengerid");

    private Leaf timestamp = new Leaf("timestamp");
    private Leaf digest = new Leaf("digest");
    //    2个暂时不能处理的信息头

    /**
     *每个请求对应不同的标识码
     */
    private Leaf transactiontype = new Leaf("transactiontype");
    /**
     * 当前的用户名
     */
    private Leaf username = new Leaf("username");

    /**
     * 获取MessageId
     * @return MessageId
     */
    public Leaf getMessengerId() {
        return messengerid;
    }

    /**
     * 获取transactiontype的Leaf对象后就可调用对当前的请求的transactiontype标识码setTagValue设置值.
     *
     * @return transactiontype的Leaf对象
     */
    public Leaf getTransactiontype() {
        return transactiontype;
    }

    /**
     * 获取Leaf对象后就可调用setTagValue设置值.
     *
     * @return username的Leaf的对象
     */
    public Leaf getUsername() {
        return username;
    }

    public Leaf getTimestamp() {
        return timestamp;
    }

    public Leaf getDigest() {
        return digest;
    }

    /**
     * 序列化这个header,仅包括 header的开始标签, 结束标签 和 中间的内容.
     *
     * @param serializer 传入一个序列化对象.
     * @param body       传入header的body 明文, 用于生成digest的value.
     */
    public void serializerHeader(XmlSerializer serializer, String body) {
//设置 时间戳的值
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(new Date());
        timestamp.setTagValue(time);
// 设置 messengerid的值.时间戳+随机6位数.

        Random random = new Random();
        int num = random.nextInt(999999) + 1;
        DecimalFormat format1 = new DecimalFormat("000000");
        messengerid.setTagValue(time + format1.format(num));
//设置 digest的值
        String orgInfo = time + ConstantValue.AGENTER_PASSWORD + body;
        String md5Hex = DigestUtils.md5Hex(orgInfo);
        digest.setTagValue(md5Hex);

//序列化header
        try {
            serializer.startTag(null, "header");
            agenterid.serializerLeaf(serializer);
            source.serializerLeaf(serializer);
            compress.serializerLeaf(serializer);

            messengerid.serializerLeaf(serializer);
            timestamp.serializerLeaf(serializer);
            digest.serializerLeaf(serializer);

            transactiontype.serializerLeaf(serializer);
            username.serializerLeaf(serializer);
            serializer.endTag(null, "header");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
