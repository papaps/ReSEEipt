package com.adrian.reseeipt.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adrian.reseeipt.ClickImageActivity;
import com.adrian.reseeipt.Constants.IntentConstants;
import com.adrian.reseeipt.Database.DatabaseUtil;
import com.adrian.reseeipt.Model.MyAppGlideModule;
import com.adrian.reseeipt.Model.ReceiptImage;
import com.adrian.reseeipt.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AddingImageAdapter extends RecyclerView.Adapter<AddingImageAdapter.AddingImageViewHolder> {
    private Context context;
    private List<byte[]> itemList = new ArrayList<>();
    private List<Uri> uriList = new ArrayList<>();
    private byte[] placeholder;

    public AddingImageAdapter(Context context) {
        this.context = context;
        placeholder = DatabaseUtil.getBytes(BitmapFactory.decodeResource(context.getResources(), R.drawable.reseept_profile_blue_min));
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
    public void onBindViewHolder(@NonNull AddingImageViewHolder holder, int position) {

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(holder.imageView.getMeasuredWidth(), holder.imageView.getMeasuredHeight()); // resize does not respect aspect ratio
        
        Glide.with(context)
                .load(itemList.get(position))
                .apply(requestOptions)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addAnotherImage(byte[] bytes){
        itemList.add(bytes);
        if (itemList.contains(placeholder)){
            itemList.remove(placeholder);
            notifyItemRemoved(0);
        }
        notifyItemInserted(itemList.size()-1);
    }

    public void addMultipleImages(ArrayList<byte[]> images){
        int last = itemList.size()-1;
        if (itemList.contains(placeholder)){
            itemList.remove(placeholder);
            notifyItemRemoved(0);
        }
        itemList.addAll(images);
        notifyItemRangeChanged(last, images.size());
    }

    public ArrayList<byte[]> getFinalImages(){
        return new ArrayList<>(itemList);
    }

    public boolean containsPlaceholder(){
        return itemList.contains(placeholder);
    }

    public class AddingImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public ImageView addImageClearImage;

        public AddingImageViewHolder(@NonNull final View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            imageView = itemView.findViewById(R.id.addImageView);
            addImageClearImage = itemView.findViewById(R.id.addImageClearImage);

            addImageClearImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int removeIndex = getAdapterPosition();
                    itemList.remove(removeIndex);
                    notifyItemRemoved(removeIndex);
                    if (getItemCount() == 0){
                        itemList.add(placeholder);
                        notifyItemInserted(0);
                    }
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int imageIndex = getAdapterPosition();
//                    Intent intent = new Intent(context, ClickImageActivity.class);
                    //String path = DatabaseUtil.saveToInternalStorage(DatabaseUtil.getImage(itemList.get(imageIndex)), context);
//                    intent.putExtra(IntentConstants.INTNT_VIEWIMAGE, path);
//                    context.startActivity(intent);
                    Bitmap bmp = DatabaseUtil.getImage(itemList.get(imageIndex));
                    try {
                        //Write file
                        String filename = "bitmap.png";
                        FileOutputStream stream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        //Cleanup
                        stream.close();
                        bmp.recycle();

                        //Pop intent
                        Intent in1 = new Intent(context, ClickImageActivity.class);
                        in1.putExtra("image", filename);
                        context.startActivity(in1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
