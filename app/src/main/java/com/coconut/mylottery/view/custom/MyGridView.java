package com.coconut.mylottery.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.coconut.mylottery.R;
import com.coconut.mylottery.util.DensityUtil;

/**
 * Created by Administrator on 2016/7/6 0006.
 */
public class MyGridView extends GridView {

    private PopupWindow pop;
    private TextView ball;
    private OnActionUpListener onActionUpListener;

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // ①手指按下
        // 显示放大的号码
        // ②手指滑动
        // 更新：号码内容+显示位置
        // ③手指抬起
        // 修改手指下面的球的背景

        View view = View.inflate(context, R.layout.il_gridview_item_pop, null);
        ball = (TextView) view.findViewById(R.id.ii_pretextView);
        ball.setGravity(Gravity.CENTER);
        ball.setTextSize(25);
        pop = new PopupWindow(context);
        pop.setContentView(view);
        pop.setBackgroundDrawable(null);
        pop.setAnimationStyle(0);
        pop.setWidth(DensityUtil.dip2px(context, 80));
        pop.setHeight(DensityUtil.dip2px(context, 75));
    }


    /**
     * 我们自定义的一个监听,用监听手指抬起时让Pop消失.并且让当前的红球被选中.
     *
     * @param onActionUpListener
     */
    public void setOnActionUpListener(OnActionUpListener onActionUpListener) {
        this.onActionUpListener = onActionUpListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 当手指按下的时候，获取到点击那个球
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        // The position of the item which contains the specified point, or INVALID_POSITION if the point
        // does not intersect an item.
        int position = this.pointToPosition(x, y);
   /*     if (position == INVALID_POSITION) {
            hiddenPop();
            return false;
        }*/
        TextView view = null;
        if (position != INVALID_POSITION) {
            view = (TextView) getChildAt(position);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (position == INVALID_POSITION) {
                    hiddenPop();
                    return false;
                }
//                view = (TextView) getChildAt(position);
                this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                showPop(view);
                break;
            case MotionEvent.ACTION_MOVE:
                if (position != INVALID_POSITION) {
//                    view = (TextView) getChildAt(position);
                    updatePop(view);
                }
                break;
            case MotionEvent.ACTION_UP:
                this.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                hiddenPop();
                if (onActionUpListener != null&&position != INVALID_POSITION) {
                    onActionUpListener.onActionUp(view, position);
                }
                break;

        }
        return true;
    }

    private void showPop(TextView child) {
        int yOffset = -(pop.getHeight() + child.getHeight());
        int xOffset = -(pop.getWidth() - child.getWidth()) / 2;

        ball.setText(child.getText());
        // Display the content view in a popup window anchored to the bottom-left corner of the anchor view.
        // pop.showAsDropDown(child);
        pop.showAsDropDown(child, xOffset, yOffset);
    }

    private void updatePop(TextView child) {
        ball.setText(child.getText());
        int yOffset = -(pop.getHeight() + child.getHeight());
        int xOffset = -(pop.getWidth() - child.getWidth()) / 2;
        // width the new width, can be -1 to ignore
        // height the new height, can be -1 to ignore
        pop.update(child, xOffset, yOffset, -1, -1);
    }

    private void hiddenPop() {
        if (pop.isShowing())
            pop.dismiss();

    }

    /**
     * 监听用户手指抬起
     *
     * @author Administrator
     */
    public interface OnActionUpListener {

        /**
         * 手指抬起
         *
         * @param view     ：当前手底下的球
         * @param position ：位置
         */
        void onActionUp(View view, int position);
    }

/*    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {

            return true;  //禁止GridView滑动
        }
        return super.dispatchTouchEvent(ev);
    }*/
}
