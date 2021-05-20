package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.app.adapter.filteradapter;
import com.example.app.adapter.myadapter;
import com.example.app.model.home;
import com.example.app.util.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FilterActivity extends AppCompatActivity  {
    private String PriceRangearray[], Allowed_Peoplearray[], Living_Preferencearray[];

    private com.google.android.material.textfield.TextInputEditText location;
    private MaterialButton Applybtn;
    private AppCompatSpinner AllowedPeoplespinner, LivingPreferencespinner, Pricerangespinner;
    private String pricerangestring, whoareyoustring, livingchoicestring;
    private SharedPreferences preferences;
    int RESULTREQUESTCODE = 1;
    private myadapter filteradapter;
    private ArrayList<home> filteredhomes;

    private RecyclerView recyclerView;
    private static final String KEY_LOCATION = "place";
    private static final String KEY_ALLOWED_PEEPS = "people_allowed";
    private static final String KEY_LIVING_PREFERENCE = "livingpref";
    private static final String KEY_PRICERANGE = "pricerange";



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.filter );
        filteredhomes = new ArrayList<> (  );

        init ( );
        preferences = getSharedPreferences ( "mypref" , MODE_PRIVATE );


        //arrays fetching
        //error
        PriceRangearray = getResources ( ).getStringArray ( R.array.PriceRange );
        ArrayAdapter<String> arrayAdapterprice = new ArrayAdapter<String> ( FilterActivity.this ,
                android.R.layout.simple_spinner_dropdown_item , PriceRangearray ) {
            @Override
            public boolean isEnabled(int position) {
                if (position >= 0) {
                    return true;

                } else {
                    return false;
                }
            }
        };

        Allowed_Peoplearray = getResources ( ).getStringArray ( R.array.Allowed_People );
        ArrayAdapter<String> arrayAdapterallowedpeople = new ArrayAdapter<String> ( FilterActivity.this ,
                android.R.layout.simple_spinner_dropdown_item , Allowed_Peoplearray ) {
            @Override
            public boolean isEnabled(int position) {
                if (position >= 0) {
                    return true;

                } else {
                    return false;
                }
            }
        };


        Living_Preferencearray = getResources ( ).getStringArray ( R.array.Living_Preference );
        ArrayAdapter<String> arrayAdapterlivingpreference = new ArrayAdapter<String> ( FilterActivity.this ,
                android.R.layout.simple_spinner_dropdown_item , Living_Preferencearray ) {
            @Override
            public boolean isEnabled(int position) {
                if (position >= 0) {
                    return true;

                } else {
                    return false;
                }
            }
        };


        Pricerangespinner.setAdapter ( arrayAdapterprice );
        Pricerangespinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent , View view , int position , long id) {
                pricerangestring = (String) parent.getItemAtPosition ( position );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
        AllowedPeoplespinner.setAdapter ( arrayAdapterallowedpeople );
        AllowedPeoplespinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent , View view , int position , long id) {
                whoareyoustring = (String) parent.getItemAtPosition ( position );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
        LivingPreferencespinner.setAdapter ( arrayAdapterlivingpreference );
        LivingPreferencespinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent , View view , int position , long id) {
                livingchoicestring = (String) parent.getItemAtPosition ( position );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
    }

    private void init() {
        location = findViewById ( R.id.location );
        Applybtn = findViewById ( R.id.ApplyBtn );
        AllowedPeoplespinner = findViewById ( R.id.allowed );
        LivingPreferencespinner = findViewById ( R.id.preference );
        Pricerangespinner = findViewById ( R.id.budget );
        recyclerView = findViewById ( R.id.recyclerView2 );
        recyclerView.setLayoutManager ( new GridLayoutManager ( FilterActivity.this , 1 ) );
        recyclerView.setHasFixedSize ( true );
    }

    @Override
    protected void onResume() {
        super.onResume ( );
        Applybtn.setOnClickListener
                ( new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View v) {


                 SharedPreferences.Editor editor = preferences.edit ();
                 editor.putString ( "pricerange", pricerangestring  );
                 editor.putString ( "livingpref" ,livingchoicestring );
                 editor.putString("allowed_People", whoareyoustring);
                 editor.putString ( "place ", location.getText ().toString () );
                 editor.apply ();
                 editor.commit ();
                 Intent intent = new Intent ( FilterActivity.this ,FilterResults.class );
                 startActivity ( intent );
                       // ApplyFilters ();
                    }
                } );
    }


}
