package com.rcacao.pocketmemes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.adapters.ResultAdapter;
import com.rcacao.pocketmemes.data.models.ResultItem;
import com.rcacao.pocketmemes.data.models.SearchResult;
import com.rcacao.pocketmemes.loaders.GoogleSearchAsyncLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rcacao.pocketmemes.ui.CreatorMemeActivity.ARG_URL_IMAGE;

public class WebSearchActivity extends BaseActivity implements
        LoaderManager.LoaderCallbacks<SearchResult>, ResultAdapter.ResultClickListener {

    private static final int ID_LOADER = 0;
    private static final int REQUEST_CREATOR = 10   ;

    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView_web)
    RecyclerView recyclerView;

    private ResultAdapter adapter;

    private LinearLayoutManager glmg;
    private SearchResult searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_search);
        setBarColor(R.color.webSearchToolbarDark);
        ButterKnife.bind(this);


        adapter = new ResultAdapter(this, null);
        recyclerView.setAdapter(adapter);

        glmg = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        setupListeners();

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

    @OnClick(R.id.menu_search_send)
    void clickSearchMenu() {

        if (!etSearch.getText().toString().trim().isEmpty()) {
            Bundle args = new Bundle();
            args.putString(GoogleSearchAsyncLoader.ARG_SEARCH, etSearch.getText().toString().trim());
            getSupportLoaderManager().restartLoader(ID_LOADER, args, this);
        }

    }

    @OnClick(R.id.menu_back)
    void clickBackMenu() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.menu_clear)
    void clickClearMenu() {
        etSearch.setText("");
    }

    @NonNull
    @Override
    public Loader<SearchResult> onCreateLoader(int id, @Nullable Bundle args) {

        progressBar.setVisibility(View.VISIBLE);
        return new GoogleSearchAsyncLoader(this, args);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<SearchResult> loader, SearchResult data) {
        searchResult = data;
        if (searchResult != null) {
            adapter.setItems(searchResult.getItems());
            adapter.notifyDataSetChanged();
            recyclerView.setLayoutManager(glmg);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<SearchResult> loader) {

    }

    @Override
    public void onClick(int id) {

        ResultItem item = searchResult.getItems().get(id);
        Intent intent = new Intent(this, CreatorMemeActivity.class);
        intent.putExtra(ARG_URL_IMAGE, item.getLink());
        startActivityForResult(intent, REQUEST_CREATOR);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CREATOR && resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }
}
