package com.nutritech.models;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Arrays;

/**
 * Enum of goals
 */
public enum Goal {
    PRISE_DE_MASSE, MAINTIEN_DE_SON_POIDS, PERTE_DE_MASSE;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
}