package com.rcacao.pocketmemes.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rcacao.pocketmemes.FileUtils;
import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.adapters.IconAdapter;
import com.rcacao.pocketmemes.data.database.DataBaseContract;
import com.rcacao.pocketmemes.data.models.GroupIcon;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rcacao.pocketmemes.data.database.DataBaseContract.GroupEntry;
import static com.rcacao.pocketmemes.data.database.DataBaseContract.MemeEntry;
import static com.rcacao.pocketmemes.data.database.DataBaseContract.TagsEntry;

public class EditActivity extends BaseActivity implements IconAdapter.IconClickListener {

    public static final String EXTRA_FILE_NAME = "filename";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setBarColor(R.color.webSearchToolbarDark);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            fileName = getIntent().getStringExtra(EXTRA_FILE_NAME);
        }
        if (fileName == null || fileName.isEmpty()) {
            finish();
        }
        openImage(fileName);

        groups = getGroupsIcons();
        adapter = new IconAdapter(this, groups);
        adapter.setHasStableIds(true);
        recyclerViewGroups.setAdapter(adapter);
        recyclerViewGroups.setHasFixedSize(true);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewGroups.setLayoutManager(layoutManager);
    }

    private void openImage(String file) {
        String filename = FileUtils.getFileNameWithPath(file);
        Uri uri = Uri.fromFile(new File(filename));
        Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageMeme);
    }

    public List<GroupIcon> getGroupsIcons() {

        List<GroupIcon> list = new ArrayList<>();

        Cursor result = getContentResolver().query(GroupEntry.CONTENT_URI,
                null, null, null, GroupEntry._ID);

        if (result != null && result.moveToFirst()) {
            do {
                GroupIcon icon = new GroupIcon(
                        result.getInt(result.getColumnIndex(GroupEntry.COLUMN_IMAGE)));
                icon.setId(result.getInt(result.getColumnIndex(GroupEntry._ID)));
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

        ContentValues values = new ContentValues();
        values.put(MemeEntry.COLUMN_NAME, editTextName.getText().toString());
        values.put(MemeEntry.COLUMN_IMAGE, fileName);
        values.put(MemeEntry.COLUMN_CREATION, Calendar.getInstance().getTime().toString());

        Uri uriResult = getContentResolver().insert(MemeEntry.CONTENT_URI, values);
        if (uriResult != null) {
            int id = Integer.parseInt(uriResult.getLastPathSegment());
            if (!editTextTags.getText().toString().trim().isEmpty()) {

                String[] tags = editTextTags.getText().toString().split(",");
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

            ContentValues valuesGroups;
            for (GroupIcon group : groups) {
                if (group.isChecked()) {
                    valuesGroups = new ContentValues();
                    valuesGroups.put(DataBaseContract.GroupMemeEntry.COLUMN_ID_MEME, id);
                    valuesGroups.put(DataBaseContract.GroupMemeEntry.COLUMN_ID_GROUP, group.getId());
                    getContentResolver().insert(DataBaseContract.GroupMemeEntry.CONTENT_URI, valuesGroups);
                }
            }

            Toast.makeText(this, R.string.meme_saved, Toast.LENGTH_SHORT).show();
            this.setResult(RESULT_OK);
            finish();
        }
    }
}
