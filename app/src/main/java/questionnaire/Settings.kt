package questionnaire


class Settings: JsonObject {

    var mark: Double = 0.0
    private set(value) = Unit
    var privacy = PUBLIC

    companion object {
        const val PUBLIC = 0
        const val PRIVATE = 1
    }

    private val marks = arrayListOf<Double>()

    fun addMark(mark: Double){
        var commonMark = 0.0
        for (existMark in marks)
            commonMark += existMark
        commonMark += mark
        this.mark = mark / (marks.size + 1)
    }

    override fun toJsonObject(): String {
        return ""
    }
}