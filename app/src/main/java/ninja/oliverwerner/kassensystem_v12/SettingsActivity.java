package ninja.oliverwerner.kassensystem_v12;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Festgelegte Feldname für die Speicherplätze in der Datei
    private static final String SHOP_NAME = "SHOP_NAME";
    private static final String THEME_COLOR = "THEME_COLOR";

    // Dateiname unter dem die Informationen gespeichert werden
    private static final String FILENAME = "KS_SETTINGS";

    // Tool zum Speichern und Lesen der Informationen in einer Datei
    SharedPreferences sharedPrefs;

    // Felder aus dem Activity-Layout
    EditText etShopName;
    EditText etThemeColor;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Views des Layouts in Variablen laden
        etShopName = (EditText) findViewById(R.id.et_shop_name);
        etThemeColor = (EditText) findViewById(R.id.et_theme_color);
        btnSave = (Button) findViewById(R.id.btn_save);

        // Beim Klick auf Speichern ...
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hole den aktuellen Text aus den Textfeldern und löschen Leerzeichen vom Anfang und Ende
                String shopName = etShopName.getText().toString().trim();
                String themeColor = etThemeColor.getText().toString().trim();

                // Stellt die Datei zum Schreiben bereit
                SharedPreferences sharedPrefs = getSharedPreferences(FILENAME, 0);

                // Es wird ein "Bearbeiter" für die Datei erstellt
                SharedPreferences.Editor editor = sharedPrefs.edit();

                // Schreibe in die Datei an Feld x String y und speicher die Datei ab
                editor.putString(SHOP_NAME, shopName).commit();
                editor.putString(THEME_COLOR, themeColor).commit();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
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
    protected void onStart() {
        super.onStart();
        // Lade die Datei zum Lesen
        sharedPrefs = getSharedPreferences(FILENAME, 0);

        // Trage den Text aus den Speicherplätzen in die Textfelder ein
        etShopName.setText(sharedPrefs.getString(SHOP_NAME,null));
        etThemeColor.setText(sharedPrefs.getString(THEME_COLOR,null));
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_dashboard: {
                Log.d("myMessage","nav_dashboard");
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_tables: {
                Log.d("myMessage","nav_tables");
                Intent intent = new Intent(this,TablesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_products: {
                Log.d("myMessage","nav_products");
                Intent intent = new Intent(this,ProductGroupsActivity.class);
                intent.putExtra("orderId","");
                startActivity(intent);
                break;
            }
            case R.id.nav_settings: {
                Log.d("myMessage","nav_settings");
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout: {
                Log.d("myMessage","nav_logout");
//                Intent intent = new Intent(this,MainActivity.class);
//                startActivity(intent);
                setTitle("Logout");
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
}
