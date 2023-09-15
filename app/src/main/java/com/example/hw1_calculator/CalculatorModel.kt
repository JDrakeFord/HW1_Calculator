package com.example.hw1_calculator

class CalculatorModel {
    fun clear(): Double {
        operand1 = Operand()
        operand2 = Operand()
        operator = Operator.NONE
        return 0.0
    }

    // <Value returned, Error>
    fun calculate(): Pair<Double, Boolean> {
        if (operator == Operator.DIVIDE && operand2.value == 0.0) {
            return Pair(0.0, true)
        } else {
            return when (operator) {
                Operator.PLUS -> Pair(operand1.value + operand2.value, false)
                Operator.MINUS -> Pair(operand1.value - operand2.value, false)
                Operator.TIMES -> Pair(operand1.value * operand2.value, false)
                Operator.DIVIDE -> Pair(operand1.value / operand2.value, false)
                Operator.NONE -> Pair(0.0, true)
            }
        }
    }

    class Operand {
        var value: Double = 2.0
        var empty: Boolean = true
    }

    enum class Operator{
        PLUS,MINUS,TIMES,DIVIDE,NONE
    }
    var operand1:Operand = Operand()
    var operand2:Operand = Operand()
    var operator:Operator = Operator.NONE


}