package com.coconut.mylottery.net.protocol;

import android.util.Xml;

import com.coconut.mylottery.util.ConstantValue;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class Message {

    private Header header = new Header();
    private Body body = new Body();

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    private void serializerMessage(XmlSerializer serializer) {
        try {
            serializer.startTag(null, "message");
            serializer.attribute(null, "version", "1.0");

            header.serializerHeader(serializer, body.getWholeBody());//获取完整的body明文

            //这里是添加的body中已经加密的DES数据. 所以body标签需要手动添加.
            serializer.startTag(null, "body");
            serializer.text(body.getBodyInsideDESInfo());
            serializer.endTag(null, "body");

            serializer.endTag(null, "message");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在使用此方法前,一定要先对对Message的信息头部分的username进行手动赋值,再声明一个本信息对应的Element对象,对该Element对象中的特殊Leaf进行赋值,
     * <p>再将该Element当做参数传入Message的getXml方法中.
     * <p>在本方法中,首先会自动根据传入Message的Element对当前请求信息的header设置Transactiontyp标识码,
     * <p>并将该element添加进body的Elements集合中.然后将当前信息进行序列化,并返回为字符串.
     *
     * @param element 添加当前请求信息中的body部分的element元素.因为用户的请求有很多种,需要动态添加进去.注意是element类的具体实现类的对象,而不是Element类的多态对象.
     * @return 将整个请求信息序列化后的字符串.
     */
    public String getXml(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("element is null ");
        }
        header.getTransactiontype().setTagValue(element.getTransactionType());
        body.getElements().add(element);

        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument(ConstantValue.ENCODING, null);
            this.serializerMessage(serializer);
            serializer.endDocument();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
