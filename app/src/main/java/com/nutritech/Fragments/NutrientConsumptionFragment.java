package com.nutritech.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nutritech.R;
import com.nutritech.models.DateSelectorViewModel;
import com.nutritech.models.FoodList;

import java.util.Calendar;

import static com.nutritech.models.UserSingleton.getUser;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class NutrientConsumptionFragment extends Fragment {

    private DateSelectorViewModel mViewModel;
    private FoodList foodList;

    private TextView kcals;
    private TextView proteins;
    private TextView carbs;
    private TextView lipids;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate fragment_tab1
        View rootView = inflater.inflate(R.layout.fragment_nutrient_consumption, container, false);

        kcals = rootView.findViewById(R.id.kcalTxt);
        proteins = rootView.findViewById(R.id.proteinsTxt);
        carbs = rootView.findViewById(R.id.carbsTxt);
        lipids = rootView.findViewById(R.id.lipidesTxt);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(getActivity()).get(DateSelectorViewModel.class);

        mViewModel.getDate().observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(@Nullable Calendar calendar) {

                // if there is a food list update the data
                if(getUser().getCalendarFoodList().containsKey(calendar)){
                    foodList = getUser().getCalendarFoodList().get(calendar);
                    kcals.setText(new StringBuilder(Math.round(foodList.getCalorie())));
                    proteins.setText(new StringBuilder(Math.round(foodList.getProteins())));
                    carbs.setText(new StringBuilder(Math.round(foodList.getCarbs())));
                    lipids.setText(new StringBuilder(Math.round(foodList.getLipid())));

                }
            }
        });


    }
}
