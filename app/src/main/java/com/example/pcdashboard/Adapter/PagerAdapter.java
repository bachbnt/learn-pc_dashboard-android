package com.example.pcdashboard.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.pcdashboard.Manager.ScreenManager;
import com.example.pcdashboard.Manager.SharedPreferencesUtils;
import com.example.pcdashboard.R;

import static com.example.pcdashboard.Manager.IScreenManager.ACCOUNT_FRAGMENT;
import static com.example.pcdashboard.Manager.IScreenManager.CLASS_FRAGMENT;
import static com.example.pcdashboard.Manager.IScreenManager.CONTACT_FRAGMENT;
import static com.example.pcdashboard.Manager.IScreenManager.DEPARTMENT_FRAGMENT;
import static com.example.pcdashboard.Manager.IScreenManager.SELECT_CLASS_FRAGMENT;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private final int NUM_PAGES = 4;
    private String title[] = {"Bộ môn", "Lớp học", "Liên hệ", "Tài khoản"};
    private int icon[] = {R.drawable.ic_department_cold_24dp, R.drawable.ic_class_cold_24dp, R.drawable.ic_contact_cold_24dp, R.drawable.ic_account_cold_24dp};
    private int selectedIcon[] = {R.drawable.ic_department_hot_24dp, R.drawable.ic_class_hot_24dp, R.drawable.ic_contact_hot_24dp, R.drawable.ic_account_hot_24dp};
    private Context context;
    private ScreenManager screenManager;


    public View initTab(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_layout, null);
        TextView tvTitle = view.findViewById(R.id.tv_title_tablayout);
        ImageView ivIcon = view.findViewById(R.id.iv_icon_tablayout);
        tvTitle.setText(title[position]);
        tvTitle.setTextColor(context.getResources().getColor(R.color.colorCold));
        ivIcon.setImageResource(icon[position]);
        return view;
    }

    public View selectTab(View view, int position, boolean isSelected) {
        TextView tvTitle = view.findViewById(R.id.tv_title_tablayout);
        ImageView ivIcon = view.findViewById(R.id.iv_icon_tablayout);
        if (isSelected) {
            tvTitle.setTextColor(context.getResources().getColor(R.color.colorHot));
            ivIcon.setImageResource(selectedIcon[position]);
        } else {
            tvTitle.setTextColor(context.getResources().getColor(R.color.colorCold));
            ivIcon.setImageResource(icon[position]);
        }
        return view;
    }

    public PagerAdapter(FragmentManager fm, Context context, ScreenManager screenManager) {
        super(fm);
        this.context = context;
        this.screenManager = screenManager;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return screenManager.openDashboardScreen(DEPARTMENT_FRAGMENT);
            case 1:
                if(SharedPreferencesUtils.loadSelf(context).getRole().equals("ROLE_TEACHER"))
                    return screenManager.openDashboardScreen(SELECT_CLASS_FRAGMENT);
                else
                    return screenManager.openDashboardScreen(CLASS_FRAGMENT);
            case 2:
                return screenManager.openDashboardScreen(CONTACT_FRAGMENT);
            case 3:
                return screenManager.openDashboardScreen(ACCOUNT_FRAGMENT);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}