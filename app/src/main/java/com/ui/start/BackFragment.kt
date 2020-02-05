package com.ui.start

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import com.R
import com.ui.MainActivity


class BackFragment : Fragment() {

    private lateinit var buttonEnter: Button
    private lateinit var buttonExit: Button
    private lateinit var iconLaunch: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val views = inflater.inflate(R.layout.fragment_back, container, false)
        views.apply {
            buttonEnter = findViewById(R.id.Start_EnterToApp)
            buttonExit = findViewById(R.id.Start_ExitFromAcc)
            iconLaunch = findViewById(R.id.Start_IconLaunch)
        }

        val appearImage = AnimationUtils.loadAnimation(context, R.anim.appear_icon)

        iconLaunch.startAnimation(appearImage)
        buttonEnter.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
        }
        buttonExit.animation = null
        buttonExit.setOnClickListener {
            val fragment = AuthFragment()
            if (activity != null)
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.MainStartActivity, fragment).commit()
        }

        return views
    }

}
