package com.jay.six;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jay.six.ui.activity.BaseActivity;
import com.jay.six.ui.fragment.JokeFragment;
import com.jay.six.ui.fragment.NewsFragment;
import com.jay.six.ui.fragment.PersonFragment;
import com.jay.six.ui.fragment.PicFragment;
import com.jay.six.ui.widget.BottomNavigationViewEx;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * BottomNavigationViewEx:
 * https://github.com/ittianyu/BottomNavigationViewEx
 *
 * */
public class MainActivity extends BaseActivity {

    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigation)
    BottomNavigationViewEx navigation;
    @BindView(R.id.container)
    LinearLayout container;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private NewsFragment newsFragment;
    private JokeFragment jokeFragment;
    private PicFragment picFragment;
    private PersonFragment personFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initFragment();
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        newsFragment = new NewsFragment();
        transaction.replace(R.id.content,newsFragment);
        transaction.commit();

    }

    private void initView() {
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(newsFragment == null){
                        newsFragment = new NewsFragment();
                    }
                    replaceFragment(newsFragment);
                    return true;
                case R.id.navigation_joke:
                    if(jokeFragment == null){
                        jokeFragment = new JokeFragment();
                    }
                    replaceFragment(jokeFragment);
                    return true;
                case R.id.navigation_pic:
                    if(picFragment == null){
                        picFragment = new PicFragment();
                    }
                    replaceFragment(personFragment);
                    return true;
                case R.id.navigation_person:
                    if(personFragment == null){
                        personFragment = new PersonFragment();
                    }
                    replaceFragment(personFragment);
                    return true;
            }
            return false;
        }

    };

    private void replaceFragment(Fragment fragment){
        transaction.replace(R.id.content,fragment);
        transaction.commit();
    }

    @Override
    public void setLeftVisibility(int visibility) {
        super.setLeftVisibility(View.GONE);
    }

    @Override
    public void setRightVisibility(int visibility) {
        super.setRightVisibility(View.GONE);
    }
}
