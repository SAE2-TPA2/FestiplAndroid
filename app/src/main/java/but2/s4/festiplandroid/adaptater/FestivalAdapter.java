package but2.s4.festiplandroid.adaptater;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import but2.s4.festiplandroid.R;
import but2.s4.festiplandroid.festivals.Festival;

public class FestivalAdapter extends RecyclerView.Adapter<FestivalAdapter.FestivalViewHolder> {
    private List<Festival> festivalList;
    private Context context;

    public FestivalAdapter(List<Festival> festivalList, Context context) {
        this.festivalList = festivalList;
        this.context = context;
    }

    @NonNull
    @Override
    public FestivalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_festival, parent, false);
        return new FestivalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FestivalViewHolder holder, int position) {
        Festival festival = festivalList.get(position);
        holder.nomFestival.setText(festival.getNomFestival());
        holder.categorie.setText(festival.getCategorieFestival());
        holder.dateDebut.setText(festival.getDateDebutFestival());
        holder.dateFin.setText(festival.getDateFinFestival());
        holder.buttonShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajoutez ici le code pour afficher les détails du festival lorsque le bouton est cliqué
            }
        });
        holder.buttonAddToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton imageButton = (ImageButton) v;
                Drawable currentDrawable = imageButton.getDrawable();

                if(festival.getFavorite()) {
                    imageButton.setImageResource(R.drawable.favorites_deselected);
                } else {
                    imageButton.setImageResource(R.drawable.favorites_selected);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return festivalList.size();
    }

    public static class FestivalViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageFestival;
        public TextView nomFestival;
        public TextView categorie;
        public TextView dateDebut;
        public TextView dateFin;
        public Button buttonShowDetails;
        public ImageButton buttonAddToFavorites;

        public FestivalViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFestival = itemView.findViewById(R.id.image_festival);
            nomFestival = itemView.findViewById(R.id.nom_festival);
            categorie = itemView.findViewById(R.id.categorie_festival);
            dateDebut = itemView.findViewById(R.id.date_debut_festival);
            dateFin = itemView.findViewById(R.id.date_fin_festival);
            buttonShowDetails = itemView.findViewById(R.id.buttonShowDetails);
            buttonAddToFavorites = itemView.findViewById(R.id.button_add_to_favorites);
        }
    }
}
