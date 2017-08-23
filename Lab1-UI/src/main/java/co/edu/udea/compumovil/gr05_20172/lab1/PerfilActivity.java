package co.edu.udea.compumovil.gr05_20172.lab1;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PerfilActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences mPreferences;
    String route;
    Bitmap b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPreferences = getSharedPreferences("DATOS", 0);

        route = mPreferences.getString("route", null);
        loadImageFromStorage(route);

        ((TextView)findViewById(R.id.txtProfileName)).setText("Name: "+ mPreferences.getString("name",null));
        ((TextView)findViewById(R.id.txtProfileLastName)).setText("LastName: "+ mPreferences.getString("lastName",null));
        ((TextView)findViewById(R.id.txtProfileGender)).setText("Gender: "+ mPreferences.getString("gender",null));
        ((TextView)findViewById(R.id.txtProfileBirthday)).setText("Birthday:"+ mPreferences.getString("birthday",null));
        ((TextView)findViewById(R.id.txtProfilePhone)).setText("Phone: "+ mPreferences.getString("phone",null));
        ((TextView)findViewById(R.id.txtProfileAddress)).setText("Address: "+ mPreferences.getString("address",null));
        ((TextView)findViewById(R.id.txtProfileEmail)).setText("Email: "+ mPreferences.getString("email",null));
        ((TextView)findViewById(R.id.txtProfileCity)).setText("City: "+ mPreferences.getString("city",null));

        //((TextView)findViewById(R.id.txtProfileNameNav)).setText(""+mPreferences.getString("name",null));
        //(TextView)findViewById(R.id.txtProfileEmailNav)).setText(""+mPreferences.getString("email",null));


    }

    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.imgProfileUser);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.perfil, menu);




        ((TextView)findViewById(R.id.txtProfileNameNav)).setText(""+mPreferences.getString("name",null));
        ((TextView)findViewById(R.id.txtProfileEmailNav)).setText(""+mPreferences.getString("email",null));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.imgUserProfileNav) {
            // Handle the camera action
        } else if (id == R.id.updateProfile) {

        } else if (id == R.id.applicationClose){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
