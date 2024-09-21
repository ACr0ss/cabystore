package com.cross.cabifystore.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cross.cabifystore.CabifyStoreApplication;
import com.cross.cabifystore.R;
import com.cross.cabifystore.adapters.DialogListAdapter;
import com.cross.cabifystore.adapters.ItemGridAdapter;
import com.cross.cabifystore.data.SQLiteHandler;
import com.cross.cabifystore.models.CartListItem;
import com.cross.cabifystore.models.Item;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ArrayList<Item> catalog = new ArrayList<>();
    private ArrayList<Item> shoppingCart = new ArrayList<>();
    private int voucherCount = 0;
    private int shirtCount = 0;
    private int mugCount = 0;
    private boolean shirtDiscountApplied = false;
    ArrayList<CartListItem> listItems = new ArrayList<>();
    private TextView total;
    private RecyclerView recyclerView;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getData();
        setViews();
    }

    private void getData() {
        CabifyStoreApplication app = (CabifyStoreApplication) getApplicationContext();
        SQLiteHandler sqLiteHandler = app.getSQLiteHandler();
        catalog = sqLiteHandler.getItems();
    }

    private void setViews() {
        recyclerView = findViewById(R.id.item_grid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        total = findViewById(R.id.total_txt);

        recyclerView.setAdapter(new ItemGridAdapter(catalog, new ItemGridAdapter.OnAddToCartClickListener() {
            @Override
            public void onAddToCartClicked(Item item) {
                addToCart(item);
                total.setText(getString(R.string.total, calculateTotal()));
            }
        }));

        LinearLayout cartDetails = findViewById(R.id.cart_details);
        cartDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItems.clear();
                populateListItems();

                if (!shoppingCart.isEmpty()) {
                    showDetailsDialog();
                }
            }
        });
    }

    private String calculateTotal() {
        double totalPrice = 0;
        for (Item item : shoppingCart) {
            totalPrice += item.getPrice();
        }

        return decimalFormat.format(totalPrice);
    }

    private void addToCart(Item item) {
        String itemCode = item.getCode();

        switch (itemCode) {
            case "VOUCHER":
                voucherCount++;
                if (voucherCount % 2 == 1) {
                    shoppingCart.add(item);
                } else {
                    shoppingCart.add(new Item(item.getCode(), item.getName(), 0));
                }
                break;
            case "TSHIRT":
                shirtCount++;
                if (shirtCount > 2) {
                    applyShirtDiscountIfNeeded(item);
                    shoppingCart.add(item);
                } else {
                    shoppingCart.add(item);
                }
                break;
            case "MUG":
                mugCount++;
            default:
                shoppingCart.add(item);
        }
    }

    private void applyShirtDiscountIfNeeded(Item item) {
        if (!shirtDiscountApplied) {
            shirtDiscountApplied = true;
            if (item.getCode().equals("TSHIRT")) {
                item.setPrice(item.getPrice() - 1);
            }
        }
    }

    private void showDetailsDialog() {
        final Dialog dialog = new Dialog(CartActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_total);

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setAttributes(lp);
        }

        RecyclerView recyclerView = dialog.findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DialogListAdapter adapter = new DialogListAdapter(listItems);
        recyclerView.setAdapter(adapter);

        TextView emptyCart = dialog.findViewById(R.id.empty_cart);
        emptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                recreate();
            }
        });

        dialog.show();
    }

    private void populateListItems() {
        for (Item item : catalog) {
            String itemCode = item.getCode();

            switch (itemCode) {
                case "VOUCHER":
                    if (voucherCount != 0) {
                        listItems.add(new CartListItem(item.getName(), getString(R.string.x_quantity, voucherCount), getItemPriceSum(itemCode)));
                        if (voucherCount > 1) {
                            int promoQuantity = (int) Math.floor((double) voucherCount / 2);
                            listItems.add(new CartListItem(getString(R.string.promo_2x1), getString(R.string.x_quantity, promoQuantity), "-" + decimalFormat.format((promoQuantity * item.getPrice())) + "€"));
                        }
                    }
                    break;
                case "TSHIRT":
                    if (shirtCount != 0) {
                        listItems.add(new CartListItem(item.getName(), getString(R.string.x_quantity, shirtCount), getItemPriceSum(itemCode)));
                        if (shirtCount > 2) {
                            listItems.add(new CartListItem(getString(R.string.promo_savings, shirtCount, item.getName()), "", ""));
                        }
                    }
                    break;
                case "MUG":
                    if (mugCount != 0) {
                        listItems.add(new CartListItem(item.getName(), getString(R.string.x_quantity, mugCount), getItemPriceSum(itemCode)));
                    }
            }
        }

    }

    private String getItemPriceSum(String code) {
        double sum = 0;
        for (Item item : shoppingCart) {
            if (item.getCode().equals(code)) {
                sum += item.getPrice();
            }
        }

        return decimalFormat.format(sum) + "€";
    }
}