package ninja.oliverwerner.kassensystem_v12;

import android.app.ActionBar;
import android.app.AlertDialog;
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
import android.text.InputType;
import android.util.Base64;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Context CONTEXT;

    static public int user_id;

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

    private Button btPassword;


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
                                                                                TextView shopname = (TextView) findViewById(R.id.store_name);
                                                                                shopname.setText(btShopName.getText().toString());
                                                                            }
                                                                        });

                                           dialog.show();
                                       }
                                   }

        );

        btPassword = (Button) findViewById(R.id.setPassword);
        btPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                changePassword(view);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView shopname = (TextView) headerView.findViewById(R.id.store_name);
        shopname.setText(sharedPrefs.getString("SHOP_NAME", "Geschäftsnamen"));


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
    public void changePassword(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.change_password);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText oldPw = new EditText(this);
        final EditText newPw1 = new EditText(this);
        final EditText newPw2 = new EditText(this);


        oldPw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPw1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPw2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        oldPw.setHint("Altes Passwort");
        newPw1.setHint("Neues Passwort");
        newPw2.setHint("Neues Passwort Wiederholt");
        layout.addView(oldPw);
        layout.addView(newPw1);
        layout.addView(newPw2);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String pwOld = oldPw.getText().toString();
                String pwNew1 = Base64.encodeToString(newPw1.getText().toString().getBytes(), Base64.DEFAULT);
                pwNew1 = pwNew1.replace("\n","");
                String pwNew2 = Base64.encodeToString(newPw2.getText().toString().getBytes(), Base64.DEFAULT);
                pwNew2 = pwNew2.replace("\n","");
                if (pwNew1.equals(pwNew2) && pwNew1.length() >= 4) {
                    try {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("method", "changePassword");
                        hashMap.put("user_id", user_id+"" );
                        hashMap.put("pw_old", pwOld);
                        hashMap.put("pw_new", pwNew1);

                        new ActivityDataSource(hashMap).execute().get();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else if (pwNew1.length() < 4) {
                    Snackbar.make(v, "Passwort zu kurz!!!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }else{
                    Snackbar.make(v, "Eingaben für das neue Passwort stimmen nicht überein", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
