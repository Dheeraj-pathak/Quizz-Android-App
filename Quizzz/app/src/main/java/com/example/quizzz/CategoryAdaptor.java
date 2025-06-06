package com.example.quizzz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryAdaptor extends RecyclerView.Adapter<CategoryAdaptor.CategoryViewHolder>{

    Context context;
    ArrayList<CategoryModel>  categoryModels;
    public CategoryAdaptor(Context context, ArrayList<CategoryModel> categoryModels){
        this.context = context;
        this.categoryModels = categoryModels;

    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_category,null);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel model = categoryModels.get(position);

        holder.textview.setText(model.getCategoryName());
        Glide.with(context)
                .load(model.getCategoryImage())
                .into(holder.imageview);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra("catId", model.getCategoryId());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView textview;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.image);
            textview = itemView.findViewById(R.id.Category);

        }
    }
}
