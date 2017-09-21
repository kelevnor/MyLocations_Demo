package com.cvapplication.kelevnor.mylocations;

import android.Manifest;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cvapplication.kelevnor.mylocations.Utils.UtilityHelperClass;
import com.cvapplication.kelevnor.mylocations.models.objects.Location;
import com.cvapplication.kelevnor.mylocations.models.objects.LocationList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, Animation.AnimationListener, SwipeMenuListView.OnMenuItemClickListener, View.OnClickListener, GoogleMap.OnMapLongClickListener {
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean hasPickedMarker = false;
    private boolean pickedMarkerBlue = false;
    private boolean pickedMarkerGreen = false;

    private boolean inSettings = false;
    private boolean inList = false;
    private boolean inMap = true;
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    RelativeLayout mapRl, settingsRl, storedListRl;
    FloatingActionButton fabMain, fabTop, fabBottom;

    private Boolean isFabOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    SwipeMenuListView listView;

    ImageView blueSelect, greenSelect, blueSelectHeight, greenSelectHeight;
    LocationList myLocationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        mapRl = (RelativeLayout) findViewById(R.id.rl_map);
        settingsRl = (RelativeLayout) findViewById(R.id.rl_settings);
        storedListRl = (RelativeLayout) findViewById(R.id.rl_storedlocations);

        blueSelect = (ImageView) findViewById(R.id.iv_blue_selected);
        greenSelect = (ImageView) findViewById(R.id.iv_green_selected);

        blueSelectHeight = (ImageView) findViewById(R.id.iv_blue_selected_height);
        greenSelectHeight = (ImageView) findViewById(R.id.iv_green_selected_height);
        listView = (SwipeMenuListView) findViewById(R.id.listView);

        Log.e("TEST_GIT","TEST_GIT");
        // set creator
        listView.setMenuCreator(creator);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabMain = (FloatingActionButton) findViewById(R.id.fabmain);
        fabTop = (FloatingActionButton) findViewById(R.id.fabtop);
        fabBottom = (FloatingActionButton) findViewById(R.id.fabbottom);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        fabTop.setOnClickListener(this);
        fabBottom.setOnClickListener(this);
        fabMain.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        String[] values  = new String[]{"60 Paulou Mela, GR, 25100", "13 Ergotimou, GR, 27687"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);
        listView.setOnMenuItemClickListener(this);

        if(UtilityHelperClass.getIntegerFromPreference(getResources().getString(R.string.list_init), getApplicationContext())==0){
            myLocationsList = new LocationList();
            displayDialogOneButton("WELCOME","Apply long clicks on the map to add\nmarkers and manage them in a list!\nWhile on map view, click + button\nto pick a marker","DISMISS");
        }
        else{
            myLocationsList = UtilityHelperClass.getLocationListFromSharedPreferences(getApplicationContext());
            if(myLocationsList.getLocations().size()==0){
                displayDialogOneButton("EMPTY","Start Long Clicking to add locations to your list!","DISMISS");
            }
            else{

            }
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            displayDialogTwoButtons("EXIT", "Are you sure you want to exit?", "NO", "YES");
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_markers) {
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
            if(hasPickedMarker){
                if(pickedMarkerBlue){
                    greenSelect.setVisibility(View.GONE);
                    blueSelect.setVisibility(View.VISIBLE);
                    greenSelectHeight.setVisibility(View.GONE);
                    blueSelectHeight.setVisibility(View.VISIBLE);
                }
                else if(pickedMarkerGreen){
                    greenSelect.setVisibility(View.VISIBLE);
                    blueSelect.setVisibility(View.GONE);
                    greenSelectHeight.setVisibility(View.VISIBLE);
                    blueSelectHeight.setVisibility(View.GONE);
                }
                else{
                    greenSelect.setVisibility(View.GONE);
                    blueSelect.setVisibility(View.GONE);
                    greenSelectHeight.setVisibility(View.GONE);
                    blueSelectHeight.setVisibility(View.GONE);
                }
            }
        } else if (id == R.id.nav_locationlist) {
            navigationSetViewsStoreBoolean(item);
            if(isFabOpen){
                animateFAB();
            }
            fabMain.hide();
            fabTop.hide();
            fabBottom.hide();
            greenSelect.setVisibility(View.GONE);
            blueSelect.setVisibility(View.GONE);
            greenSelectHeight.setVisibility(View.GONE);
            blueSelectHeight.setVisibility(View.GONE);

        } else if (id == R.id.nav_settings) {
            navigationSetViewsStoreBoolean(item);
            if(isFabOpen){
                animateFAB();
            }
            fabMain.hide();
            fabTop.hide();
            fabBottom.hide();
            greenSelect.setVisibility(View.GONE);
            blueSelect.setVisibility(View.GONE);
            greenSelectHeight.setVisibility(View.GONE);
            blueSelectHeight.setVisibility(View.GONE);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
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
                // open
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
            case R.id.fabtop:
                Log.d("Marios", "Fab 1");
                greenSelect.setVisibility(View.GONE);
                blueSelect.setVisibility(View.VISIBLE);
                greenSelectHeight.setVisibility(View.GONE);
                blueSelectHeight.setVisibility(View.VISIBLE);
                hasPickedMarker = true;
                pickedMarkerBlue = true;
                pickedMarkerGreen = false;
                animateFAB();
                break;
            case R.id.fabbottom:
                Log.d("Marios", "Fab 2");
                greenSelect.setVisibility(View.VISIBLE);
                blueSelect.setVisibility(View.GONE);
                greenSelectHeight.setVisibility(View.VISIBLE);
                blueSelectHeight.setVisibility(View.GONE);
                hasPickedMarker = true;
                pickedMarkerGreen = true;
                pickedMarkerBlue = false;
                animateFAB();
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void animateFAB(){

        if(isFabOpen){
            fabMain.startAnimation(rotate_backward);
            fabTop.startAnimation(fab_close);
            fabBottom.startAnimation(fab_close);
            fabTop.setClickable(false);
            fabBottom.setClickable(false);
            isFabOpen = false;
            Log.d("Marios", "close");

        } else {
            fabMain.startAnimation(rotate_forward);
            fabTop.startAnimation(fab_open);
            fabBottom.startAnimation(fab_open);
            fabTop.setClickable(true);
            fabBottom.setClickable(true);
            isFabOpen = true;
            Log.d("Marios","open");

        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        if(hasPickedMarker){
            displayDialogAddMarkerName("ENTER NAME:", "CANCEL", "ADD", latLng);
        }
        else{
            displayDialogOneButton("OOPS!!!","You need to pick a marker icon.\nClick + button to pick a marker.", "DISMISS");
        }


    }

    private void displayDialogTwoButtons (String title, String message, String leftBtn, String rightBtn){
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
    private void displayDialogAddMarkerName(String title, String leftBtn, String rightBtn, final LatLng latLng){
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

                    Location tempLocation = new Location();

                    tempLocation.setName(input.getText().toString());
                    tempLocation.setLocationLatitude(latLng.latitude);
                    tempLocation.setLocationLongitude(latLng.longitude);

                    if(pickedMarkerBlue){
                        tempLocation.setMarkerColor("BLUE");
//                        myLocationsList.getLocations().add(tempLocation);
                        displayDialogOneButton("SAVED", "Your location with name:\n"+input.getText().toString()+"\nis saved!", "DISMISS");

                        addMarkerOnMap(latLng, input.getText().toString(), "BLUE");

                    }
                    else if(pickedMarkerGreen){
                        tempLocation.setMarkerColor("GREEN");
//                        myLocationsList.getLocations().add(tempLocation);
                        displayDialogOneButton("SAVED", "Your location with name:\n"+input.getText().toString()+"\nis saved!", "DISMISS");

                        addMarkerOnMap(latLng, input.getText().toString(), "GREEN");
                    }
                    else{
                        displayDialogOneButton("OOPS!!!", "It looks like your marker selection is not set.\nClick + button to pick a marker.", "DISMISS");
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

    private void addMarkerOnMap(LatLng latlng, String name, String color){

        if(color.equals("BLUE")){
            mMap.addMarker(new MarkerOptions().position(latlng)
                    .title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_blue_24dp)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        }
        else{
            mMap.addMarker(new MarkerOptions().position(latlng)
                    .title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_green_24dp)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        }


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

}
