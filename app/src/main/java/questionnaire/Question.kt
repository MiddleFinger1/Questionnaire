package questionnaire

import java.io.File

class Question(var question: String, val statements: Statements, var truth: String) {

    var decription = ""
    var jsonObject = ""
    var icon: File? = null
    var type = ON_DEFAULT

    init {

    }

    companion object {

        const val ON_DEFAULT = 0
        const val ON_TIME = 1

        fun createQuestion(json: String): Question? {

            return null
        }

    }

}