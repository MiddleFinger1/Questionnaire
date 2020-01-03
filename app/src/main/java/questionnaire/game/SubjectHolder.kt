package questionnaire.game

import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.widget.TextView
import android.widget.Toast
import application.Helper
import application.R
import org.json.simple.JSONObject
import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser
import questionnaire.Settings
import questionnaire.ui.QuestionnaireGroup


class SubjectHolder(val view: View): ViewHolder(view){

    lateinit var activity: AppCompatActivity
    private val titleSubject: TextView
    private val keysCount: TextView
    private val countCompleted: TextView

    init {
        view.apply {
            titleSubject = findViewById(R.id.Subject_Title)
            keysCount = findViewById(R.id.Subject_KeysCount)
            countCompleted = findViewById(R.id.Subject_CountCompletes)
        }
    }

    fun downloadResources(subject: Subject){
        try {
            titleSubject.text = subject.subject
            val input = activity.assets.open(subject.background)
            val drawable = Drawable.createFromStream(input, null)
            view.background = drawable
            view.setOnClickListener{
                try{
                    val json = Helper.converting(activity.assets.open(subject.path))
                    val jsonObject = JSONParser().parse(json) as JSONObject
                    val jsonArray = jsonObject["settings"] as JSONArray
                    val settings = arrayListOf<Settings>()
                    for (item in jsonArray)
                        settings.add(Settings.createSettings(item.toString()))

                    val fragment = QuestionnaireGroup()
                    fragment.activity = activity
                    fragment.settings = settings
                    activity.supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
                }
                catch (ex: Exception){
                    Toast.makeText(activity, ex.stackTrace.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
        catch (ex: Exception) {
            Toast.makeText(activity.baseContext, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }

}