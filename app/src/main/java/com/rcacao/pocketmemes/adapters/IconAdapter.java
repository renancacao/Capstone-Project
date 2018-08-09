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

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconViewHolder> {


    //final private ListItemClickListener mOnClickListener;
    private int[] icons;
    private boolean values[];
    private final Context context;
    private int selected = -1;
    private IconClickListener listener;

    public IconAdapter(Context context) {
        this.context = context;
        this.listener = (IconClickListener) context;

        icons = getIcons();
        values = new boolean[icons.length];
    }

    private int[] getIcons() {
        return new int[]{R.drawable.ic_accessibility,
                R.drawable.ic_account_balance,
                R.drawable.ic_account_box,
                R.drawable.ic_airline_seat_individual_suite,
                R.drawable.ic_airplanemode_active,
                R.drawable.ic_android,
                R.drawable.ic_assessment,
                R.drawable.ic_attach_file,
                R.drawable.ic_attach_money,
                R.drawable.ic_beach_access,
                R.drawable.ic_bookmark,
                R.drawable.ic_brightness_low,
                R.drawable.ic_build,
                R.drawable.ic_business,
                R.drawable.ic_card_giftcard,
                R.drawable.ic_card_travel,
                R.drawable.ic_casino,
                R.drawable.ic_child_friendly,
                R.drawable.ic_directions_car,
                R.drawable.ic_directions_railway,
                R.drawable.ic_directions_run,
                R.drawable.ic_event_seat,
                R.drawable.ic_explore,
                R.drawable.ic_extension,
                R.drawable.ic_face,
                R.drawable.ic_favorite,
                R.drawable.ic_fiber_new,
                R.drawable.ic_fitness_center,
                R.drawable.ic_flag,
                R.drawable.ic_free_breakfast,
                R.drawable.ic_games,
                R.drawable.ic_gavel,
                R.drawable.ic_grade,
                R.drawable.ic_group_work,
                R.drawable.ic_headset,
                R.drawable.ic_headset_mic,
                R.drawable.ic_help,
                R.drawable.ic_high_quality,
                R.drawable.ic_home,
                R.drawable.ic_https,
                R.drawable.ic_insert_emoticon,
                R.drawable.ic_kitchen,
                R.drawable.ic_language,
                R.drawable.ic_library_music,
                R.drawable.ic_lightbulb_outline,
                R.drawable.ic_local_taxi,
                R.drawable.ic_looks,
                R.drawable.ic_mood_bad,
                R.drawable.ic_motorcycle,
                R.drawable.ic_movie,
                R.drawable.ic_music_video,
                R.drawable.ic_new_releases,
                R.drawable.ic_pan_tool,
                R.drawable.ic_perm_identity,
                R.drawable.ic_pets,
                R.drawable.ic_radio,
                R.drawable.ic_room,
                R.drawable.ic_school,
                R.drawable.ic_shopping_cart,
                R.drawable.ic_smoking_rooms,
                R.drawable.ic_store,
                R.drawable.ic_supervisor_account,
                R.drawable.ic_theaters,
                R.drawable.ic_thumb_down,
                R.drawable.ic_thumb_up,
                R.drawable.ic_timeline,
                R.drawable.ic_videogame_asset,
                R.drawable.ic_wc,
                R.drawable.ic_weekend,
                R.drawable.ic_whatshot};
    }

    public int getSelectedId() {
        return icons[selected];
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_icon, parent,  false);
        return new IconViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {

        holder.imageIcon.setImageResource(icons[position]);
        selectIcon(holder, values[position]);

    }

    private void selectIcon(@NonNull IconViewHolder holder, boolean value) {

        if (value){
            holder.imageIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
        }
        else{
            holder.imageIcon.setColorFilter(ContextCompat.getColor(context, R.color.iconDark));
        }
    }


    @Override
    public int getItemCount() {
        if  (icons != null){
            return icons.length;
        }
        else{
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



    public class IconViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image_icon) ImageView imageIcon;

        IconViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selected = getAdapterPosition();
            Arrays.fill(values,false);
            values[selected] = true;
            notifyDataSetChanged();
            listener.onClick(getSelectedId());
        }
    }

    public interface IconClickListener{
        void onClick(int id);
    }


}
