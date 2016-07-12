package com.coconut.mylottery.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coconut.mylottery.R;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.view.manager.BaseUI;

/**
 * 第一个简单的页面
 * Created by Administrator on 2016/6/30 0030.
 */
public class SecondUI extends BaseUI {

    private TextView textView;

    @Override
    public int getId() {
        return ConstantValue.VIEW_SECOND;
    }

    public SecondUI(Context context) {
        super(context);
        init();
    }

    @Override
    protected void setListener() {

    }

    /**
     * 获取需要在中间容器中加载的界面控件
     * @return 返回一个简单的FirstUI
     */
    public TextView getChild() {
        return textView;
    }

    public  void init() {
        textView = new TextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);
        textView.setTextSize(30);
        textView.setBackgroundColor(context.getResources().getColor(R.color.red));
        textView.setText("这是第二个界面");
    }
}
