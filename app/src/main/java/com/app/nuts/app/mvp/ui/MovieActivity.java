package com.app.nuts.app.mvp.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.app.nuts.R;
import com.app.nuts.app.common.AppComponent;
import com.app.nuts.app.common.BaseActivity;
import com.app.nuts.app.di.component.DaggerMovieComponent;
import com.app.nuts.app.di.module.MovieModule;
import com.app.nuts.app.mvp.contract.MovieContract;
import com.app.nuts.app.mvp.entity.MovieInfo;
import com.app.nuts.app.mvp.presenter.MoviePresenter;
import com.app.nuts.app.mvp.ui.adapter.MovieInfoAdapter;
import com.app.nuts.utils.UiUtils;
import com.paginate.Paginate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;


public class MovieActivity extends BaseActivity<MoviePresenter> implements MovieContract.View , SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    MovieInfoAdapter adapter;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private List<MovieInfo.SubjectsBean> ms = new ArrayList<MovieInfo.SubjectsBean>();
    int start = 0;
    int count = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        toolbar.setTitle("豆瓣电影top250");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        initRecycleView();
        adapter = new MovieInfoAdapter(this, ms);
        recyclerView.setAdapter(adapter);

        mPresenter.getMovieInfo(true);
    }
    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        swiperefreshlayout.setOnRefreshListener(this);
        configRecycleView(recyclerView, new LinearLayoutManager(this));
    }
    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMovieComponent
                .builder()
                .appComponent(appComponent)
                .movieModule(new MovieModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void showMovieInfo(MovieInfo movieInfos) {
        ms.addAll(movieInfos.getSubjects());
        adapter.notifyDataSetChanged();
        initPaginate();
    }

    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }
    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }
    /**
     * 配置recycleview
     *
     * @param recyclerView
     * @param layoutManager
     */
    private void configRecycleView(RecyclerView recyclerView
            , RecyclerView.LayoutManager layoutManager
    ) {
        recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getMovieInfo(false);
                }

                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return false;
                }
            };

            mPaginate = Paginate.with(recyclerView, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }
//    /**
//     * json 格式化
//     * @param bodyString
//     * @return
//     */
//    public static String jsonFormat(String bodyString) {
//        String message;
//        try {
//            if (bodyString.startsWith("{")) {
//                JSONObject jsonObject = new JSONObject(bodyString);
//                message = jsonObject.toString(4);
//            } else if (bodyString.startsWith("[")) {
//                JSONArray jsonArray = new JSONArray(bodyString);
//                message = jsonArray.toString(4);
//            } else {
//                message = bodyString;
//            }
//        } catch (JSONException e) {
//            message = bodyString;
//        }
//        return message;
//    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void showLoading() {
        Timber.tag(TAG).w("showLoading");
        Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    swiperefreshlayout.setRefreshing(true);
                });
    }

    @Override
    public void hideLoading() {
        Timber.tag(TAG).w("hideLoading");
        swiperefreshlayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mPaginate = null;
    }
}
