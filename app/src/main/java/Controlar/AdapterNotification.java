package Controlar;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.noureddine.stockmanagment.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder>{

    List<String> list = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void updateNotification(List<String> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearNotification() {
        if (list != null){
            this.list = list != null ? list : new ArrayList<>();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public AdapterNotification.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viwe = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(viwe);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNotification.ViewHolder holder, int position) {

        holder.name.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView30);

        }
    }

}
