package application

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import questionnaire.Questionnaire
import questionnaire.ui.PresentativeQuestionnaire
import questionnaire.ui.QuestionSession

class MainActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                textMessage.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        try {
            val fragment = PresentativeQuestionnaire()

            val questionnaire = Questionnaire("test")
            fragment.questionnaire = questionnaire

            supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
        }
        catch (ex: Exception) {
            Toast.makeText(baseContext, ex.toString(), Toast.LENGTH_LONG).show()
        }

    }
}
