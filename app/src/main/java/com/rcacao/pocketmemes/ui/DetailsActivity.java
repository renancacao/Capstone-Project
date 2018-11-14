package com.rcacao.pocketmemes.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.adapters.GroupAdapter;
import com.rcacao.pocketmemes.data.models.Group;
import com.rcacao.pocketmemes.data.models.Meme;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends BaseActivity {

    public static final String EXTRA_MEME = "meme";

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
        setBarColor(R.color.colorShareDark);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            meme = getIntent().getParcelableExtra(EXTRA_MEME);
        }

        if (meme == null) {
            finish();
        }

        loadMeme();
    }

    private void loadMeme() {

        textViewName.setText(meme.getName());
        textViewTags.setText(getTags(meme.getTags()));

        openImage(meme.getImageUri());

        List<Group> groups = meme.getGroups();
        GroupAdapter adapter = new GroupAdapter(this, groups);
        adapter.setHasStableIds(true);
        recyclerViewGroups.setAdapter(adapter);
        recyclerViewGroups.setHasFixedSize(true);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewGroups.setLayoutManager(layoutManager);
    }

    private String getTags(List<String> tags) {
        StringBuilder stringBuilderTag = new StringBuilder();
        for (String tag : tags) {
            if (stringBuilderTag.length() > 0) {
                stringBuilderTag.append(", ");
            }
            stringBuilderTag.append(tag);
        }
        return stringBuilderTag.toString();
    }

    private void openImage(Uri uri) {
        Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageMeme);
    }


    @OnClick(R.id.menu_back)
    void clickBack() {
        this.setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.button_share)
    void clickShare() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        Uri uri = meme.getImageUri();
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, getString(R.string.sharing_title)));

    }


}
