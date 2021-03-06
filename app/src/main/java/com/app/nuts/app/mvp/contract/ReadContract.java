package com.app.nuts.app.mvp.contract;

import com.app.nuts.base.mvp.AppModel;
import com.app.nuts.base.mvp.BaseView;

import java.util.Map;

import rx.Observable;

/**
 * Created by 王立强 on 2017/2/4.
 */

public interface ReadContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        void showReadInfo(String  readInfos);
    }
    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends AppModel {
        Observable<String> getReadInfo(Map<String, String> id);
    }
}