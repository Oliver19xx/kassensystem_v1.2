package ninja.oliverwerner.kassensystem_v12;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TablesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Tische");
        setContentView(R.layout.activity_tables);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadTables();
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPrefs = getSharedPreferences("SETTINGS", 0);
        int currentBackgroundColor = sharedPrefs.getInt("THEME_COLOR", 0xffffffff);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView shopname = (TextView) headerView.findViewById(R.id.store_name);
        shopname.setText(sharedPrefs.getString("SHOP_NAME", "Geschäftsnamen"));

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#"+Integer.toHexString(currentBackgroundColor).toUpperCase()));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                Log.d("myMessage","action_settings");
                Intent intent = new Intent(this,TablesSettingsActivity.class);
                startActivity(intent);
                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

    private void loadTables() {
        ArrayList<Table> tableList = new ArrayList<>();

        try {
            // HashMap erstellen und Daten für die DB-Abfrage im Inneren speichern
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("method", "getActivTables");

            // Hole mir den Rückgabe-String und speicher ihn in einer Variable ab
            String jsonString = new ActivityDataSource(hashMap).execute().get();

            // Erstelle aus dem JSON-String ein JSONArray
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    // Hole aus dem JSONArray ein JSONObjekt und speicher die Daten in Variablen
                    JSONObject oneObject = jsonArray.getJSONObject(i);
                    int id = oneObject.getInt("table_id");
                    String name = oneObject.getString("table_name");
                    int state = oneObject.getInt("table_state");

                    // Füge die Daten aus dem JSONObjekt in die Erstellung eines neuen Tisches ein und hänge diesen an die Liste an
                    tableList.add(new Table(id, name, state));
//                    Log.d("myMessage", "ID->" + id + " | name->" + name + " | status->" + status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        GridView gvTables = (GridView) findViewById(R.id.gvTables);
        Log.d("myMessage","tableList.length()="+tableList.size());
        TableGridAdapter adapter = new TableGridAdapter(this, R.layout.custom_button_layout, tableList);
        Log.d("myMessage","TableGridAdapter => "+adapter.toString());
        gvTables.setAdapter(adapter);
    }

}
