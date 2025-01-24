package Controlar;

import static com.noureddine.stockmanagment.Opiration.lengthText;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noureddine.stockmanagment.R;

import java.io.File;
import java.util.List;

import model.Product;
import model.ProductCart;

public class AdapterProductCart extends RecyclerView.Adapter<AdapterProductCart.ViewHolder> {

    OnCPClickListener onCPClickListener;
    List<Product> productList;
    Context context;
    String type;
    List<ProductCart> productCarts;


    public AdapterProductCart(List<Product> productList,List<ProductCart> productCarts,String type, Context context, OnCPClickListener onCPClickListener) {
        this.productCarts = productCarts;
        this.productList = productList;
        this.context = context;
        this.type = type;
        this.onCPClickListener = onCPClickListener;
    }

    @NonNull
    @Override
    public AdapterProductCart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viwe = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_sell_buy, parent, false);
        return new ViewHolder(viwe);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProductCart.ViewHolder holder, int position) {

        int p = holder.getAdapterPosition();
        final int[] qty = {1};

        Product product = productList.get(p);
        holder.name.setText(lengthText(product.getName(),10));
        holder.quantity.setText(String.valueOf(qty[0]));
        holder.idp.setText(product.getIdp());


        if (type.equals("sell")){
            holder.price.setText(String.valueOf(product.getPriceSell()));
            holder.total.setText(String.valueOf(product.getPriceSell()));
        }else {
            holder.price.setText(String.valueOf(product.getPriceBuy()));
            holder.total.setText(String.valueOf(product.getPriceBuy()));
        }

        File pathImgProduct = new File(product.getImagePath());
        if (product.getImagePath().equals("") || product.getImagePath().equals(null)){
            holder.img.setImageResource(R.drawable.product);
        }else {
            if (pathImgProduct.exists()) {
                holder.img.setImageURI(Uri.parse(product.getImagePath()));
            }else {
                holder.img.setImageResource(R.drawable.product);
            }
        }

        holder.add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                qty[0]++;

                if (type.equals("sell")){
                    if (product.getQuantity()>=qty[0]){
                        holder.total.setText(String.valueOf(Double.parseDouble(String.valueOf(qty[0]*product.getPriceSell()))));
                        holder.quantity.setText(qty[0]+"");
                        productCarts.get(p).setQty(qty[0]);
                        onCPClickListener.onItemCklick(productCarts);
                    }else {
                        Toast.makeText(context, "هده اقصى كمية في الخزون", Toast.LENGTH_SHORT).show();
                        qty[0]--;
                    }
                }else {
                    holder.total.setText(String.valueOf(Double.parseDouble(String.valueOf(qty[0]*product.getPriceBuy()))));
                    holder.quantity.setText(qty[0]+"");
                    productCarts.get(p).setQty(qty[0]);
                    onCPClickListener.onItemCklick(productCarts);
                }

            }
        });

        holder.mins.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                qty[0]--;
                if (qty[0]<=0){
                    dellFromList(p);
                }else {
                    if (type.equals("sell")){
                        holder.total.setText(String.valueOf(Double.parseDouble(String.valueOf(qty[0]*product.getPriceSell()))));
                        holder.quantity.setText(qty[0]+"");
                        productCarts.get(p).setQty(qty[0]);
                        onCPClickListener.onItemCklick(productCarts);
                    }else {
                        holder.total.setText(String.valueOf(Double.parseDouble(String.valueOf(qty[0]*product.getPriceBuy()))));
                        holder.quantity.setText(qty[0]+"");
                        productCarts.get(p).setQty(qty[0]);
                        onCPClickListener.onItemCklick(productCarts);
                    }
                }
            }
        });

        holder.dell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dellFromList(p);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,quantity,price,total,idp;
        private ImageView img,add,mins,dell;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView_item_name);
            quantity = itemView.findViewById(R.id.textView_item_cart_counts);
            price = itemView.findViewById(R.id.textView_item_price);
            total = itemView.findViewById(R.id.textView_total_price);
            idp = itemView.findViewById(R.id.textView_idp);

            img = itemView.findViewById(R.id.imageView_product);
            add = itemView.findViewById(R.id.imageView_item_cart_plus);
            mins = itemView.findViewById(R.id.imageView_item_cart_minus);
            dell = itemView.findViewById(R.id.imageView_dell_item);

        }
    }

    public void dellFromList(int position){
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing() && !activity.isDestroyed()){

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("ازالة");
                alertDialog.setMessage("هل تريد ازالة هدا المنتج من القائمة ؟");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ازالة", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productList.remove(position);
                        notifyItemRemoved(position);
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();

            }
        }
    }

}
