package com.example.pcdashboard.Services;

import android.content.Context;

import com.example.pcdashboard.Manager.SharedPreferencesUtils;
import com.example.pcdashboard.Model.Token;
import com.example.pcdashboard.Model.User;
import com.example.pcdashboard.Request.InfoRequest;
import com.example.pcdashboard.Request.PasswordRequest;
import com.example.pcdashboard.Request.TokenRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountService {
    private static AccountService accountService;
    private static IAccountService iAccountService;
    private Context context;
    private LoginListener loginListener;
    private ForgotListener forgotListener;
    private InfoListener infoListener;
    private PasswordListener passwordListener;

    public interface LoginListener {
        void onTokenSuccess(Token token);

        void onSelfSuccess(User self);

        void onLoginFailure();

    }

    public interface ForgotListener {
        void onSuccess();

        void onFailure();
    }

    public interface InfoListener {
        void onSuccess(User self);

        void onFailure();
    }

    public interface PasswordListener {
        void onSuccess();

        void onFailure();
    }

    private AccountService(Context context) {
        this.context = context;
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebService.urlServer)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        iAccountService = retrofit.create(IAccountService.class);
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    public void setForgotListener(ForgotListener listener) {
        this.forgotListener = listener;
    }

    public void setInfoListener(InfoListener listener) {
        this.infoListener = listener;
    }

    public void setPasswordListener(PasswordListener listener) {
        this.passwordListener = listener;
    }

    public static AccountService getInstance(Context context) {
        if (accountService == null)
            accountService = new AccountService(context);
        return accountService;
    }

    public void getToken(String userId, String password) {
        Call<Token> call = iAccountService.getToken(new TokenRequest(userId, password));
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token token = response.body();
                if (loginListener != null)
                    if (token != null)
                        loginListener.onTokenSuccess(token);
                    else loginListener.onLoginFailure();
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                if (loginListener != null)
                    loginListener.onLoginFailure();
            }
        });
    }

    public void forgotPassword(String userId) {
        Call<String> call = iAccountService.forgetPassword(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String email = response.body();
                if (forgotListener != null)
                    if (email != null) {
                        SharedPreferencesUtils.saveEmailForgot(context, email);
                        forgotListener.onSuccess();
                    } else forgotListener.onFailure();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (forgotListener != null)
                    forgotListener.onFailure();
            }
        });
    }

    public void changePassword(String oldPassword, String newPassword) {
        String token = SharedPreferencesUtils.loadToken(context).getTokenType() + " " + SharedPreferencesUtils.loadToken(context).getAccessToken();
        Call<Boolean> call = iAccountService.changePassword(token, new PasswordRequest(SharedPreferencesUtils.loadSelf(context).getId(), oldPassword, newPassword));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (passwordListener != null)
                    if (response.body())
                        passwordListener.onSuccess();
                    else passwordListener.onFailure();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (passwordListener != null)
                    passwordListener.onFailure();
            }
        });
    }

    public void updateInfo(String email, String phone) {
        String token = SharedPreferencesUtils.loadToken(context).getTokenType() + " " + SharedPreferencesUtils.loadToken(context).getAccessToken();
        Call<User> call = iAccountService.updateInfo(token, new InfoRequest(SharedPreferencesUtils.loadSelf(context).getId(), email, phone));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User self = response.body();
                if (infoListener != null)
                    if (self != null)
                        infoListener.onSuccess(self);
                    else infoListener.onFailure();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (infoListener != null)
                    infoListener.onFailure();
            }
        });
    }

    public void getSelf() {
        String token = SharedPreferencesUtils.loadToken(context).getTokenType() + " " + SharedPreferencesUtils.loadToken(context).getAccessToken();
        Call<User> call = iAccountService.getSelf(token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User self = response.body();
                if (loginListener != null)
                    if (self != null)
                        loginListener.onSelfSuccess(self);
                    else loginListener.onLoginFailure();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (loginListener != null)
                    loginListener.onLoginFailure();
            }
        });
    }
}