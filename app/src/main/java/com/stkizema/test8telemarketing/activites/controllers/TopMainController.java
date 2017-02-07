package com.stkizema.test8telemarketing.activites.controllers;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.stkizema.test8telemarketing.R;

public class TopMainController {
    private View parent;
    private Context context;
    private AutoCompleteTextView tv;
    private ImageView btnMenu, btnNextSearch;
    private boolean searchMovie = false;

    private static final String[] COUNTRIES = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    public TopMainController(View parent, final Context context) {
        this.parent = parent;
        this.context = context;
        tv = (AutoCompleteTextView) parent.findViewById(R.id.auto_tv);
        btnMenu = (ImageView) parent.findViewById(R.id.id_menu);
        btnNextSearch = (ImageView) parent.findViewById(R.id.id_next);
        btnNextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchMovie) {
                    tv.setHint("Search for movie category");
                    searchMovie = false;
                } else {
                    searchMovie = true;
                    tv.setHint("Search for movie");
                }

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        tv.setAdapter(adapter);

        tv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
//                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }


}
