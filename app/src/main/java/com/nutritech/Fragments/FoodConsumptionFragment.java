package com.nutritech.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nutritech.FoodRecyclerAdapter;
import com.nutritech.Helpers.RecyclerItemTouchHelper;
import com.nutritech.Helpers.RecyclerItemTouchHelperListener;
import com.nutritech.R;
import com.nutritech.models.DateSelectorViewModel;
import com.nutritech.models.Food;
import com.nutritech.models.FoodList;
import com.nutritech.models.UserSingleton;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import static com.nutritech.models.UserSingleton.getUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodConsumptionFragment extends Fragment implements RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private FoodRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FoodList foodList;
    private List<Food> data;
    private DateSelectorViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate fragment_tab1
        View rootView = inflater.inflate(R.layout.fragment_food_consumption, container, false);


        //setContentView(R.layout.activity_food_recycler);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        foodList = new FoodList();
        foodList.getDailyFood().add(new Food("Pomme", 20, 40, 50));
        foodList.getDailyFood().add(new Food("Poire", 41, 18, 22));

        this.data = foodList.getDailyFood();

        Button sortBtn = rootView.findViewById(R.id.sortBtn);
        sortBtn.setOnClickListener(v -> {
           foodList.getDailyFood().sort((o1, o2) -> Math.toIntExact(o1.getCalorie() - o2.getCalorie()));
            // clear old list
            data.clear();
           data.addAll(foodList.getDailyFood());
           mAdapter.notifyDataSetChanged();
        });


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(),DividerItemDecoration.VERTICAL));

        // specify an adapter (see also next example)
        mAdapter = new FoodRecyclerAdapter(rootView.getContext(),this.data);
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

            foodList.removeItem(position);

            int deleteIndex = viewHolder.getAdapterPosition();
            mAdapter.removeFood(deleteIndex);
            mViewModel.setDate(mViewModel.getDate().getValue());

        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(getActivity()).get(DateSelectorViewModel.class);

        mViewModel.getDate().observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(@Nullable Calendar calendar) {

              //  Log.d("RECEIVED",calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR));
                // clear old list
                data.clear();

                // if there is a food list update the data
                if(getUser().getCalendarFoodList().containsKey(calendar.get(Calendar.DAY_OF_MONTH))){
                    foodList = UserSingleton.getUser().getCalendarFoodList().get(calendar.get(Calendar.DAY_OF_MONTH));
                     data.addAll(foodList.getDailyFood());

                }
                // notify adapter
                mAdapter.notifyDataSetChanged();
            }
        });


    }

}

