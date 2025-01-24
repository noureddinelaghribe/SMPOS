package Controlar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.noureddine.stockmanagment.R;
import java.util.ArrayList;
import java.util.List;
import model.Customer;
import model.Supplier;

public class AdapterCustomerSupplier extends RecyclerView.Adapter<AdapterCustomerSupplier.ViewHolder> {

    List<Supplier> supplersList = new ArrayList<>();
    List<Customer> customersList = new ArrayList<>();
    Context context;
    boolean isSupplier;
    OnCSClickListener onCSClickListener;

    public AdapterCustomerSupplier(List<Supplier> supplersList, Context context,boolean isSupplier,OnCSClickListener onCSClickListener) {
        this.supplersList = supplersList;
        this.context = context;
        this.isSupplier = isSupplier;
        this.onCSClickListener = onCSClickListener;
    }

    public AdapterCustomerSupplier(List<Customer> customersList, Context context,OnCSClickListener onCSClickListener) {
        this.customersList = customersList;
        this.context = context;
        this.onCSClickListener = onCSClickListener;
    }

    @NonNull
    @Override
    public AdapterCustomerSupplier.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viwe = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_supplier, parent, false);
        return new ViewHolder(viwe);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCustomerSupplier.ViewHolder holder, int position) {
        int p = holder.getAdapterPosition();
        if (isSupplier){
            Supplier supplier = supplersList.get(p);
            holder.name.setText(supplier.getName());
            holder.phoneNumber.setText(supplier.getPhoneNumber());
        }else {
            Customer customer = customersList.get(p);
            holder.name.setText(customer.getName());
            holder.phoneNumber.setText(customer.getPhoneNumber());
        }

        holder.itemView.setOnClickListener(v -> {
            if (onCSClickListener != null) {
                if (isSupplier){
                    onCSClickListener.onItemCklick(supplersList.get(p),v.toString());
                }else {
                    onCSClickListener.onItemCklick(customersList.get(p),v.toString());
                }
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), holder.more);
                popup.inflate(R.menu.menu_customer_supplier);

                String phoneNumber=holder.phoneNumber.getText().toString();
                Menu menu = popup.getMenu();
                MenuItem item = menu.findItem(R.id.call);

                if (phoneNumber.isEmpty() || phoneNumber.equals(null)){
                    item.setVisible(false);
                }else {
                    item.setVisible(true);
                }


//                if (!customersList.isEmpty()){
//                    if (!customersList.get(p).getPhoneNumber().isEmpty()){
//                        phoneNumber=customersList.get(p).getPhoneNumber();
//                        Menu menu = popup.getMenu();
//                        MenuItem item = menu.findItem(R.id.call);
//                        item.setVisible(true);
//                    }
//                }
//                if (!supplersList.isEmpty()){
//                    if (!supplersList.get(p).getPhoneNumber().isEmpty()){
//                        phoneNumber= supplersList.get(p).getPhoneNumber();
//                        Menu menu = popup.getMenu();
//                        MenuItem item = menu.findItem(R.id.call);
//                        item.setVisible(true);
//                    }
//                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.delete) {
                            if (onCSClickListener != null) {
                                if (isSupplier){
                                    onCSClickListener.onItemCklick(supplersList.get(p),String.valueOf(item.getTitle()));
                                }else {
                                    onCSClickListener.onItemCklick(customersList.get(p),String.valueOf(item.getTitle()));
                                }
                            }
                            return true;
                        }else if (item.getItemId() == R.id.call){

                            Context context = v.getContext();
                            if (context instanceof Activity) {
                                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                                if (!customersList.isEmpty()){
                                    dialIntent.setData(Uri.parse("tel:" + phoneNumber));
                                }else if (!supplersList.isEmpty()){
                                    dialIntent.setData(Uri.parse("tel:" + phoneNumber));
                                }
                                context.startActivity(dialIntent);
                            }

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
        if (isSupplier){
            return supplersList.size();
        }
        return customersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,phoneNumber;
        private ImageView more;
        LinearLayout linearItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            linearItem=itemView.findViewById(R.id.linearItem);
            name = itemView.findViewById(R.id.textView_item_name);
            phoneNumber = itemView.findViewById(R.id.textView_phone_number);
            more = itemView.findViewById(R.id.imageView_more);

        }
    }


}
