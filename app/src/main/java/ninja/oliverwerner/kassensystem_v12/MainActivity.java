package ninja.oliverwerner.kassensystem_v12;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button table_button;
    Button product_button;
    Button setting_button;
    Button calculate_button;
    TextView year_value;
    TextView year_price;
    TextView month_value;
    TextView month_price;
    TextView day_value;
    TextView day_price;
    TextView calc_value;
    TextView calc_price;
    EditText edit_year;
    EditText edit_month;
    EditText edit_day;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        table_button = (Button) findViewById(R.id.main_table);
        table_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TablesActivity.class);
                startActivity(intent);
            }
        });
        product_button = (Button) findViewById(R.id.main_product);
        product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProductGroupsActivity.class);
                startActivity(intent);
            }
        });
        setting_button = (Button) findViewById(R.id.main_settings);
        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),SettingsActivity.class);
                startActivity(intent);
            }
        });

        year_value  = (TextView) findViewById(R.id.year_value);
        year_price  = (TextView) findViewById(R.id.year_price);
        month_value = (TextView) findViewById(R.id.month_value);
        month_price = (TextView) findViewById(R.id.month_price);
        day_value   = (TextView) findViewById(R.id.day_value);
        day_price   = (TextView) findViewById(R.id.day_price);

        calc_value = (TextView) findViewById(R.id.date_value);
        calc_price = (TextView) findViewById(R.id.date_price);

        edit_day = (EditText) findViewById(R.id.edit_day);
        edit_month = (EditText) findViewById(R.id.edit_month);
        edit_year = (EditText) findViewById(R.id.edit_year);

        int year = new GregorianCalendar().get(GregorianCalendar.YEAR);
        int month = new GregorianCalendar().get(GregorianCalendar.MONTH)+1;
        int day = new GregorianCalendar().get(GregorianCalendar.DATE);

        // day average
        try {
            // HashMap erstellen und Daten für die DB-Abfrage im Inneren speichern
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("method", "getStatistics");
            hashMap.put("day",day+"");
            hashMap.put("month",month+"");
            hashMap.put("year",year+"");

            // Hole mir den Rückgabe-String und speicher ihn in einer Variable ab
            String jsonString = new ActivityDataSource(hashMap).execute().get();

            // Erstelle aus dem JSON-String ein JSONArray
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");

            // Hole aus dem JSONArray ein JSONObjekt und speicher die Daten in Variablen
            JSONObject oneObject = jsonArray.getJSONObject(0);
            String value ;
            String price;
            value = oneObject.getString("ordered_products");
            if(value.equalsIgnoreCase("")) value = "0";
            price = oneObject.getString("volume_of_sale");
            if(price.equalsIgnoreCase("")) price = "0";

            day_value.setText(value);
            day_price.setText(price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Month average
        try {
            // HashMap erstellen und Daten für die DB-Abfrage im Inneren speichern
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("method", "getStatistics");
            hashMap.put("day","");
            hashMap.put("month",month+"");
            hashMap.put("year",year+"");

            // Hole mir den Rückgabe-String und speicher ihn in einer Variable ab
            String jsonString = new ActivityDataSource(hashMap).execute().get();

            // Erstelle aus dem JSON-String ein JSONArray
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");

            // Hole aus dem JSONArray ein JSONObjekt und speicher die Daten in Variablen
            JSONObject oneObject = jsonArray.getJSONObject(0);
            String value ;
            String price;
            value = oneObject.getString("ordered_products");
            if(value.equalsIgnoreCase("")) value = "0";
            price = oneObject.getString("volume_of_sale");
            if(price.equalsIgnoreCase("")) price = "0";

            month_value.setText(value);
            month_price.setText(price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // year avg
        try {
            // HashMap erstellen und Daten für die DB-Abfrage im Inneren speichern
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("method", "getStatistics");
            hashMap.put("day","");
            hashMap.put("month","");
            hashMap.put("year",year+"");

            // Hole mir den Rückgabe-String und speicher ihn in einer Variable ab
            String jsonString = new ActivityDataSource(hashMap).execute().get();

            // Erstelle aus dem JSON-String ein JSONArray
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");

            // Hole aus dem JSONArray ein JSONObjekt und speicher die Daten in Variablen
            JSONObject oneObject = jsonArray.getJSONObject(0);
            String value ;
            String price;
            value = oneObject.getString("ordered_products");
            if(value.equalsIgnoreCase("")) value = "0";
            price = oneObject.getString("volume_of_sale");
            if(price.equalsIgnoreCase("")) price = "0";

            year_value.setText(value);
            year_price.setText(price);
        } catch (Exception e) {
            e.printStackTrace();
        }

        calculate_button = (Button) findViewById(R.id.calculate_button);
        calculate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iYear = 0;
                int iMonth = 0;
                int iDay =  0;

                if(!edit_day.getText().toString().equalsIgnoreCase("")){
                    iDay =  Integer.parseInt(edit_day.getText()+"");

                    if(!edit_month.getText().toString().equalsIgnoreCase("")){
                        iMonth = Integer.parseInt(edit_month.getText()+"");
                    }else{
                        iMonth = new GregorianCalendar().get(GregorianCalendar.MONTH)+1;
                    }

                    if(!edit_year.getText().toString().equalsIgnoreCase("")){
                        iYear = Integer.parseInt(edit_year.getText()+"");
                    }else{
                        iYear = new GregorianCalendar().get(GregorianCalendar.YEAR);
                    }

                }else if(!edit_month.getText().toString().equalsIgnoreCase("")){
                    iMonth = Integer.parseInt(edit_month.getText()+"");

                    if(!edit_year.getText().toString().equalsIgnoreCase("")){
                        iYear = Integer.parseInt(edit_year.getText()+"");
                    }else{
                        iYear = new GregorianCalendar().get(GregorianCalendar.YEAR);
                    }

                }else if (!edit_year.getText().toString().equalsIgnoreCase("")){

                    iYear = Integer.parseInt(edit_year.getText().toString());

                }
                calc_value.setText(0+"");
                calc_price.setText(0+"");
                if(iYear != 0){
                    String sDay = "";
                    String sMonth = "";
                    if( iMonth > 0){
                        sMonth = iMonth +"";
                        if(iDay > 0){
                            sDay = iDay + "";
                        }
                    }

                    try {
                        // HashMap erstellen und Daten für die DB-Abfrage im Inneren speichern
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("method", "getStatistics");
                        hashMap.put("day",sDay);
                        hashMap.put("month",sMonth);
                        hashMap.put("year",iYear+"");

                        // Hole mir den Rückgabe-String und speicher ihn in einer Variable ab
                        String jsonString = new ActivityDataSource(hashMap).execute().get();

                        // Erstelle aus dem JSON-String ein JSONArray
                        JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");

                        // Hole aus dem JSONArray ein JSONObjekt und speicher die Daten in Variablen
                        JSONObject oneObject = jsonArray.getJSONObject(0);
                        String value ;
                        String price;
                        value = oneObject.getString("ordered_products");
                        if(value.equalsIgnoreCase("")) value = "0";
                        price = oneObject.getString("volume_of_sale");
                        if(price.equalsIgnoreCase("")) price = "0";

                        calc_value.setText(value);
                        calc_price.setText(price);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Activity refreshed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // TODO: 12.12.2016 Diese Funktion wird bei allen Aktivities aufgerufen. Kann auf einmal  
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_dashboard: {
                Intent intent = new Intent(this,MainActivity.class);
                setTitle(R.string.title_activity_dashboard);
                startActivity(intent);
                break;
            }
            case R.id.nav_tables: {
                Intent intent = new Intent(this,TablesActivity.class);
                setTitle(R.string.title_activity_table);
                startActivity(intent);
                break;
            }
            case R.id.nav_products: {
                Intent intent = new Intent(this,ProductGroupsActivity.class);
                setTitle(R.string.title_activity_product_groups);
                startActivity(intent);
                break;
            }
            case R.id.nav_settings: {
                Intent intent = new Intent(this,SettingsActivity.class);
                setTitle(R.string.title_activity_settings);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout: {
                Intent intent = new Intent(this,LoginActivity.class);
                setTitle(R.string.title_activity_login);
                startActivity(intent);
                break;
            }
            default: {
                Log.d("myMessage","nav_default");
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        KeyboardController.hideKeyboard(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPrefs = getSharedPreferences("SETTINGS", 0);
        int currentBackgroundColor = sharedPrefs.getInt("THEME_COLOR", 0xffffffff);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#"+Integer.toHexString(currentBackgroundColor).toUpperCase()));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }
}
