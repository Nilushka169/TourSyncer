package com.s22009961.toursyncer;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class Temperature extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager; private Sensor temperatureSensor; private TextView temperatureTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        this.getWindow().setFlags(WindowManager.LayoutParams.
                FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        temperatureTextView = findViewById(R.id.temperatureTextView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);}
        if (temperatureSensor == null) {
            temperatureTextView.setText("Temperature sensor not available");}}
    @Override
    protected void onResume() {super.onResume();
        if (temperatureSensor != null) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);}}
    @Override
    protected void onPause() {
        super.onPause();
        if (temperatureSensor != null) {sensorManager.unregisterListener(this);}}
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {float temperature = event.values[0];
            temperatureTextView.setText("Temperature: " + temperature + " °C");}}
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}}