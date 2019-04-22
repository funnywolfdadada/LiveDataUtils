package com.example.mvvmdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mvvmdemo.fragment.ActionFragment;
import com.example.mvvmdemo.fragment.DisplayFragment;

/**
 * @author funnywolf
 * @since 2019-04-22
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.top_layout, new DisplayFragment())
                .add(R.id.bottom_layout, new ActionFragment())
                .commit();
    }
}
