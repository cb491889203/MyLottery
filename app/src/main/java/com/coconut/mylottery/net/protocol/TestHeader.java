package com.coconut.mylottery.net.protocol;

import android.util.Log;
import android.util.Xml;

import com.coconut.mylottery.util.ConstantValue;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Administrator on 2016/6/18 0018.
 */
public class TestHeader {
    private static final String TAG ="TestHeader" ;

    public static void main(String[] args) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter stringWriter = new StringWriter();
        System.out.println(serializer);
        String body = "<body>123fsdk12ljh4l2jl12jl431j2l3j1</body>";
        Header header = new Header();
        header.getTransactiontype().setTagValue("12002");
        header.getUsername().setTagValue("chenbao");
        try {
            serializer.setOutput(stringWriter);
            serializer.startDocument(ConstantValue.ENCODING, null);
            header.serializerHeader(serializer,body);
            serializer.endDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "testHeader: "+stringWriter.toString());
    }
}
