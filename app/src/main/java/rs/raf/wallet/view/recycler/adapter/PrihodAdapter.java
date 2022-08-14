package rs.raf.wallet.view.recycler.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Function;

import rs.raf.wallet.R;
import rs.raf.wallet.model.Prihod;
import rs.raf.wallet.view.activities.EditFinanceActivity;
import rs.raf.wallet.view.activities.FinanceActivity;
import rs.raf.wallet.view_model.RecyclerViewModelPrihod;

public class PrihodAdapter extends ListAdapter<Prihod, PrihodAdapter.ViewHolder> {

    private final Function<Prihod, Void> onPrihodClicked;
    private final Function<Prihod, Void> onPrihodDelete;
    private final Function<Prihod, Void> onPrihodEdit;


    public PrihodAdapter(@NonNull DiffUtil.ItemCallback<Prihod> diffCallback, Function<Prihod, Void> onPrihodClicked, Function<Prihod, Void> onPrihodDelete,
                         Function<Prihod, Void> onPrihodEdit) {
        super(diffCallback);
        this.onPrihodClicked = onPrihodClicked;
        this.onPrihodDelete = onPrihodDelete;
        this.onPrihodEdit = onPrihodEdit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_prihod, parent, false);
        return new ViewHolder(view, parent.getContext(), position -> {
            Prihod prihod = getItem(position);
            onPrihodClicked.apply(prihod);
            return null;
        }, position -> {
            Prihod prihod = getItem(position);
            onPrihodDelete.apply(prihod);
            return null;
        },position -> {
            Prihod prihod = getItem(position);
            onPrihodEdit.apply(prihod);
            return null;
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prihod prihod = getItem(position);
        holder.bind(prihod);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;

        public ViewHolder(@NonNull View itemView, Context context, Function<Integer, Void> onItemClicked, Function<Integer, Void> onItemDelete,
                          Function<Integer, Void> onItemEdit) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(v -> {
                if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClicked.apply(getAdapterPosition());
                }
            });
            itemView.findViewById(R.id.deletePicture).setOnClickListener(v -> {
                if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemDelete.apply(getAdapterPosition());
                }
            });
            itemView.findViewById(R.id.editPicture).setOnClickListener(v -> {
                if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemEdit.apply(getAdapterPosition());
                }
            });
        }

        public void bind(Prihod prihod) {
            ImageView imageView = itemView.findViewById(R.id.prihodPicture);
            ((TextView)itemView.findViewById(R.id.naslovItem)).setText(prihod.getNaslov());
            ((TextView)itemView.findViewById(R.id.kolicinaItem)).setText(prihod.getKolicina()+"");
            ImageView editPicture = itemView.findViewById(R.id.editPicture);
            ImageView deletePicture = itemView.findViewById(R.id.deletePicture);

        }

    }
}
