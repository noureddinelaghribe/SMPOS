package Controlar;

import model.Customer;
import model.Supplier;

public interface OnCSClickListener {

    void onItemCklick(Supplier supplier, String s);
    void onItemCklick(Customer customer, String s);

}
