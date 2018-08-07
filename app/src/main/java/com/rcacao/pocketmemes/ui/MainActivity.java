package com.rcacao.pocketmemes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.rcacao.pocketmemes.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int NEWGROUP_REQUEST = 10;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;

    @BindView(R.id.search_toolbar)
    Toolbar searchToolbar;

    @BindView(R.id.et_search)
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupMenuListener();

    }

    private void setupMenuListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.mnuAdd) {

                    openNewGroup();

                }

                return true;
            }
        });
    }

    private void openNewGroup() {
        Intent intent = new Intent(this, NewGroupActivity.class);
        startActivityForResult(intent, NEWGROUP_REQUEST);
    }

    @OnClick(R.id.menu_slide) void clickMenuSlide(){

        drawerLayout.openDrawer(GravityCompat.START);

    }

    @OnClick(R.id.menu_search) void clickMenuSearch(){

        searchToolbar.setVisibility(View.VISIBLE);
        mainToolbar.setVisibility(View.GONE);
        etSearch.requestFocus();

    }

    @OnClick(R.id.menu_back) void clickMenuBack(){

        searchToolbar.setVisibility(View.GONE);
        mainToolbar.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.menu_clear) void clickMenuClear(){
        etSearch.setText("");
        etSearch.requestFocus();
    }

    @OnClick(R.id.menu_search_send) void clickSearchSend(){

    }
}
