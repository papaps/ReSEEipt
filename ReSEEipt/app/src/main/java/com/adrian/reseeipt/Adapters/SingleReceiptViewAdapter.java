package com.adrian.reseeipt.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adrian.reseeipt.Database.DatabaseUtil;
import com.adrian.reseeipt.Model.Receipt;
import com.adrian.reseeipt.Model.ReceiptImage;
import com.adrian.reseeipt.R;

import java.util.ArrayList;
import java.util.List;

public class SingleReceiptViewAdapter extends RecyclerView.Adapter<SingleReceiptViewAdapter.SingleReceiptViewHolder> {

    private Context context;
    private List<Receipt> itemList = new ArrayList<>();

    public SingleReceiptViewAdapter(Context context, List<Receipt> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public SingleReceiptViewAdapter.SingleReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_receipt_view_recycler, parent, false);
        return new SingleReceiptViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleReceiptViewAdapter.SingleReceiptViewHolder holder, int position) {
        Receipt receipt = itemList.get(position);
        holder.individualReceiptOneTitle.setText(receipt.getTitle());
        holder.individualReceiptOneImage.setImageBitmap(DatabaseUtil.loadImageFromStorage(receipt.getImages().get(0).getImagePath()));
        if (receipt.getImages().size() == 1){
            holder.multipleImagesIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemList(List<Receipt> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public class SingleReceiptViewHolder extends RecyclerView.ViewHolder {

        protected TextView individualReceiptOneTitle;
        protected ImageView individualReceiptOneImage;
        protected ImageView multipleImagesIcon;
        public SingleReceiptViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            individualReceiptOneTitle = itemView.findViewById(R.id.individualReceiptOneTitle);
            individualReceiptOneImage = itemView.findViewById(R.id.individualReceiptOneImage);
            multipleImagesIcon = itemView.findViewById(R.id.multipleImagesIcon);
        }
    }
}
