package com.app.nuts.app.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.app.nuts.R;
import com.app.nuts.app.common.AppComponent;
import com.app.nuts.app.common.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.movie_btn)
    Button movieBtn;
    @BindView(R.id.movie1_btn)
    Button movieBtn1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle("Nuts");
        setSupportActionBar(toolbar);
    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @OnClick({R.id.movie_btn, R.id.movie1_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.movie_btn:
                startActivity(new Intent(this, MovieActivity.class));
                break;
            case R.id.movie1_btn:
                startActivity(new Intent(this, ReadActivity.class));
                break;
        }

    }
}
