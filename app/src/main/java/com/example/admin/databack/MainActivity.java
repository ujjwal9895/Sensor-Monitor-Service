package com.example.admin.databack;

import android.app.DownloadManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mGravity, mGyroscope, mLight;
    private Sensor mLinearAcceleration, mMagneticField, mPressure;
    private Sensor mProximity, mHumidity, mRotationVector, mAmbientTemperature;
    private float acc, gra, gyr, lig, lin, mag, pre, pro, hum, rot, amb;
    private int[] sen = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double latitude, longitude;

    protected LocationManager locationManager;
    protected LocationListener locationListener;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        LoadSensors();
    }

    public void LoadSensors() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAmbientTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        mRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        SendData sendData = new SendData();

        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER && sen[0] == 0) {
            acc = event.values[0];
            sen[0] = 1;
            sendData.execute(0);
        }
        else if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE && sen[1] == 0) {
            amb = event.values[0];
            sen[1] = 1;
            sendData.execute(1);
        }
        else if (sensor.getType() == Sensor.TYPE_GRAVITY && sen[2] == 0) {
            gra = event.values[0];
            sen[2] = 1;
            sendData.execute(2);
        }
        else if (sensor.getType() == Sensor.TYPE_GYROSCOPE && sen[3] == 0) {
            gyr = event.values[0];
            sen[3] = 1;
            sendData.execute(3);
        }
        else if (sensor.getType() == Sensor.TYPE_LIGHT && sen[4] == 0) {
            lig = event.values[0];
            sen[4] = 1;
            sendData.execute(4);
        }
        else if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && sen[5] == 0) {
            lin = event.values[0];
            sen[5] = 1;
            sendData.execute(5);
        }
        else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && sen[6] == 0) {
            mag = event.values[0];
            sen[6] = 1;
            sendData.execute(6);
        }
        else if (sensor.getType() == Sensor.TYPE_PRESSURE && sen[7] == 0) {
            pre = event.values[0];
            sen[7] = 1;
            sendData.execute(7);
        }
        else if (sensor.getType() == Sensor.TYPE_PROXIMITY && sen[8] == 0) {
            pro = event.values[0];
            sen[8] = 1;
            sendData.execute(8);
        }
        else if (sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY && sen[9] == 0) {
            hum = event.values[0];
            sen[9] = 1;
            sendData.execute(9);
        }
        else if (sensor.getType() == Sensor.TYPE_ROTATION_VECTOR && sen[10] == 0) {
            rot = event.values[0];
            sen[10] = 1;
            sendData.execute(10);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mPressure,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mHumidity, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mRotationVector, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAmbientTemperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //Log.v("Location", latitude + " " + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class SendData extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            OkHttpClient client = new OkHttpClient();

            try {
                String[] val = new String[11];

                for (int i = 0; i <= 10; i++) {
                    val[i] = "0";
                }

                if (params[0] == 0) {
                    val[0] = "" + acc;
                }
                else if (params[0] == 1) {
                    val[1] = "" + amb;
                }
                else if (params[0] == 2) {
                    val[2] = "" + gra;
                }
                else if (params[0] == 3) {
                    val[3] = "" + gyr;
                }
                else if (params[0] == 4) {
                    val[4] = "" + lig;
                }
                else if (params[0] == 5) {
                    val[5] = "" + lin;
                }
                else if (params[0] == 6) {
                    val[6] = "" + mag;
                }
                else if (params[0] == 7) {
                    val[7] = "" + pre;
                }
                else if (params[0] == 8) {
                    val[8] = "" + pro;
                }
                else if (params[0] == 9) {
                    val[9] = "" + hum;
                }
                else if (params[0] == 10) {
                    val[10] = "" + rot;
                }

                while (latitude == 0.0);

                String json = "{ \"latitude\" : " + latitude + "," + " \"longitude\" : " + longitude + ","
                        + "\"acceleration\" : " + val[0] + "," + " \"ambient\" : " + val[1] + "," +
                        " \"gravity\" : " + val[2] + "," + " \"gyration\" : " + val[3] + "," +
                        " \"light\" : " + val[4] + "," + " \"linear\" : " + val[5] + "," +
                        " \"magnetic\" : " + val[6] + "," + " \"pressure\" : " + val[7] + "," +
                        " \"proximity\" : " + val[8] + "," + " \"humidity\" : " + val[9] + "," +
                        " \"rotation\" : " + val[10] + " }";

                //Log.v("JSON", json);

                RequestBody body = new FormBody.Builder()
                        .add("data", json)
                        .build();

                String url = "http://192.168.0.103/softlab/index.php";

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                //Log.v("response", response.body().string());
            }
            catch (Exception e) {
                Log.v("Exception", e + "");
            }

            return null;
        }
    }
}
