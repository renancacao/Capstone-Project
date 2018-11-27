package com.rcacao.pocketmemes.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rcacao.pocketmemes.FileUtils;
import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.adapters.GroupAdapter;
import com.rcacao.pocketmemes.data.database.DataBaseContract;
import com.rcacao.pocketmemes.data.models.Group;
import com.rcacao.pocketmemes.data.models.Meme;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rcacao.pocketmemes.data.util.DataUtils.getMemeFromCursor;

public class DetailsActivity extends BaseActivity {

    public static final String EXTRA_MEME = "meme";
    public static final String EXTRA_MEME_ID = "meme_id";
    private static final String ANALYTIC_NAME = "details";
    private static final int REQUEST_EDIT = 0;
    @BindView(R.id.image_meme)
    ImageView imageMeme;

    @BindView(R.id.recyclerView_groups)
    RecyclerView recyclerViewGroups;

    @BindView(R.id.textView_name)
    TextView textViewName;

    @BindView(R.id.textView_tags)
    TextView textViewTags;

    private Meme meme = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setupActivity(R.color.colorShareDark, ANALYTIC_NAME);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        setupView(intent);
    }


    private void setupView(Intent intent) {

        if (intent != null) {
            if (intent.hasExtra(EXTRA_MEME)) {
                meme = getIntent().getParcelableExtra(EXTRA_MEME);
            } else if (intent.hasExtra(EXTRA_MEME_ID)){
                meme = getMemeFromID(intent.getIntExtra(EXTRA_MEME_ID,0));
                setResult(RESULT_OK);
            }
        }

        if (meme == null) {
            finish();
        }

        loadMeme();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setupView(intent);
    }

    private Meme getMemeFromID(int memeId) {

        Meme memeFromId = null;
        Cursor cursor = getContentResolver().query(
                Uri.parse(DataBaseContract.MemeEntry.CONTENT_URI.toString() + "/" + memeId),
                null, null, null, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                memeFromId = getMemeFromCursor(cursor,this);
            }
            cursor.close();
        }

        return memeFromId;

    }

    private void loadMeme() {

        textViewName.setText(meme.getName());
        textViewTags.setText(meme.getTagsText());

        openImage(meme.getImageUri(this));

        List<Group> groups = meme.getGroups();
        GroupAdapter adapter = new GroupAdapter(this, groups);
        adapter.setHasStableIds(true);
        recyclerViewGroups.setAdapter(adapter);
        recyclerViewGroups.setHasFixedSize(true);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewGroups.setLayoutManager(layoutManager);
    }


    private void openImage(Uri uri) {
        Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).error(R.drawable.notfound).into(imageMeme);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK && data != null) {
            meme = data.getParcelableExtra(EXTRA_MEME);
            setResult(RESULT_OK);
            loadMeme();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.menu_back)
    void clickBack() {
        finish();
    }

    @OnClick(R.id.button_edit)
    void clickEdit() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(EditActivity.EXTRA_EDIT_MEME, meme);
        startActivityForResult(intent, REQUEST_EDIT);
    }

    @OnClick(R.id.button_share)
    void clickShare() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        File file = new File(FileUtils.getFileNameWithPath(meme.getImage()));
        if (file.exists()) {
            Uri uri = meme.getImageUri(this);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, getString(R.string.sharing_title)));
        } else {
            Toast.makeText(this, getString(R.string.file_not_found), Toast.LENGTH_SHORT).show();
        }
    }


}
