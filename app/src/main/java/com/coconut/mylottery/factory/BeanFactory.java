package com.coconut.mylottery.factory;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.Properties;

/**
 * 依据配置文件,加载实例.
 * Created by Administrator on 2016/6/24 0024.
 */
public class BeanFactory {

    private static final String TAG = "BeanFactory";
    private static Properties properties = new Properties();;
    private static BeanFactory newInstance;

    private BeanFactory(Context context) {


        //            File file = new File("F:/ASworkspace/MyLottery/app/src/main/java/bean.properties");
        //            properties.load(new FileInputStream(file));
        try {
            Log.i(TAG, "static initializer: context================" + context);
            properties.load(context.getAssets().open("bean.properties"));
            //                properties.load(Be);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //            properties.load(BeanFactory.class.getResourceAsStream("/raw/bean.properties"));
    }


    public static BeanFactory newInstance(Context con) {
        synchronized (BeanFactory.class) {
            if (newInstance == null) {
                newInstance = new BeanFactory(con);
            }
            return newInstance;
        }
    }

    /**
     * 根据传入的接口类名,在配置文件中查找后加载相应的业务实现类.
     *
     * @param clazz 接口类名
     * @return 实现了接口的业务类.
     */
    public  <T> T getImpl(Class<T> clazz) {
        //        if (properties == null) {
        //            try {
        //            InputStream inputStream = context.getAssets().open("bean.properties");
        //                properties.load(inputStream);
        //            } catch (IOException e) {
        //                e.printStackTrace();
        //            }
        //        }
        String name = clazz.getSimpleName();
        String implClass = properties.getProperty(name);
        Log.i(TAG, "getImpl: implClass========================" + implClass);
        try {

            return (T) Class.forName(implClass).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
