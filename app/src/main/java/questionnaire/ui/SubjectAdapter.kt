package questionnaire.ui

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import application.R
import questionnaire.game.Subject


class SubjectAdapter: Adapter<ViewHolder>() {

    lateinit var activity: AppCompatActivity
    lateinit var subjects: ArrayList<Subject>
    private lateinit var layoutInflater: LayoutInflater

    override fun getItemCount() = subjects.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is SubjectHolder) {
            holder.activity = activity
            holder.downloadResources(subjects[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.subject_card_view, parent, false)
        return SubjectHolder(view)
    }

}