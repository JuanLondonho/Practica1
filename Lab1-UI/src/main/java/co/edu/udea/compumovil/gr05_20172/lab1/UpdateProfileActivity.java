package co.edu.udea.compumovil.gr05_20172.lab1;

import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateProfileActivity extends AppCompatActivity {
    SharedPreferences mPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        mPreference = getSharedPreferences("DATOS",0);
        showData();

    }


    private void showData(){
        ((EditText)findViewById(R.id.txtUserName)).setText(mPreference.getString("name",null));
        ((EditText)findViewById(R.id.txtUserLastName)).setText(mPreference.getString("lastName",null));
        ((EditText)findViewById(R.id.txtUserPhone)).setText(mPreference.getString("phone",null));
        ((EditText)findViewById(R.id. txtUserAddress)).setText(mPreference.getString("address",null));
        ((EditText)findViewById(R.id.txtUserEmail)).setText(mPreference.getString("email",null));
        ((EditText)findViewById(R.id.txtUserPassword)).setText(mPreference.getString("password",null));
        ((TextView)findViewById(R.id.tvDate)).setText(mPreference.getString("birthday",null));

    }

}
