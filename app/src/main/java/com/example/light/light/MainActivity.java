package com.example.light.light;

import com.example.light.light.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;


public class MainActivity extends Activity {

    private static final String DEVICE_ADDRESS =  "00:12:11:30:16:64"; //"00:06:66:03:73:7B";


    private ArduinoReceiver arduinoReceiver = new ArduinoReceiver();
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(arduinoReceiver, new IntentFilter(AmarinoIntent.ACTION_CONNECT));
        registerReceiver(arduinoReceiver, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));
        registerReceiver(arduinoReceiver, new IntentFilter(AmarinoIntent.ACTION_SEND));


        // this is how you tell Amarino to connect to a specific BT device from within your own code
        Amarino.connect(this, DEVICE_ADDRESS);

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
    public class ArduinoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = null;

            // the device address from which the data was sent, we don't need it here but to demonstrate how you retrieve it
            //final String address = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);

            // the type of data which is added to the intent
            final int dataType = intent.getIntExtra(AmarinoIntent.EXTRA_DATA_TYPE, -1);

            // we only expect String data though, but it is better to check if really string was sent
            // later Amarino will support differnt data types, so far data comes always as string and
            // you have to parse the data to the type you have sent from Arduino, like it is shown below
            if (dataType == AmarinoIntent.STRING_EXTRA){
                data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);

                if (data != null){

                    try {

                        //ACTION

                        // since we know that our string value is an int number we can parse it to an integer
                        String sensorReading = String.valueOf(data);
                        View led = findViewById(R.id.view1);

                        if(sensorReading.equals("red"))
                        {
                            led.setBackgroundColor(Color.RED);

                        }else if(sensorReading.equals("green"))
                        {
                            led.setBackgroundColor(Color.GREEN);
                        }

                        TextView b = (TextView)findViewById(R.id.textView1);

                        b.setText(data);


                    }
                    catch (NumberFormatException e) { /* oh data was not an integer */ }
                }
            }
        }
    }
}
