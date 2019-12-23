package questionnaire.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import application.R
import questionnaire.Questionnaire


class PresentativeQuestionnaire : Fragment() {

    lateinit var questionnaire: Questionnaire

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_presentive_questionnaire, container, false)
    }


}
