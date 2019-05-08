package com.nutritech.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Calendar;

public class DateSelectorViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<Calendar> calendar = new MutableLiveData<Calendar>();

    /**
     * Get the stored date
     * @return
     */
    public LiveData<Calendar> getDate(){
        if(this.calendar.getValue() == null){
            this.calendar.setValue(Calendar.getInstance());
        }

        return this.calendar;
    }

    /**
     * Set a new date
     * @param cal
     */
    public void setDate(Calendar cal){
        calendar.setValue(cal);
    }

}
