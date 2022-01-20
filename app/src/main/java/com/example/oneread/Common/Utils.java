package com.example.oneread.Common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.oneread.Activity.MainActivity;
import com.example.oneread.Model.User;


import static com.example.oneread.Common.Common.toast;

public class Utils {

    public static String formatName(String name){
        name = name.trim().toLowerCase();
        StringBuilder builder = new StringBuilder();
        String[] res = name.split(" ");
        for(int i=0; i< res.length; i++){
            builder.append(res[i].toUpperCase().charAt(0)).append(res[i].substring(1).toLowerCase());
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    public static void showToast (Context context, String content, int length) {
        try {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(context, content, length);
            toast.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void resetObjects(Object[] objects) {
        for(Object o : objects){
            if(o instanceof EditText){
                ((EditText) o).setText("");
            }
        }
    }

    //region permission
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnected();
    }

    public static boolean checkWriteExternalStorage(Context context) {
        int hasWriteExternalStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return hasWriteExternalStorage != PackageManager.PERMISSION_GRANTED;
    }
    //endregion

    //region file
    /*
     * get file name: if it a picture from camera (not saved yet and dont have name) the fileuri scheme is "content"
     * */
    @SuppressLint("Range")
    public static String getFileName(ContentResolver contentResolver, Uri fileUri){
        String result = null;
        if(fileUri.getScheme().equals("content")){
            Cursor cursor = null;
            cursor = contentResolver.query(fileUri, null, null, null, null);
            try {
                if(cursor != null && cursor.moveToFirst())
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }finally {
                if(cursor != null) cursor.close();
            }
        }

        if(result == null){
            result = fileUri.getPath();
            int cut = result.lastIndexOf("/");
            if(cut != -1){
                result = result.substring(cut + 1);
            }
        }

        return result;
    }
    //endregion

    //region firebase

    //endregion


    //region validate
    public static boolean checkEmptyComponents(Object[] objects){
        for(Object o : objects){
            if(o instanceof EditText){
                if (((EditText) o).getText().toString().trim().equals("")) {
                    ((EditText) o).setError("Not Empty");
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValidEmailAddress(String email) {
        // ảo thật đấy
        //String pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        String regex = "^([a-zA-Z0-9]{5,})@((([a-zA-Z]+\\.)([a-zA-Z]{2,})(\\.[a-zA-Z]{2,})?))$";
        return email.matches(regex);
    }

    public static boolean isValidEmailAddress(EditText email) {
        String regex = "^([a-zA-Z0-9]{5,})@((([a-zA-Z]+\\.)([a-zA-Z]{2,})(\\.[a-zA-Z]{2,})?))$";
        if (!email.getText().toString().matches(regex)) {
            email.setError(Message.emailInvalid);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidPasswordAddress(String password) {
        /*
        (?=x).{1,} match "xy" in "ayxy"
        (?=.x).{1,} match "yxy" in "xyxy"
        (?=.*x).{1,} match only "xyxy"
        */
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return password.matches(regex);
    }

    public static boolean isValidPasswordAddress(EditText password) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        if (!password.getText().toString().matches(regex)) {
            password.setError(Message.passwordInvalid);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidName(String name){
        String regex = "^[a-zA-Z\\s]+";
        return name.matches(regex);
    }

    public static boolean isValidName(EditText name){
        String regex = "^[a-zA-Z\\s]+";
        if (!name.getText().toString().matches(regex)) {
            name.setError(Message.nameInvalid);
            return false;
        } else {
            return true;
        }
    }

    //endregion
}
