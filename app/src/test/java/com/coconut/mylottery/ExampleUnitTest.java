package com.coconut.mylottery;

import android.util.Log;
import android.util.Xml;

import com.coconut.mylottery.bean.test.Father;
import com.coconut.mylottery.bean.test.Person;
import com.coconut.mylottery.bean.test.Son;
import com.coconut.mylottery.net.protocol.Header;
import com.coconut.mylottery.util.ConstantValue;

import org.junit.Test;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    private static final String TAG = "TEST";
    @Test
    public void sonFather(){
        Person person = new Person();
        person.setName("chenbao");

        Father son = new Son(person);

        System.out.println("son属性调用==="+son.person.getName());
        System.out.println(son.getPerson().getName());
        son.showPerson();
        son.sayPerson();

    }
    @Test
    public void testHeader(){
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
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


}