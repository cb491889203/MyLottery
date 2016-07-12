package com.coconut.mylottery.net.protocol;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * Created by Administrator on 2016/6/18 0018.
 */
public class Leaf {
    private String tagName;
    private String tagValue;

    /**
     * 普通的叶子构造方法,只指定名称,值在处理具体逻辑时调用setTagValue()方法动态设置.
     * @param tagName
     */
    public Leaf(String tagName) {
        this.tagName = tagName;
    }

    /**
     * 常量叶子的构造方法,直接指定名称和值.如用于agenterId叶子和source等叶子.
     * @param tagValue
     * @param tagName
     */
    public Leaf(String tagName,String tagValue) {
        this.tagValue = tagValue;
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    /**
     * 设置叶子名称
     * @param tagName
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /**
     * 设置叶子值
     * @param tagValue
     */
    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    /**
     * 序列化这个叶子. 仅包括这个叶子的开始标签, 标签值和结束标签.
     * @param serializer 传入一个序列化对象
     */
    public void serializerLeaf(XmlSerializer serializer){
        if (tagValue == null) {
            tagValue = "";
        }
        try {
            serializer.startTag(null, tagName);
            serializer.text(tagValue);
            serializer.endTag(null, tagName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
