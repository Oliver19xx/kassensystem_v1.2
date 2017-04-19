package ninja.oliverwerner.kassensystem_v12;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Variablen die benötigt werden wurden hier definiert
    static Switch plus_minus = null;
    private PaymentListAdapter adapter = null;
    int tableID = 0;
    Button payment_button;
    Button all_button;
    static TextView sum_Price;
    SharedPreferences sharedPrefs;

    /* onCreate()
     * @param Bundle
     * erstellt die Activity mit den Buttons etc.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setzt den Titel der Activity
        setTitle("Bezahlen");

        //verweist auf das Layout was Benutzt werden soll
        setContentView(R.layout.activity_payment);

        //Initalisiert die Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Initialisiert die Nawigationsleiste
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Initsaliesieren der Anzeige für den Preis
        sum_Price = (TextView) findViewById(R.id.sum_price);
        sum_Price.setText("0.00");

        //Initialisert den All_Button mit der Entsprechenden Id
        // und der Funktion beim Klick
        all_button = (Button) findViewById(R.id.all_Button);
        all_button.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                double dCost = 0;

                //Geht die komplette Listview entlang um alle Produkte auf ihren Max
                // bezahlwert zu setzen
                for(int iGr = 0; iGr < adapter.getCount(); iGr++){
                    //die Werte werden aus dem Aktuellen Listview Element
                    int productid = adapter.getItem(iGr).getProductID();
                    String name = adapter.getItem(iGr).getName() + "".toString();
                    double dPri = adapter.getItem(iGr).getPrice();
                    int tableid = adapter.getItem(iGr).getTableId();
                    int value = adapter.getItem(iGr).getNumber();

                    //errechnen der Preise
                    dCost += (value*dPri);
                    dCost = Math.round(dCost * 100);
                    dCost = dCost / 100;

                    //der Alte Datensatz wird aus der Listview ersetzt
                    // und durch denn neuen ersetzt
                    adapter.remove(adapter.getItem(iGr));
                    adapter.insert(new Product(
                            productid, name, dPri , value, tableid, value), iGr);
                }
                //Der neu errechnete Preis wird in das entsprechende Feld geschrieben
                sum_Price.setText(dCost+"");
            }
        });

        //Initialisieren des Bezahl Buttons mit der Id und der Funktion beim Klicken
        payment_button = (Button) findViewById(R.id.pay_Button);
        payment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //die Variable i bekommt die Größe des adapters
                int i = adapter.getCount();

                //ein JsonArray wird erstellt damit diese später
                // an die Datenbank übergeben werden kann
                JSONArray dataArray = new JSONArray();

                //geht die Komplette liste Durch
                for(int iGr = 0; iGr < i; iGr++){

                    //die Werte werden aus dem aktuellen Listenelement gelesen
                    int productid = adapter.getItem(iGr).getProductID();
                    String name = adapter.getItem(iGr).getName();
                    double dPri  = adapter.getItem(iGr).getPrice();
                    int orderValue = adapter.getItem(iGr).getNumber();
                    int tableid = adapter.getItem(iGr).getTableId();
                    int value = adapter.getItem(iGr).getValue();

                    //Berechnen der neue Anzahl
                    int new_anz = orderValue - value;

                    //wurde ein Produkt ausgewählt
                    if(value > 0) {
                        //Das zu bezahlene Produkt wird mit der Produkt ID
                        // und der Anzahl in das Array geschrieben
                        try {
                            //erstellen des JSONObjekte für die Werte
                            JSONObject data = new JSONObject();
                            data.put("pid", productid);
                            data.put("paycount", value);

                            //Das JSONObjekt wird in das JSONArray geschrieben
                            dataArray.put(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //ist die Neue Anzahl größer als 1 wird das Element geändert
                        if (new_anz > 0) {
                            //Das Element wird gelöscht
                            adapter.remove(adapter.getItem(iGr));
                            //das Element mit der änderung wird an der
                            // Alten Position eingefügt
                            adapter.insert(new Product(
                                    productid, name, dPri, new_anz, tableid, 0), iGr);
                        } else {
                            //Das Objekt wird gelöcht da die Anzahl auf 0 gefallen ist
                            adapter.remove(adapter.getItem(iGr));

                            //i wird reduziert da die größe der Liste sich um 1
                            // verkleinert hat
                            i--;
                            //iGr wird reduziert da die gelöschte Position
                            // druch das Element der höheren Position ersetzt wurde
                            //und diese auch abgefragt werden muss
                            iGr--;
                        }
                    }
                }

                //das Preisfeld wird auf 0 gesetzt
                sum_Price.setText("0.00");

                //Die Funktion zum Abrechnen wird übergeben
                try {
                    //initalisierten der Hashmap
                    HashMap<String, String> hashMap = new HashMap<>();

                    //Festlegen der Funktion
                    hashMap.put("method", "payOrder");

                    //Legt Fest welche Werte Übergeben werden
                    hashMap.put("table_id", tableID+"");
                    hashMap.put("paid_products", dataArray.toString());

                    // Hole mir den Rückgabe-String und speicher ihn in einer Variable ab
                    String jsonString = new ActivityDataSource(hashMap).execute().get();

                    // Erstelle aus dem JSON-String ein JSONArray
                    JSONObject oneObject = new JSONObject(jsonString).getJSONObject("data");

                    //der Bondode Wird als rückgabewert verlangt
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(oneObject.getString("bon_code"));

                    //der Code wird in ein String geschrieben
                    // und anschließen über die Funktion showbon ausgegeben
                    String bonid =  oneObject.getString("bon_code");
                    showBon(bonid);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //der Übergabewert beim aufruf wird in die entsprechene Variable Geschrieben
        Intent intent = getIntent();
        tableID = Integer.parseInt(intent.getStringExtra("table_id"));


        // Lade Liste Bestellter Produkte
        loadOrderedList(intent.getStringExtra("table_id"));
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

    public void loadOrderedList(String tableID){
        plus_minus = (Switch) findViewById(R.id.plus_minus_pay);
        ListView listView = (ListView) findViewById(R.id.lvPaymentProducts);
        adapter = new PaymentListAdapter(this, R.layout.payment_list_item_layout, new ArrayList<Product>());
        listView.setAdapter(adapter);
        try {
            // HashMap erstellen und Daten für die DB-Abfrage im Inneren speichern
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("method", "getOrders");
            hashMap.put("tableID", tableID);

            // Hole mir den Rückgabe-String und speicher ihn in einer Variable ab
            String jsonString = new ActivityDataSource(hashMap).execute().get();
            Log.d("arraytest", jsonString+"");


            // Erstelle aus dem JSON-String ein JSONArray
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    // Hole aus dem JSONArray ein JSONObjekt und speicher die Daten in Variablen
                    JSONObject oneObject = jsonArray.getJSONObject(i);

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(oneObject.getString("order_id"));
                    stringBuilder.append(oneObject.getString("F_product_id"));
                    stringBuilder.append(oneObject.getString("product_name"));
                    stringBuilder.append(oneObject.getString("product_price"));
                    stringBuilder.append(oneObject.getString("product_count"));
                    stringBuilder.append(oneObject.getString("product_paid"));

                    //orderId =  Integer.getInteger(tableID);
                    Log.d("myMessage",stringBuilder.toString());


                    if(( oneObject.getInt("product_count") - oneObject.getInt("product_paid")) != 0) {
                        adapter.insert(new Product(
                                oneObject.getInt("F_product_id"),
                                oneObject.getString("product_name"),
                                oneObject.getDouble("product_price"),
                                oneObject.getInt("product_count") - oneObject.getInt("product_paid"),
                                this.tableID,
                                0), 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showBon(String bon_id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.bon);

        final TextView bonID = new TextView(this);
        bonID.setTextSize(22);


        bonID.setText("  " + bon_id);
        builder.setView(bonID);

        // Set up the buttons

        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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
}