package com.cvapplication.kelevnor.mylocations.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * Class to check for Camera, Location and Storage Permissions
 * Finally, after buiding the page to display the weather, I realized I needed the Storage Read and Write Permissions
 * and I had this class ready to integrate, I did that but did not have time to integrate in the flow
 */
public class UtilityHelper_Permissions {

    OnAsyncResult onAsync;
    Activity act;

    //Constructor
    public UtilityHelper_Permissions(OnAsyncResult onAsync, Activity act){
        this.onAsync = onAsync;
        this.act = act;
    }

    public static int PERMISSION_LOCATION = 1111;
    public static int PERMISSION_CAMERA = 1112;
    public static int PERMISSION_STORAGE = 1113;

    public void onRequestPermissionAction(int requestCode,
                                          String permissions[], int[] grantResults){

        if(requestCode == UtilityHelper_Permissions.PERMISSION_STORAGE){
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if(!checkIfAlreadyhaveLocationPermission(act)){
                    ActivityCompat.requestPermissions(act,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_LOCATION);
                }
                else{

                    if(!checkIfAlreadyhaveCameraPermission(act)){
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA},
                                PERMISSION_CAMERA);
                    }
                    else{
                        if(UtilityHelperClass.isNetworkAvailable(act)){
                            onAsync.onInternetPermissionSuccess(1,"SUCCESS_PERMISSIONS_GRANTED");
                        }
                        else{
                            displayDialogOneButtonNoAction(act, "Oops", "No Internet Permission");
                            onAsync.onInternetPermissionFail(0,"FAIL_NET+_SUCCESS_PERMISSIONS");
                        }
                    }
                }
                Log.e("storage read", "storage read");

            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                displayDialogOneButtonNoAction(act,"Permission Required", " Read Storage Permission is required");
                onAsync.onInternetPermissionFail(0,"SUCCESS_NET+_FAIL_PERMISSIONS");
            }
        }
        else if(requestCode == UtilityHelper_Permissions.PERMISSION_LOCATION){
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if(!checkIfAlreadyhaveCameraPermission(act)){
                    ActivityCompat.requestPermissions(act,
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_CAMERA);
                }
                else{
                    if(UtilityHelperClass.isNetworkAvailable(act)){
                        onAsync.onInternetPermissionSuccess(1,"SUCCESS_PERMISSIONS_GRANTED");
                    }
                    else{
                        displayDialogOneButtonNoAction(act, "Oops", "No Internet Permission");
                        onAsync.onInternetPermissionFail(0,"FAIL_NET+_SUCCESS_PERMISSIONS");
                    }
                }
            } else {
                Toast.makeText(act, "Permission denied to read your Access Location", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == UtilityHelper_Permissions.PERMISSION_CAMERA){
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(UtilityHelperClass.isNetworkAvailable(act)){
                    onAsync.onInternetPermissionSuccess(1,"SUCCESS_PERMISSIONS_GRANTED");
                }
                else{
                    displayDialogOneButtonNoAction(act, "Oops", "No Internet Permission");
                    onAsync.onInternetPermissionFail(0,"FAIL_NET+_SUCCESS_PERMISSIONS");
                }

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {
                Toast.makeText(act, "Permission denied to take pictures and record video", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void CheckGenericPermissions(){
        if(!checkIfAlreadyhaveExternalStoragePermission(act)){
            if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                displayDialogOneButton(act,"Permission Required", "In order to access your files and images, the following device permission is required!", Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_STORAGE);
            } else {
                Log.e("!READ_EXTERNAL_STORAGE","!READ_EXTERNAL_STORAGE");
                ActivityCompat.requestPermissions(act,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_STORAGE);
            }
        }
        else{
            if(!checkIfAlreadyhaveLocationPermission(act)){
                if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    displayDialogOneButton(act,"Permission Required", "TruckPay requires the location of the user in parts of the app, to allow the user have better control of the job flow", Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_LOCATION);
                }
                else{
                    ActivityCompat.requestPermissions(act,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_LOCATION);
                }
            }
            else{
                if(!checkIfAlreadyhaveCameraPermission(act)){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                            Manifest.permission.CAMERA)) {
                        displayDialogOneButton(act,"Permission Required", "TruckPay requires the use of camera to take pictures", Manifest.permission.CAMERA, PERMISSION_CAMERA);
                    }
                    else{
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISSION_CAMERA);
                    }
                }
                else{

                    int permissionCheckStorage = ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    int permissionCheckLocation = ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION);
                    Log.e("ACCESS_COARSE_LOCATION",String.valueOf(permissionCheckLocation));
                    Log.e("READ_EXTERNAL_STORAGE",String.valueOf(permissionCheckStorage));
                    if(UtilityHelperClass.isNetworkAvailable(act)){
                        onAsync.onInternetPermissionSuccess(1,"SUCCESS_NET+_SUCCESS_PERMISSIONS");
                    }
                    else{
                        displayDialogOneButtonNoAction(act, "Oops", "No Internet Detected");
                        onAsync.onInternetPermissionFail(0,"FAIL_NET+_SUCCESS_PERMISSIONS");
                    }
                }
            }
        }
    }

    private boolean checkIfAlreadyhaveLocationPermission(Activity act) {
        int resultFine = ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION);
        int resultCoarse = ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (resultFine == PackageManager.PERMISSION_GRANTED&&resultCoarse==PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkIfAlreadyhaveExternalStoragePermission(Activity act) {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private boolean checkIfAlreadyhaveCameraPermission(Activity act) {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void displayDialogOneButton (final Activity act, String titleStr, String subtitleStr, final String permission, final int permissionRequestCode){
        final android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(act);
        final AppCompatDialog alert = builder.create();
        builder.setTitle(titleStr);
        builder.setMessage(subtitleStr);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                requestPermission(permission, permissionRequestCode, act);

            }
        });


        builder.show();
    }

    private void displayDialogOneButtonNoAction (final Activity act, String titleStr, String subtitleStr){
        final android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(act);
        final AppCompatDialog alert = builder.create();
        builder.setTitle(titleStr);
        builder.setMessage(subtitleStr);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });


        builder.show();
    }




    private void requestPermission(String permissionName, int permissionRequestCode, Activity act) {
        ActivityCompat.requestPermissions(act,
                new String[]{permissionName}, permissionRequestCode);
    }

    public interface OnAsyncResult {
        void onInternetPermissionSuccess(int resultCode, String message);
        void onInternetPermissionFail(int resultCode, String errorMessage);
    }
}
