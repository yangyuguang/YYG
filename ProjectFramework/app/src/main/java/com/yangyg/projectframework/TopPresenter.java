package com.yangyg.projectframework;

import android.support.annotation.NonNull;

/**
 * Created by yangyuguang on 16/3/21.
 */
public class TopPresenter {

    private TopView mView;

//    private TopUseCase mUseCase;

    public TopPresenter() {
//        mUseCase = new TopUseCase();
    }

    public void onCreate(@NonNull TopView topView) {
        mView = topView;
        mView.initViews();


    }

//    public void updateCalendarDate() {
//        // do not forget to return if view instances is null
//        if (mView == null) {
//            return;
//        }
//
//        // here logic comes
//        String dateToDisplay = mUseCase.getDateToDisplay(mContext.getResources());
//        mView.updateCalendarDate(dateToDisplay);
//
//        // here you save date, and this logic is hidden in UseCase class
//        mUseCase.saveCalendarDate();
//    }
}
