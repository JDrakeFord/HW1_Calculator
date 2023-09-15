package com.example.hw1_calculator

import android.util.Log
import java.lang.Integer.min
import java.math.RoundingMode
import java.text.DecimalFormat

class CalculatorPresenter : CalculatorContract.Presenter {
    lateinit var mModel: CalculatorModel
    lateinit var mView: CalculatorContract.View
    var textEntry: String = "0"
    var newEntry = false
    var allowDecimal = true
    var inputLog = ArrayList<String>()

    override fun setView(view: CalculatorContract.View) {
        mView = view
        Log.d("view", "set view!")
    }

    override fun start() {
        mModel = CalculatorModel()
    }

    fun clear(): String {
        inputLog.add("C")
        textEntry = "0"
        mModel.clear()
        mView.recolor(mModel.operator)
        return textEntry
    }

    fun decimal(): String {
        inputLog.add(".")
        if (!allowDecimal) {
            return textEntry
        } else {
            allowDecimal = false
            if (newEntry) {
                newEntry = false;
                textEntry = "."
            } else {
                textEntry += '.';
            }
        }
        return textEntry
    }

    fun textEntryToNumber(txt: String): Double {
        var doub: Double
        if (txt[txt.length - 1] == '.') {
            doub = txt.substring(0, txt.length - 1).toDouble()
        } else {
            doub = txt.toDouble()
        }
        return doub
    }

    fun numberToTextEntry(num: Double): String {
        val df = DecimalFormat("#.######")
        df.roundingMode = RoundingMode.HALF_UP
        val sci = String.format("%s", num)
        Log.d("sci:", sci)
        if ((df.format(num).length > 8 || (num < 0.000001 && num > 0)) && sci.contains('E')) {
            val sci_1: String
            if (sci.substring(sci.indexOf('.') + 1, sci.indexOf('E')) == "0") {
                sci_1 = sci.substring(0, sci.indexOf('.'))
            } else {
                sci_1 = sci.substring(0, min((10 - (sci.length - sci.indexOf('E'))), sci.indexOf('E')))
            }
            val sci_2 = sci.substring(sci.indexOf('E'))
            return sci_1 + sci_2
        }
        else if (num.toInt().toDouble() == num) {
            return df.format(num.toInt().toDouble())
        } else {
            return df.format(num)
        }


    }

    fun calculate(oper: CalculatorModel.Operator): String {
        newEntry = true
        if (mModel.operator == CalculatorModel.Operator.NONE && oper == CalculatorModel.Operator.NONE) {
            return mView.getText()
        }
        if (mModel.operand1.empty) {
            mModel.operand1.value = textEntryToNumber(textEntry)
            mModel.operand1.empty = false
            mModel.operator = oper
            mView.recolor(mModel.operator)
            return textEntry
        } else if (mModel.operator == CalculatorModel.Operator.NONE && oper != CalculatorModel.Operator.NONE) {
            textEntry = mView.getText()
            if (textEntry != numberToTextEntry(mModel.operand1.value)) {
                mModel.operand1.value = textEntryToNumber(textEntry)
            }
            mModel.operator = oper
            mView.recolor(oper)
            return mView.getText()
        } else {
            mModel.operand2.value = textEntryToNumber(textEntry)
            mModel.operand2.empty = false
            val answer = mModel.calculate()
            mModel.operator = oper
            if (answer.second) {
                mModel.clear()
                mView.recolor(mModel.operator)
                return "Error"
            } else {
                mModel.operand1.value = answer.first
                mView.recolor(mModel.operator)
                return numberToTextEntry(answer.first)
            }
        }
    }

    fun button(num: String): String {
        var operators = arrayOf("/", "X", "-", "+")
        when (num) {
            "/" -> {
                allowDecimal = true
                if (!operators.contains(inputLog.last())) {
                    inputLog.add(num)
                    return calculate(CalculatorModel.Operator.DIVIDE)
                }
                else {
                    inputLog.add(num)
                    mModel.operator = CalculatorModel.Operator.DIVIDE
                    mView.recolor(mModel.operator)
                    return textEntry
                }
            }
            "X" -> {
                allowDecimal = true
                if (!operators.contains(inputLog.last())) {
                    inputLog.add(num)
                    return calculate(CalculatorModel.Operator.TIMES)
                }
                else {
                    inputLog.add(num)
                    mModel.operator = CalculatorModel.Operator.TIMES
                    mView.recolor(mModel.operator)
                    return textEntry
                }
            }
            "—" -> {
                allowDecimal = true
                if (!operators.contains(inputLog.last())) {
                    inputLog.add("-")
                    return calculate(CalculatorModel.Operator.MINUS)
                }
                else {
                    inputLog.add("-")
                    mModel.operator = CalculatorModel.Operator.MINUS
                    mView.recolor(mModel.operator)
                    return textEntry
                }
            }
            "+" -> {
                allowDecimal = true
                if (!operators.contains(inputLog.last())) {
                    inputLog.add(num)
                    return calculate(CalculatorModel.Operator.PLUS)
                }
                else {
                    inputLog.add(num)
                    mModel.operator = CalculatorModel.Operator.PLUS
                    mView.recolor(mModel.operator)
                    return textEntry
                }
            }
            "=" -> {
                inputLog.add("=")
                allowDecimal = true
                return calculate(CalculatorModel.Operator.NONE)
            }
            else -> {
                inputLog.add(num)
                if (textEntry.length >= 12 && !newEntry) {
                    // Do nothing - limit reached
                }
                else if (textEntry == "0" || newEntry) {
                    textEntry = num
                    newEntry = false
                } else if (textEntry == "-0") {
                    textEntry = "-$num"
                }
                else {
                    textEntry += num
                }
                return textEntry
            }
        }
    }

    fun switchPos(): String {
        if (newEntry) {
            textEntry = "0"
            newEntry = false;
        }
        if (textEntry[0] == '-') {
            textEntry = textEntry.substring(1, textEntry.length)
        } else {
            textEntry = "-$textEntry";
        }
        return textEntry
    }

    fun backspace(): String {
        if (newEntry) {
            textEntry = "0"
            return textEntry
        }
        if (textEntry == "0" || textEntry == "-0") {
            return textEntry
        } else if (textEntry.length == 1) {
            textEntry = "0"
        } else {
            textEntry = textEntry.substring(0, textEntry.length - 1)
        }
        return textEntry
    }

    override fun calculate(cmd: String) {
    }

}