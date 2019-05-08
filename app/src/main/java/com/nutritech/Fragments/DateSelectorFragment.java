package com.nutritech.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutritech.R;
import com.nutritech.models.DateSelectorViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateSelectorFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DateSelectorViewModel mViewModel;
    private Button textDate;

    public static DateSelectorFragment newInstance() {
        return new DateSelectorFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mViewModel = ViewModelProviders.of(getActivity()).get(DateSelectorViewModel.class);
        mViewModel.setDate(Calendar.getInstance());
        mViewModel.incrementDate();

        Locale.setDefault(Locale.FRANCE);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this,year,month,day);
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.date_selector_fragment, container, false);

        this.textDate =(Button) v.findViewById(R.id.button);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        String formatted = format1.format(cal.getTime());
        this.textDate.setText(formatted);

        mViewModel = ViewModelProviders.of(getActivity()).get(DateSelectorViewModel.class);
        mViewModel.setDate(Calendar.getInstance());
        mViewModel.incrementDate();



        return v;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        mViewModel = ViewModelProviders.of(getActivity()).get(DateSelectorViewModel.class);
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,dayOfMonth);

        Button btn = (Button) getActivity().findViewById(R.id.button);

        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        String formatted = format1.format(cal.getTime());
        btn.setText(formatted);
        mViewModel.setDate(cal);

    }
}


