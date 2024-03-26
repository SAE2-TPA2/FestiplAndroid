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
import java.util.List;

import but2.s4.festiplandroid.DetailsActivity;
import but2.s4.festiplandroid.R;
import but2.s4.festiplandroid.festivals.Festival;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {
    private final List<Festival> favoriteList;
    private Context context;

    public FavoritesAdapter(List<Festival> festivalList) {
        this.favoriteList = festivalList;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorites_festival, parent, false);
        return new FavoritesAdapter.FavoritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.FavoritesViewHolder holder, int position) {
        Festival festival = favoriteList.get(position);
        holder.nomFestival.setText(festival.getNomFestival());
        holder.categorie.setText(festival.getCategorieFestival());
        holder.dateDebut.setText(festival.getDateDebutFestival());
        holder.dateFin.setText(festival.getDateFinFestival());
        holder.buttonShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                intent.putExtra("ID_EXTRA", festival.getDateFinFestival());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageFestival;
        public TextView nomFestival;
        public TextView categorie;
        public TextView dateDebut;
        public TextView dateFin;
        public Button buttonShowDetails;
        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFestival = itemView.findViewById(R.id.image_festival);
            nomFestival = itemView.findViewById(R.id.nom_festival);
            categorie = itemView.findViewById(R.id.categorie_festival);
            dateDebut = itemView.findViewById(R.id.date_debut_festival);
            dateFin = itemView.findViewById(R.id.date_fin_festival);
            buttonShowDetails = itemView.findViewById(R.id.buttonShowDetails);
        }
    }
}
