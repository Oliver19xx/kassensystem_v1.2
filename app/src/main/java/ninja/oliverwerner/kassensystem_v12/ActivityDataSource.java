package ninja.oliverwerner.kassensystem_v12;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

public class ActivityDataSource extends AsyncTask<String, Void, String> {

    // TODO: 02.12.2016 Azhentifikations-Schlüssel generieren, in der Datenbank speichern und in der strings.xml hinterlegen

    public static final String POST_PARAM_KEYVALUE_SEPARATOR = "=";
    public static final String POST_PARAM_SEPARATOR = "&";

    private URLConnection conn;
    private String LOG = "myMessage";
    private HashMap<String, String> params = null;

    public ActivityDataSource(HashMap<String, String> params) {
        this.params = params;
        this.params.put("authkey","test321");
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d(LOG,"doInBackground");
        try {
            openConnection();
            return readResult();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Öffnet eine Verbindung {@link URLConnection}.
     * @throws IOException
     */
    private void openConnection() throws IOException{
        Log.d(LOG,"openConnection");
        //StringBuffer für das zusammensetzen der URL
        StringBuffer dataBuffer = new StringBuffer();

        for (String key : this.params.keySet()){
            Log.d("myMessage","PostParam= "+key+"["+this.params.get(key)+"]");
            dataBuffer.append(URLEncoder.encode(key, "UTF-8"));
            dataBuffer.append(POST_PARAM_KEYVALUE_SEPARATOR);
            dataBuffer.append(URLEncoder.encode(this.params.get(key), "UTF-8"));
            dataBuffer.append(POST_PARAM_SEPARATOR);
        }

        //Adresse der PHP Schnittstelle für die Verbindung zur MySQL Datenbank
        URL url = new URL("http://oliverwerner.ninja/reader.php");
        conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(dataBuffer.toString());
        Log.d(LOG,"openConnection - OutputStream="+dataBuffer.toString());
        wr.flush();
    }

    /**
     * Ließt das Ergebnis aus der geöffneten Verbindung.
     * @return liefert ein String mit dem gelesenen Werten.
     * @throws IOException
     */
    private String readResult()throws IOException{
        Log.d(LOG,"readResult");
        String result = null;
        //Lesen der Rückgabewerte vom Server
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        //Solange Daten bereitstehen werden diese gelesen.
        while ((line = reader.readLine()) != null) {
            Log.d(LOG,"readResult() - while="+sb.toString());
            sb.append(line);
        }
        return sb.toString();
    }
}