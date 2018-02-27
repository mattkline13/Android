package com.ebookfrenzy.recognizinggesters

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private var mSensorManager : SensorManager ?= null
    private var mAccelerometer : Sensor ?= null
    private var mDetector: GestureDetector? = null
    private val DEBUG_TAG = "Gestures"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mDetector = GestureDetector(this, this)
        mDetector!!.setOnDoubleTapListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        textView.text = event!!.values.zip("XYZ".toList()).fold("") { acc, pair ->
            "$acc${pair.second}: ${pair.first}\n"
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        this.mDetector!!.onTouchEvent(event)
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event)
    }

    override fun onDown(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onDown: " + event.toString())
        return true
    }

    override fun onFling(event1: MotionEvent, event2: MotionEvent,
                velocityX: Float, velocityY: Float): Boolean {
        Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString())
        return true
    }

    override fun onDoubleTapEvent(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString())
        return true
    }

    override fun onShowPress(event: MotionEvent?) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString())
    }

    override fun onSingleTapUp(event: MotionEvent?): Boolean {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString())
        return true
    }

    override fun onScroll(event: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        Log.d(DEBUG_TAG, "onScroll: " + event.toString())
        return true
    }

    override fun onLongPress(event: MotionEvent?) {
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString())
    }

    override fun onDoubleTap(event: MotionEvent?): Boolean {
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString())
        return true
    }

    override fun onSingleTapConfirmed(event: MotionEvent?): Boolean {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString())
        return true
    }
    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(
                this,
                mAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        mSensorManager!!.unregisterListener(this)
    }
}
