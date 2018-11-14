package com.rcacao.pocketmemes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.data.models.ResultItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultHolder> {


    private final Context context;
    private List<ResultItem> items;
    private final ResultClickListener listener;

    public ResultAdapter(Context context, List<ResultItem> items) {
        this.context = context;
        this.items = items;
        listener = (ResultClickListener)context;
    }

    public void setItems(List<ResultItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_result, parent, false);
        return new ResultHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {

        Picasso.get().load(items.get(position).getLink())
                .resize(R.integer.width_resize,0)
                .onlyScaleDown()
                .into(holder.imageResult);

    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
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

    public interface ResultClickListener {
        void onClick(int id);
    }

    public class ResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image_result)
        ImageView imageResult;

        ResultHolder(View itemView) {
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
