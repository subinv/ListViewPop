package com.example.subinv.listviewpop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ArrayList<HashMap<String,String>> list;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView=(ListView)findViewById(R.id.list_name);
        list=new ArrayList<>();
        listView.setOnItemClickListener(this);
        simpleAdapter=new SimpleAdapter(this,list,R.layout.list_view_layout,new String[]{"src","eqid","timedate","lat",
                "lon","magnitude","depth","region"},
                new int[]{R.id.src_holder,R.id.eqid_holder,R.id.time_date_holder,R.id.latitude_holder,R.id.longitude_holder,
                        R.id.magnitude_holder,R.id.depth_holder,R.id.region_holder});
        listView.setAdapter(simpleAdapter);
        WebService webServicePost=new WebService(this);
        webServicePost.execute("http://www.seismi.org/api/eqs/2011/03?min_magnitude=6:");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,MapView.class);
        intent.putExtra("src",list.get(position).get("src"));
        intent.putExtra("eqid",list.get(position).get("eqid"));
        intent.putExtra("timedate",list.get(position).get("timedate"));
        intent.putExtra("magnitude",list.get(position).get("magnitude"));
        intent.putExtra("lat",list.get(position).get("lat"));
        intent.putExtra("lon",list.get(position).get("lon"));
        intent.putExtra("depth",list.get(position).get("depth"));
        startActivity(intent);
    }

    private class WebService extends AsyncTask<String,Void,String>{
        private ProgressDialog progressDialog;

        public WebService(Context context){
            progressDialog=ProgressDialog.show(context,"Loading","Connecting to Server");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Fetching");
        }

        @Override
        protected String doInBackground(String... strings) {
            progressDialog.setMessage("Loading Data Please Wait....");
            String url=strings[0];
            StringBuilder stringBuilder=new StringBuilder();
            try {
                URL reqUrl=new URL(url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)reqUrl.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"),
                        2*1024);
                String line;
                while ((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line).append("\n");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error because 1"+e,Toast.LENGTH_LONG).show();
                Log.e("error","1 "+e);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error because 2"+e,Toast.LENGTH_LONG).show();
                Log.e("error", "2 " + e);
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject=new JSONObject(s);
                JSONArray jsonArray=jsonObject.getJSONArray("earthquakes");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=new JSONObject(jsonArray.getJSONObject(i).toString());
                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("src",jsonObject1.getString("src"));
                    hashMap.put("eqid",jsonObject1.getString("eqid"));
                    hashMap.put("timedate",jsonObject1.getString("timedate"));
                    hashMap.put("lat",jsonObject1.getString("lat"));
                    hashMap.put("lon",jsonObject1.getString("lon"));
                    hashMap.put("magnitude",jsonObject1.getString("magnitude"));
                    hashMap.put("depth",jsonObject1.getString("depth"));
                    hashMap.put("region", jsonObject1.getString("region"));
                    list.add(hashMap);
                    simpleAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error because "+e,Toast.LENGTH_LONG).show();
                Log.e("error", "" + e);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
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
