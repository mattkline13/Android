package android.matt.flashbeepshake

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private var toggleFlash = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        TextField.text = getString(R.string.message_start)

        button_flash.setOnClickListener { flash() }

        button_beep.setOnClickListener { beep() }

        button_shake.setOnClickListener { shake() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_flash -> {
                flash()
                return true
            }
            R.id.action_beep -> {
                beep()
                return true
            }
            R.id.action_shake -> {
                shake()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun flash() {
        TextField.text = getString(R.string.message_flash)

        val camMan = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraList = camMan.cameraIdList
            val cameraID = cameraList[0]
            if (toggleFlash) {
                camMan.setTorchMode(cameraID, false)
                toggleFlash = true
            } else if (!toggleFlash) {
                camMan.setTorchMode(cameraID, true)
                toggleFlash = false
            }
        } catch (e: CameraAccessException) {
            Log.d("Camera Error: ", "No camera found")
        }

    }

    private fun beep() {
        TextField.text = getString(R.string.message_beep)
        val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        tone.startTone(ToneGenerator.TONE_DTMF_3,500)	//play specific tone for 600ms
    }

    private fun shake() {
        TextField.text = getString(R.string.message_shake)
        val vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibratorService.vibrate(500)
    }
}
