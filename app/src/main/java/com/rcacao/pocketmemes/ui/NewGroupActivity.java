package com.rcacao.pocketmemes.ui;

import android.content.ContentValues;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rcacao.pocketmemes.Constants;
import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.adapters.IconAdapter;
import com.rcacao.pocketmemes.data.database.DataBaseContract.GroupEntry;
import com.rcacao.pocketmemes.data.models.GroupIcon;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewGroupActivity extends AppCompatActivity implements IconAdapter.IconClickListener {


    @BindView(R.id.recyclerView_icons)
    RecyclerView recyclerViewIcons;

    @BindView(R.id.et_newgroup)
    EditText etNewGroup;

    @BindView(R.id.image_icon)
    ImageView imageIcon;

    private IconAdapter adapter;

    private List<GroupIcon> icons;
    private int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        ButterKnife.bind(this);

        icons = Constants.getIcons();

        adapter = new IconAdapter(this, icons);
        adapter.setHasStableIds(true);
        recyclerViewIcons.setAdapter(adapter);
        recyclerViewIcons.setHasFixedSize(true);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 3, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewIcons.setLayoutManager(layoutManager);
    }

    @OnClick(R.id.menu_back)
    void clickMenuBack() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.menu_ok)
    void clickMenuOK() {

        if (selected == -1) {
            Toast.makeText(this, R.string.select_icon, Toast.LENGTH_SHORT).show();
            return;
        }

        if (etNewGroup.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.enter_group_name, Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(GroupEntry.COLUMN_NAME, etNewGroup.getText().toString().trim());
        values.put(GroupEntry.COLUMN_IMAGE, icons.get(selected).getImage());

        Uri insert;
        try {
            insert = getContentResolver().insert(GroupEntry.CONTENT_URI, values);
        } catch (SQLiteConstraintException ex) {
            insert = null;
            Toast.makeText(this, R.string.group_exists, Toast.LENGTH_SHORT).show();
            etNewGroup.requestFocus();
            etNewGroup.selectAll();
        }
        if (insert != null) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onClick(int id) {
        selected = id;
        for (GroupIcon i : icons) {
            i.setChecked(false);
        }
        icons.get(selected).setChecked(true);
        adapter.setIcons(icons);
        adapter.notifyDataSetChanged();
        imageIcon.setImageResource(icons.get(selected).getImage());
    }
}
