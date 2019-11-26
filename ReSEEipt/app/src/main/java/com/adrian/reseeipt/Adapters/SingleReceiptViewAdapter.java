package com.adrian.reseeipt.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adrian.reseeipt.Database.DatabaseUtil;
import com.adrian.reseeipt.Model.Receipt;
import com.adrian.reseeipt.Model.ReceiptImage;
import com.adrian.reseeipt.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SingleReceiptViewAdapter extends RecyclerView.Adapter<SingleReceiptViewAdapter.SingleReceiptViewHolder> implements Filterable {

    private Context context;
    private List<Receipt> itemList = new ArrayList<>();
    private List<Receipt> itemListOld = new ArrayList<>();

    public SingleReceiptViewAdapter(Context context, List<Receipt> itemList) {
        this.context = context;
        this.itemList = itemList;
        itemListOld = itemList;
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
        itemListOld = itemList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemList = itemListOld;
                } else {
                    List<Receipt> filteredList = new ArrayList<>();
                    for (Receipt row : itemListOld) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                   itemList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemList = (ArrayList<Receipt>) filterResults.values;
                System.out.println(itemList.size());
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public void searchQuery(String query){
        getFilter().filter(query);
    }

    public void clearQuery(){
        getFilter().filter("");
    }

    public List<Receipt> getItemList() {
        return itemList;
    }

    public void sortReceipt(String sortQuery){
        if (sortQuery.equals("Oldest")){
            Collections.sort(itemList, new Comparator<Receipt>() {
                @Override
                public int compare(Receipt o1, Receipt o2) {
                    long one = Long.parseLong(o1.getDateAdded());
                    long two = Long.parseLong(o2.getDateAdded());
                    return (int) (one-two);
                }
            });
        } else {
            Collections.sort(itemList, new Comparator<Receipt>() {
                @Override
                public int compare(Receipt o1, Receipt o2) {
                    long one = Long.parseLong(o1.getDateAdded());
                    long two = Long.parseLong(o2.getDateAdded());
                    return (int) (two - one);
                }
            });
        }
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
