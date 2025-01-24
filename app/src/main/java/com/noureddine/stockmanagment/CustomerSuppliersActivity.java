package com.noureddine.stockmanagment;

import static com.noureddine.stockmanagment.Opiration.editSupplierCustomer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.noureddine.stockmanagment.R;

import java.util.List;

import Controlar.AdapterCustomerSupplier;
import Controlar.OnCSClickListener;
import model.Customer;
import model.Supplier;
import model.Transactions;

public class CustomerSuppliersActivity extends AppCompatActivity {

    FloatingActionButton fab;
    SMViewModel smViewModel;
    Bundle extras;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    EditText editTextSearch;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suppliers_customers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.RecyclerView);
        editTextSearch = findViewById(R.id.editText_search);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        smViewModel = new ViewModelProvider(this).get(SMViewModel.class);
        extras = getIntent().getExtras();

        if (extras.getString("type").equals("customers")){

            smViewModel.getAllCustomers().observe(CustomerSuppliersActivity.this, new Observer<List<Customer>>() {
                @Override
                public void onChanged(List<Customer> customers) {
                    adapter = new AdapterCustomerSupplier(customers, getBaseContext(), new OnCSClickListener() {
                        @Override
                        public void onItemCklick(Supplier supplier,String s) {}
                        @Override
                        public void onItemCklick(Customer customer,String s) {
                            if (s.equals("حذف")){
                                //smViewModel.deleteCustomerById(customer.getId());
                                customer.setDeleted(true);
                                smViewModel.updateCustomer(customer);
                                //   int custmerId, int supplierId, int productId, int buyingId, int sellingId, String type, long timestamp
                                smViewModel.inserttransaction(new Transactions(customer.getId(),
                                        0,0,0,0,null,"deleteCustomer",System.currentTimeMillis()));
                                Toast.makeText(CustomerSuppliersActivity.this, "تم حذف العميل", Toast.LENGTH_SHORT).show();
                            }else{
//                                editSupplierCustomer(new Supplier(),customer,true);

                                editSupplierCustomer(
                                        CustomerSuppliersActivity.this,
                                        extras.getString("type"),
                                        new Supplier(),
                                        customer,
                                        true
                                );

                            }
                        }
                    });
                    recyclerView.setAdapter(adapter);
                }
            });

        }else {

            smViewModel.getAllSuppliers().observe(CustomerSuppliersActivity.this, new Observer<List<Supplier>>() {
                @Override
                public void onChanged(List<Supplier> suppliers) {
                    adapter = new AdapterCustomerSupplier(suppliers, getBaseContext(), true, new OnCSClickListener() {
                        @Override
                        public void onItemCklick(Supplier supplier,String s) {
                            if (s.equals("حذف")){
                                //smViewModel.deleteSupplierById(supplier.getId());
                                supplier.setDeleted(true);
                                smViewModel.updateSupplier(supplier);
                                //   int custmerId, int supplierId, int productId, int buyingId, int sellingId, String type, long timestamp
                                smViewModel.inserttransaction(new Transactions(0,supplier.getId(),
                                        0,0,0,null,"deleteSupplier",System.currentTimeMillis()));
                                Toast.makeText(CustomerSuppliersActivity.this, "تم حذف المورد", Toast.LENGTH_SHORT).show();
                            }else{
//                                editSupplierCustomer(supplier,new Customer(),true);

                                editSupplierCustomer(
                                        CustomerSuppliersActivity.this,
                                        extras.getString("type"),
                                        supplier,
                                        new Customer(),
                                        true
                                );

                            }

                        }
                        @Override
                        public void onItemCklick(Customer customer,String s) {}
                    });
                    recyclerView.setAdapter(adapter);
                }
            });

        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                editSupplierCustomer(new Supplier(),new Customer(),false);

                editSupplierCustomer(
                        CustomerSuppliersActivity.this,
                        extras.getString("type"),
                        new Supplier(),
                        new Customer(),
                        false
                );

            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (extras.getString("type").equals("customers")){

                    if(s.length() == 0){

                        smViewModel.getAllCustomers().observe(CustomerSuppliersActivity.this, new Observer<List<Customer>>() {
                            @Override
                            public void onChanged(List<Customer> customers) {
                                adapter = new AdapterCustomerSupplier(customers, getBaseContext(), new OnCSClickListener() {
                                    @Override
                                    public void onItemCklick(Supplier supplier,String s) {}
                                    @Override
                                    public void onItemCklick(Customer customer,String s) {
                                        if (s.equals("حذف")){
                                            //smViewModel.deleteCustomerById(customer.getId());
                                            customer.setDeleted(true);
                                            smViewModel.updateCustomer(customer);
                                            //   int custmerId, int supplierId, int productId, int buyingId, int sellingId, String type, long timestamp
                                            smViewModel.inserttransaction(new Transactions(customer.getId(),0,0,0,0,null,"deleteCustomer",System.currentTimeMillis()));
                                            Toast.makeText(CustomerSuppliersActivity.this, "تم حذف العميل", Toast.LENGTH_SHORT).show();
                                        }else{
//                                editSupplierCustomer(new Supplier(),customer,true);

                                            editSupplierCustomer(
                                                    CustomerSuppliersActivity.this,
                                                    extras.getString("type"),
                                                    new Supplier(),
                                                    customer,
                                                    true
                                            );

                                        }
                                    }
                                });
                                recyclerView.setAdapter(adapter);
                            }
                        });

                    }else {

                        smViewModel.searchCustomer(s.toString()).observe(CustomerSuppliersActivity.this, new Observer<List<Customer>>() {
                            @Override
                            public void onChanged(List<Customer> customers) {

                                adapter = new AdapterCustomerSupplier(customers, getBaseContext(), new OnCSClickListener() {
                                    @Override
                                    public void onItemCklick(Supplier supplier,String s) {}
                                    @Override
                                    public void onItemCklick(Customer customer,String s) {
                                        if (s.equals("حذف")){
                                            //smViewModel.deleteCustomerById(customer.getId());
                                            customer.setDeleted(true);
                                            smViewModel.updateCustomer(customer);
                                            smViewModel.inserttransaction(new Transactions(customer.getId(),0,0,0,0,null,"deleteCustomer",System.currentTimeMillis()));
                                            Toast.makeText(CustomerSuppliersActivity.this, "تم حذف العميل", Toast.LENGTH_SHORT).show();
                                        }else{
//                                        editSupplierCustomer(new Supplier(),customer,true);

                                            editSupplierCustomer(
                                                    CustomerSuppliersActivity.this,
                                                    extras.getString("type"),
                                                    new Supplier(),
                                                    customer,
                                                    true
                                            );

                                        }
                                    }
                                });
                                recyclerView.setAdapter(adapter);

                            }
                        });

                    }

                }else {

                    if(s.length() == 0){

                        smViewModel.getAllSuppliers().observe(CustomerSuppliersActivity.this, new Observer<List<Supplier>>() {
                            @Override
                            public void onChanged(List<Supplier> suppliers) {
                                adapter = new AdapterCustomerSupplier(suppliers, getBaseContext(), true, new OnCSClickListener() {
                                    @Override
                                    public void onItemCklick(Supplier supplier,String s) {
                                        if (s.equals("حذف")){
                                            //smViewModel.deleteSupplierById(supplier.getId());
                                            supplier.setDeleted(true);
                                            smViewModel.updateSupplier(supplier);
                                            smViewModel.inserttransaction(new Transactions(0,supplier.getId(),0,0,0,null,"deleteSupplier",System.currentTimeMillis()));
                                            Toast.makeText(CustomerSuppliersActivity.this, "تم حذف المورد", Toast.LENGTH_SHORT).show();
                                        }else{
//                                editSupplierCustomer(supplier,new Customer(),true);

                                            editSupplierCustomer(
                                                    CustomerSuppliersActivity.this,
                                                    extras.getString("type"),
                                                    supplier,
                                                    new Customer(),
                                                    true
                                            );

                                        }

                                    }
                                    @Override
                                    public void onItemCklick(Customer customer,String s) {}
                                });
                                recyclerView.setAdapter(adapter);
                            }
                        });

                    }else {

                        smViewModel.searchSupplier(s.toString()).observe(CustomerSuppliersActivity.this, new Observer<List<Supplier>>() {
                            @Override
                            public void onChanged(List<Supplier> suppliers) {

                                adapter = new AdapterCustomerSupplier(suppliers, getBaseContext(), true, new OnCSClickListener() {
                                    @Override
                                    public void onItemCklick(Supplier supplier,String s) {
                                        if (s.equals("حذف")){
                                            //smViewModel.deleteSupplierById(supplier.getId());
                                            supplier.setDeleted(true);
                                            smViewModel.updateSupplier(supplier);
                                            smViewModel.inserttransaction(new Transactions(0,supplier.getId(),0,0,0,null,"deleteSupplier",System.currentTimeMillis()));
                                            Toast.makeText(CustomerSuppliersActivity.this, "تم حذف المورد", Toast.LENGTH_SHORT).show();
                                        }else{
//                                        editSupplierCustomer(supplier,new Customer(),true);

                                            editSupplierCustomer(
                                                    CustomerSuppliersActivity.this,
                                                    extras.getString("type"),
                                                    supplier,
                                                    new Customer(),
                                                    true
                                            );

                                        }

                                    }
                                    @Override
                                    public void onItemCklick(Customer customer,String s) {}
                                });
                                recyclerView.setAdapter(adapter);

                            }
                        });

                    }

                }

            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }


}