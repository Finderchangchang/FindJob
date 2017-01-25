package liuliu.findjob.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.XFragmentAdapter;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import liuliu.findjob.R;
import liuliu.findjob.pager.PBasePager;

public class MainActivity extends XActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.tab_pager)
    ViewPager viewPager;

    List<Fragment> fragmentList = new ArrayList<>();
    String[] titles = {"首页", "工种", "班组", "项目", "资讯"};

    XFragmentAdapter adapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        fragmentList.clear();
        fragmentList.add(MainFragment.newInstance(0));
        fragmentList.add(MainFragment.newInstance(1));
        fragmentList.add(MainFragment.newInstance(2));
        fragmentList.add(MainFragment.newInstance(3));
        fragmentList.add(MainFragment.newInstance(4));
        if (adapter == null) {
            adapter = new XFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Object newP() {
        return null;
    }
}
