package com.ebookfrenzy.fragmentexample

import android.support.v4.app.FragmentActivity
import android.os.Bundle

class FragmentExampleActivity : FragmentActivity(), ToolbarFragment.ToolbarListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_example)
    }

    override fun onButtonClick(fonrsize: Int, text: String) {
        val textFragment = supportFragmentManager.findFragmentById(R.id.text_fragment) as TextFragment

        textFragment.changeTextProperties(fonrsize, text)
    }
}
