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
        return this.calendar;
    }

    /**
     * Set a new date
     * @param cal
     */
    public void setDate(Calendar cal){
        calendar.setValue(cal);
    }

    /**
     * Increment the stored date
     */
    public void incrementDate(){// TODO : may not work
        calendar.getValue().add(Calendar.DATE, 1);

    }

    /**
     * Decrement the stored date
     */
    public void decrementDate(){
        calendar.getValue().add(Calendar.DATE, -1);

    }
}
