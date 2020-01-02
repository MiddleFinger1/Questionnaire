package questionnaire.ui

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import application.R
import questionnaire.Settings


class SettingsHolder(val view: View): ViewHolder(view){

    lateinit var activity: AppCompatActivity
    private val titleSettings: TextView
    private val privacySettings: ImageView
    private val groupSettings: TextView
    private val markSettings: TextView

    init {
        view.apply {
            titleSettings = findViewById(R.id.Settings_Title)
            privacySettings = findViewById(R.id.Settings_Privacy)
            groupSettings = findViewById(R.id.Settings_Group)
            markSettings = findViewById(R.id.Settings_Mark)
        }
    }

    fun downloadSettings(settings: Settings){
        settings.apply {
            titleSettings.text = title
            groupSettings.text = group
            markSettings.text = mark.toString()
        }
        view.setOnClickListener {
            openQuestionnaire(activity, settings)
        }
    }

}