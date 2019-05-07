package com.nutritech.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nutritech.DashboardActivity;
import com.nutritech.R;

import static android.support.v4.content.ContextCompat.startActivity;


public class WeightAlertBuilder {

    private final Context context;
    private AlertDialog.Builder builder;
    private LinearLayout layout;
    private EditText poids;

    public WeightAlertBuilder(Context context, int theme) {
        this.context = context;
        this.builder = new AlertDialog.Builder(context, theme);
        builder.setTitle("Title");
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
    }

    public void setMainTextView() {
        TextView title = new TextView(context);
        title.setText(R.string.new_poids);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        builder.setCustomTitle(title);
    }

    public void setWeightEditBox() {
        poids = new EditText(context);
        poids.setInputType(InputType.TYPE_CLASS_NUMBER);
        poids.setHint("Poids (en kg)");
        poids.setShowSoftInputOnFocus(true);
        poids.setTextColor(Color.BLACK);
        poids.setPadding(5, 0, 0, 0);
        poids.setCursorVisible(true);
        layout.addView(poids);
    }

    public void setSecondaryTextView() {
        TextView msg = new TextView(context);
        msg.setText("\n\nVeuillez entrer votre nouveau poids. \n Cela nous permettra de suivre l'évolution de votre poids ! \n\n");
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        layout.addView(msg);

    }

    public void setButtons() {

        builder.setPositiveButton("Ajouter", (dialog, which) -> {
                    UserSingleton.getUser().addWeight(Integer.valueOf(poids.getText().toString()));
                    Toast.makeText(context, "Votre nouveau poids a été ajouté (" + poids.getText().toString() + " kg)", Toast.LENGTH_LONG).show();
            startActivity(context, new Intent(context, DashboardActivity.class), null);

                }
        );

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
    }

    public void build() {
        builder.setView(layout);
        builder.show();
    }
}
