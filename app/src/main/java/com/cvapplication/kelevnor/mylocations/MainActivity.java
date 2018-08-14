package com.cvapplication.kelevnor.mylocations;

import android.Manifest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cvapplication.kelevnor.mylocations.ADAPTER.ADAPTER_LocationItem;
import com.cvapplication.kelevnor.mylocations.REST.PullLocationData;
import com.cvapplication.kelevnor.mylocations.REST.PullLocationQueryData;
import com.cvapplication.kelevnor.mylocations.Utils.Config;
import com.cvapplication.kelevnor.mylocations.Utils.LocationUtility.FallbackLocationTracker;
import com.cvapplication.kelevnor.mylocations.Utils.LocationUtility.LocationTracker;
import com.cvapplication.kelevnor.mylocations.Utils.LocationUtility.ProviderLocationTracker;
import com.cvapplication.kelevnor.mylocations.Utils.PermissionUtils;
import com.cvapplication.kelevnor.mylocations.Utils.UtilityHelperClass;
import com.cvapplication.kelevnor.mylocations.Utils.UtilityHelper_Permissions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Marios Sifalakis 08/13/2018
 * Main Activity of the application. It contains three views, immitating FragmentTransactions
 * CheckLocation view is the main view of the app
 * Click  the floating actionbar to add a location through query or coordinates
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,  UtilityHelper_Permissions.OnAsyncResult,
        ActivityCompat.OnRequestPermissionsResultCallback, Animation.AnimationListener, SwipeMenuListView.OnMenuItemClickListener, View.OnClickListener, GoogleMap.OnMapLongClickListener, SeekBar.OnSeekBarChangeListener, ADAPTER_LocationItem.onItemClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean inSettings = false;
    private boolean inList = false;
    private boolean inMap = true;
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    int TYPE_ADD = 111;
    int TYPE_NORMAL = 211;
    int DEFAULT_TYPE  = TYPE_NORMAL;
    android.support.v7.widget.RecyclerView lv;
    android.support.v7.widget.RecyclerView lv_log;
    LinearLayout mapRl;
    RelativeLayout settingsRl, storedListRl;
    FloatingActionButton fabMain, fabTop, fabBottom;
    Dialog dialog;
    TextView menuCounter, latlongfab, queryfab;

    private Boolean isFabOpen = false;
    private Animation rotate_forward,rotate_backward;

    SwipeMenuListView listView;

    SeekBar timeVariantSB;
    TextView emptyList, emptyActivityList, timeValue;
    ADAPTER_LocationItem listAdapter;
    UtilityHelper_Permissions utilityPermissions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        timeVariantSB = findViewById(R.id.sb_timevariant);
        timeValue = findViewById(R.id.tv_timevalue);
        timeValue = findViewById(R.id.tv_timevalue);
        utilityPermissions = new UtilityHelper_Permissions(this, this);

        mapRl = findViewById(R.id.ll_map);
        settingsRl = findViewById(R.id.rl_settings);
        storedListRl = findViewById(R.id.rl_storedlocations);
        listAdapter = new ADAPTER_LocationItem(MainActivity.this, Config.storedActivityLog, MainActivity.this);

        latlongfab = findViewById(R.id.tv_latlong);
        queryfab = findViewById(R.id.tv_query);
        emptyList = findViewById(R.id.tv_empty_list);
        emptyActivityList = findViewById(R.id.tv_empty_activity_list);

        listView = findViewById(R.id.listView);

        lv_log = findViewById(R.id.rv_activitylog);

//        lv_log.setVisibility(View.GONE);
        // set creator
        listView.setMenuCreator(creator);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabMain = findViewById(R.id.fabmain);
        fabTop = findViewById(R.id.fabtop);
        fabBottom = findViewById(R.id.fabbottom);

        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        latlongfab.setOnClickListener(this);
        queryfab.setOnClickListener(this);
        fabTop.setOnClickListener(this);
        fabBottom.setOnClickListener(this);
        fabMain.setOnClickListener(this);

        timeVariantSB.setOnSeekBarChangeListener(this);

        timeVariantSB.setProgress(60);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


        menuCounter =(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_locationlist));

        listView.setOnMenuItemClickListener(this);

        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");
            // record the fact that the app has been started at least once
            displayDialogOneButton("WELCOME","Enable Location Update Service to \nperiodically store your location and \ncheck the weather. \n\nYou can also pick locations and \nstore them, not just in the activity \nlog, but also in a personalized list","DISMISS");
            menuCounter.setText("0");
            Config.storedActivityLog = new ArrayList<>();
            emptyActivityList.setVisibility(View.VISIBLE);
            lv_log.setVisibility(View.GONE);
            UtilityHelperClass.saveLocationListInSharedPreferences(new ArrayList<com.cvapplication.kelevnor.mylocations.MODELS.locations_list.Location>(), getApplicationContext());
            settings.edit().putBoolean("my_first_time", false).commit();
        }
        else{
            Config.storedActivityLog = UtilityHelperClass.getLocationListFromSharedPreferences(getApplicationContext());
            if(Config.storedActivityLog.size()==0){
                displayDialogOneButton("WELCOME","Enable Location Update Service to \nperiodically store your location and \ncheck the weather. \n\nYou can also pick locations and \nstore them, not just in the activity \nlog, but also in a personalized list","DISMISS");
                menuCounter.setText("0");
                emptyActivityList.setVisibility(View.VISIBLE);
                lv_log.setVisibility(View.GONE);
            }
            else{
                //show locations
                emptyActivityList.setVisibility(View.GONE);
                lv_log.setVisibility(View.VISIBLE);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                lv_log.setLayoutManager(mLayoutManager);
                lv_log.setItemAnimator(new DefaultItemAnimator());
                listAdapter = new ADAPTER_LocationItem(MainActivity.this, Config.storedActivityLog, MainActivity.this);
                lv_log.setAdapter(listAdapter);
                menuCounter.setText(String.valueOf(Config.storedActivityLog.size()));
            }
        }


    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.M) {
//            utilityPermissions.CheckGenericPermissions();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission to access the location is missing.
                PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                        Manifest.permission.ACCESS_FINE_LOCATION, true);
            } else if (mMap != null) {
                // Access to the location has been granted to the app.
                mMap.setMyLocationEnabled(true);
            }
        }
        else{
            mMap.setMyLocationEnabled(true);

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            displayDialogTwoButtonsBackClicked("EXIT", "Are you sure you want to exit?", "NO", "YES");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_clear_markers) {
            clearMarkersFromMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            navigationSetViewsStoreBoolean(item);
            if(isFabOpen){
                animateFAB();
            }
            fabMain.show();
            fabTop.hide();
            fabBottom.hide();
            queryfab.setVisibility(View.INVISIBLE);
            latlongfab.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_locationlist) {
            navigationSetViewsStoreBoolean(item);
            if(isFabOpen){
                animateFAB();
            }
            fabMain.hide();
            fabTop.hide();
            fabBottom.hide();
            queryfab.setVisibility(View.INVISIBLE);
            latlongfab.setVisibility(View.INVISIBLE);

//            adapterAction();

        } else if (id == R.id.nav_settings) {
            navigationSetViewsStoreBoolean(item);
            if(isFabOpen){
                animateFAB();
            }
            fabMain.hide();
            fabTop.hide();
            fabBottom.hide();
            queryfab.setVisibility(View.INVISIBLE);
            latlongfab.setVisibility(View.INVISIBLE);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }



    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "open" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            openItem.setWidth(dp2px(70));
            openItem.setTitle("Edit");
            openItem.setTitleColor(Color.WHITE);
            openItem.setTitleSize(12);
            openItem.setBackground(new ColorDrawable(Color.rgb(0xCC, 0xCC, 0xCC)));
            // add to menu
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            deleteItem.setWidth(dp2px(70));
            deleteItem.setTitle("Remove");
            deleteItem.setTitleColor(Color.WHITE);
            deleteItem.setTitleSize(12);
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));

            menu.addMenuItem(deleteItem);
        }
    };

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
            case 0:
                // edit
                break;
            case 1:
                // delete

                break;
        }
        // false : close the menu; true : not close the menu
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabmain:
                animateFAB();
                break;

            case R.id.tv_query:
                animateFAB();
                displayDialogAddLocationName("Add your location", "Cancel" ,"Search");

                break;
            case R.id.tv_latlong:
                animateFAB();
                Location loc = null;
                LocationTracker lt = new FallbackLocationTracker(this, ProviderLocationTracker.ProviderType.NETWORK);

                if (lt.hasLocation()) {
                    loc = lt.getLocation();

                    if(UtilityHelperClass.isNetworkAvailable(getApplicationContext())){
                        new PullLocationData(loc.getLatitude(), loc.getLongitude(), pullCoordinates);
                        Log.e("lt LAT/LONG", String.valueOf(loc.getLatitude()) + " / " + String.valueOf(loc.getLongitude()));
                    }
                    else{
                        displayDialogOneButton("Error","Could not establish Internet Connection!","Dismiss");
                    }

                } else {
                    if (lt.hasPossiblyStaleLocation()) {
                        loc = lt.getPossiblyStaleLocation();
                        if(UtilityHelperClass.isNetworkAvailable(getApplicationContext())){
                            new PullLocationData(loc.getLatitude(), loc.getLongitude(), pullCoordinates);
                        }
                        else{
                            displayDialogOneButton("Error","Could not establish Internet Connection!","Dismiss");
                        }
                        Log.e("STALE lt LAT/LONG", String.valueOf(loc.getLatitude()) + " / " + String.valueOf(loc.getLongitude()));
                    } else {
                        displayDialogOneButton("Error","No Coordinates found on the device!","Dismiss");

                        Log.e("NO LOCATION FOUND", "NO LOCATION FOUND");
                    }
                }
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void animateFAB(){

        if(isFabOpen){
            fabMain.startAnimation(rotate_backward);
            fabTop.setClickable(false);
            fabBottom.setClickable(false);
            latlongfab.setVisibility(View.INVISIBLE);
            queryfab.setVisibility(View.INVISIBLE);
            isFabOpen = false;
            Log.d("Marios", "close");

        } else {
            fabMain.startAnimation(rotate_forward);
            latlongfab.setVisibility(View.VISIBLE);
            queryfab.setVisibility(View.VISIBLE);
            isFabOpen = true;
            Log.d("Marios","open");

        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    private void displayDialogTwoButtons (String title, String message, String leftBtn, String rightBtn, final com.cvapplication.kelevnor.mylocations.MODELS.locations_list.Location loc){
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(rightBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Config.storedActivityLog.add(loc);
                emptyActivityList.setVisibility(View.GONE);
                lv_log.setVisibility(View.VISIBLE);
                listAdapter = new ADAPTER_LocationItem(MainActivity.this, Config.storedActivityLog, MainActivity.this);
                lv_log.setAdapter(listAdapter);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                lv_log.setLayoutManager(mLayoutManager);
                lv_log.setItemAnimator(new DefaultItemAnimator());
                DEFAULT_TYPE = TYPE_NORMAL;
                menuCounter.setText(String.valueOf(Config.storedActivityLog.size()));
                UtilityHelperClass.saveStringInPreference(getResources().getString(R.string.list_init),"NOT_NA", getApplicationContext());
                UtilityHelperClass.saveLocationListInSharedPreferences(Config.storedActivityLog, getApplicationContext());

            }
        });
        builder.setNegativeButton(leftBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        builder.show();
    }

    private void displayDialogTwoButtonsBackClicked (String title, String message, String leftBtn, String rightBtn){
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(rightBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.setNegativeButton(leftBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        builder.show();
    }

    private void displayDialogOneButton (String title, String message, String centerBtn){
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        final AppCompatDialog alert = builder.create();
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(centerBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        builder.show();
    }
    private void displayDialogAddLocationName(String title, String leftBtn, String rightBtn){
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle(title);
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton(rightBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if(input.getText().length()!=0){

                    if(UtilityHelperClass.isNetworkAvailable(getApplicationContext())){
                        Log.e("Connection Avail", "Connection Avail");
                        new PullLocationQueryData(input.getText().toString(), pullQuery);
                    }
                    else if(!UtilityHelperClass.isNetworkAvailable(getApplicationContext())){
                        Log.e("!Connection Avail", "!Connection Avail");
                        displayDialogOneButton("Error","Could not establish Internet Connection!","Dismiss");
                    }
                }
                else{
                    displayDialogOneButton("ERROR", "Name is Empty!!!", "DISMISS");
                }

            }
        });
        builder.setNegativeButton(leftBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        builder.show();
    }

    private void clearMarkersFromMap(){

        Config.storedActivityLog.clear();
        Config.storedActivityLog = new ArrayList<>();
        UtilityHelperClass.saveLocationListInSharedPreferences(Config.storedActivityLog, getApplicationContext());

        menuCounter.setText("0");
        lv_log.setVisibility(View.GONE);
        emptyActivityList.setVisibility(View.VISIBLE);
    }

    private void navigationSetViewsStoreBoolean(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.nav_map) {
            inList = false;
            inSettings = false;
            inMap = true;
            mapRl.setVisibility(View.VISIBLE);
            settingsRl.setVisibility(View.GONE);
            storedListRl.setVisibility(View.GONE);
        } else if (id == R.id.nav_locationlist) {
            inList = true;
            inSettings = false;
            inMap = false;
            mapRl.setVisibility(View.GONE);
            settingsRl.setVisibility(View.GONE);
            storedListRl.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_settings) {
            inList = false;
            inSettings = true;
            inMap = false;
            mapRl.setVisibility(View.GONE);
            settingsRl.setVisibility(View.VISIBLE);
            storedListRl.setVisibility(View.GONE);
        }
    }


    private void adapterAction(){
        if(UtilityHelperClass.getStringFromPreference(getResources().getString(R.string.list_init), getApplicationContext()).equals("NA")){
            listView.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
        }
        else{

        }



    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        timeValue.setText("every "+convertToTime(i+10));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private String convertToTime(int number){
        int minutes = (number % 3600) / 60;
        int seconds = number % 60;
        return String.valueOf(minutes)+" min "+ String.valueOf(seconds)+" sec";
    }

    //On result listener for PullLocationQueryData Api Call - to retreive location result from search?query metaweather data
    PullLocationQueryData.OnAsyncResult pullQuery= new PullLocationQueryData.OnAsyncResult() {

        @Override
        public void onResultSuccess(int resultCode, List<com.cvapplication.kelevnor.mylocations.MODELS.locations_list.Location> locations) {

            Boolean hasValues = false;
            try {
                Log.e("No# of Results:", String.valueOf(locations.size()));
                hasValues = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(hasValues&&locations.size()>0){
                displayDialogTwoButtons("Success","Following Location Found: \n\nTitle: "+locations.get(0).getTitle()+" \nType: "+locations.get(0).getLocationType()+" \nCoordinates: "+locations.get(0).getLattLong()+" \n\nWould you like to add the location?","Cancel","Add",  locations.get(0));
            }
            else{
                displayDialogOneButton("Oops", "No Locations found with that name!","Dismiss");
            }

        }

        @Override
        public void onResultFail(int resultCode, String errorResult) {
            Log.e("FAIL_RESPONSE", errorResult);
        }
    };

//On result listener for PullLocationQueryData Api Call - to retreive location result from search?coordinates metaweather data

    PullLocationData.OnAsyncResult pullCoordinates= new PullLocationData.OnAsyncResult() {

        @Override
        public void onResultSuccess(int resultCode, List<com.cvapplication.kelevnor.mylocations.MODELS.locations_list.Location> locations) {

            DEFAULT_TYPE = TYPE_ADD;
            Boolean hasValues = false;
            try {
                Log.e("No# of Results:", String.valueOf(locations.size()));
                hasValues = true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("No# of Results:", "Something went wrong");
            }

            if(hasValues&&locations.size()>0){

                dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_list_locations);

                dialog.setCancelable(true);
                dialog.setTitle("ListView");
                lv = (android.support.v7.widget.RecyclerView ) dialog.findViewById(R.id.rv_locationslist);
                ADAPTER_LocationItem listAdapter = new ADAPTER_LocationItem(MainActivity.this, locations, MainActivity.this);
                lv.setAdapter(listAdapter);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                lv.setLayoutManager(mLayoutManager);
                lv.setItemAnimator(new DefaultItemAnimator());

                dialog.show();
            }
            else{
                displayDialogOneButton("Oops", "No Locations found with that name!","Dismiss");
            }
        }

        @Override
        public void onResultFail(int resultCode, String errorResult) {
            Log.e("FAIL_RESPONSE", errorResult);
        }
    };


    @Override
    public void onItemClick(com.cvapplication.kelevnor.mylocations.MODELS.locations_list.Location location) {

        if(DEFAULT_TYPE == TYPE_NORMAL){
//            animateFAB();
            Toast.makeText(this, "Check Weather with WOEID: "+ location.getWoeid(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, LocationWeatherActivity.class);
            i.putExtra("woeid",String.valueOf(location.getWoeid()));
            startActivity(i);
        }
        else if(DEFAULT_TYPE == TYPE_ADD){
            dialog.dismiss();
            displayDialogTwoButtons("Action Required","Following Location Picked: \n\nTitle: "+location.getTitle()+" \nType: "+location.getLocationType()+" \nCoordinates: "+location.getLattLong()+" \n\nWould you like to add the location?","Cancel","Add", location);
        }
    }

    @Override
    public void onInternetPermissionSuccess(int resultCode, String message) {

    }

    @Override
    public void onInternetPermissionFail(int resultCode, String errorMessage) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        utilityPermissions.onRequestPermissionAction(requestCode, permissions, grantResults);

    }
}

