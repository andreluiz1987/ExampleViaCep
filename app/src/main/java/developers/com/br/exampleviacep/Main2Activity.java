package developers.com.br.exampleviacep;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main2Activity extends AppCompatActivity {

    interface ICallback {
        void backCep(JSONObject array);
    }

    private EditText edtCep;
    private Button btnSend;
    private String strUrl = "https://viacep.com.br/ws/[CEP]/json/unicode/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        edtCep = (EditText) findViewById(R.id.edtCep);
        btnSend = (Button) findViewById(R.id.btnSendCep);

        btnSend.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (edtCep.getText().toString().length() <= 0) {
                Toast.makeText(Main2Activity.this, "Informe o cep", Toast.LENGTH_SHORT).show();
            } else {
                new getCEPAsync(edtCep.getText().toString(), iCallback).execute();
            }
        }
    };

    ICallback iCallback = new ICallback() {
        @Override
        public void backCep(JSONObject array) {

            Intent intent = new Intent(Main2Activity.this, MainActivity.class);
            intent.putExtra("CEP", array.toString());
            startActivity(intent);
        }
    };

    private class getCEPAsync extends AsyncTask<Void, Void, String> {

        ICallback callback;
        String numCEP;

        public getCEPAsync(String numCep, ICallback callback) {
            this.numCEP = numCep;
            this.callback = callback;
        }

        //Metodo Asincrono para fazer a chamada da API
        @Override
        protected String doInBackground(Void... voids) {

            try {

                String response = request(strUrl.replace("[CEP]", numCEP));
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) return;

            try {
                JSONObject array = new JSONObject(s);

                if (callback != null) {
                    callback.backCep(array);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String request( String uri ) throws Exception {

        URL url = new URL( uri );
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader r = new BufferedReader(new InputStreamReader(in));

        StringBuilder jsonString = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            jsonString.append(line);
        }

        urlConnection.disconnect();

        return jsonString.toString();
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return "";
    }
}
