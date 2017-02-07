package com.stkizema.test8telemarketing.activites.controllers;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.stkizema.test8telemarketing.R;

public class TopMainController {
    private View parent;
    private Context context;
    private AutoCompleteTextView tv;

    private static final String[] COUNTRIES = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    public TopMainController(View parent, final Context context) {
        this.parent = parent;
        this.context = context;
        tv = (AutoCompleteTextView) parent.findViewById(R.id.auto_tv);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        tv.setAdapter(adapter);

        tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }


}
