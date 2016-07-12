package com.coconut.mylottery.net.protocol;

import android.util.Xml;

import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.util.DES;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息体节点封装
 * Created by Administrator on 2016/6/21 0021.
 */
public class Body {
    private List<Element> elements = new ArrayList<>();

    public List<Element> getElements() {
        return elements;
    }

    /**
     * 序列化body,序列化的是明文数据.
     *
     * @param serializer 序列化对象.
     */
    public void serializerBody(XmlSerializer serializer) {
        try {
            serializer.startTag(null, "body");
            serializer.startTag(null, "elements");
            for (Element ele : elements) {
                ele.serializerElement(serializer);
            }
            serializer.endTag(null, "elements");
            serializer.endTag(null, "body");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 返回信息的完整明文body的字符串, 如果出现io异常,返回空.
     */
    public String getWholeBody() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            this.serializerBody(serializer);
            //刷新后writer中才有字符串.
            serializer.flush();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 用DES加密方式对body明文加密.需保证body需要的elements已经被传入body中.
     * 在
     *
     * @return body标签中的字符串经过DES加密后得到的字符串(不包含<body></body>的标签)
     */
    public String getBodyInsideDESInfo() {
        String orgInfo = StringUtils.substringBetween(this.getWholeBody(), "<body>", "</body>");

        DES des = new DES();
        return des.authcode(orgInfo, "ENCODE", ConstantValue.DES_PASSWORD);

    }

    //***********************服务器端回复时需要的****************************

    /**
     * 服务器传回来的body部分的DES加密数据. 出去body标签的
     */
    private String serviceBodyInsideDESInfo;
    private Oelement oelement = new Oelement();

    public Oelement getOelement() {
        return oelement;
    }

    /**
     * 获取从服务器返回来的xml信息中的已经DES加密了的body标签中的数据.
     * <p> 只要服务器的数据已经返回,这个方法就可以正常调用,在Baseengine中已经解析好了xml并已经赋值给message.getBody().setServiceBodyInsideDESInfo了.
     * @return 服务器返回来的body标签中的DES加密数据.还没有解密的.
     */
    public String getServiceBodyInsideDESInfo() {
        return serviceBodyInsideDESInfo;
    }

    /**
     * 此方法是在BaseEngine的getResult方法中自动调用的,解析从服务器返回来的xml信息后就赋值给serviceBodyInsideDESInfo
     * @param serviceBodyInsideDESInfo 服务器返回来的xml中的body标签中的DES加密信息.此信息还没有解密.
     */
    public void setServiceBodyInsideDESInfo(String serviceBodyInsideDESInfo) {
        this.serviceBodyInsideDESInfo = serviceBodyInsideDESInfo;
    }
    //***********************服务器端回复时需要的****************************


}
