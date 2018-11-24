package com.rcacao.pocketmemes.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rcacao.pocketmemes.FileUtils;
import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.adapters.IconAdapter;
import com.rcacao.pocketmemes.data.models.Group;
import com.rcacao.pocketmemes.data.models.GroupIcon;
import com.rcacao.pocketmemes.data.models.Meme;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rcacao.pocketmemes.data.database.DataBaseContract.GroupEntry;
import static com.rcacao.pocketmemes.data.database.DataBaseContract.GroupMemeEntry;
import static com.rcacao.pocketmemes.data.database.DataBaseContract.MemeEntry;
import static com.rcacao.pocketmemes.data.database.DataBaseContract.TagsEntry;
import static com.rcacao.pocketmemes.ui.DetailsActivity.EXTRA_MEME;

public class EditActivity extends BaseActivity implements IconAdapter.IconClickListener {

    public static final String EXTRA_EDIT_MEME = "editmeme";
    public static final String EXTRA_FILE_NAME = "filename";
    private static final String ANALYTIC_NAME = "edit";
    @BindView(R.id.image_meme)
    ImageView imageMeme;

    @BindView(R.id.recyclerView_groups)
    RecyclerView recyclerViewGroups;

    @BindView(R.id.editText_name)
    EditText editTextName;

    @BindView(R.id.editText_tags)
    EditText editTextTags;

    private IconAdapter adapter;
    private List<GroupIcon> groups;
    private String fileName;
    private Meme editMeme = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setupActivity(R.color.webSearchToolbarDark, ANALYTIC_NAME);
        ButterKnife.bind(this);

        setupGroupsList();

        if (getIntent() != null) {
            editMeme = getIntent().getParcelableExtra(EXTRA_EDIT_MEME);
            if (isEdition()) {
                fileName = editMeme.getImage();
                setupView(editMeme);
            } else {
                fileName = getIntent().getStringExtra(EXTRA_FILE_NAME);
            }
            openImage(fileName);
        }

    }

    private boolean isEdition() {
        return editMeme != null;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private void setupView(Meme meme) {
        editTextName.setText(meme.getName());
        editTextTags.setText(meme.getTagsText());
        for (Group g : meme.getGroups()) {
            int id;
            if ((id = groups.indexOf(g)) >= 0) {
                groups.get(id).setChecked(true);
            }
        }

    }

    private void openImage(String file) {
        String filename = FileUtils.getFileNameWithPath(file);
        Uri uri = Uri.fromFile(new File(filename));
        Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.drawable.notfound).into(imageMeme);
    }

    private void setupGroupsList() {
        groups = getGroupsIcons();
        adapter = new IconAdapter(this, groups);
        adapter.setHasStableIds(true);
        recyclerViewGroups.setAdapter(adapter);
        recyclerViewGroups.setHasFixedSize(true);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2,
                        LinearLayoutManager.HORIZONTAL, false);

        recyclerViewGroups.setLayoutManager(layoutManager);
    }

    private List<GroupIcon> getGroupsIcons() {

        List<GroupIcon> list = new ArrayList<>();

        Cursor result = getContentResolver().query(GroupEntry.CONTENT_URI,
                null, null, null, GroupEntry._ID);

        if (result != null && result.moveToFirst()) {
            do {
                GroupIcon icon = new GroupIcon(
                        result.getInt(result.getColumnIndex(GroupEntry.COLUMN_IMAGE)));
                icon.setId(result.getInt(result.getColumnIndex(GroupEntry._ID)));
                icon.setName(result.getString(result.getColumnIndex(GroupEntry.COLUMN_NAME)));
                list.add(icon);
            }
            while (result.moveToNext());
            result.close();
        }
        return list;
    }

    @Override
    public void onClick(int id) {
        groups.get(id).setChecked(!groups.get(id).isChecked());
        adapter.setIcons(groups);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.menu_back)
    void clickBack() {
        this.setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.menu_ok)
    void clickOK() {

        if (isEdition()) {
            saveEditedMeme(editMeme.getId());
        } else {
            createNewMeme();
        }

    }

    private void saveEditedMeme(int id) {

        ContentValues values = new ContentValues();
        values.put(MemeEntry.COLUMN_NAME, editTextName.getText().toString());

        editMeme.setName(editTextName.getText().toString());

        int result = getContentResolver().update(MemeEntry.CONTENT_URI, values,
                MemeEntry._ID + "=?", new String[]{String.valueOf(id)});
        if (result > 0) {

            getContentResolver().delete(GroupMemeEntry.CONTENT_URI,
                    GroupMemeEntry.COLUMN_ID_MEME + "=?", new String[]{String.valueOf(id)});
            getContentResolver().delete(TagsEntry.CONTENT_URI,
                    TagsEntry.COLUMN_ID_MEME + "=?", new String[]{String.valueOf(id)});

            insertTags(id);
            insertGroups(id);

            Toast.makeText(this, R.string.meme_saved, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.putExtra(EXTRA_MEME,editMeme);
            this.setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void createNewMeme() {
        ContentValues values = new ContentValues();
        values.put(MemeEntry.COLUMN_NAME, editTextName.getText().toString());
        values.put(MemeEntry.COLUMN_IMAGE, fileName);
        values.put(MemeEntry.COLUMN_CREATION, Calendar.getInstance().getTime().toString());

        Uri uriResult = getContentResolver().insert(MemeEntry.CONTENT_URI, values);
        if (uriResult != null) {
            int id = Integer.parseInt(uriResult.getLastPathSegment());
            insertTags(id);
            insertGroups(id);

            Toast.makeText(this, R.string.new_meme_saved, Toast.LENGTH_SHORT).show();
            this.setResult(RESULT_OK);
            finish();
        }
    }

    private void insertTags(int id) {
        if (!editTextTags.getText().toString().trim().isEmpty()) {

            String[] tags = editTextTags.getText().toString().split(",");

            if (isEdition()) {
                editMeme.setTags(Arrays.asList(tags));
            }

            ContentValues valuesTags;
            for (String tag : tags) {
                if (!tag.trim().isEmpty()) {
                    valuesTags = new ContentValues();
                    valuesTags.put(TagsEntry.COLUMN_ID_MEME, id);
                    valuesTags.put(TagsEntry.COLUMN_TAG, tag.trim());
                    getContentResolver().insert(TagsEntry.CONTENT_URI, valuesTags);
                }
            }

        }
    }

    private void insertGroups(int id) {
        ContentValues valuesGroups;

        List<Group> newgroups = new ArrayList<>();

        for (GroupIcon group : groups) {
            if (group.isChecked()) {
                valuesGroups = new ContentValues();
                valuesGroups.put(GroupMemeEntry.COLUMN_ID_MEME, id);
                valuesGroups.put(GroupMemeEntry.COLUMN_ID_GROUP, group.getId());
                getContentResolver().insert(GroupMemeEntry.CONTENT_URI, valuesGroups);
                newgroups.add(new Group(group.getId(), group.getImage(), group.getName()));
            }
        }

        if (isEdition()) {
            editMeme.setGroups(newgroups);
        }
    }
}
