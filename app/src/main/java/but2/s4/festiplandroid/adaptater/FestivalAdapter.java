package but2.s4.festiplandroid.adaptater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import but2.s4.festiplandroid.R;
import but2.s4.festiplandroid.festivals.Festival;

public class FestivalAdapter extends RecyclerView.Adapter<FestivalAdapter.FestivalViewHolder> {
    private ArrayList<Festival> festivalList;

    public FestivalAdapter(ArrayList<Festival> festivalList) {
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
        holder.bind(festival);
    }

    @Override
    public int getItemCount() {
        return festivalList.size();
    }

    public static class FestivalViewHolder extends RecyclerView.ViewHolder {
        private TextView nomTextView;
        private TextView categorieTextView;

        public FestivalViewHolder(@NonNull View itemView) {
            super(itemView);
            nomTextView = itemView.findViewById(R.id.nom_festival);
            categorieTextView = itemView.findViewById(R.id.categorie_festival);
        }

        public void bind(Festival festival) {
            nomTextView.setText(festival.getNomFestival());
            categorieTextView.setText(festival.getCategorieFestival());
        }
    }
}
