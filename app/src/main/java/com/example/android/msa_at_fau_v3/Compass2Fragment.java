package com.example.android.msa_at_fau_v3;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.SENSOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Compass2Fragment extends Fragment implements SensorEventListener{

    private ImageView mPointer;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private Sensor mRotationVector;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] rMatrix = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    TextView compassAccuracy2;
    TextView tvHeading2;
    private String status;
    private float[] orientation = new float[9];
    private int mAzimuth;
    public static TextView qiblaDirection;
    public static String compass="Compass";

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];

    public Compass2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compass2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvHeading2 = (TextView) view.findViewById(R.id.tvHeading2);
        compassAccuracy2 = (TextView) view.findViewById(R.id.compassAccuracy2);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPointer = (ImageView) view.findViewById(R.id.imageViewCompass2);

        qiblaDirection = (TextView) view.findViewById(R.id.qiblaDirection2);
        fetchData process = new fetchData(compass);
        process.execute();
    }

    public void onResume() {
        super.onResume();
        if(mRotationVector!=null){
            mSensorManager.registerListener(this,mRotationVector, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else if(mRotationVector==null){
            if(mAccelerometer!=null && mMagnetometer!=null){
                mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
            else{
                compassAccuracy2.setText("SORRY! COMPASS IS NOT SUPPORTED ON THIS PHONE");
            }
        }

        //SensorManager.SENSOR_DELAY_NORMAL
        //1000000
    }

    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mRotationVector);
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    private float[] lowPass(float[] input, float[] output) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + 0.35f * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.accuracy==SensorManager.SENSOR_STATUS_ACCURACY_LOW){
            status="LOW!";
        }
        else if(event.accuracy==SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM){
            status="Average";
        }
        else if(event.accuracy==SensorManager.SENSOR_STATUS_ACCURACY_HIGH){
            status="High";
        }
        else{
            status="Unreliable";
        }
        compassAccuracy2.setText("Compass Accuracy: " + status);

        //add code for TYPE_ROTATION_VECTOR
        if(event.sensor==mRotationVector){
            SensorManager.getRotationMatrixFromVector(rMatrix, event.values);
            mAzimuth = (int) ((Math.toDegrees(SensorManager.getOrientation(rMatrix, orientation)[0])+360)%360);
            float degrees = Math.round(mAzimuth);

            tvHeading2.setText("Heading: " + Float.toString(degrees) + " degrees");

            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -degrees,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(210);

            ra.setFillAfter(true);

            mPointer.startAnimation(ra);
            mCurrentDegree = -degrees;
        }
        else{
            if (event.sensor == mAccelerometer) {
                //System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
                lowPass(event.values, mGravity);
                mLastAccelerometerSet = true;
            }
            else if (event.sensor == mMagnetometer) {
                //System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
                lowPass(event.values, mGeomagnetic);
                mLastMagnetometerSet = true;
            }

            if (mLastAccelerometerSet && mLastMagnetometerSet) {
                SensorManager.getRotationMatrix(mR, null, mGravity, mGeomagnetic);
                SensorManager.getOrientation(mR, mOrientation);
                float azimuthInRadians = mOrientation[0];
                float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
                float degrees = Math.round(azimuthInDegress);

                tvHeading2.setText("Heading: " + Float.toString(degrees) + " degrees");

                RotateAnimation ra = new RotateAnimation(
                        mCurrentDegree,
                        -degrees,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);

                ra.setDuration(210);

                ra.setFillAfter(true);

                mPointer.startAnimation(ra);
                mCurrentDegree = -degrees;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

}
