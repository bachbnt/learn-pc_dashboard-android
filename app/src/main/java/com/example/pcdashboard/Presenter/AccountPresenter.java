package com.example.pcdashboard.Presenter;

import android.content.Context;

import com.example.pcdashboard.Manager.DatabaseHelper;
import com.example.pcdashboard.Model.User;
import com.example.pcdashboard.Manager.SharedPreferencesUtils;
import com.example.pcdashboard.Services.WebService;
import com.example.pcdashboard.View.IAccountView;

interface IAccountPresenter {
    void onInit();

    void changeLoginStatus(boolean status);

    void changeFirstRequestSchedule();

    void deleteDatabase();

    void clearSharedPreferences();

    void blockNotifications();
}

public class AccountPresenter implements IAccountPresenter {
    private Context context;
    private IAccountView view;
    private DatabaseHelper databaseHelper;
    private WebService webService;


    public AccountPresenter(Context context) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);
        webService=WebService.getInstance(context);
    }

    public void setAccountView(IAccountView iAccountView) {
        this.view = iAccountView;
    }

    @Override
    public void onInit() {
        User self = SharedPreferencesUtils.loadSelf(context);
        view.onInit(self);
    }

    @Override
    public void changeLoginStatus(boolean status) {
        SharedPreferencesUtils.saveStatusLogin(context, status);
    }

    @Override
    public void changeFirstRequestSchedule() {
        SharedPreferencesUtils.saveFirstRequestSchedule(context,true);
    }

    @Override
    public void deleteDatabase() {
        databaseHelper.deleteUserStudents();
        databaseHelper.deleteUserTeachers();
        databaseHelper.deleteDepartmentPosts();
        databaseHelper.deleteClassPosts();
        databaseHelper.deleteChatMessages();
        databaseHelper.deleteSchedules();
        databaseHelper.deleteExams();
        databaseHelper.deleteYearStudents(3);
        databaseHelper.deleteYearStudents(4);
        databaseHelper.deleteYearClassPosts(3);
        databaseHelper.deleteYearClassPosts(4);
    }

    @Override
    public void clearSharedPreferences() {
        SharedPreferencesUtils.clearSelf(context);
        SharedPreferencesUtils.clearToken(context);
        SharedPreferencesUtils.clearTabId(context);
        SharedPreferencesUtils.clearPostComment(context);
        SharedPreferencesUtils.clearClassPost(context);
        SharedPreferencesUtils.clearEmailForgot(context);
        SharedPreferencesUtils.clearClassId(context);
    }

    @Override
    public void blockNotifications() {
        webService.sendFCMToken("");
    }
}
