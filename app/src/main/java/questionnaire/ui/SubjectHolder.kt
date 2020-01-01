package questionnaire.ui

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.widget.TextView
import android.widget.Toast
import application.R
import questionnaire.game.Subject

class SubjectHolder(val view: View): ViewHolder(view) {

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
        }
        catch (ex: Exception) {
            Toast.makeText(activity.baseContext, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }

}