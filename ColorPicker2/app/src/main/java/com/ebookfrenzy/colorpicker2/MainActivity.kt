package com.ebookfrenzy.colorpicker2

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {
    val FILENAME = "saved_colors.txt"
    var colorList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        seekBarRed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                redValue.setText(seekBarRed.progress.toString())
                surface.setBackgroundColor(Color.rgb(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress))
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        seekBarGreen.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                greenValue.setText(seekBarGreen.progress.toString())
                surface.setBackgroundColor(Color.rgb(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress))
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        seekBarBlue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                blueValue.setText(seekBarBlue.progress.toString())
                surface.setBackgroundColor(Color.rgb(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress))
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        redValue.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                seekBarRed.setProgress(redValue.text.toString().toInt())
                surface.setBackgroundColor(Color.rgb(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        greenValue.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                seekBarGreen.setProgress(greenValue.text.toString().toInt())
                surface.setBackgroundColor(Color.rgb(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        blueValue.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                seekBarBlue.setProgress(blueValue.text.toString().toInt())
                surface.setBackgroundColor(Color.rgb(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val builder = AlertDialog.Builder(this)

        return when (item.itemId) {
            R.id.action_save -> {
                var name:EditText?=null

                with (builder) {
                    setTitle("Save Color")
                    setMessage("Enter Color Name Below to Save")

                    name= EditText(context)
                    name!!.hint="Color Name"
                    name!!.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

                    setPositiveButton("OK") { dialog, whichButton ->
                        colorList.add("${name!!.text}, " + seekBarRed.progress + ", " + seekBarGreen.progress + ", " + seekBarBlue.progress)
                        dialog.dismiss()
                    }

                    setNegativeButton("NO") { dialog, whichButton -> dialog.dismiss() }
                }

                builder.create()
                builder.setView(name)
                builder.show()
                return true
            }

            R.id.action_recall -> {
                with (builder) {
                    setTitle("Recall Color")

                    setItems(colorList.toTypedArray(), DialogInterface.OnClickListener { dialog, which ->
                        var selectedColor = colorList.get(which)
                        var data = selectedColor.split(", ")
                        redValue.setText(data[1])
                        greenValue.setText(data[2])
                        blueValue.setText(data[3])
                    })

                    setPositiveButton("OK") { dialog, whichButton -> dialog.dismiss() }

                    setNegativeButton("NO") { dialog, whichButton -> dialog.dismiss() }
                }

                builder.create()
                builder.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()

        val f = File(filesDir, FILENAME)
        val output = f.printWriter()

        for (color in colorList) {
            output.println("$color")
        }

        output.flush()
        output.close()
    }

    override fun onResume() {
        super.onResume()

        val f = File(filesDir, FILENAME)
        val input = Scanner(f)

        while (input.hasNextLine()) {
            colorList.add(input.nextLine())
        }

        input.close()
    }
}
