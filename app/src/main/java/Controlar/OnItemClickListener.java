package Controlar;

import android.view.View;

import model.Product;

public interface OnItemClickListener {
    //void onItemCklick(int postion);
    void onItemCklick(Product product, String s);

}
