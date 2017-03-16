package ninja.oliverwerner.kassensystem_v12;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Context CONTEXT;

    // Festgelegte Feldname für die Speicherplätze in der Datei
    private static final String SHOP_NAME = "SHOP_NAME";
    private static final String THEME_COLOR = "THEME_COLOR";

    // Dateiname unter dem die Informationen gespeichert werden
    private static final String FILENAME = "SETTINGS";

    // Tool zum Speichern und Lesen der Informationen in einer Datei
    SharedPreferences sharedPrefs;

    // Felder aus dem Activity-Layout
    private Button btShopName;
    private ActionBar bar;

    // Für den Color-Picker
    private int currentBackgroundColor = 0xffffffff;
    private Button btThemeColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CONTEXT = this.getBaseContext();

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Views des Layouts in Variablen laden
        btShopName = (Button) findViewById(R.id.bt_shop_name);
        btThemeColor = (Button) findViewById(R.id.bt_theme_color);
        bar = getActionBar();

        changeBackgroundColor(currentBackgroundColor);
        btThemeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = SettingsActivity.this;

                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle(R.string.color_dialog_title)
                        .initialColor(currentBackgroundColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorChangedListener(new OnColorChangedListener() {
                            @Override
                            public void onColorChanged(int selectedColor) {
                                // Handle on color change
                                Log.d("ColorPicker", "onColorChanged: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                changeBackgroundColor(selectedColor);
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("Color List:");
                                        sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null)
                                        Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(SettingsActivity.this, android.R.color.holo_blue_bright))
                        .build()
                        .show();
            }
        });

        btShopName.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           final Dialog dialog = new Dialog(SettingsActivity.this);
                                           dialog.setContentView(R.layout.edit_field_dialog);
                                           dialog.setTitle("Geschäftsname");

                                           final EditText dialogText = (EditText) dialog.findViewById(R.id.det_text);
                                           Button dialogBtn = (Button) dialog.findViewById(R.id.dbt_save);

                                           dialogText.setText(btShopName.getText().toString());

                                           dialogBtn.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View view) {
                                                                                btShopName.setText(dialogText.getText());
                                                                                dialog.dismiss();
                                                                            }
                                                                        });

                                           dialog.show();
                                       }
                                   }

        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()

                               {
                                   @Override
                                   public void onClick(View view) {
                                       Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                               .setAction("Action", null).show();
                                   }
                               }

        );

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void changeBackgroundColor(int selectedColor) {
        currentBackgroundColor = selectedColor;
        btThemeColor.setBackgroundColor(selectedColor);

        Log.d("TITLE-COLOR","#"+Integer.toHexString(currentBackgroundColor).toUpperCase());
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#"+Integer.toHexString(currentBackgroundColor).toUpperCase()));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        KeyboardController.hideKeyboard(SettingsActivity.this);
        // Lade die Datei zum Lesen
        sharedPrefs = getSharedPreferences(FILENAME, 0);

        // Trage den Text aus den Speicherplätzen in die Textfelder ein
        btShopName.setText(sharedPrefs.getString(SHOP_NAME, null));
        Log.d("THEME_COLOR","loadColor: "+sharedPrefs.getInt(THEME_COLOR, 0xffffffff));
        changeBackgroundColor(sharedPrefs.getInt(THEME_COLOR, 0xffffffff));


    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPrefs = getSharedPreferences(FILENAME, 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putInt(THEME_COLOR, currentBackgroundColor);

        String shopName = btShopName.getText().toString().trim();
        editor.putString(SHOP_NAME, shopName);

        editor.apply();
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
                Intent intent = new Intent(this, MainActivity.class);
                setTitle(R.string.title_activity_dashboard);
                startActivity(intent);
                break;
            }
            case R.id.nav_tables: {
                Intent intent = new Intent(this, TablesActivity.class);
                setTitle(R.string.title_activity_table);
                startActivity(intent);
                break;
            }
            case R.id.nav_products: {
                Intent intent = new Intent(this, ProductGroupsActivity.class);
                setTitle(R.string.title_activity_product_groups);
                startActivity(intent);
                break;
            }
            case R.id.nav_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                setTitle(R.string.title_activity_settings);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout: {
                Intent intent = new Intent(this, LoginActivity.class);
                setTitle(R.string.title_activity_login);
                startActivity(intent);
                break;
            }
            default: {
                Log.d("myMessage", "nav_default");
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
