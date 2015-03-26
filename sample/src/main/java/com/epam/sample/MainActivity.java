package com.epam.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.epam.widget.SquareImagesLayout;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] a =
                {
                        //R.mipmap.ic_launcher,
                        //R.mipmap.ic_launcher,
                        //R.mipmap.ic_launcher,
                        R.mipmap.ic_launcher
                };
        SquareImagesLayout s = (SquareImagesLayout) findViewById(R.id.sil);
        s.setImagesResource(a);
    }
}
