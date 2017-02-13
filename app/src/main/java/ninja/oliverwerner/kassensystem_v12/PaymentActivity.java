package ninja.oliverwerner.kassensystem_v12;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static Switch plus_minus = null;
    private PaymentListAdapter adapter = null;
    int tableID = 0;
    Button payment_button;
    static TextView sum_Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        payment_button = (Button) findViewById(R.id.pay_Button);
        payment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = adapter.getCount();
                JSONArray dataArray = new JSONArray();
                int first = 0;
                for(int iGr = 0; iGr < i; iGr++){
                    int productid = adapter.getItem(iGr).getProductID();
                    String name = adapter.getItem(iGr).getName() + "".toString();
                    String sPri = adapter.getItem(iGr).getPrice() + "".toString();
                    String sBez = adapter.getItem(iGr).getNumber() + "".toString();
                    int tableid = adapter.getItem(iGr).getTableId();
                    int value = adapter.getItem(iGr).getValue();
                    int orderValue = Integer.parseInt(sBez.toString());
                    double dPri = Double.parseDouble(sPri.toString());
                    int new_anz = orderValue - value;
                    if(value > 0) {
                        try {
                            JSONObject data = new JSONObject();
                            data.put("pid", productid);
                            data.put("paycount", value);
                            dataArray.put(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (new_anz > 0) {
                            adapter.remove(adapter.getItem(iGr));
                            adapter.insert(new Product(productid, name, dPri, new_anz, tableid, 0), iGr);
                        } else {
                            adapter.remove(adapter.getItem(iGr));
                            i--;
                            iGr--;
                        }
                    }
                }
                Log.d("arraytest",dataArray.toString());
                sum_Price.setText("0.00");
                //TODO Absprache mit Oliver wie der Array übergeben werden muss
                try {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("method", "payOrder");
                    hashMap.put("table_id", tableID+"".toString());
                    hashMap.put("paid_products", dataArray.toString());

                    // Hole mir den Rückgabe-String und speicher ihn in einer Variable ab
                    String jsonString = new ActivityDataSource(hashMap).execute().get();
                    Log.d("arraytest", jsonString+"");

                    // Erstelle aus dem JSON-String ein JSONArray
                    JSONObject oneObject = new JSONObject(jsonString).getJSONObject("data");



                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(oneObject.getString("bon_code"));


                    String bonid =  oneObject.getString("bon_code");
                    showBon(bonid);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        sum_Price = (TextView) findViewById(R.id.sum_price);
        sum_Price.setText("0.00");
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
//                Intent intent = new Intent(this,MainActivity.class);
//                startActivity(intent);
                setTitle("Einstellungen");
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
        builder.setTitle("Bon");

        final TextView bonID = new TextView(this);
        bonID.setTextSize(22);


        bonID.setText("  " + bon_id);
        builder.setView(bonID);

        // Set up the buttons

        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}