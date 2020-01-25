package com

import android.os.CountDownTimer
import android.util.Log
import com.questionnaire.Question
import com.questionnaire.Questionnaire
import com.questionnaire.Statements
import com.users.ItemResult
import com.users.ObResult
import kotlin.random.Random


class XEngine {

    var timer = 0
    var isFinished = false
    lateinit var question: Question

    private var sceneInstance = -1
    private val sceneArray = arrayListOf<Int>()
    private lateinit var countDownTime: CountDownTimer
    private lateinit var questionnaire: Questionnaire
    private lateinit var obResult: ObResult
    private lateinit var onActionTimeTick: CountDownTimer.() -> Unit
    private lateinit var onActionTimeFinish: CountDownTimer.() -> Unit
    private lateinit var onActionEndEvent: () -> Unit

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

    fun setOnActionTimeTick(action: CountDownTimer.() -> Unit){
        onActionTimeTick = action
    }

    fun setOnActionTimeFinish(action: CountDownTimer.() -> Unit){
        onActionTimeFinish = action
    }

    fun setOnActionEndEvent(action: () -> Unit){
        onActionEndEvent = action
    }

    fun beginEvent(questionnaire: Questionnaire, lastObResult: ObResult?){
        this.questionnaire = questionnaire
        this.lastObResult = lastObResult
        obResult = ObResult()
        obResult.id = questionnaire.settings.id
        obResult.isPresentedTruth = questionnaire.settings.isPresented
        if (lastObResult != null)
            obResult.tries = ++lastObResult.tries
        else obResult.tries += 1
        fillArray()
        next()
    }

    fun endEvent() = ObResult.apply {
        while (sceneInstance != questionnaire.lastIndex){
            next()
            getAnswer(arrayListOf())
        }
        onActionEndEvent()
    }

    fun start() {
        if (question.isDefault)
            return

        countDownTime = object : CountDownTimer(question.time*1000, 1000L) {
            override fun onFinish() {
                onActionTimeFinish(this)
            }

            override fun onTick(p0: Long) {
                timer += 1
                onActionTimeTick(this)
            }
        }.start()
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
            question = questionnaire[sceneArray[sceneInstance]]
        }
        else isFinished = true

    fun back() =
        if (sceneInstance > 0) {
            sceneInstance -= 1
            isFinished = false
            question = questionnaire[sceneArray[sceneInstance]]
        }
        else Unit
}