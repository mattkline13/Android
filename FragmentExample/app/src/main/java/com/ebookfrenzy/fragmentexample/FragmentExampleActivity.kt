package com.ebookfrency.fragmentexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class FragmentExampleActivity : AppCompatActivity(), ToolbarFragment.ToolbarListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_example)
    }

    override fun onButtonClick(fontsize: Int, text: String) {

        val textFragment = supportFragmentManager.findFragmentById(R.id.text_fragment) as TextFragment
        textFragment.changeTextProperties(fontsize,text)
    }
}
