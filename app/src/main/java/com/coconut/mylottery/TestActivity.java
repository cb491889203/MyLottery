package com.coconut.mylottery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.coconut.mylottery.net.protocol.Message;
import com.coconut.mylottery.net.protocol.element.UserLoginElement;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    private TextView testView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        testView = (TextView) findViewById(R.id.tv_test);
        testMessage();

    }
    public void testMessage(){
        Message message = new Message();

        UserLoginElement element = new UserLoginElement();
        element.getActpassword().setTagValue("111111");
        message.getHeader().getUsername().setTagValue("黄燕");
        String xml = message.getXml(element);
        testView.setText(xml);
    }

//    public void testMethod(){
//        XmlSerializer serializer = Xml.newSerializer();
//        StringWriter stringWriter = new StringWriter();
//        System.out.println(serializer);
//        String body = "<body>123fsdk12ljh4l2jl12jl431j2l3j1</body>";
//        Header header = new Header();
//        header.getTransactiontype().setTagValue("12002");
//        header.getUsername().setTagValue("chenbao");
//        try {
//            serializer.setOutput(stringWriter);
//            serializer.startDocument(ConstantValue.ENCODING, null);
//            header.serializerHeader(serializer,body);
//            serializer.endDocument();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        testView.setText(stringWriter.toString());
//        Log.i(TAG, "testHeader: "+stringWriter.toString());
//
//    }
}
