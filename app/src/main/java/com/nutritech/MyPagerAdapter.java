package com.nutritech;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by alexislefebvre on 02/05/2019.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    public int nbTab;

    public  MyPagerAdapter(FragmentManager fm, int numTab){
        super(fm);
        this.nbTab = numTab;
    }
    @Override
    public Fragment getItem(int position) {

        FoodConsumptionFragment fragment1 = new FoodConsumptionFragment();
        NutrientConsumptionFragment fragment2 = new NutrientConsumptionFragment();


        switch (position){
            case 0: return fragment1;
            case 1: return fragment2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return nbTab;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Aliments consommés";
            case 1:
                return "Nutriments consommés";
            default:
                return null;
        }
    }
}
