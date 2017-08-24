package co.edu.udea.compumovil.gr05_20172.lab1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
// 24 or higher
//import android.icu.util.Calendar;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import static java.lang.System.out;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, PlaceSelectionListener{

    private TextView displayDate;
    private static final int SELECTED_PICTURE=1;
    private ImageView iv;

    // Global values for sharedPrefences

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    String name="";
    String lastName="";
    String gender="";
    String birthDate="DD/MM/AA";
    String phone="";
    String address="";
    String email="";
    String password="";
    String city="";


    // image setup

    ImageView imageFile;
    String route="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        displayDate = (TextView) findViewById(R.id.tvDate);
        displayDate.setOnClickListener(this);
        iv = (ImageView)findViewById(R.id.imgUser);


        // Google PlaceAutocomplete API set up
        PlaceAutocompleteFragment autocompleteFragment =
                (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .setCountry("CO")
                .build();

        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(this);

        Button button = ((Button)findViewById(R.id.btnUserRegister));
        button.setOnClickListener(this);

        // Shared preferences set up

        mPreferences = getSharedPreferences("DATOS", 0);
        mEditor = mPreferences.edit();


        // restore the image route and the date value after the cellphone has been rotated.

        if(savedInstanceState!=null){
            try {
                File f=new File(savedInstanceState.getString("route"), "profile.jpg");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                ImageView img=(ImageView)findViewById(R.id.imgUser);
                img.setImageBitmap(b);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

            // persist the birthDate and the route link after consecutive rotations

            birthDate =savedInstanceState.getString("birthday");
            route = savedInstanceState.getString("route");

            ((TextView)findViewById(R.id.tvDate)).setText(savedInstanceState.getString("birthday"));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("route",route);
        outState.putString("birthday", birthDate);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvDate:
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        this,
                        year, month, day);

                // setting the background to a transparent color
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                break;

            case R.id.imgUser:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECTED_PICTURE);
                break;

            case R.id.btnUserRegister:



                name = ((EditText) findViewById(R.id.txtUserName)).getText().toString();
                lastName = ((EditText) findViewById(R.id.txtUserLastName)).getText().toString();

                if(((RadioButton)findViewById(R.id.rBtnMale)).isChecked()){
                    gender = ((TextView)findViewById(R.id.rBtnMale)).getText().toString();
                }
                if(((RadioButton)findViewById(R.id.rBtnFemale)).isChecked()) {
                    gender = ((TextView)findViewById(R.id.rBtnFemale)).getText().toString();
                }

                phone = ((EditText) findViewById(R.id.txtUserPhone)).getText().toString();
                address = ((EditText) findViewById(R.id.txtUserAddress)).getText().toString();
                email = ((EditText) findViewById(R.id.txtUserEmail)).getText().toString();
                password =  ((EditText) findViewById(R.id.txtUserPassword)).getText().toString();



                if(name.equals("") || lastName.equals("") || gender.equals("") || phone.equals("")
                        || address.equals("") || email.equals("") || password.equals("") || city.equals("")
                        || birthDate.equals("") || route.equals("")){

                    Toast toast = Toast.makeText(getApplicationContext(), "Faltan campos por rellenar", Toast.LENGTH_SHORT);
                    toast.show();

                }else {

                    mEditor.putString("route", route);
                    mEditor.putString("name", name);
                    mEditor.putString("lastName", lastName);
                    mEditor.putString("gender", gender);
                    mEditor.putString("birthday", birthDate);
                    mEditor.putString("phone", phone);
                    mEditor.putString("address", address);
                    mEditor.putString("email", email);
                    mEditor.putString("password", password);
                    mEditor.putString("city", city);
                    mEditor.commit();

                    checkSharedPreferences();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                break;
        }
    }


    private void checkSharedPreferences() {
        String name = mPreferences.getString("name", "");
        String password = mPreferences.getString("password", "");
        String route = mPreferences.getString("route","");

        Log.d("TAG", "name: " + name + " password: " + password + " route:" + route);
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case SELECTED_PICTURE:
                if(resultCode==RESULT_OK){
                    Uri uri = data.getData();
                    String[]projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, projection,null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filePath = cursor.getString(columnIndex);

                    cursor.close();

                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);


                    Drawable d = new BitmapDrawable(yourSelectedImage);
                    out.println("STEP1");
                    iv.setBackground(d);
                    iv.setImageResource(0);

                    // set the image as a Bitmap object to store it in the internal memory.
                    imageFile = (ImageView) findViewById(R.id.imgUser);
                    imageFile.setDrawingCacheEnabled(true);
                    imageFile.buildDrawingCache();
                    Bitmap bitmap =  Bitmap.createBitmap(imageFile.getDrawingCache());
                    route = saveToInternalStorage(bitmap);

                }
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month = month + 1;
        String date =  + month + "/" + day  + "/" + year;

        birthDate = date;

        displayDate.setText(date);
        Log.d("TAG", "onDateSet: " + birthDate);
    }

    @Override
    public void onPlaceSelected(Place place) {
        // do something after the place was selected

        city = (String) place.getName();

        Log.i("TAG", "Place: " + city);
    }

    @Override
    public void onError(Status status) {
        Log.i("TAG", "An error occurred: " + status);
    }
}
