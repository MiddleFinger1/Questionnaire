package com.logic

import android.os.CountDownTimer
import android.util.Log
import com.json.questionnaire.Question
import com.json.questionnaire.Questionnaire
import com.json.questionnaire.Statements
import com.json.user.ItemResult
import com.json.user.ObResult
import kotlin.random.Random


class Executor {

    var timer = 0
    private set
    var isExit = false
    var isInLast = false
	private set
    var isInFirst = false
	private set
    lateinit var question: Question

    var sceneInstance = -1
    private set

    private val sceneArray = arrayListOf<Int>()
    private lateinit var obResult: ObResult
    private lateinit var countDownTime: XCountDownTimer
    private lateinit var questionnaire: Questionnaire
    private lateinit var onActionTimeTick: (XCountDownTimer) -> Unit
    private lateinit var onActionTimeFinish: (XCountDownTimer) -> Unit
    private lateinit var onActionEndEvent: () -> Unit

    inner class XCountDownTimer(time: Long, interval: Long): CountDownTimer(time, interval){
        override fun onFinish() {
            onActionTimeFinish(this)
        }

        override fun onTick(p0: Long) {
            if (!isExit) {
                onActionTimeTick(this)
                timer += 1
            }
            else cancel()
        }
    }

    private var lastObResult: ObResult? = null

    private fun fillArray(){
        sceneArray.clear()
        if (questionnaire.isRandom){
            val range = arrayListOf<Int>()
            range.addAll(0.until(questionnaire.size))
            while (sceneArray.size != questionnaire.maxQuestions){
                val int = Random(System.currentTimeMillis()).nextInt(range.size)
                sceneArray.add(range[int])
                range.removeAt(int)
            }
        }
        else sceneArray.addAll(0..questionnaire.lastIndex)
    }

    fun setOnActionTimeTick(action: (XCountDownTimer) -> Unit){
        onActionTimeTick = action
    }

    fun setOnActionTimeFinish(action: (XCountDownTimer) -> Unit){
        onActionTimeFinish = action
    }

    fun setOnActionEndEvent(action: () -> Unit){
        onActionEndEvent = action
    }

    fun createEvent(questionnaire: Questionnaire, lastObResult: ObResult? = null){
        this.questionnaire = questionnaire
        this.lastObResult = lastObResult
        obResult = ObResult()
        obResult.id = questionnaire.settings.id
        obResult.isPresentedTruth = questionnaire.settings.isPresented
        if (lastObResult != null)
            obResult.tries = ++lastObResult.tries
        else obResult.tries += 1
        fillArray()
    }

    fun endEvent() = obResult.apply {
        while (sceneInstance != questionnaire.lastIndex){
            next()
            getAnswer(arrayListOf())
        }
        onActionEndEvent()
    }

    fun startQuestion() {
        if (question.isDefault)
            return
        countDownTime = XCountDownTimer(question.time * 1000, 1000L)
        countDownTime.start()
        isExit = false
    }

    // проверяет ответ на правильность
    fun getAnswer(answer: ArrayList<Int>) {
        Log.e("ex", sceneInstance.toString())
        //сбор текста ответов
        var answerString = ""
        for (item in answer)
            answerString += when (question.statements.type){
                Statements.ENTER -> question.statements.entered
                Statements.MULTI -> "$item) ${question.statements[item]}\n"
                Statements.SINGLE -> question.statements[item]
                else -> ""
            }
        var truthString = ""
        if (obResult.isPresentedTruth)
            for (item in question.truth)
                truthString += when (question.statements.type){
                    Statements.ENTER -> question.statements[item]
                    Statements.MULTI -> "$item) ${question.statements[item]}\n"
                    Statements.SINGLE -> question.statements[item]
                    else -> ""
                }
        //проверка на схожесть ответа на правильность
        var truth = true
        for (item in question.truth)
            if (answer.indexOf(item) < 0)
                truth = false
        if (question.truth.size != answer.size)
            truth = false
        try {
            val item = ItemResult(question.question, answerString, truth, truthString)
            item.array = answer
            if (sceneInstance >= obResult.size)
                obResult.add(item)
            else obResult[sceneInstance] = item
            if (item.truth)
                obResult.cost += question.cost
        }
        catch (ex: Exception){
            Log.e("ex", ex.toString())
        }
        Log.e("ex", question.toJsonObject())
    }

    // возвращает последний резултат с вопроса
    fun getObItem() =
        if (sceneInstance > obResult.lastIndex)
            null
        else obResult[sceneInstance]

    fun next() =
        if (sceneInstance < sceneArray.lastIndex){
            sceneInstance += 1
            isInFirst = false
            question = questionnaire[sceneArray[sceneInstance]]
        }
        else isInLast = true

    fun back() =
        if (sceneInstance > 0) {
            sceneInstance -= 1
            isInLast = false
            question = questionnaire[sceneArray[sceneInstance]]
        }
        else isInFirst = true
}