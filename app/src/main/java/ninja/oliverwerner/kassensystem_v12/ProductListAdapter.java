package ninja.oliverwerner.kassensystem_v12;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Oliver on 12.12.2016.
 */

public class ProductListAdapter extends ArrayAdapter<Product> {
    private List<Product> items;
    private int layoutResourceId;
    private Context context;
    private View v;
    public AlertDialog.Builder builder;


    public ProductListAdapter(Context context, int layoutResourceId, List<Product> items){
        super(context, layoutResourceId,  items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        v = convertView;

        if(v == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(layoutResourceId, null);
        }

        Product item = getItem(position);

        if (item != null){
            TextView product_name = (TextView) v.findViewById(R.id.product_name);
            TextView product_price = (TextView) v.findViewById(R.id.product_price);
            Button number_Button = (Button) v.findViewById(R.id.number_Button);

            if (product_name != null){
                product_name.setText(item.getName());
            }

            if (product_price != null){
                product_price.setText(""+item.getPrice());
            }

            if (number_Button != null){
                if(getItem(position).getTableId() > 0){
                    number_Button.setText(""+item.getNumber());
                }else if (ProductGroupsActivity.table_id != 0) {
                    number_Button.setBackgroundResource(android.R.drawable.ic_input_add);
                }else{
                    number_Button.setBackgroundResource(android.R.drawable.ic_menu_edit);
                }
            }
            number_Button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (getItem(position).getTableId() > 0) {
                        int productid = getItem(position).getProductID();
                        String name = getItem(position).getName() + "".toString();
                        String sPri = getItem(position).getPrice() + "".toString();
                        String sBez = getItem(position).getNumber() + "".toString();
                        int tableid = getItem(position).getTableId();
                        yes_no_dialog(position,tableid, productid);
                        int bez = Integer.parseInt(sBez.toString());
                        double dPri = Double.parseDouble(sPri.toString());
                        if (TableActivity.plus_minus.isChecked()) {
                            if (bez > 1) {
                                bez--;
                                remove(getItem(position));
                                insert(new Product(productid, name, dPri, bez, tableid), position);

                                try {
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("method", "updateOrder");
                                    hashMap.put("tableID", tableid + "".toString());
                                    hashMap.put("productID", productid + "".toString());
                                    hashMap.put("mp_operator", "-");
                                    new ActivityDataSource(hashMap).execute().get();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                builder.setMessage("You want to delete " + name);
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } else {
                            bez++;
                            remove(getItem(position));
                            insert(new Product(productid, name, dPri, bez, tableid), position);
                            try {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("method", "updateOrder");
                                hashMap.put("tableID", tableid + "".toString());
                                hashMap.put("productID", productid + "".toString());
                                hashMap.put("mp_operator", "+");
                                new ActivityDataSource(hashMap).execute().get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }else if(ProductGroupsActivity.table_id != 0){
                        int productid = getItem(position).getProductID();
                        try {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("method", "updateOrder");
                            hashMap.put("tableID", ProductGroupsActivity.table_id + "".toString());
                            hashMap.put("productID", productid + "".toString());
                            hashMap.put("mp_operator", "+");
                            new ActivityDataSource(hashMap).execute().get();
                        } catch (Exception e) {
                            Log.d("testtest",e+"");
                            e.printStackTrace();
                        }
                        Snackbar.make(view, "Produkt hinzugefügt", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }else{
                        int productid = getItem(position).getProductID();
                        String name = getItem(position).getName() + "".toString();
                        String sPri = getItem(position).getPrice() + "".toString();
                        int sGroup = getItem(position).getProductGroup();
                        Log.d("testtest", productid + " " + name + " " + sPri + " " + position);
                        changeProduct(productid, name, sPri, position, sGroup);
                    }
                }
            });
        }


        return v;

    }
    public void yes_no_dialog(final int ipos, final int tableid, final int productid){
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Title");
        builder.setMessage("You want to delete ");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                remove(getItem(ipos));

                try {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("method", "updateOrder");
                    hashMap.put("tableID", tableid + "".toString());
                    hashMap.put("productID", productid + "".toString());
                    hashMap.put("mp_operator", "-");
                    new ActivityDataSource(hashMap).execute().get();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });
    }

    public void changeProduct(final int pr_id, final String name , final String price, final int pos, final int pgroup){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(R.string.change_product);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editName = new EditText(context);
        final EditText editprice = new EditText(context);

        editName.setInputType(InputType.TYPE_CLASS_TEXT);
        editprice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editName.setHint(name);
        editprice.setHint(price);
        layout.addView(editName);
        layout.addView(editprice);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton(R.string.save_product, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String prName = editName.getText().toString()+"";
                String prPrice = editprice.getText().toString()+"";
                if(!prName.equalsIgnoreCase("") || !prPrice.equalsIgnoreCase("")){
                    if (prName.equalsIgnoreCase("")){ prName = name;}
                    if (prPrice.equalsIgnoreCase("")){ prPrice = price;}
                    try {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("method", "editProduct");
                        hashMap.put("productID", pr_id+"".toString());
                        hashMap.put("productName", prName+"".toString());
                        hashMap.put("productPrice", prPrice+"".toString());

                        // Hole mir den Rückgabe-String und speicher ihn in einer Variable ab
                        String jsonString = new ActivityDataSource(hashMap).execute().get();
                        JSONObject oneObject = new JSONObject(jsonString).getJSONObject("data");

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(oneObject.getInt("product_id"));

                        int productid =  oneObject.getInt("product_id");
                        remove(getItem(pos));
                        insert(new Product(prName,Double.parseDouble(prPrice),productid,pgroup), pos);
//<3
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.d("testtest", "änderen " + prName+ " " + prPrice );
                }
            }
        });
        builder.setNeutralButton(R.string.delete_product, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("method", "removeProduct");
                    hashMap.put("productID", pr_id+"".toString());

                    // Hole mir den Rückgabe-String und speicher ihn in einer Variable ab
                     new ActivityDataSource(hashMap).execute().get();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                remove(getItem(pos));
                dialog.cancel();
            }
        });

        builder.setNegativeButton(R.string.abort_product, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }
}
