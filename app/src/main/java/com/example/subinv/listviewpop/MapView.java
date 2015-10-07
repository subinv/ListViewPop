package com.example.subinv.listviewpop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapView extends AppCompatActivity {
    private LatLng location;
    private GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        Bundle bundle=getIntent().getExtras();
        TextView src=(TextView)findViewById(R.id.src_holder);
        TextView eqid=(TextView)findViewById(R.id.eqid_holder);
        TextView depth=(TextView)findViewById(R.id.depth_holder);
        TextView magnitude=(TextView)findViewById(R.id.magnitude_holder);
        TextView latitude=(TextView)findViewById(R.id.latitude_holder);
        TextView longitude=(TextView)findViewById(R.id.longitude_holder);
        TextView timedate=(TextView)findViewById(R.id.time_date_holder);
        src.setText(bundle.getString("src"));
        eqid.setText(bundle.getString("eqid"));
        depth.setText(bundle.getString("depth"));
        magnitude.setText(bundle.getString("magnitude"));
        latitude.setText(bundle.getString("lat"));
        longitude.setText(bundle.getString("lon"));
        timedate.setText(bundle.getString("timedate"));
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
        location=new LatLng(Double.parseDouble(latitude.getText().toString().trim()),Double.parseDouble(longitude.getText().toString().trim()));
        Toast.makeText(this,"location" +location,Toast.LENGTH_LONG).show();
    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
           googleMap.addMarker(new MarkerOptions().position(location).title("Find it Here"));

            CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(location, 16);
            googleMap.animateCamera(cameraUpdate);

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_view, menu);
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
}
