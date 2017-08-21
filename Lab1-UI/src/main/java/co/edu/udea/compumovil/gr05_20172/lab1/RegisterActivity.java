package co.edu.udea.compumovil.gr05_20172.lab1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
// 24 or higher
//import android.icu.util.Calendar;
import java.util.Calendar;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, PlaceSelectionListener{

    private TextView displayDate;
    private static final int SELECTED_PICTURE=1;
    private ImageView iv;

    // Global values for sharedPrefences

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    String name;
    String lastName;
    String gender;
    String birthDate;
    String phone;
    String address;
    String email;
    String password;
    String city;


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

        // Shared preferences set up

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();


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

                name = ((TextView) findViewById(R.id.txtUserName)).getText().toString();
                lastName = ((TextView) findViewById(R.id.txtUserLastName)).getText().toString();

                if(((RadioButton)findViewById(R.id.rBtnMale)).isChecked()){
                    gender = "male";
                } else {
                    gender = "false";
                }

                phone = ((EditText) findViewById(R.id.txtUserPhone)).getText().toString();
                address = ((EditText) findViewById(R.id.txtUserAddress)).getText().toString();
                email = ((EditText) findViewById(R.id.txtUserPassword)).getText().toString();
                password =  ((EditText) findViewById(R.id.txtUserPassword)).getText().toString();

                mEditor.putString("name", name);
                mEditor.putString("lastname", lastName);
                mEditor.putString("gender", gender);
                mEditor.putString("birthDate", birthDate);
                mEditor.putString("phone", phone);
                mEditor.putString("adress", address);
                mEditor.putString("email", email);
                mEditor.putString("password", password);
                mEditor.putString("city", city);
                mEditor.commit();

                // check if variables were saved in shared preferences.
                checkSharedPreferences();
                break;
        }
    }

    private void checkSharedPreferences() {
        String name = mPreferences.getString("name", "");
        String password = mPreferences.getString("password", "");

        Log.d("TAG", "name: " + name + " password: " + password);
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

                    System.out.println("STEP1");
                    iv.setBackground(d);
                    iv.setImageResource(0);

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
