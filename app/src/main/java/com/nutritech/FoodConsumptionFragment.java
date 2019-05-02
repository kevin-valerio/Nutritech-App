package com.nutritech;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nutritech.Helpers.RecyclerItemTouchHelper;
import com.nutritech.Helpers.RecyclerItemTouchHelperListener;
import com.nutritech.models.Food;
import com.nutritech.models.FoodList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodConsumptionFragment extends Fragment implements RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private FoodRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FoodList foodList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate fragment_tab1
        View rootView = inflater.inflate(R.layout.fragment_food_consumption, container, false);


        //setContentView(R.layout.activity_food_recycler);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        foodList = new FoodList();
        foodList.getDailyFood().add(new Food("Pomme", 20, 40, 50));
        foodList.getDailyFood().add(new Food("Poire", 41, 18, 22));


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(),DividerItemDecoration.VERTICAL));

        // specify an adapter (see also next example)
        mAdapter = new FoodRecyclerAdapter(rootView.getContext(),foodList.getDailyFood());
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);


        return rootView;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof FoodRecyclerAdapter.MyViewHolder){
            String name = foodList.getDailyFood().get(viewHolder.getAdapterPosition()).getName();
            Food deletedFood = foodList.getDailyFood().get(viewHolder.getAdapterPosition());

            int deleteIndex = viewHolder.getAdapterPosition();
            mAdapter.removeFood(deleteIndex);
        }

    }
}

