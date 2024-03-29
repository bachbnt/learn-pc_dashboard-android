package com.example.pcdashboard.Presenter;

import android.content.Context;
import android.text.TextUtils;

import com.example.pcdashboard.Model.User;
import com.example.pcdashboard.Services.AccountService;
import com.example.pcdashboard.Manager.SharedPreferencesUtils;
import com.example.pcdashboard.View.IUpdateInfoView;

interface IInfoPresenter {
    void onLoad();
    void onCheck(String email,String phone);
    void onRequest(String email,String phone);
    void onResponse();
}
public class InfoPresenter implements IInfoPresenter, AccountService.InfoListener {
    private Context context;
    private IUpdateInfoView view;
    private AccountService accountService;

    public InfoPresenter(Context context) {
        this.context = context;
        accountService=AccountService.getInstance(context);
    }
    public void setInfoView(IUpdateInfoView iInfoView){
        this.view=iInfoView;
    }

    public void addInfoListener(){
        accountService.setInfoListener(this);
    }

    public void removeInfoListener(){
        accountService.setInfoListener(null);
    }

    @Override
    public void onSuccess(User self) {
        SharedPreferencesUtils.saveSelf(context,self);
        onResponse();
    }

    @Override
    public void onFailure() {
        view.onUpdateFailure();
    }


    @Override
    public void onLoad() {
        view.onLoad(SharedPreferencesUtils.loadSelf(context));

    }

    @Override
    public void onCheck(String email,String phone) {
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(phone)){
            onRequest(email,phone);
        }else view.onCheckFailure();
    }

    @Override
    public void onRequest(String email, String phone) {
        accountService.updateInfo(email,phone);
    }

    @Override
    public void onResponse() {
        view.onUpdateSuccess();
    }
}
