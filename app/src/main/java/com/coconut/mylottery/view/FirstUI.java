package com.coconut.mylottery.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.coconut.mylottery.R;
import com.coconut.mylottery.TestActivity;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.view.manager.BaseUI;

/**
 * 第一个简单的页面
 * Created by Administrator on 2016/6/30 0030.
 */
public class FirstUI extends BaseUI{


    private Button testBtn;

    @Override
    public int getId() {
        return ConstantValue.VIEW_FIRST;
    }

    public FirstUI(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {
        testBtn.setOnClickListener(this);
    }



    @Override
    public void init() {
        middleView = (RelativeLayout)View.inflate(this.context, R.layout.il_first, null);
        testBtn = (Button)findViewById(R.id.btn_test);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this.context, TestActivity.class);
        context.startActivity(intent);
        super.onClick(v);
    }
}
