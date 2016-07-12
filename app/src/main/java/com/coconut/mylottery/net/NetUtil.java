package com.coconut.mylottery.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 网络工具,检查是否联网, 并判断联网的工具和方式是什么
 * Created by Administrator on 2016/6/23 0023.
 */
public class NetUtil {

    private static String TAG = "NetUtil";

    /**
     * 检查是否联网.
     *
     * @param context 上下文对象,用于获取手机的管理器.
     * @return 连接上返回true, 否者返回false.
     */
    public static boolean checkNet(Context context) {
        // ① 判断是否是wifi方式
        boolean isWIFI = isWIFIConnection(context);
        //②判断是 mobile方式
        boolean isMOBILE = isMOBILEConnection(context);
        //③如果是mobile方式,判断是哪个APN被选中,如果APN中的proxy 和port有值,说明是wap方式,需要特殊处理.
//        if (isMOBILE) {
//            readAPN(context);
//        }

        if (!isMOBILE && !isWIFI) {
            return false;
        }
        return true;
    }

    /**
     * 与读取手机联系人类似 利用内容提供者查询.
     * @param context
     */
   /* private static void readAPN(Context context) {
        Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");//4.0模拟器屏蔽掉该权限
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(PREFERRED_APN_URI, null, null, null, null);
        if(cursor!=null && cursor.moveToFirst()){
            GlobalParams.proxy= cursor.getString(cursor.getColumnIndex("proxy"));
            GlobalParams.port= cursor.getInt(cursor.getColumnIndex("port"));
        }else{
            Log.i(TAG, "readAPN: APN读取的proxy和port信息为空!");
        }
    }*/

    private static boolean isMOBILEConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            return networkInfo.isConnected();
        } else {
            Log.i(TAG, "isMOBILEConnection: 读取网络信息不成功! networkInfo=null.");
            return false;
        }

    }

    private static boolean isWIFIConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            return networkInfo.isConnected();
        } else {
            Log.i(TAG, "isWIFIConnection: 读取网络信息不成功! networkInfo=null.");
            return false;
        }
    }
}
