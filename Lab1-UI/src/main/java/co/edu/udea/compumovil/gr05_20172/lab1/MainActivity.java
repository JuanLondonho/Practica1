package co.edu.udea.compumovil.gr05_20172.lab1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences mPreferences;
    String email;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getSharedPreferences("DATOS", 0);

        Button button = (Button)findViewById(R.id.btnLogin);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                email = ((EditText)findViewById(R.id.txtUserLog)).getText().toString();
                password = ((EditText)findViewById(R.id.txtUserPass)).getText().toString();
                if(email.equalsIgnoreCase(mPreferences.getString("email", null)) && password.equals(mPreferences.getString("password", null))){

                    ((EditText)findViewById(R.id.txtUserLog)).setText("");
                    ((EditText) findViewById(R.id.txtUserPass)).setText("");
                    Intent intent = new Intent(getApplicationContext(), PerfilActivity.class);
                    startActivity(intent);


                }else {
                    TextView textView = ((TextView)findViewById(R.id.txtUserPass));
                    textView.setText("");
                    Toast toast = Toast.makeText(getApplicationContext(),"Datos incorrectos", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

            case R.id.txtViewRegister:
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                break;
        }
    }
}
