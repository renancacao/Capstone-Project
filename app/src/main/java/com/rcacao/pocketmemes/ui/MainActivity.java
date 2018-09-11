package com.rcacao.pocketmemes.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.adapters.MemeAdapter;
import com.rcacao.pocketmemes.data.database.DataBaseContract;
import com.rcacao.pocketmemes.data.models.Meme;
import com.rcacao.pocketmemes.loaders.MemesAsyncLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Meme>>, MemeAdapter.ResultClickListener {

    private static final int NEWGROUP_REQUEST = 10;
    private static final int LOADER_MEMES = 1;

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

    @BindView(R.id.recyclerView_memes)
    RecyclerView recyclerViewMemes;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private int MENU_ADD_ID = -1;
    private MemeAdapter memeAdapter;
    private List<Meme> memes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        StaggeredGridLayoutManager lymg = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);

        memeAdapter = new MemeAdapter(this, memes);

        recyclerViewMemes.setLayoutManager(lymg);
        recyclerViewMemes.setAdapter(memeAdapter);

        setupMenuListener();
        loadMenu();
        loadMemes();
    }

    private void loadMemes() {
        getSupportLoaderManager().restartLoader(LOADER_MEMES, null, this);
        progressBar.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.menu_slide)
    void clickMenuSlide() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.menu_search)
    void clickMenuSearch() {
        searchToolbar.setVisibility(View.VISIBLE);
        mainToolbar.setVisibility(View.GONE);
        etSearch.requestFocus();
    }

    @OnClick(R.id.menu_back)
    void clickMenuBack() {
        mainToolbar.setVisibility(View.VISIBLE);
        searchToolbar.setVisibility(View.GONE);
    }

    @OnClick(R.id.menu_clear)
    void clickMenuClear() {
        etSearch.setText("");
        etSearch.requestFocus();
    }

    @OnClick(R.id.menu_search_send)
    void clickSearchSend() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEWGROUP_REQUEST) {
            if (resultCode == RESULT_OK) {
                loadMenu();
            }
        }
    }

    @OnClick(R.id.fabNewMeme)
    void clickFabNew() {
        Intent intent = new Intent(this, WebSearchActivity.class);
        startActivity(intent);
    }

    private void loadMenu() {
        Menu menu = navigationView.getMenu();
        menu.clear();
        String[] project = new String[]{
                DataBaseContract.GroupEntry._ID,
                DataBaseContract.GroupEntry.COLUMN_NAME,
                DataBaseContract.GroupEntry.COLUMN_IMAGE};

        Cursor cursor = getContentResolver().query(DataBaseContract.GroupEntry.CONTENT_URI,
                project, "", null, DataBaseContract.GroupEntry.COLUMN_NAME);

        int group = 0;
        int order = 0;
        MenuItem item;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                item = menu.add(group, cursor.getInt(0), order, cursor.getString(1));
                item.setIcon(cursor.getInt(2));
                order += 1;
            }
            cursor.close();
        }

        item = menu.add(group, MENU_ADD_ID, order, R.string.new_group);
        item.setIcon(R.drawable.ic_add_group_24dp);
    }

    @NonNull
    @Override
    public Loader<List<Meme>> onCreateLoader(int id, @Nullable Bundle args) {
        return new MemesAsyncLoader(this, args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Meme>> loader, List<Meme> data) {
        memes = data;
        memeAdapter.setMemes(memes);
        memeAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Meme>> loader) {

    }

    @Override
    public void onMemeClick(int id) {

    }
}
