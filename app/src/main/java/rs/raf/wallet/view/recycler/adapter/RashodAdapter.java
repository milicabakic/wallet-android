package rs.raf.wallet.view.recycler.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Function;

import rs.raf.wallet.R;
import rs.raf.wallet.model.Rashod;
import rs.raf.wallet.view.activities.EditFinanceActivity;


public class RashodAdapter extends ListAdapter<Rashod, RashodAdapter.ViewHolder> {

    private final Function<Rashod, Void> onRashodClicked;
    private final Function<Rashod, Void> onRashodDelete;
    private final Function<Rashod, Void> onRashodEdit;


    public RashodAdapter(@NonNull DiffUtil.ItemCallback<Rashod> diffCallback, Function<Rashod, Void> onFinanceClicked,
                         Function<Rashod, Void> onFinanceDelete,  Function<Rashod, Void> onFinanceEdit) {
        super(diffCallback);
        this.onRashodClicked = onFinanceClicked;
        this.onRashodDelete = onFinanceDelete;
        this.onRashodEdit = onFinanceEdit;
    }

    @NonNull
    @Override
    public RashodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rashod, parent, false);
        return new RashodAdapter.ViewHolder(view, parent.getContext(), position -> {
            Rashod rashod = getItem(position);
            onRashodClicked.apply(rashod);
            return null;
        }, position -> {
            Rashod rashod = getItem(position);
            onRashodDelete.apply(rashod);
            return null;
        }, position -> {
            Rashod rashod = getItem(position);
            onRashodEdit.apply(rashod);
            return null;
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RashodAdapter.ViewHolder holder, int position) {
        Rashod rashod = getItem(position);
        holder.bind(rashod);
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
            itemView.findViewById(R.id.pictureDelete).setOnClickListener(v -> {
                if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemDelete.apply(getAdapterPosition());
                }
            });
            itemView.findViewById(R.id.pictureEdit).setOnClickListener(v -> {
                if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemEdit.apply(getAdapterPosition());
                }
            });
        }

        public void bind(Rashod rashod) {
            ImageView imageView = itemView.findViewById(R.id.rashodPicture);
            ((TextView)itemView.findViewById(R.id.itemNaslov)).setText(rashod.getNaslov());
            ((TextView)itemView.findViewById(R.id.itemKolicina)).setText(rashod.getKolicina()+"");
            ImageView editPicture = itemView.findViewById(R.id.pictureEdit);
            ImageView deletePicture = itemView.findViewById(R.id.pictureDelete);
        }

    }
}
