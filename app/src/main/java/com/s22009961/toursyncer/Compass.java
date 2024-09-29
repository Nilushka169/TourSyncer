package com.s22009961.toursyncer;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
public class Compass extends AppCompatActivity implements SensorEventListener{
    TextView degreetxt; ImageView compassimg; SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        degreetxt = findViewById(R.id.tvDegrees);
        compassimg = findViewById(R.id.imgCompass);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);}
    @Override
    protected void onResume() {
        super.onResume();
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor s: sensorList){
            Log.d("Sensor",s.toString());}
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_NORMAL);}
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);}
    @Override
    public void onSensorChanged(SensorEvent event) {
        int degree = Math.round(event.values[0]);
        degreetxt.setText(degree+"º");
        compassimg.setRotation(-degree);}
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {    }
}