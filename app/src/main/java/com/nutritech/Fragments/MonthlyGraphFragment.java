package com.nutritech.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.nutritech.R;
import com.nutritech.models.DateTuple;
import com.nutritech.models.UserSingleton;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyGraphFragment extends Fragment {




    public MonthlyGraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MonthlyGraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthlyGraphFragment newInstance() {
        MonthlyGraphFragment fragment = new MonthlyGraphFragment();
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

    //Initialise le graph 2
    public static void initGraph(View view){
        GraphView graph = view.findViewById(R.id.monthlyGraphFragment);

        LineGraphSeries series2 = new LineGraphSeries<>();

        for (DateTuple weights : UserSingleton.getUser().getWeights()){
            series2.appendData(new DataPoint(weights.getDate(),weights.getWeight()),false,100);
        }
        graph.addSeries(series2);

        graph.setTitle("Evolution du poids");
        series2.setTitle("Poids");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(13);
        graph.getViewport().setMaxX(25);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
    }
}
