package but2.s4.festiplandroid.adaptater;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import but2.s4.festiplandroid.DetailsActivity;
import but2.s4.festiplandroid.R;
import but2.s4.festiplandroid.api.FestiplanApi;
import but2.s4.festiplandroid.festivals.Festival;
import but2.s4.festiplandroid.session.User;

public class FestivalAdapter extends RecyclerView.Adapter<FestivalAdapter.FestivalViewHolder> {

    private Context context;
    private List<Festival> festivalList;

    private RequestQueue fileRequete;

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

//        if(festival.getFavorite()){
//            holder.imageFavorites.setImageResource(R.drawable.favorites_selected);
//        } else {
//            holder.imageFavorites.setImageResource(R.drawable.favorites_deselected);
//        }

        updateFavori(holder, festival);
    }

    /**
     * Parcours des favoris de l'utilisateur pour mettre à jour l'icône de favoris présent
     * @param holder : holder de l'item
     * @param festival : festival à mettre à jour
     */
    public void updateFavori(FestivalViewHolder holder, Festival festival){
        JsonArrayRequest userFavoris = new JsonArrayRequest(
            FestiplanApi.getURLFestivalAllFavorites(User.getInstance().getIdUser()),
            response -> {
                // Parcours des favoris de l'utilisateur
                for (int i = 0; i < response.length(); i++) {
                    try {
                        if (response.getJSONObject(i).getInt("idFestival") == festival.getIdFestival()) {
                            // Si le festival est dans les favoris de l'utilisateur,
                            // on met l'icône de favoris à l'état sélectionné
                            holder.imageFavorites.setImageResource(R.drawable.favorites_selected);
                            festival.setFavorite(true);
                        }
                    } catch (Exception e) {
                        // Si une erreur survient, on met l'icône de favoris à l'état déselectionné
                        holder.imageFavorites.setImageResource(R.drawable.favorites_deselected);
                        festival.setFavorite(false);
                        e.printStackTrace();
                    }
                }
            },
            error -> {
                error.printStackTrace();
            }
        ) {
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("APIKEY", User.getInstance().getAPIKey());
                return headers;
            }
        };

        getFileRequete().add(userFavoris);
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

    /**
     * Renvoie la file d'attente pour les requêtes Web :
     * - si la file n'existe pas encore : elle est créée puis renvoyée
     * - si une file d'attente existe déjà : elle est renvoyée
     * On assure ainsi l'unicité de la file d'attente
     *
     * @return RequestQueue une file d'attente pour les requêtes Volley
     */
    private RequestQueue getFileRequete() {
        if (fileRequete == null) {
            fileRequete = Volley.newRequestQueue(context);
        }
        return fileRequete;
    }
}
