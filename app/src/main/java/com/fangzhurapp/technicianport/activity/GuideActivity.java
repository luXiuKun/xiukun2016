package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.vp_guide)
    ViewPager vpGuide;
    @Bind(R.id.ll_guide_dot)
    LinearLayout llGuideDot;
    @Bind(R.id.btn_guide)
    Button btnGuide;
    @Bind(R.id.tv_text1)
    TextView tvText1;
    @Bind(R.id.tv_text2)
    TextView tvText2;
    private ArrayList<Integer> imgList;
    private List<View> imgviewList;
    private ArrayList<View> dots;

    private int oldPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        imgList = new ArrayList<>();
        imgList.add(R.drawable.img_guide2);
        imgList.add(R.drawable.img_guide4);
        imgList.add(R.drawable.img_guide3);
        imgList.add(R.drawable.img_guide1);

        initView();
        initEvent();

    }

    private void initEvent() {
        btnGuide.setOnClickListener(this);

        vpGuide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dots.get(oldPosition).setBackgroundResource(R.drawable.img_dot_nor);
                dots.get(position).setBackgroundResource(R.drawable.img_dot_pre);

                oldPosition = position;

                if (position == 3) {
                    btnGuide.setVisibility(View.VISIBLE);
                } else {

                    btnGuide.setVisibility(View.GONE);
                }

                changeText(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void changeText(int position) {

        switch (position){

            case 0:
                tvText1.setText("工资授权  移动管店");
                tvText2.setText("工资智能核算，手机一键发放");
                break;

            case 1:
                tvText1.setText("随时随地  查看业绩");
                tvText2.setText("时刻掌握店铺动态，省时省心省力");
                break;

            case 2:
                tvText1.setText("接单上钟  实时分账");
                tvText2.setText("上钟提醒实时推送，记钟记账手机搞定");
                break;

            case 3:
                tvText1.setText("订单查询  工资提现");
                tvText2.setText("历史订单永久保存，工资钱包一键提现");
                break;

        }
    }

    private void initView() {

        imgviewList = new ArrayList<>();
        for (int i = 0; i < imgList.size(); i++) {

            //ImageView imageView = new ImageView(GuideActivity.this);
            View view = View.inflate(GuideActivity.this, R.layout.layout_guide, null);
            ImageView lv = (ImageView) view.findViewById(R.id.img_guide);

            lv.setBackgroundResource(imgList.get(i));

            imgviewList.add(view);
        }
        dots = new ArrayList<>();
        for (int i = 0; i < imgList.size(); i++) {

            View view = new View(GuideActivity.this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            if (i != 0) {
                params.leftMargin = 30;
            }
            if (i == 0) {

                view.setBackgroundResource(R.drawable.img_dot_pre);
                view.setLayoutParams(params);
                dots.add(view);
                llGuideDot.addView(view);
            } else {

                view.setBackgroundResource(R.drawable.img_dot_nor);
                view.setLayoutParams(params);
                dots.add(view);
                llGuideDot.addView(view);
            }


        }
        tvText1.setText("工资授权  移动管店");
        tvText2.setText("工资智能核算，手机一键发放");

        vpGuide.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgviewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                ((ViewPager) container).addView(imgviewList.get(position));


                return imgviewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView(imgviewList.get(position));
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.btn_guide:
                SpUtil.putBoolean(GuideActivity.this, "firstlogin", false);
                Intent intent = new Intent(GuideActivity.this, SelectIdent.class);
                startActivity(intent);
                GuideActivity.this.finish();
                break;
        }
    }
}
