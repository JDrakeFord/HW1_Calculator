package com.example.hw1_calculator

class CalculatorContract {
    interface View {
        fun setResult(result: String)
        fun recolor(operator: CalculatorModel.Operator)
        fun getText(): String
    }

    interface Presenter {
        fun setView(view: View)
        fun start()
        fun calculate(oper: CalculatorModel.Operator): String
    }

}