package com.example.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.app.adapter.myadapter;
import com.example.app.model.SliderItem;
import com.example.app.model.home;
import com.example.app.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VirtualTourAct extends AppCompatActivity  {
    private ViewPager2 viewPager2;
    private  Handler sliderHandler = new Handler ( );

    private SliderAdapter sliderAdapter;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        this.setContentView ( R.layout.activity_virtual_tour );

        this.viewPager2 = this.findViewById ( R.id.viewPagerImageSlider );
        //Here, fetching images from drawable
        //Json fetching here
        this.loadImages ();
        this.viewPager2.setClipToPadding ( false );
        this.viewPager2.setClipChildren ( false );
        this.viewPager2.setOffscreenPageLimit ( 3 );
        this.viewPager2.getChildAt ( 0 ).setOverScrollMode ( RecyclerView.OVER_SCROLL_NEVER );
         CompositePageTransformer compositePageTransformer = new CompositePageTransformer ( );
        compositePageTransformer.addTransformer ( new MarginPageTransformer ( 20 ) );
        compositePageTransformer.addTransformer ( (page , position) -> {
           float r = 1 - Math.abs ( position );
            page.setScaleY ( 0.85f + r * 0.15f );

        } );

        this.viewPager2.setPageTransformer ( compositePageTransformer );
        this.viewPager2.registerOnPageChangeCallback ( new ViewPager2.OnPageChangeCallback ( ) {
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected ( position );
                VirtualTourAct.this.sliderHandler.removeCallbacks ( VirtualTourAct.this.sliderRunnable );
                VirtualTourAct.this.sliderHandler.postDelayed ( VirtualTourAct.this.sliderRunnable , 3000 );
                //slide duration 3 seconds
            }
        } );


    }


    private final Runnable sliderRunnable = new Runnable ( ) {
        @Override
        public void run() {
            VirtualTourAct.this.viewPager2.setCurrentItem ( VirtualTourAct.this.viewPager2.getCurrentItem ( ) + 1 );

        }
    };

    @Override
    protected void onPause() {
        super.onPause ( );
        this.sliderHandler.removeCallbacks ( this.sliderRunnable );

    }

    @Override
    protected void onResume() {
        super.onResume ( );
        this.sliderHandler.postDelayed ( this.sliderRunnable , 3000 );
    }


    private void loadImages() {
        ArrayList<SliderItem> sliderItems = new ArrayList<> (  );
         StringRequest stringRequest = new StringRequest ( Request.Method.GET ,
                Constants.House_image_URL ,  new Response.Listener<String> ( ) {
            @Override
            public void onResponse(final String response) {
                Log.i ( "INFOimage" , response );
                try {
                     JSONObject jsonObject = new JSONObject ( response );
                     JSONArray jsonArray = jsonObject.getJSONArray ( "data" );

                    for (int i = 0; i < jsonArray.length ( ); i++) {
                        JSONObject jOBJ = jsonArray.getJSONObject ( i );
                        SliderItem homeimages = new SliderItem ( jOBJ.getString ( "img1" ) ,
                                jOBJ.getString ( "img2" ) ,
                                jOBJ.getString ( "img3" ) ,
                                jOBJ.getString ( "img4" ) ,
                                jOBJ.getString ( "img5" ) );
                                sliderItems.add(homeimages); }


                    viewPager2.setAdapter ( new SliderAdapter ( sliderItems, viewPager2 ));

                } catch (JSONException ex) {
                    Log.e ( "JSON" , ex.getMessage ( ) );
                }
            }
        },new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        } );

        VolleySingleton singleton = VolleySingleton.getInstance ( this );
        singleton.addToRequestQueue ( stringRequest );

    }

    }

