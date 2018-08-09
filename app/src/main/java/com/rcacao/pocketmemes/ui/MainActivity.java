package com.rcacao.pocketmemes.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.data.DataBaseContract;

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
    private int MENU_ADD_ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupMenuListener();
        loadMenu();
    }

    private void setupMenuListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == MENU_ADD_ID) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==NEWGROUP_REQUEST){

            if(resultCode==RESULT_OK){
                loadMenu();
            }
        }

    }

    private void loadMenu() {

        Menu menu = navigationView.getMenu();
        menu.clear();

        String[] project = new String[]{
                DataBaseContract.GroupEntry._ID,
                DataBaseContract.GroupEntry.COLUMN_NAME,
                DataBaseContract.GroupEntry.COLUMN_IMAGE};

        Cursor cursor = getContentResolver().query(DataBaseContract.GroupEntry.CONTENT_URI,
                project, "",null, DataBaseContract.GroupEntry.COLUMN_NAME);

        int group = 0;
        int order = 0;
        MenuItem item;
        if (cursor != null) {
            while(cursor.moveToNext()){

                item = menu.add(group, cursor.getInt(0), order, cursor.getString(1));
                item.setIcon(cursor.getInt(2));
                order+=1;
            }

            cursor.close();
        }

        item = menu.add(group, MENU_ADD_ID, order, R.string.new_group);
        item.setIcon(R.drawable.ic_add_group_24dp);

    }
}
