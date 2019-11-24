package com.adrian.reseeipt.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adrian.reseeipt.Database.DatabaseUtil;
import com.adrian.reseeipt.Model.ReceiptImage;
import com.adrian.reseeipt.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AddingImageAdapter extends RecyclerView.Adapter<AddingImageAdapter.AddingImageViewHolder> {
    private Context context;
    private List<ReceiptImage> itemList = new ArrayList<>();
    private ReceiptImage placeholder;

    public AddingImageAdapter(Context context) {
        this.context = context;
        byte[] stuff = DatabaseUtil.getBytes(BitmapFactory.decodeResource(context.getResources(), R.drawable.reseept_profile_blue_min));
        placeholder = new ReceiptImage();
        placeholder.setImageBytes(stuff);
        itemList.add(placeholder);
    }

    @NonNull
    @Override
    public AddingImageAdapter.AddingImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_image_add_recycler, parent, false);
        return new AddingImageViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AddingImageAdapter.AddingImageViewHolder holder, int position) {
        ReceiptImage receiptImage = itemList.get(position);
        Glide.with(context)
                .asBitmap()
                .load(receiptImage.getImageBytes())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addAnotherImage(ReceiptImage receiptImage){
        itemList.add(receiptImage);
        if (itemList.contains(placeholder)){
            itemList.remove(placeholder);
            notifyItemRemoved(0);
        }
        notifyItemInserted(itemList.size()-1);
    }

    public class AddingImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public AddingImageViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            imageView = itemView.findViewById(R.id.addImageView);
        }
    }
}
