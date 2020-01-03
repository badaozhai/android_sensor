package com.chenjian.android_sensor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static String TAG = "SENSOR_DEMO";

    private TextView ms_sensor;

    private SensorManager sm ;

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
        // TYPE_ORIENTATION     3
        mSensorOrientation = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
//        //注册数值变化监听器
//        sm.registerListener(this, mSensorOrientation,SensorManager.SENSOR_DELAY_UI);

        // 获取加速度传感器
        // TYPE_ACCELEROMETER       1
//        mSensorOrientation = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //陀螺仪传感器的类型是
        // Sensor.TYPE_GYROSCOPE      4
//        mSensorOrientation = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //注册数值变化监听器
        sm.registerListener(this, mSensorOrientation,SensorManager.SENSOR_DELAY_UI);

        ms_x = (TextView) findViewById(R.id.ms_x);
        ms_y = (TextView) findViewById(R.id.ms_y);
        ms_z = (TextView) findViewById(R.id.ms_z);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onSensorChanged(SensorEvent event) {

        //数据的精度
        int accuracy = event.accuracy;//精度
        Log.v(TAG,"数据精度:"+accuracy);
        //产生数据的传感器
        Sensor ss = event.sensor;
        Log.v(TAG,"传感器:"+ss.toString());
        Log.v(TAG,"传感器类型:"+ss.getStringType());
        // 产生数据时的时间戳
        long timestamp = event.timestamp;
        Log.v(TAG,"时间戳:"+timestamp);

        float[] fArr5 = new float[9];
        float[] fArr6 = new float[3];
        SensorManager.getOrientation(fArr5, fArr6);

        // 以及传感器记录的新的数据。
        Log.v(TAG,"值1：" + (float) (Math.round(event.values[0] * 100)) / 100);
        ms_x.setText("值1：" + (float) (Math.round(event.values[0] * 100)) / 100);

        Log.v(TAG,"值2：" + (float) (Math.round(event.values[1] * 100)) / 100);
        ms_y.setText("值2：" + (float) (Math.round(event.values[1] * 100)) / 100);

        Log.v(TAG,"值3：" + (float) (Math.round(event.values[2] * 100)) / 100);
        ms_z.setText("值3：" + (float) (Math.round(event.values[2] * 100)) / 100);


    }
    //向量求模
    public double magnitude(float x, float y, float z) {
        double magnitude = 0;
        magnitude = Math.sqrt(x * x + y * y + z * z);
        return magnitude;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sm.unregisterListener(this);
    }
}
