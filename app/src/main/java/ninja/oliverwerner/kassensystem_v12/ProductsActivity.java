package ninja.oliverwerner.kassensystem_v12;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String prgrp_id;
    String prgrp_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductDialog();
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
        prgrp_id = intent.getStringExtra("pGroupID");
        loadProducts(intent.getStringExtra("pGroupID"));
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

    public void loadProducts(String pGroupID){
        ArrayList<Product> productsList = new ArrayList<Product>();

        try {
            // HashMap erstellen und Daten für die DB-Abfrage im Inneren speichern
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("method", "getProducts");
            hashMap.put("pGroupID", pGroupID);

            // Hole mir den Rückgabe-String und speicher ihn in einer Variable ab
            String jsonString = new ActivityDataSource(hashMap).execute().get();

            // Erstelle aus dem JSON-String ein JSONArray
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    // Hole aus dem JSONArray ein JSONObjekt und speicher die Daten in Variablen
                    JSONObject oneObject = jsonArray.getJSONObject(i);

                    setTitle(oneObject.getString("p_group_name"));
                    prgrp_name = (oneObject.getString("p_group_name"));



                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(oneObject.getString("product_name"));
                    stringBuilder.append(oneObject.getString("product_price"));
                    stringBuilder.append(oneObject.getString("product_id"));
                    stringBuilder.append(oneObject.getString("F_p_group_id"));

                    Log.d("myMessage",stringBuilder.toString());

                    productsList.add(new Product(
                            oneObject.getString("product_name"),
                            oneObject.getDouble("product_price"),
                            oneObject.getInt("product_id"),
                            oneObject.getInt("F_p_group_id")
                            ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ListView lvProducts = (ListView) findViewById(R.id.lvProducts);
        Log.d("myMessage","tableList.length()="+productsList.size());
        ProductListAdapter adapter = new ProductListAdapter(this, R.layout.product_list_item_layout, productsList); // TODO: 15.12.2016 Adapter testen
        lvProducts.setAdapter(adapter);
    }
    public void addProductDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_product);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editName = new EditText(this);
        final EditText editprice = new EditText(this);

        editName.setInputType(InputType.TYPE_CLASS_TEXT);
        editprice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editName.setHint("Produktname");
        editprice.setHint("Preis");
        layout.addView(editName);
        layout.addView(editprice);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String prName = editName.getText().toString();
                String prPrice = editprice.getText().toString();
                if(prName != "" && prPrice != ""){
                    try {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("method", "addProduct");
                        hashMap.put("productName", prName );
                        hashMap.put("productPrice", prPrice);
                        hashMap.put("productGroup", prgrp_name);
                        new ActivityDataSource(hashMap).execute().get();
                        //tewst

                        loadProducts(prgrp_id);
                    } catch (Exception e) {
                        Log.d("myMessage","addTableDialog - Exception: "+e.getMessage());
                        e.printStackTrace();
                    }
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
