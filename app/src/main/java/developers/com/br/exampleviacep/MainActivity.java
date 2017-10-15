package developers.com.br.exampleviacep;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    static GoogleMap googleMap;
    CEPAdapter cepAdapter;
    RecyclerView recyclerView;
    static List<JSONObject> jsonObjectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        cepAdapter = new CEPAdapter(this, jsonObjectList, callbackmap);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cepAdapter);

        fillRecycler();

        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        LatLng latlng = new LatLng(-19.9364375, -43.9685697);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.0f));
    }

    private void fillRecycler() {

        String strArrayCep = getIntent().getStringExtra("CEP");

        if (strArrayCep != null) {

            try {

                JSONObject jsonObject = new JSONObject(strArrayCep);

                cepAdapter.addCep(jsonObject);

            } catch (JSONException joe) {

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    CEPAdapter.callbackmap callbackmap = new CEPAdapter.callbackmap() {
        @Override
        public void setPosition(String data) {

            LatLng latlng = new LatLng(-19.9364375, -43.9685697);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.0f));
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
