package questionnaire


class Statements: ArrayList<String>(){

    var groupsType = SINGLE

    companion object {

        const val SINGLE = 0
        const val MULTI = 1
        const val ENTER = 2
    }


}