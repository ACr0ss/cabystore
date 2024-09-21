package com.cross.cabifystore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cross.cabifystore.R;
import com.cross.cabifystore.models.CartListItem;

import java.util.ArrayList;

public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder> {

    private ArrayList<CartListItem> cartListItems;

    public DialogListAdapter(ArrayList<CartListItem> cartListItems) {
        this.cartListItems = cartListItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartListItem item = cartListItems.get(position);
        holder.itemName.setText(item.getItemName());
        holder.itemQuantity.setText(item.getQuantity());
        holder.itemSum.setText(item.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return cartListItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView itemQuantity;
        TextView itemSum;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            itemSum = itemView.findViewById(R.id.item_sum);
        }
    }
}
