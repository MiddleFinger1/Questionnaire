package modulas.computers

import kotlin.math.*

/*
этот синглтон, в отличии от Evaluate, может работать с основными математическими функциями
 */

/*
    функции можно редактировать (в поддержке имеется):
    abc()
    sin(), cos(), tan(), ctg()
    asin(), acos(), atan()
    sinh, cosh(), tanh()
    rad(), deg()
    ln(), sqrt(), sqr(), cube(), exp(),
    round(), ceil, floor()
    int(), frac()
 */

object MathCalculator {

    private val tokens = arrayListOf<String>()

    private interface Operator

    private interface UnaryOperator: Operator {
        fun execute(operand: Double): String
    }

    private interface BinaryOperator: Operator {
        fun execute(operand1: Double, operand2: Double): String
    }

    private val mapOperators = mapOf(
        "!" to object: UnaryOperator{
            override fun execute(operand: Double): String =
                if (operand <= 1) "1"
                else (operand * execute(operand - 1.0).toDouble()).toString()
        },
        "^" to object: BinaryOperator{
            override fun execute(operand1: Double, operand2: Double) =
                (operand1.pow(operand2)).toString()
        },
        "√" to object: UnaryOperator, BinaryOperator {
            override fun execute(operand: Double) = sqrt(operand).toString()
            override fun execute(operand1: Double, operand2: Double) =
                (operand2.pow(1 / operand1)).toString()
        },
        "/" to object: BinaryOperator{
            override fun execute(operand1: Double, operand2: Double) =
                (operand1 / operand2).toString()
        },
        "*" to object: BinaryOperator{
            override fun execute(operand1: Double, operand2: Double) =
                (operand1 * operand2).toString()
        },
        "%" to object: BinaryOperator {
            override fun execute(operand1: Double, operand2: Double) =
                (operand1 % operand2).toString()
        },
        "-" to object: UnaryOperator, BinaryOperator {
            override fun execute(operand: Double) = (-1 * operand).toString()
            override fun execute(operand1: Double, operand2: Double) =
                (operand1 - operand2).toString()
        },
        "+" to object: UnaryOperator, BinaryOperator{
            override fun execute(operand: Double) = operand.toString()
            override fun execute(operand1: Double, operand2: Double) =
                (operand1 + operand2).toString()
        },
        "(" to object: Operator{},
        ")" to object: Operator{},
        " " to object: Operator{}
    )

    private val mapFunctions = mapOf(
        "exp"   to { number: Double -> exp(number).toString() },
        "round" to { number: Double -> round(number).toString() },
        "floor" to { number: Double -> floor(number).toString() },
        "asin"  to { number: Double -> asin(number).toString() },
        "abs"   to { number: Double -> abs(number).toString() },
        "sin"   to { number: Double -> sin(number).toString() },
        "sinh"  to { number: Double -> sinh(number).toString() },
        "tanh"  to { number: Double -> tanh(number).toString() },
        "cos"   to { number: Double -> cos(number).toString() },
        "tan"   to { number: Double -> tan(number).toString() },
        "acos"  to { number: Double -> acos(number).toString() },
        "sqrt"  to { number: Double -> sqrt(number).toString() },
        "ctg"   to { number: Double -> (cos(number) / sin(number)).toString() },
        "int"   to { number: Double -> (number).toInt().toString() },
        "cube"  to { number: Double -> number.pow(3).toString() },
        "atan"  to { number: Double -> atan(number).toString() },
        "ceil"  to { number: Double -> ceil(number).toString() },
        "frac"  to { number: Double -> (number - number.toInt()).toString() },
        "sqr"   to { number: Double -> (number * number).toString() },
        "rad"   to { number: Double -> ((180 / PI) * number).toString() },
        "deg"   to { number: Double -> ((PI / 180) * number).toString() },
        "ln"    to { number: Double -> ln(number).toString() },
        "cosh"  to { number: Double -> cosh(number).toString() }
    )

    fun eval(request: String): String {
        tokens.clear()
        getTokens(request)
        while (tokens.count() > 1){
            var beginPoint = 0
            var endPoint = tokens.count() - 1
            for (index in 0..endPoint){
                val token = tokens[index]
                if (token == "("){
                    beginPoint = index
                    endPoint = tokens.indexOf(")")
                    if ("(" !in tokens.subList(beginPoint + 1, endPoint))
                        break
                }
            }
            val sublist = tokens.subList(beginPoint, endPoint + 1)
            sublist.remove("(")
            sublist.remove(")")
            tokens[beginPoint] = decideExpressions(sublist)
            if (beginPoint >= 1)
                if (mapFunctions[tokens[beginPoint - 1]] != null){
                    val function = mapFunctions[tokens[beginPoint - 1]]
                    val value = function?.let { it(tokens[beginPoint].toDouble()) }
                    tokens[beginPoint - 1] = value ?: ""
                    tokens.removeAt(beginPoint)
                }
        }
        return tokens[0]
    }

    private fun decideExpressions(tokens: MutableList<String>): String {
        for (symbol in mapOperators.keys){
            val operator = mapOperators[symbol] as Operator
            while (tokens.contains(symbol)){
                val index = tokens.indexOf(symbol)
                if (operator is BinaryOperator && index >= 1 && mapOperators[tokens[index - 1]] == null) {
                    val operand1 = tokens[index - 1].toDouble()
                    val operand2 = tokens[index + 1].toDouble()
                    tokens[index - 1] = operator.execute(operand1, operand2)
                    tokens.removeAt(index)
                    tokens.removeAt(index)
                }
                else if (operator is UnaryOperator && index >= 0) {
                    val operand = tokens[index + 1].toDouble()
                    tokens[index + 1] = operator.execute(operand)
                    tokens.removeAt(index)
                }
            }
        }
        return tokens[0]
    }

    private fun getTokens(request: String){
        var reader = ""
        for (symbol in request)
            if (mapOperators[symbol.toString()] != null){
                if (reader != "")
                    tokens += reader
                reader = ""
                tokens += symbol.toString()
            }
            else reader += symbol
        if (reader != "") tokens += reader
    }
}