package Controlar;

import static com.noureddine.stockmanagment.Opiration.lengthText;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noureddine.stockmanagment.R;

import java.io.File;
import java.util.List;

import model.Product;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder> {

    List<Product> productList;
    Context context;
    OnItemClickListener onItemClickListener;

    public AdapterProduct(List<Product> productList, Context context,OnItemClickListener onItemClickListener) {
        this.productList = productList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AdapterProduct.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viwe = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        return new ViewHolder(viwe);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition();

        Product product = productList.get(currentPosition);
        holder.name.setText(lengthText(product.getName(),10));
        holder.quantity.setText(String.valueOf(product.getQuantity()));
        holder.price.setText(String.valueOf(product.getPriceSell()));


//        if (product.getImagePath().length() < 5){
//            holder.img.setImageResource(R.drawable.product);
//        }else {
//            holder.img.setImageURI(Uri.parse(product.getImagePath()));
//        }


        File pathImgProduct = new File(product.getImagePath());
        if (product.getImagePath().equals("") || product.getImagePath().equals(null) || product.getImagePath().length() < 5){
            holder.img.setImageResource(R.drawable.product);
        }else {
            if (pathImgProduct.exists()) {
                holder.img.setImageURI(Uri.parse(product.getImagePath()));
            }else {
                holder.img.setImageResource(R.drawable.product);
            }
        }


//        if (!productList.get(currentPosition).getImagePath().equals("") && !productList.get(currentPosition).getImagePath().equals(null)){
//            holder.img.setImageURI(Uri.parse(product.getImagePath()));
//        }
//        else {
//            holder.img.setImageResource(R.drawable.product);
//        }

//        if (product.getImagePath() != null){
//            File file =new File(product.getImagePath());
//            if (file.exists()){
//                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                holder.img.setImageBitmap(bitmap);
//            }
//        }

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemCklick(productList.get(currentPosition),"");
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (product.getImagePath().length() > 5 && pathImgProduct.exists()){
                    Activity activity = (Activity) v.getContext(); // Ensure we get the correct Activity context
                    if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                        LayoutInflater inflater = LayoutInflater.from(activity);
                        View dialogView = inflater.inflate(R.layout.view_img_product, null);
                        ImageView imgProduct = dialogView.findViewById(R.id.imageView8);
                        ImageView close = dialogView.findViewById(R.id.imageView_close);
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setView(dialogView);
                        AlertDialog dialog = builder.create();
                        if (!product.getImagePath().equals("") || !product.getImagePath().equals(null)){
                            imgProduct.setImageURI(Uri.parse(product.getImagePath()));
                        }
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                }

            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), holder.more);
                popup.inflate(R.menu.menu_product);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.delete) {
                            if (onItemClickListener != null) {
                                onItemClickListener.onItemCklick(productList.get(currentPosition),String.valueOf(item.getTitle()));
                            }
                            return true;
                        }
                        return false;
                    }
                });
                popup.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,quantity,price;
        private ImageView img,more;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView_item_name);
            quantity = itemView.findViewById(R.id.textView_item_price);
            price = itemView.findViewById(R.id.textView_price);
            img = itemView.findViewById(R.id.imageView_product);
            more = itemView.findViewById(R.id.imageView_more);

//            itemView.setOnClickListener(v -> {
//                if (onItemClickListener != null){
//                    int position =getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION){
//                        onItemClickListener.onItemCklick(position);
//                    }
//                }
//            });
        }
    }
}
