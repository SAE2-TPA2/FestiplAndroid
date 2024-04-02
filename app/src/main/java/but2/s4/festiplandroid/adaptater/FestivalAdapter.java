package but2.s4.festiplandroid.adaptater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import but2.s4.festiplandroid.DetailsActivity;
import but2.s4.festiplandroid.R;
import but2.s4.festiplandroid.festivals.Festival;

public class FestivalAdapter extends RecyclerView.Adapter<FestivalAdapter.FestivalViewHolder> {

    private Context context;
    private List<Festival> festivalList;

    public FestivalAdapter(Context context, List<Festival> festivalList) {
        this.context = context;
        this.festivalList = festivalList;
    }

    @NonNull
    @Override
    public FestivalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_festival, parent, false);
        return new FestivalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FestivalViewHolder holder, int position) {
        Festival festival = festivalList.get(position);
        Glide.with(context)
                .load(festival.getImagePath())
                .into(holder.imageFestival);
        holder.nomFestival.setText(festival.getNomFestival());
        holder.dateDebutFestival.setText(festival.getDateDebutFestival());
        holder.dateFinFestival.setText(festival.getDateFinFestival());
        holder.buttonShowDetails.setId(festival.getIdFestival());
        holder.buttonShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("ID_EXTRA", festival.getIdFestival());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        if(festival.getFavorite()){
            holder.imageFavorites.setImageResource(R.drawable.favorites_selected);
        } else {
            holder.imageFavorites.setImageResource(R.drawable.favorites_deselected);
        }
    }

    @Override
    public int getItemCount() {
        return festivalList.size();
    }

    public static class FestivalViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFestival;
        TextView nomFestival;
        TextView dateDebutFestival;
        TextView dateFinFestival;
        Button buttonShowDetails;
        ImageView imageFavorites;

        public FestivalViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFestival = itemView.findViewById(R.id.image_festival);
            nomFestival = itemView.findViewById(R.id.nom_festival);
            dateDebutFestival = itemView.findViewById(R.id.date_debut_festival);
            dateFinFestival = itemView.findViewById(R.id.date_fin_festival);
            buttonShowDetails = itemView.findViewById(R.id.button_show_details);
            imageFavorites = itemView.findViewById(R.id.image_favorites);
        }
    }
}
