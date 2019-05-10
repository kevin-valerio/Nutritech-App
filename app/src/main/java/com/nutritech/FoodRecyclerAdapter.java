package com.nutritech;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nutritech.Helpers.ItemClickListener;
import com.nutritech.models.Food;
import com.nutritech.models.FoodList;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

/**
 * Created by alexislefebvre on 01/05/2019.
 */

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<Food> dataSet;


    private Integer[] tab_images_pour_la_liste = {
            R.drawable.food_apple, R.drawable.food_apple,
            R.drawable.food_apple,
            R.drawable.food_apple,
            R.drawable.food_apple,
            R.drawable.food_apple,
            R.drawable.food_apple,
            R.drawable.food_apple,
            R.drawable.food_apple,
    };

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView textView;
        public TextView textCal;
        public TextView textEmpty;
        public ImageView imageView;
        public RelativeLayout viewForeground,viewBackground;
        public ItemClickListener itemClickListener;

        public MyViewHolder(View v) {
            super(v);

            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.label);
            textCal = (TextView) v.findViewById(R.id.cal);
            imageView = (ImageView) v.findViewById(R.id.icon);
            textEmpty = (TextView) v.findViewById(R.id.emptyList);
            viewForeground =  v.findViewById(R.id.view_foreground);
            viewBackground =  v.findViewById(R.id.view_background);

        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FoodRecyclerAdapter(Context context, List<Food> foodList) {
        this.context =  context;
        dataSet = foodList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FoodRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_row, parent, false);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Food food = dataSet.get(position);
        holder.textView.setText(food.getName());
        holder.textCal.setText(String.valueOf(round(food.getCalorie()*(food.getQuantite()/100))));
        holder.imageView.setImageResource(tab_images_pour_la_liste[position]);
        holder.textView.setText(dataSet.get(position).getName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int adapterPosition, boolean isLongClick) {
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void removeFood(int position){
        dataSet.remove(position);
        this.notifyDataSetChanged();


    }


    public void restoreFood(Food food,int position){
        dataSet.add(position,food);
    }

}
