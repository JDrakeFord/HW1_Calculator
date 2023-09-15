package com.example.hw1_calculator

import android.util.Log
import java.lang.Integer.min
import java.math.RoundingMode
import java.text.DecimalFormat

class CalculatorPresenter : CalculatorContract.Presenter {
    lateinit var mModel: CalculatorModel
    lateinit var mView: CalculatorContract.View
    var textEntry: String = "0"
    var newEntry = false // is this a new entry or continuing input?
    var allowDecimal = true // Is a decimal allowed at this state?
    var inputLog = ArrayList<String>() // Log of input to detect operator switching

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
            if (newEntry) { // Replace text if it is a new entry
                newEntry = false
                textEntry = "."
            } else {
                textEntry += '.';
            }
        }
        return textEntry
    }

    fun textEntryToNumber(txt: String): Double {
        var doub: Double
        if (txt[txt.length - 1] == '.') { // Remove decimal if no trailing digits
            doub = txt.substring(0, txt.length - 1).toDouble()
        } else {
            doub = txt.toDouble()
        }
        return doub
    }

    // Format the number for on screen display
    fun numberToTextEntry(num: Double): String {
        val df = DecimalFormat("#.######") // 6 digits
        df.roundingMode = RoundingMode.HALF_UP
        val sci = String.format("%s", num) // Scientific notation
        Log.d("sci:", sci)
        if ((df.format(num).length > 8 || (num < 0.000001 && num > 0)) && sci.contains('E')) { // If scientific notation should be used
            val sci_1: String
            if (sci.substring(sci.indexOf('.') + 1, sci.indexOf('E')) == "0") {
                sci_1 = sci.substring(0, sci.indexOf('.')) // Remove decimal if not needed
            } else {
                sci_1 = sci.substring(0, min((10 - (sci.length - sci.indexOf('E'))), sci.indexOf('E'))) // Pare down leading number
            }
            val sci_2 = sci.substring(sci.indexOf('E')) // Get 'E' to end
            return sci_1 + sci_2
        }
        else if (num.toInt().toDouble() == num) { // If it can be shown as int
            return df.format(num.toInt().toDouble())
        } else {
            return df.format(num)
        }


    }

    override fun calculate(oper: CalculatorModel.Operator): String {
        newEntry = true
        // Just hitting equals over and over
        if (mModel.operator == CalculatorModel.Operator.NONE && oper == CalculatorModel.Operator.NONE) {
            return mView.getText()
        }
        // Collect operator 1 (no calculation needed)
        if (mModel.operand1.empty) {
            Log.d("HERE", "HERE")
            mModel.operand1.value = textEntryToNumber(textEntry)
            mModel.operand1.empty = false
            mModel.operator = oper
            mView.recolor(mModel.operator)
            return textEntry
        }
        // User hit equals previously, but now wants to use answer as operand 1
        else if (mModel.operator == CalculatorModel.Operator.NONE && oper != CalculatorModel.Operator.NONE) {
            textEntry = mView.getText()
            if (textEntry != numberToTextEntry(mModel.operand1.value)) {
                mModel.operand1.value = textEntryToNumber(textEntry)
            }
            mModel.operator = oper
            mView.recolor(oper)
            return mView.getText()
        }
        // Now we have operand 2 - go ahead and calculate
        else {
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
                textEntry = numberToTextEntry(answer.first)
                mView.recolor(mModel.operator)
                return numberToTextEntry(answer.first)
            }
        }
    }

    // Handler for un-special buttons
    fun button(num: String): String {
        val operators = arrayOf("/", "X", "-", "+")
        when (num) {
            "/" -> {
                allowDecimal = true
                if (inputLog.isEmpty()) {
                    inputLog.add(num)
                    return calculate(CalculatorModel.Operator.DIVIDE)
                }
                else if (!operators.contains(inputLog.last())) {
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
                if (inputLog.isEmpty()) {
                    inputLog.add(num)
                    return calculate(CalculatorModel.Operator.TIMES)
                }
                else if (!operators.contains(inputLog.last())) {
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
                if (inputLog.isEmpty()) {
                    inputLog.add(num)
                    return calculate(CalculatorModel.Operator.MINUS)
                }
                else if (!operators.contains(inputLog.last())) {
                    inputLog.add(num)
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
                if (inputLog.isEmpty()) {
                    inputLog.add(num)
                    return calculate(CalculatorModel.Operator.PLUS)
                }
                else if (!operators.contains(inputLog.last())) {
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
                else if (textEntry == "0" || newEntry) { // Replace text
                    textEntry = num
                    newEntry = false
                } else if (textEntry == "-0") { // Replace text but negative
                    textEntry = "-$num"
                }
                else {
                    textEntry += num // Append to text
                }
                return textEntry
            }
        }
    }

    fun switchPos(): String {
        if (newEntry) { // Reset for new input
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
        if (newEntry) { // Reset if new entry
            textEntry = "0"
            return textEntry
        }
        if (textEntry == "0" || textEntry == "-0") { // Don't delete the 0
            return textEntry
        } else if (textEntry.length == 1) { // Replace the last digit to delete with 0
            textEntry = "0"
        } else {
            textEntry = textEntry.substring(0, textEntry.length - 1) // Remove 1 digit
        }
        return textEntry
    }


}