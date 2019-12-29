package application.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import application.Helper
import application.R
import questionnaire.Questionnaire


class GameOfflineSessions : Fragment() {

    private lateinit var views: View
    private lateinit var text: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.layout_game_offline_sessions, container, false)

        return views.apply {

            text = findViewById(R.id.text)

            val fileStream = resources.openRawResource(R.raw.json)

            val json = Helper.converting(fileStream)
            val questionnaire = Questionnaire.createQuestionnaire(json)


            text.append("\n$json\n")
            text.append(questionnaire.toString())

        }
    }


}
