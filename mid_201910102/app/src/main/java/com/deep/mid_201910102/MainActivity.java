package com.deep.mid_201910102;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.deep.mid_201910102.custom.ListItemView;

public class MainActivity extends AppCompatActivity {

    Button resetBtn, cntDownBtn, cntUpBtn, addTotalBtn;
    ImageView coffeeImg;
    TextView totalTxt, cntTxt, emptyMenuTxt;
    RadioGroup coffeeGrp, sizeGrp;
    RadioButton americano, latte, cappuccino, small, medium, large;
    LinearLayout orderListView;
    HorizontalScrollView listItemScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init views
        // button
        resetBtn = (Button) findViewById(R.id.resetBtn);
        cntDownBtn = (Button) findViewById(R.id.minusCntBtn);
        cntUpBtn = (Button) findViewById(R.id.addCntBtn);
        addTotalBtn = (Button) findViewById(R.id.totalSumBtn);

        // img view
        coffeeImg = (ImageView) findViewById(R.id.coffeeImg);

        // text view
        totalTxt = (TextView) findViewById(R.id.totalPriceTxt);
        cntTxt = (TextView) findViewById(R.id.cntTxt);
        emptyMenuTxt = (TextView) findViewById(R.id.emptyMenutxt);

        // radio group
        coffeeGrp = (RadioGroup) findViewById(R.id.coffeeRadioGrp);
        sizeGrp = (RadioGroup) findViewById(R.id.sizeRadioGrp);

        // radio button
        americano = (RadioButton) findViewById(R.id.americanoRdo);
        latte = (RadioButton) findViewById(R.id.cafelatteRdo);
        cappuccino = (RadioButton) findViewById(R.id.cappuccinoRdo);
        small = (RadioButton) findViewById(R.id.smallRdo);
        medium = (RadioButton) findViewById(R.id.mediumRdo);
        large = (RadioButton) findViewById(R.id.largeRdo);

        // list view
        orderListView = (LinearLayout) findViewById(R.id.listView);

        // scroll view
        listItemScroll = (HorizontalScrollView) findViewById(R.id.listItemScrollV);

        // set event handlers
        // reset btn
        resetBtn.setOnClickListener(new ResetBtnOnClickListener());

        // count btn
        cntUpBtn.setOnClickListener(new CntBtnOnClickListener());
        cntDownBtn.setOnClickListener(new CntBtnOnClickListener());

        // add total price btn
        addTotalBtn.setOnClickListener(new TotalPriceBtnOnClickListener());

        // radio button
        americano.setOnClickListener(new RadioButtonOnClickListener());
        latte.setOnClickListener(new RadioButtonOnClickListener());
        cappuccino.setOnClickListener(new RadioButtonOnClickListener());
        small.setOnClickListener(new RadioButtonOnClickListener());
        medium.setOnClickListener(new RadioButtonOnClickListener());
        large.setOnClickListener(new RadioButtonOnClickListener());

    }

    // custom methods
    // total price
    public void addToTotalPrice(Long price) {
        totalTxt.setText(getTotalPrice() + price + "￦");
    }

    public Long getTotalPrice() {
        String priceTxt = totalTxt.getText().toString();
        return Long.parseLong(priceTxt.substring(0, priceTxt.length() - 1));
    }

    // reset
    public void resetExceptTotalPrice() {
        coffeeGrp.check(R.id.americanoRdo);
        sizeGrp.check(R.id.smallRdo);

        coffeeImg.setImageResource(R.drawable.americano);

        cntTxt.setText("1");
    }

    // get current coffee, size prices
    public Long getCoffeePrice() {
        String priceStr = ((RadioButton) findViewById(coffeeGrp.getCheckedRadioButtonId())).getText().toString();
        return Long.parseLong(priceStr.split("\\(")[1].split("￦")[0]);
    }

    public Long getSizePrice() {
        String priceStr = ((RadioButton) findViewById(sizeGrp.getCheckedRadioButtonId())).getText().toString();
        return Long.parseLong(priceStr.split("\\+")[1].split("￦")[0]);
    }

    // get current coffee, size names
    public String getCoffeeName() {
        String name = ((RadioButton) findViewById(coffeeGrp.getCheckedRadioButtonId())).getText().toString();
        return name.split("\\(")[0];
    }

    public String getSizeName() {
        String name = ((RadioButton) findViewById(sizeGrp.getCheckedRadioButtonId())).getText().toString();
        return name.split("\\(")[0];
    }

    // add list in to the bottom scrollview
    public void addListItem(String coffee, String size) {
        LinearLayout wrapper = new LinearLayout(MainActivity.this);  // wrapper layout (그냥 custom 추가하면 margin때문에 점점 더 잘리므로 한번 더 감싸서 추가)
        wrapper.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 10, 0);  // margin을 여기서 주기
        wrapper.setLayoutParams(layoutParams);

        ListItemView item = new ListItemView(MainActivity.this);  // main context 위에 생성
        item.setCoffee(coffee);
        item.setSize(size);

        item.setOnClickListener(new ListItemOnClickListener());  // item 터치하면 상품목록 삭제시키기

        wrapper.addView(item);
        orderListView.addView(wrapper);

        listItemScroll.postDelayed(new Runnable() {  // delay scroll to end
            @Override
            public void run() {

                listItemScroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 150L);
    }

    // check menu is empty
    public boolean isMenuListEmpty() {
        return orderListView.getChildCount() == 0;
    }

    // event handler classes
    // count buttons
    class CntBtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Long cnt = Long.parseLong(cntTxt.getText().toString());
            if (view.getId() == R.id.addCntBtn)  // add count button
                cntTxt.setText(cnt + 1 + "");
            else {  // minus count button
                if (cnt == 1)
                    Toast.makeText(MainActivity.this, "1개 이하로 설정할 수 없습니다", Toast.LENGTH_SHORT).show();
                else cntTxt.setText(cnt - 1 + "");
            }
        }
    }

    // reset button
    class ResetBtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            resetExceptTotalPrice();
            addToTotalPrice(-getTotalPrice());  // reset total price
            // 리스트 초기화
            orderListView.removeAllViews();
            emptyMenuTxt.setText("메뉴를 선택해주세요!");
        }
    }

    // add total price button
    class TotalPriceBtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (isMenuListEmpty()) emptyMenuTxt.setText("");

            Long cnt = Long.parseLong(cntTxt.getText().toString());
            addToTotalPrice((getCoffeePrice() + getSizePrice()) * cnt);

            // add order list
            for (int i = 0; i < cnt; i++)
                addListItem(getCoffeeName(), getSizeName());

            // reset selected
            resetExceptTotalPrice();
        }
    }

    // ordered list item onclick
    class ListItemOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ListItemView menu = (ListItemView) view;  // CLICKED obj

            // 삭제할건지 알림
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
            alertBuilder.setTitle("메뉴 삭제");
            alertBuilder.setMessage("정말로 [" + menu.getMenu() + "] 을(를) 삭제 하시겠습니까?");
            alertBuilder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {  // 삭제 버튼 클릭
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {  // 삭제
                    addToTotalPrice(-menu.getPrice());
                    orderListView.removeView((LinearLayout) menu.getParent());
                    listItemScroll.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                    if (isMenuListEmpty()) emptyMenuTxt.setText("메뉴를 선택해주세요!");
                }
            });
            alertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alertBuilder.create();
            alertBuilder.show();
        }
    }


    // for radio buttons
    class RadioButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int checkedRadioId = view.getId();
            switch (checkedRadioId) {
                case R.id.americanoRdo:
                    coffeeImg.setImageResource(R.drawable.americano);
                    break;
                case R.id.cafelatteRdo:
                    coffeeImg.setImageResource(R.drawable.latte);
                    break;
                case R.id.cappuccinoRdo:
                    coffeeImg.setImageResource(R.drawable.cappuccino);
                    break;
            }

            // Toast
            String text = ((RadioButton) findViewById(checkedRadioId)).getText().toString().split("\\(")[0];
            Toast.makeText(MainActivity.this, text + "을(를) 선택했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}