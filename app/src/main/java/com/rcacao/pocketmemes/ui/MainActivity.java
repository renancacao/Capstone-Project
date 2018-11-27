package com.rcacao.pocketmemes.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.adapters.MemeAdapter;
import com.rcacao.pocketmemes.data.database.DataBaseContract;
import com.rcacao.pocketmemes.data.loaders.MemesAsyncLoader;
import com.rcacao.pocketmemes.data.models.Meme;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<Meme>>, MemeAdapter.ResultClickListener {

    private static final String ANALYTIC_NAME = "main";

    private static final int NEWGROUP_REQUEST = 10;
    private static final int NEWMEME_REQUEST = 15;
    private static final int DETAILS_REQUEST = 20;

    private static final int LOADER_MEMES = 1;
    private static final String PREF_ORDER = "pref_order";
    private static final int MENU_ADD_ID = -1;
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
    @BindView(R.id.group_layout)
    ConstraintLayout groupLayout;
    @BindView(R.id.image_group)
    ImageView imageGroup;
    @BindView(R.id.textview_group)
    TextView textViewGroup;
    @BindView(R.id.fabNewMeme)
    FloatingActionButton fabNewMeme;
    @BindView(R.id.adView)
    AdView adView;
    private MemeAdapter memeAdapter;
    private List<Meme> memes = null;

    private String selectedGroup = "";
    private String order = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActivity(R.color.colorPrimaryDark, ANALYTIC_NAME);

        ButterKnife.bind(this);

        initSetup();

    }

    private void initSetup() {
        getOrderFromPreferences();
        setupRecyclerView();
        setupMenuListener();
        loadMenu();
        loadMemes("", "", order);
        setupListeners();
        setSupportActionBar(mainToolbar);
        setupAds();
    }

    private void getOrderFromPreferences() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        order = preferences.getString(PREF_ORDER, MemesAsyncLoader.ORDER_NAME);
    }

    private void setupRecyclerView() {
        StaggeredGridLayoutManager lymg = new StaggeredGridLayoutManager(
                getResources().getInteger(R.integer.main_collumns),
                StaggeredGridLayoutManager.VERTICAL);

        memeAdapter = new MemeAdapter(this, memes);

        recyclerViewMemes.setLayoutManager(lymg);
        recyclerViewMemes.setAdapter(memeAdapter);
    }

    private void setupMenuListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == MENU_ADD_ID) {
                    openNewGroup();
                } else {
                    selectGroup(item);

                }
                return true;
            }
        });
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
        int menuOrder = 0;
        MenuItem item;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                item = menu.add(group, cursor.getInt(0), menuOrder, cursor.getString(1));
                item.setIcon(cursor.getInt(2));
                menuOrder += 1;
            }
            cursor.close();
        }

        item = menu.add(group, MENU_ADD_ID, menuOrder, R.string.new_group_item);
        item.setIcon(R.drawable.ic_add_group_24dp);

    }

    private void loadMemes(String search, String idGroup, String order) {
        Bundle args = new Bundle();
        args.putString(MemesAsyncLoader.ARG_SEARCH, search);
        args.putString(MemesAsyncLoader.ARG_GROUP, idGroup);
        args.putString(MemesAsyncLoader.ARG_ORDER, order);

        LoaderManager.getInstance(this).restartLoader(LOADER_MEMES, args, this);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setupListeners() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clickSearchMenu();
                    return true;

                }
                return false;
            }
        });
    }

    private void setupAds() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
    }

    private void openNewGroup() {
        Intent intent = new Intent(this, NewGroupActivity.class);
        startActivityForResult(intent, NEWGROUP_REQUEST);
        drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void selectGroup(@NonNull MenuItem item) {
        selectedGroup = String.valueOf(item.getItemId());
        groupLayout.setVisibility(View.VISIBLE);
        imageGroup.setImageDrawable(item.getIcon());
        textViewGroup.setText(item.getTitle());
        loadMemes(etSearch.getText().toString(), selectedGroup, order);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @OnClick(R.id.menu_search_send)
    void clickSearchMenu() {
        loadMemes(etSearch.getText().toString(), selectedGroup, order);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_order_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuName:
                order = MemesAsyncLoader.ORDER_NAME;
                loadMemes(etSearch.getText().toString(), selectedGroup, order);
                return true;
            case R.id.mnuNewest:
                order = MemesAsyncLoader.ORDER_NEWEST;
                loadMemes(etSearch.getText().toString(), selectedGroup, order);
                return true;
            case R.id.mnuOldest:
                order = MemesAsyncLoader.ORDER_OLDEST;
                loadMemes(etSearch.getText().toString(), selectedGroup, order);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @OnClick(R.id.image_clear_group)
    void clickClearGroup() {
        selectedGroup = "";
        groupLayout.setVisibility(View.GONE);
        loadMemes(etSearch.getText().toString(), selectedGroup, order);

    }

    @OnClick(R.id.menu_slide)
    void clickMenuSlide() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.menu_search)
    void clickMenuSearch() {
        setBarColor(R.color.searchToolbarDark);
        searchToolbar.setVisibility(View.VISIBLE);
        mainToolbar.setVisibility(View.GONE);
        fabNewMeme.hide();
        adView.setVisibility(View.VISIBLE);
        showInput(etSearch);
    }


    @OnClick(R.id.menu_back)
    void clickMenuBack() {
        setBarColor(R.color.colorPrimaryDark);
        mainToolbar.setVisibility(View.VISIBLE);
        searchToolbar.setVisibility(View.GONE);
        etSearch.setText("");
        fabNewMeme.show();
        adView.setVisibility(View.GONE);
        closeInput();
        loadMemes("", selectedGroup, order);
    }

    @OnClick(R.id.menu_clear)
    void clickMenuClear() {
        etSearch.setText("");
        etSearch.requestFocus();
        loadMemes(etSearch.getText().toString(), selectedGroup, order);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == NEWGROUP_REQUEST) {
                loadMenu();
            } else if (requestCode == NEWMEME_REQUEST || requestCode ==  DETAILS_REQUEST) {
                loadMemes(etSearch.getText().toString(), selectedGroup, order);
            }
        }
    }

    @OnClick(R.id.fabNewMeme)
    void clickFabNew() {
        Intent intent = new Intent(this, WebSearchActivity.class);
        startActivityForResult(intent, NEWMEME_REQUEST);
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
        //unused
    }

    @Override
    public void onMemeClick(int id) {
        Meme meme = memes.get(id);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_MEME, meme);
        startActivityForResult(intent, DETAILS_REQUEST);
    }
}
