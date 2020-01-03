package com.chenjian.android_sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static String TAG = "SENSOR_DEMO";

    private TextView ms_sensor;

    private SensorManager sm;

    private TextView ms_x,ms_y,ms_z;

    private Sensor mSensorOrientation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ms_sensor = (TextView) findViewById(R.id.ms_sensor);

        List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sb = new StringBuilder();

        sb.append("你手机上有以下 " + allSensors.size() + " 个传感器有：\n\n");

        for (Sensor s:allSensors){
            switch (s.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    sb.append(s.getType() + " 加速度传感器(Accelerometer sensor)" + "\n");
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    sb.append(s.getType() + " 陀螺仪传感器(Gyroscope sensor)" + "\n");
                    break;
                case Sensor.TYPE_LIGHT:
                    sb.append(s.getType() + " 光线传感器(Light sensor)" + "\n");
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sb.append(s.getType() + " 磁场传感器(Magnetic field sensor)" + "\n");
                    break;
                case Sensor.TYPE_ORIENTATION:
                    sb.append(s.getType() + " 方向传感器(Orientation sensor)" + "\n");
                    break;
                case Sensor.TYPE_PRESSURE:
                    sb.append(s.getType() + " 气压传感器(Pressure sensor)" + "\n");
                    break;
                case Sensor.TYPE_PROXIMITY:
                    sb.append(s.getType() + " 距离传感器(Proximity sensor)" + "\n");
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    sb.append(s.getType() + " 温度传感器(Temperature sensor)" + "\n");
                    break;
                default:
                    sb.append(s.getType() + " 其他传感器" + "\n");
                    break;
            }
            sb.append("设备名称：" + s.getName() + "\n设备版本：" + s.getVersion() + "\n供应商："
                    + s.getVendor() + "\n\n");
        }
        //ms_sensor.setText(sb.toString());
        Log.v(TAG,sb.toString());

        // 获取方向传感器
        mSensorOrientation = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        //注册数值变化监听器
        sm.registerListener(this, mSensorOrientation,SensorManager.SENSOR_DELAY_UI);

        ms_x = (TextView) findViewById(R.id.ms_x);
        ms_y = (TextView) findViewById(R.id.ms_y);
        ms_z = (TextView) findViewById(R.id.ms_z);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        ms_x.setText("方位角：" + (float) (Math.round(event.values[0] * 100)) / 100);
        ms_y.setText("倾斜角：" + (float) (Math.round(event.values[1] * 100)) / 100);
        ms_z.setText("滚动角：" + (float) (Math.round(event.values[2] * 100)) / 100);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
