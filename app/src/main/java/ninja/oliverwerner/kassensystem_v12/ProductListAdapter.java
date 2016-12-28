package ninja.oliverwerner.kassensystem_v12;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

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
                number_Button.setText(""+item.getNumber());
            }
            number_Button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    yes_no_dialog(position);
                    int productid = getItem(position).getProductID();
                    String name = getItem(position).getName()+"".toString();
                    String sPri = getItem(position).getPrice()+"".toString();
                    String sBez = getItem(position).getNumber()+"".toString();
                    int  orderId = getItem(position).getOrderId();
                    int bez = Integer.parseInt(sBez.toString());
                    double dPri = Double.parseDouble(sPri.toString());
                    if (TableActivity.plus_minus.isChecked()) {
                        if( bez > 1) {
                            bez--;
                            remove(getItem(position));
                            insert(new Product(productid, name, dPri ,bez, orderId), position);

                            try {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("method", "updateOrder");
                                hashMap.put("orderID", orderId + "".toString());
                                hashMap.put("productID",productid+"".toString() );
                                hashMap.put("mp_operator", "-");
                                String jsonString = new ActivityDataSource(hashMap).execute().get();

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Log.d("DatabaseStatement", "i hope");

                        }
                        else{
                            // TODO: 19.12.2016 Datenbankanbindung erstellen mit dem befehl das das produkt komplett gelöscht wird. Absprache mit Oliver
                            builder.setMessage("You want to delete "+name);
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    } else {
                        bez++;
                        remove(getItem(position));
                        insert(new Product(productid, name, dPri ,bez, orderId), position);
                        try {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("method", "updateOrder");
                            hashMap.put("orderID", orderId + "".toString());
                            hashMap.put("productID", productid + "".toString());
                            hashMap.put("mp_operator", "+");
                            String jsonString = new ActivityDataSource(hashMap).execute().get();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        }


        return v;

    }
    public void yes_no_dialog(final int ipos){
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Title");
        builder.setMessage("You want to delete ");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                remove(getItem(ipos));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }
}
