package ninja.oliverwerner.kassensystem_v12;

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
import android.widget.ListView;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Table table = new Table();
    static Switch plus_minus = null;
    private ProductListAdapter adapter = null;
    static int orderId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderId != 0) {
                    Intent intent = new Intent(view.getContext(), ProductGroupsActivity.class);
                    intent.putExtra("order", 1);
                    startActivity(intent);
                }else{
                    // TODO: 29.12.2016 neue OrderBestellen und orderid Abfragen dann productgroupsactivity starten
                    Snackbar.make(view, "In Arbeit", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();

        // Hole Tisch Tischinformationen
        loadTableInfo(intent.getStringExtra("tableID"));

        // Lade Liste Bestellter Produkte
        loadOrderedList(intent.getStringExtra("tableID"));
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

    public void loadTableInfo(String tableID){
        try {
            // HashMap erstellen und Daten für die DB-Abfrage im Inneren speichern
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("method", "getTable");
            hashMap.put("tableID", tableID);

            // Hole mir den Rückgabe-String und speicher ihn in einer Variable ab
            String jsonString = new ActivityDataSource(hashMap).execute().get();
            Log.d("myMessage","Table-jsonString = "+jsonString);

            // Erstelle aus dem JSON-String ein JSONArray
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject oneObject = jsonArray.getJSONObject(0);

            // Schreibe die Daten in das Table-Objekt
            table.setTableId(oneObject.getInt("ID"));
            table.setTableName(oneObject.getString("name"));
            table.setTableState(oneObject.getInt("state"));

        }catch (Exception e){
            e.printStackTrace();
        }
        
        String state="";
        // TODO: 11.12.2016 string aus resourcen ziehen
        switch (table.getTableState()){
            case 0:
                state = "frei";
                break;
            case 1:
                state = "reserviert";
                break;
            case 2:
                state = "besetzt";
                break;
            default:
        }

        StringBuilder titel = new StringBuilder();
        titel.append("ID: "+table.getTableId());
        titel.append(" ");
        titel.append("Name: "+table.getTableName());
        titel.append(" ");
        titel.append("Status: "+state);
        setTitle(titel.toString());
    }

    public void loadOrderedList(String tableID){
        plus_minus = (Switch) findViewById(R.id.plus_minus);
        ListView listView = (ListView) findViewById(R.id.lvOrderedProducts);
        adapter = new ProductListAdapter(this, R.layout.product_list_item_layout, new ArrayList<Product>());
        listView.setAdapter(adapter);

        try {
            // HashMap erstellen und Daten für die DB-Abfrage im Inneren speichern
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("method", "getOrders");
            hashMap.put("tableID", tableID);

            // Hole mir den Rückgabe-String und speicher ihn in einer Variable ab
            String jsonString = new ActivityDataSource(hashMap).execute().get();
            Log.d("myMessage","Table-jsonString = "+jsonString);

            // Erstelle aus dem JSON-String ein JSONArray
            JSONArray jsonArray = new JSONArray(jsonString);
            orderId = 0;
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

                    orderId =  oneObject.getInt("order_id");
                    Log.d("myMessage",stringBuilder.toString());
                    // Wenn mindestens   noch ein Produkt vorhanden ist dann füge hinzu
                    if(( oneObject.getInt("product_count") - oneObject.getInt("product_paid")) != 0) {
                        adapter.insert(new Product(
                                oneObject.getInt("F_product_id"),
                                oneObject.getString("product_name"),
                                oneObject.getDouble("product_price"),
                                oneObject.getInt("product_count") - oneObject.getInt("product_paid"),
                                oneObject.getInt("order_id")), 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
