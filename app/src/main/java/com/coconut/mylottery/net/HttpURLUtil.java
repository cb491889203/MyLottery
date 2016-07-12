package com.coconut.mylottery.net;

import android.util.Log;

import com.coconut.mylottery.util.GlobalParams;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class HttpURLUtil {

    //    private static HttpClient client;
    //    private static HttpPost post;
    private static String TAG = "HttpURLUtil";
    private static HttpURLConnection conn = null;
    //    private static Proxy proxy;

    public HttpURLUtil() {
        //        client = HttpClients.createDefault();
       /* if (StringUtils.isNotBlank(GlobalParams.proxy)) {
            // 设置代理信息,并且将这个host当做参数传给client.
            //            HttpHost host = new HttpHost(GlobalParams.proxy, GlobalParams.port);
            //            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
            InetSocketAddress socketAddress = new InetSocketAddress(GlobalParams.proxy, GlobalParams.port);
            proxy = new Proxy(Proxy.Type.SOCKS, socketAddress);

        }*/

    }


    /**
     * 通过httpClient向指定uri发送xml,并等待返回的结果.
     *
     * @param uri uri地址
     * @param xml 要发送的xml文件
     * @return 经过服务器处理后返回的inputStream, 输入流中也是xml文件字符串.
     */
    public static InputStream sendXml(String uri, String xml) {

        //        post = new HttpPost(uri);
        try {

            /*if (StringUtils.isNotBlank(GlobalParams.proxy)) {
                // 设置代理信息,并且将这个host当做参数传给client.
                //            HttpHost host = new HttpHost(GlobalParams.proxy, GlobalParams.port);
                //            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
                InetSocketAddress socketAddress = new InetSocketAddress(GlobalParams.proxy, GlobalParams
                .port);
                proxy = new Proxy(Proxy.Type.SOCKS, socketAddress);

            }*/

            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(xml.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream inputStream = conn.getInputStream();
                return inputStream;
            } else {
                Log.i(TAG, "sendXml: HttpURLConnection 连接失败,返回码为:" + responseCode);
            }
        /*HttpEntity entity = new StringEntity(xml, ConstantValue.ENCODING);
        post.setEntity(entity);
            HttpResponse response = client.execute(post);*/

            //            if (response.getStatusLine().getStatusCode() == 200) {
            //                return response.getEntity().getContent();
            //            } else {
            //                Log.i(TAG, "sendXml: 连接失败,返回状态码错误.");
            //            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    /**
     * 根据流返回一个字符串信息
     *
     * @param is
     * @return
     * @throws IOException
     */
    private static String getStringFromInputStream(InputStream is) {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;


        try {
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String result = baos.toString();    // 把流中的数据转换成字符串, 采用的编码是: utf-8

        //        String html = new String(baos.toByteArray(), "GBK");

        try {
            if (baos != null) {
                baos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    //**********************okhttp************************//
    //
    private static OkHttpClient client;

    public static InputStream sendXmlByOkHttp(String uri, String xml) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (StringUtils.isNotBlank(GlobalParams.proxy)) {
            InetSocketAddress socketAddress = new InetSocketAddress(GlobalParams.proxy, GlobalParams.port);
            builder.proxy(new Proxy(Proxy.Type.SOCKS, socketAddress));
        }
        client = builder.connectTimeout(5000, TimeUnit.MILLISECONDS).readTimeout(5000, TimeUnit
                .MILLISECONDS).build();
        RequestBody body = RequestBody.create(MediaType.parse("text/xml; charset=utf-8"), xml);
        Request request = new Request.Builder().url(uri).post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        OkHttpClient.Builder builder = client.newBuilder();
        if (response == null) {
            Log.i(TAG, "sendXmlByOkHttp: 连接不成功");

            return null;
        } else {
            InputStream inputStream = response.body().byteStream();
            return inputStream;
        }
    }

}
