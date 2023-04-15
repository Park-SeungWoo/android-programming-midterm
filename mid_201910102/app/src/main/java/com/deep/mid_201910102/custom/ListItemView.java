package com.deep.mid_201910102.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.deep.mid_201910102.MainActivity;
import com.deep.mid_201910102.R;

import java.util.HashMap;
import java.util.Map;

public class ListItemView extends androidx.constraintlayout.widget.ConstraintLayout {

    TextView coffee, size;
    Long price;

    public ListItemView(Context context) {
        super(context);
        initView();
    }

    public ListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.list_item_view, this, false);
        addView(v);

        coffee = (TextView) findViewById(R.id.listCoffeeTxt);
        size = (TextView) findViewById(R.id.listSizeTxt);
        price = 0L;
    }

//    @Override  // set margin right  (DEPRECATED)
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        MarginLayoutParams margins = MarginLayoutParams.class.cast(getLayoutParams());
//        margins.rightMargin = 7;
//        setLayoutParams(margins);
//    }

    public void setCoffee(String coffee_str) {
        switch (coffee_str) {
            case "아메리카노":
                price += 1000;
                break;
            case "카페 라떼":
                price += 1500;
                break;
            case "카푸치노":
                price += 2000;
                break;
        }
        coffee.setText(coffee_str);
    }

    public void setSize(String size_str) {
        switch (size_str) {
            case "Medium":
                price += 500;
                break;
            case "Large":
                price += 1000;
                break;
        }
        size.setText(size_str);
    }

    public Long getPrice() {
        return this.price;
    }

    public String getMenu() {
        return this.coffee.getText() + " / " + this.size.getText();
    }

}
