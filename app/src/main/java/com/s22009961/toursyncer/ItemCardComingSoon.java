package com.s22009961.toursyncer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemCardComingSoon extends RecyclerView.Adapter<ItemCardComingSoon.CardViewHolder> {

    private int itemCount;

    public ItemCardComingSoon(int itemCount) {
        this.itemCount = itemCount;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.cardText.setText("Card " + (position + 1));
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView cardText;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardText = itemView.findViewById(R.id.cardText);
        }
    }
}
