package Controlar;

import static android.view.View.GONE;

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

import model.ReportT1;
import model.ReportT2;

public class AdapterReport extends RecyclerView.Adapter<AdapterReport.ViewHolder>{

    //Context context;
    List<Object> transactionsList = new ArrayList<>();
    //SMViewModel smViewModel;
    public static final int VIEW_TYPE_1 = 0;
    public static final int VIEW_TYPE_2 = 1;

    public AdapterReport() {}


    @SuppressLint("NotifyDataSetChanged")
    public void addReport(List<Object> transactionsList) {
        this.transactionsList = transactionsList != null ? transactionsList : new ArrayList<>();;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearData() {
        if (transactionsList != null) {
            transactionsList.clear(); // Clear the list
            notifyDataSetChanged(); // Notify the adapter
        }
    }

    @Override
    public int getItemViewType(int position) {

        Object item = transactionsList.get(position);
        if (item instanceof ReportT1) {

            return VIEW_TYPE_1;
        } else if (item instanceof ReportT2) {

            return VIEW_TYPE_2;
        } else {
            //Log.e("AdapterReport", "Unknown type at position " + position + ": " + item.getClass().getSimpleName());
            throw new IllegalArgumentException("Unknown type at position " + position);
        }

    }

    @NonNull
    @Override
    public AdapterReport.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_1) {
            View view = inflater.inflate(R.layout.item_report1, parent, false);
            return new ViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_report2, parent, false);
            return new ViewHolder(view);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterReport.ViewHolder holder, int position) {

        if (transactionsList.get(position) instanceof ReportT1) {
            ReportT1 report = (ReportT1) transactionsList.get(position);
            //deleteCustomer deleteSupplier updateCustomer updateSupplier insertCustomer insertSupplier insertProduct updateProduct deleteProduct
            switch (report.getType()){
                case "deleteCustomer":
                    holder.transactionType.setText("تم حذف العميل : ");
                    break;
                case "deleteSupplier":
                    holder.transactionType.setText("تم حذف المورد : ");
                    break;
                case "updateCustomer":
                    holder.transactionType.setText("تم تحذيث معلومات العميل : ");
                    break;
                case "updateSupplier":
                    holder.transactionType.setText("تم تحذيث معلومات المورد : ");
                    break;
                case "insertCustomer":
                    holder.transactionType.setText("تم اضافة العميل : ");
                    break;
                case "insertSupplier":
                    holder.transactionType.setText("تم اضافة العميل : ");
                    break;
                case "insertProduct":
                    holder.transactionType.setText("تم اضافة المنتج : ");
                    break;
                case "updateProduct":
                    holder.transactionType.setText("تم تحذيث المنتج : ");
                    break;
                case "deleteProduct":
                    holder.transactionType.setText("تم حذف المنتج : ");
                    break;
            }

            holder.transactionDetails.setText(report.getName().toString());
            holder.transactionDate.setText(report.getDate().toString());

        } else if (transactionsList.get(position) instanceof ReportT2) {
            ReportT2 report = (ReportT2) transactionsList.get(position);

            switch (report.getType()){
                case "insertBuying":
                    holder.type.setText("عملية شراء : "+report.getId());
                    holder.name.setText("اسم المورد :  "+report.getName());
                    break;
                case "insertSelling":
                    holder.type.setText("عملية بيع : "+report.getId());
                    holder.name.setText("اسم العميل :  "+report.getName());
                    break;
            }

            holder.namesProducts.setText(report.getNamesProducts());
            holder.date.setText(report.getDate());
            if (report.getType().equals("insertBuying")){

                holder.totalAmount.setText("المبلغ الاجمالي : "+report.getBuy().getTotalAmount());
                holder.discountAmount.setText("مبلخ التخفيض : "+report.getBuy().getDiscountAmount());
                holder.paymentAmount.setText("المبلغ المدفوع : "+report.getBuy().getPaymentAmount());
                holder.restAmount.setText("المبلغ المتبقي : "+report.getBuy().getRestAmount());

                switch (report.getBuy().getTypePayment()){
                    case "debt":
                        holder.typePayment.setText("طريقة الدفع : "+"اجل");
                        break;
                    case "cash":
                        holder.typePayment.setText("طريقة الدفع : "+"نقدا");
                        break;
                }

                if (report.getBuy().getNote().isEmpty()){
                    holder.note.setVisibility(GONE);
                }else {
                    holder.note.setText("ملاحضة : "+report.getBuy().getNote());
                }

            }else {

                holder.totalAmount.setText("المبلغ الاجمالي : "+report.getSell().getTotalAmount());
                holder.discountAmount.setText("مبلخ التخفيض : "+report.getSell().getDiscountAmount());
                holder.paymentAmount.setText("المبلغ المدفوع : "+report.getSell().getPaymentAmount());
                holder.restAmount.setText("المبلغ المتبقي : "+report.getSell().getRestAmount());

                switch (report.getSell().getTypePayment()){
                    case "debt":
                        holder.typePayment.setText("طريقة الدفع : "+"اجل");
                        break;
                    case "cash":
                        holder.typePayment.setText("طريقة الدفع : "+"نقدا");
                        break;
                }

                if (report.getSell().getNote().isEmpty()){
                    holder.note.setVisibility(GONE);
                }else {
                    holder.note.setText("ملاحضة : "+report.getSell().getNote());
                }

            }



        }




    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView transactionType ,transactionDetails ,transactionDate,
        type,name,namesProducts,date,totalAmount,discountAmount,paymentAmount,restAmount,typePayment,note ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            transactionType  = itemView.findViewById(R.id.textView_item_report1_type);
            transactionDetails  = itemView.findViewById(R.id.textView_item_report1_content);
            transactionDate  = itemView.findViewById(R.id.textView_item_report1_date);

            type  = itemView.findViewById(R.id.textView_item_report2_type);
            name  = itemView.findViewById(R.id.textView_item_report2_content);
            namesProducts  = itemView.findViewById(R.id.textView_item_report2_namesProducts);
            date  = itemView.findViewById(R.id.textView_item_report2_date);
            totalAmount  = itemView.findViewById(R.id.textView_item_report2_totalAmount);
            discountAmount  = itemView.findViewById(R.id.textView_item_report2_discountAmount);
            paymentAmount  = itemView.findViewById(R.id.textView_item_report2_paymentAmount);
            restAmount  = itemView.findViewById(R.id.textView_item_report2_restAmount);
            typePayment  = itemView.findViewById(R.id.textView_item_report2_typePayment);
            note  = itemView.findViewById(R.id.textView_item_report2_note);

        }
    }


}
