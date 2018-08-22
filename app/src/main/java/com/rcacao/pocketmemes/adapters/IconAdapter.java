package com.rcacao.pocketmemes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.data.models.GroupIcon;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconViewHolder> {

    private final Context context;
    private List<GroupIcon> icons;
    private IconClickListener listener;

    public IconAdapter(Context context, List<GroupIcon> icons) {
        this.context = context;
        this.listener = (IconClickListener) context;
        this.icons = icons;
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_icon, parent, false);
        return new IconViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {

        holder.imageIcon.setImageResource(icons.get(position).getImage());
        selectIcon(holder, icons.get(position).isChecked());

    }

    private void selectIcon(@NonNull IconViewHolder holder, boolean value) {

        if (value) {
            holder.imageIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            holder.imageIcon.setColorFilter(ContextCompat.getColor(context, R.color.iconDark));
        }
    }


    @Override
    public int getItemCount() {
        if (icons != null) {
            return icons.size();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setIcons(List<GroupIcon> icons) {
        this.icons = icons;
    }

    public interface IconClickListener {
        void onClick(int id);
    }

    public class IconViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image_icon)
        ImageView imageIcon;

        IconViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(getAdapterPosition());
        }
    }


}
