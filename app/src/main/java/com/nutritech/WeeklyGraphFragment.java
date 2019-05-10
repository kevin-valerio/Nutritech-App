package com.nutritech;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.nutritech.models.DateTuple;
import com.nutritech.models.UserSingleton;

import java.io.Console;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link WeeklyGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyGraphFragment extends Fragment {



    public WeeklyGraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeeklyGraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeeklyGraphFragment newInstance(String param1, String param2) {
        WeeklyGraphFragment fragment = new WeeklyGraphFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monthly_graph, container, false);
        initGraph(view);
        return view;
    }


    public static void initGraph(View view){
        GraphView graph = view.findViewById(R.id.monthlyGraphFragment);

        LineGraphSeries series2 = new LineGraphSeries<>();
        series2.appendData(new DataPoint(8,1800),false,100);
        series2.appendData(new DataPoint(UserSingleton.getUser().getDateOfTheDayCalories(),UserSingleton.getUser().getKcalCurrent()),false,100);
        Log.d("Cal",Double.toString(UserSingleton.getUser().getKcalCurrent()));
        Log.d("Date",Integer.toString(UserSingleton.getUser().getDateOfTheDayCalories()));

        graph.addSeries(series2);

        graph.setTitle("Evolution des calories");
        series2.setTitle("Calories");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(2500);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(5);
        graph.getViewport().setMaxX(25);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
    }
}
