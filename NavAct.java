package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.app.adapter.filteradapter;
import com.example.app.adapter.myadapter;

import com.example.app.model.home;
import com.example.app.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NavAct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, myadapter.OnItemClickListener

{
    private SharedPreferences preferences;
    private TextView unameText, emailText;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private myadapter adapter;
    private ArrayList<home> homes;
    private ArrayList<home> filterhomes;
    private AppCompatImageView houseimage;
    private AppCompatTextView housename, place , price, noofbed , noofbath;
    EditText searchview;
    CharSequence search = "";
    private String pricerangestring, whoareyoustring, livingchoicestring , user_id;
    private com.google.android.material.textfield.TextInputEditText location;
    ArrayList<home> homelist;

     GoogleSignInClient mGoogleSignInClient;
     Bundle bundle = null;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate ( savedInstanceState );

        this.setContentView ( R.layout.activity_main );

        this.searchview = this.findViewById ( R.id.search_bar );


        this.preferences = this.getSharedPreferences ( "mypref" , Context.MODE_PRIVATE );
        user_id = preferences.getString ( "email" , "" );

        this.recyclerView = this.findViewById ( R.id.recyclerView );
        this.recyclerView.setLayoutManager ( new GridLayoutManager ( this , 1 ) );
        this.recyclerView.setHasFixedSize ( true );
        this.homes = new ArrayList<> ( );

        final Toolbar toolbar = this.findViewById ( R.id.toolbar );
        //setSupportActionBar(toolbar);
        final FloatingActionButton fab = this.findViewById ( R.id.fab );
        fab.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            //Post your property
            public void onClick(final View view) {

                NavAct.this.startActivity ( new Intent ( NavAct.this.getApplicationContext ( ) , PostHomeInfo.class ) );
            }
        } );
        final DrawerLayout drawer = this.findViewById ( R.id.drawer_layout );
        this.navigationView = this.findViewById ( R.id.nav_view );
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle ( this , drawer , toolbar ,
                R.string.navigation_drawer_open , R.string.navigation_drawer_close );
        toggle.syncState ( );
        this.navigationView.setNavigationItemSelectedListener ( this );

//        unameText = navigationView.getHeaderView(0).findViewById(R.id.usernameText);
//        emailText = navigationView.getHeaderView(0).findViewById(R.id.emailText);
//       // imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
//        bundle = getIntent().getExtras();
//        if(bundle!=null)
//        {
//            Uri.Builder builder = new Uri.Builder();
//            builder.path(bundle.getString("display_image")).build();
        // Picasso.get().load(builder.toString()).fit().into(imageView);
//            unameText.setText(bundle.getCharSequence("display_name"));
//            emailText.setText(bundle.getCharSequence("display_email"));
//      }
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail().build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        this.loadData ( );

        //Search code

        this.searchview.addTextChangedListener ( new TextWatcher ( ) {
            @Override
            public void beforeTextChanged( CharSequence charSequence ,  int start , int count ,  int after) {


            }

            @Override
            public void onTextChanged( CharSequence charSequence ,  int start , int before ,  int count) {

                myadapter.getFilter().filter ( charSequence );
                NavAct.this.search = charSequence;
            }

            @Override
            public void afterTextChanged(final Editable s) {
            }
        } );

    }

    @Override
    protected void onResume() {
        super.onResume ( );
        //  unameText = navigationView.getHeaderView ( 0 ).findViewById ( R.id.usernameText );
        this.emailText = this.navigationView.getHeaderView ( 0 ).findViewById ( R.id.emailText );
        //  unameText.setText ( preferences.getString ( "uname" , "" ) );
        this.emailText.setText ( this.preferences.getString ( "email" , "" ) );
//        final Bundle bundle = this.getIntent ().getExtras ();
//        if (bundle != null)
//        {
//            this.housename.setText ( bundle.getCharSequence ( "name" ) );
//            this.noofbed.setText ( bundle.getCharSequence ( "bedroom" ) );
//            this.noofbath.setText ( bundle.getCharSequence ( "restroom" ) );
//            this.place.setText ( bundle.getCharSequence ( "place" ) );
//            this.price.setText ( bundle.getCharSequence ( "price" ) );
//            Picasso.get().load ( String.valueOf ( bundle.getCharSequence("image" ) ) ).fit ().into ( this.houseimage );
//        }

    }

    private void loadData() {

        //mydata.php(Json database)

        final StringRequest stringRequest = new StringRequest ( Request.Method.GET ,
                Constants.URL , new Response.Listener<String> ( )
         {
            @Override
            public void onResponse( String response) {
                Log.i ( "INFOHOME" , response );
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
                        NavAct.this.homes.add ( mHome );
                    }

                    NavAct.this.adapter = new myadapter ( NavAct.this , NavAct.this.homes , homes);

                    NavAct.this.recyclerView.setAdapter ( NavAct.this.adapter );

                    myadapter.setOnItemClickListener ( NavAct.this );

                } catch (final JSONException ex) {
                    Log.e ( "JSON" , ex.getMessage ( ) );
                }
            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse( VolleyError error) {

            }
        } ) ;

        VolleySingleton singleton = VolleySingleton.getInstance ( this );
        singleton.addToRequestQueue ( stringRequest );

    }

//    @Override
//    public boolean onCreateOptionsMenu(final Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        this.getMenuInflater ( ).inflate ( R.menu.activity_main_drawer , menu );
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
//        final int id = item.getItemId ( );
//        if (id == R.id.action_logout) {
//
//        }
//        return true;
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        final int id = item.getItemId ( );
        Intent intent = new Intent ( this.getApplicationContext ( ) , NavAct.class );
        if (id == R.id.nav_home) {
            intent = new Intent ( new Intent ( this.getApplicationContext ( ) , NavAct.class));
            startActivity ( intent );
        } else if (id == R.id.nav_profile) {
            intent = new Intent ( this.getApplicationContext ( ) , ProfileAct.class );
            startActivity ( intent );
        } else if (id == R.id.Filters) {
            intent = new Intent ( this.getApplicationContext ( ) , FilterActivity.class );
            startActivity ( intent );
        }
        else if (id == R.id.action_logout){

            SharedPreferences.Editor editor = this.preferences.edit ( );
            editor.remove ( "email" );
            editor.remove ( "password" );
            editor.clear ( );
            editor.apply ( );
            this.startActivity ( new Intent ( this.getApplicationContext ( ) , LoginAct.class ) );
            this.finish ( );

        }


        return true;
    }

    @Override
    public void onItemClick( int position )
    {
        Bundle bundle = new Bundle ( );
        home mHome = this.homes.get ( position );
        Intent intent = new Intent ( NavAct.this , DisplayAct.class );
        bundle.putString ( "image" , mHome.getImage ( ) );
        bundle.putString ( "name" , mHome.getName ( ) );
        bundle.putString ( "description" , mHome.getDescription ( ) );
        bundle.putString ( "bedroom" , mHome.getBedroom ( ) );
        bundle.putString ( "restroom" , mHome.getRestroom ( ) );
        bundle.putString ( "rating" , mHome.getRating ( ) );
        bundle.putString ( "owner_details" , mHome.getOwner_details ( ) );
        bundle.putString ( "people_allowed" , mHome.getPeople_allowed ( ) );
        bundle.putString ( "livingpref" , mHome.getLivingpref ( ) );
        bundle.putString ( "pricerange" , mHome.getPricerange ( ) );
        intent.putExtras ( bundle );
        this.startActivity ( intent );
    }

         }

