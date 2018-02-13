package com.jingtuo.android.lottery.common.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jingtuo.android.lottery.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 双色球中某色球的布局
 * Created by jingtuo on 2018/2/12.
 */

public class BallLayout extends LinearLayout {

    //组件
    private List<Button> balls;

    public BallLayout(Context context) {
        this(context, null);
    }

    public BallLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BallLayout, defStyleAttr, 0);
        int count = array.getInt(R.styleable.BallLayout_count, 0);
        int lineSpace = array.getDimensionPixelOffset(R.styleable.BallLayout_lineSpace, 0);
        int columnSpace = array.getDimensionPixelOffset(R.styleable.BallLayout_columnSpace, 0);
        int textSize = array.getDimensionPixelOffset(R.styleable.BallLayout_android_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, metrics));
        ColorStateList textColorStateList = array.getColorStateList(R.styleable.BallLayout_android_textColor);
        int ballWidth = array.getDimensionPixelOffset(R.styleable.BallLayout_ballWidth, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 40, metrics));
        int ballHeight = array.getDimensionPixelOffset(R.styleable.BallLayout_ballHeight, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 40, metrics));
        int ballBg = array.getResourceId(R.styleable.BallLayout_ballBg, 0);
        array.recycle();

        setOrientation(VERTICAL);

        int contentWidth = metrics.widthPixels - 32 * metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;

        int countPerLine = (contentWidth + columnSpace) / (ballWidth + columnSpace);
        int lineCount = (count + countPerLine - 1) / countPerLine;

        balls = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            LinearLayout llLine = new LinearLayout(context);
            llLine.setOrientation(HORIZONTAL);
            for (int j = 0; j < countPerLine; j++) {
                int fIndex = i * countPerLine + j;
                if (fIndex >= count) {
                    break;
                }

                final Button ball = new Button(context);
                ball.setText(String.format(Locale.getDefault(), "%1$02d", (i * countPerLine + j + 1)));
                if (textColorStateList != null) {
                    ball.setTextColor(textColorStateList);
                }
                ball.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                if (ballBg != 0) {
                    ball.setBackgroundResource(ballBg);
                }
                ball.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ball.setSelected(!ball.isSelected());
                    }
                });
                ball.setPadding(0, 0, 0, 0);
                LayoutParams params = new LayoutParams(ballWidth, ballHeight);
                if (j != 0) {
                    params.leftMargin = columnSpace;
                }
                llLine.addView(ball, params);
                balls.add(ball);
            }
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                params.topMargin = lineSpace;
            }
            addView(llLine, params);
        }
    }
}
