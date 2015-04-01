package com.epam.sample.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.epam.sample.R;
import com.epam.widget.SquareImagesLayout;
import com.epam.widget.SquareImagesView;

import java.util.Random;


public class TestViewActivity extends BaseActivity {

    SquareImagesView s;
    final static int[] a =
            {
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher
            };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_test_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        s = (SquareImagesView) findViewById(R.id.siv);
        s.addImage(R.mipmap.ic_launcher);

        findViewById(R.id.ac).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.setNumberOfColumns(s.getNumberOfColumns() + 1);
               // s.setImages(a);
            }
        });

        findViewById(R.id.ar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.setNumberOfRows(s.getNumberOfRows() + 1);
               // s.setImages(a);
            }
        });

        findViewById(R.id.rc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = s.getNumberOfColumns() - 1;
                s.setNumberOfColumns(res > 0 ? res : 1);
            }
        });

        findViewById(R.id.rr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = s.getNumberOfRows() - 1;
                s.setNumberOfRows(res > 0 ? res : 1);
            }
        });

        findViewById(R.id.ip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.setContentPadding(s.getContentPadding() + 10);
            }
        });

        findViewById(R.id.dp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.setContentPadding(s.getContentPadding() - 10);
            }
        });

        findViewById(R.id.st).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Random r = new Random();
                //s.setScaleType(ST[r.nextInt(ST.length)]);
            }
        });
    }

    static final ImageView.ScaleType[] ST = ImageView.ScaleType.values();
}
