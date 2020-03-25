package com.jkurapati.android.inventoryapplication.helper;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkurapati.android.inventoryapplication.Item;
import com.jkurapati.android.inventoryapplication.ItemEditorActivity;
import com.jkurapati.android.inventoryapplication.R;
import com.jkurapati.android.inventoryapplication.db.ItemContract;

import java.util.List;
import java.util.Locale;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private final LayoutInflater mInflater;
    private List<Item> itemsList;

    public ItemsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    public void setItems(List<Item> items) {
        itemsList = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_view, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return itemsList.get(position).getId();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (itemsList != null) {
            Item item = itemsList.get(position);
            holder.itemNameView.setText(item.getName());
            holder.itemQuantityView.setText(String.format(Locale.US, "%d", item.getQuantity()));
            holder.itemExpiryView.setText(item.getExpirationDate());
        }
    }

    @Override
    public int getItemCount() {
        return itemsList == null ? 0 : itemsList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView itemNameView;
        final TextView itemQuantityView;
        final TextView itemExpiryView;

        ItemViewHolder(View v) {
            super(v);
            this.itemNameView = v.findViewById(R.id.list_item_name);
            this.itemQuantityView = v.findViewById(R.id.list_item_quantity);
            this.itemExpiryView = v.findViewById(R.id.list_item_expiry_date);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Create new intent to go to {@link EditorActivity}
            Intent intent = new Intent(v.getContext(), ItemEditorActivity.class);
            Uri currentItemUri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, getItemId());
            intent.setData(currentItemUri);
            v.getContext().startActivity(intent);
        }
    }

}
