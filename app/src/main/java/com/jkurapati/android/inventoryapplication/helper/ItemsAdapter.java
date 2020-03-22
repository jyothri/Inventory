package com.jkurapati.android.inventoryapplication.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkurapati.android.inventoryapplication.Item;
import com.jkurapati.android.inventoryapplication.R;

import java.util.List;
import java.util.Locale;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private final List<Item> itemsList;

    public ItemsAdapter(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);
        return new ItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemsList.get(position);
        View layout = holder.view;
        TextView itemName = layout.findViewById(R.id.list_item_name);
        itemName.setText(item.getName());

        TextView itemQuantity = layout.findViewById(R.id.list_item_quantity);
        itemQuantity.setText(String.format(Locale.US, "%d", item.getQuantity()));

        TextView itemExpiryDate = layout.findViewById(R.id.list_item_expiry_date);
        itemExpiryDate.setText(item.getExpirationDate());

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        final View view;

        ItemViewHolder(View v) {
            super(v);
            this.view = v;
        }
    }

}
