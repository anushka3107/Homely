package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.app.adapter.myadapter;
import com.example.app.model.home;
import com.example.app.util.Constants;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FilterResults extends AppCompatActivity implements myadapter.OnItemClickListener{

    private myadapter filteradapter;
    private ArrayList<home> filteredhomes;

    private RecyclerView recyclerView;
    private static final String KEY_LOCATION = "place";
    private static final String KEY_ALLOWED_PEEPS = "people_allowed";
    private static final String KEY_LIVING_PREFERENCE = "livingpref";
    private static final String KEY_PRICERANGE = "pricerange";
    private SharedPreferences preferences;
    private String pricerangestring, whoareyoustring, livingchoicestring , location;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_filter_results );

        preferences = getSharedPreferences ( "mypref" , MODE_PRIVATE );
        pricerangestring = preferences.getString ( "pricerange" , "" );
        whoareyoustring = preferences.getString ( "allowed_People" , "" );
        livingchoicestring = preferences.getString ( "livingpref" , "" );
        location = preferences.getString ( "place" , "" );
        recyclerView = findViewById ( R.id.recyclerView3 );
        this.recyclerView.setLayoutManager ( new GridLayoutManager ( this , 1 ) );
        this.recyclerView.setHasFixedSize ( true );
        this.filteredhomes = new ArrayList<> ( );
        loadData ();


    }

    private void loadData() {

        //mydata.php(Json database)

        final StringRequest stringRequest = new StringRequest ( Request.Method.POST ,
                Constants.FILTER_URL , new Response.Listener<String> ( )
        {
            @Override
            public void onResponse( String response) {
                Log.i ( "INFOfilter" , response );
                try {
                    final JSONObject jsonObject = new JSONObject ( response );
                    final JSONArray jsonArray = jsonObject.getJSONArray ( "data" );
                    for (int i = 0; i < jsonArray.length ( ); i++) {


                        JSONObject jOBJ = jsonArray.getJSONObject ( i );
                        home mHome = new home ( jOBJ.getString ( "image" ) ,
                                jOBJ.getString ( "name" ) ,
                                jOBJ.getString ( "place" ) ,
                                jOBJ.getString ( "price" ) ,
                                jOBJ.getString ( "description" ) ,
                                jOBJ.getString ( "bedroom" ) ,
                                jOBJ.getString ( "restroom" ) ,
                                jOBJ.getString ( "rating" ) ,
                                jOBJ.getString ( "owner_details" ) ,
                                jOBJ.getString ( "people_allowed" ) ,
                                jOBJ.getString ( "livingpref" ) ,
                                jOBJ.getString ( "pricerange" ));


                            filteredhomes.add(mHome);

                    }

                    filteradapter = new myadapter ( FilterResults.this , filteredhomes );

                    recyclerView.setAdapter ( filteradapter );

                     myadapter.setOnItemClickListener ( FilterResults.this );

                } catch (final JSONException ex) {
                    Log.e ( "JSON" , ex.getMessage ( ) );
                }
            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse( VolleyError error) {

            }
        } ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> hashMap = new HashMap<> ( );
                hashMap.put ( KEY_LOCATION , location);
                hashMap.put ( KEY_LIVING_PREFERENCE , livingchoicestring );
                hashMap.put ( KEY_ALLOWED_PEEPS , whoareyoustring );
                hashMap.put ( KEY_PRICERANGE , pricerangestring );
                return hashMap;
            }
        };

        VolleySingleton singleton = VolleySingleton.getInstance ( this );
        singleton.addToRequestQueue ( stringRequest );

    }

    @Override
    public void onItemClick(int position)
    {

        Bundle filterbundle = new Bundle ();
        home mHome = this.filteredhomes.get ( position );
        Intent intent = new Intent ( FilterResults.this , DisplayAct.class );
        filterbundle.putString ( "image" , mHome.getImage () );
        filterbundle.putString ( "name" , mHome.getName () );
        filterbundle.putString ( "description" , mHome.getDescription () );
        filterbundle.putString ( "bedroom" , mHome.getBedroom () );
        filterbundle.putString ( "restroom" , mHome.getRestroom () );
        filterbundle.putString ( "rating" , mHome.getRating () );
        filterbundle.putString ( "owner_details" , mHome.getOwner_details () );
        filterbundle.putString ( "people_allowed" , mHome.getPeople_allowed () );
        filterbundle.putString ( "livingpref" , mHome.getLivingpref () );
        filterbundle.putString ( "pricerange" , mHome.getPricerange () );
        intent.putExtras (filterbundle);
        this.startActivity ( intent );
    }
}