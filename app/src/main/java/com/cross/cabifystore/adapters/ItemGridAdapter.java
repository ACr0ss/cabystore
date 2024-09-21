package com.cross.cabifystore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cross.cabifystore.R;
import com.cross.cabifystore.models.Item;

import java.util.ArrayList;

public class ItemGridAdapter extends RecyclerView.Adapter<ItemGridAdapter.ItemViewHolder> {
    private ArrayList<Item> items;
    private OnAddToCartClickListener addToCartClickListener;

    public ItemGridAdapter(ArrayList<Item> items, OnAddToCartClickListener addToCartClickListener) {
        this.items = items;
        this.addToCartClickListener = addToCartClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_adapter, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(item.getPrice() + "€");
        setIcon(holder, item.getCode());

        if (holder.parent != null) {
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.quantity.getVisibility() == View.GONE) {
                        holder.quantity.setVisibility(View.VISIBLE);
                    }

                    int quantity = Integer.parseInt((String) holder.quantity.getText());
                    quantity++;
                    holder.quantity.setText(String.valueOf(quantity));

                    addToCartClickListener.onAddToCartClicked(item);
                }
            });
        }
    }

    private void setIcon(ItemViewHolder holder, String code) {
        switch (code) {
            case "VOUCHER":
                holder.icon.setImageResource(R.drawable.voucher);
                break;
            case "TSHIRT":
                holder.icon.setImageResource(R.drawable.shirt);
                break;
            case "MUG":
                holder.icon.setImageResource(R.drawable.mug);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnAddToCartClickListener {
        void onAddToCartClicked(Item item);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        LinearLayout parent;
        ImageView icon;
        TextView quantity;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            parent = itemView.findViewById(R.id.parent);
            icon = itemView.findViewById(R.id.icon);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }

}
