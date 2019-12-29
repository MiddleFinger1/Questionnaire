package questionnaire.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import application.R

class ActivityQuestionnaire : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)


    }

}
