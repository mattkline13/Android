package android.matt.colorpicker3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.graphics.ColorUtils
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var blendedColor: Int = 0
    var colorOne: Int = 0
    var colorTwo: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blendColor(progress / 100f)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun blendColor(percentage: Float) {
        blendedColor = ColorUtils.blendARGB(colorOne, colorTwo, percentage)
        surfaceView.setBackgroundColor(blendedColor)
    }

    fun sendIntent(view : View, requestCode: Int, extras: String){
        val launchColorPicker = packageManager.getLaunchIntentForPackage("com.example.hamzakhokhar.colorpicker")  as Intent
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.putExtra(extras,requestCode)
        startActivityForResult(intent, requestCode)
    }

    fun firstColor (view : View){
        sendIntent(view, 1, "ColorOne")
    }

    fun secondColor(view : View){
        sendIntent(view, 2, "ColorTwo")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data?.extras != null){
            colorOne = data!!.getIntExtra("Color", 0)
            colorA.setBackgroundColor(colorOne)
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data?.extras != null){
            colorTwo = data!!.getIntExtra("Color", 0)
            colorB.setBackgroundColor(colorTwo)
        }
    }
}

