package com.example.hw1_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), CalculatorContract.View, View.OnClickListener {
    lateinit var mPresenter: CalculatorPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPresenter = CalculatorPresenter()
        Log.d("about", "about to call set view!")
        mPresenter.setView(this)
        mPresenter.start()
        findViewById<Button>(R.id.btn_c).setOnClickListener(this)
        findViewById<Button>(R.id.btn_pm).setOnClickListener(this)
        findViewById<Button>(R.id.btn_back).setOnClickListener(this)
        findViewById<Button>(R.id.btn_0).setOnClickListener(this)
        findViewById<Button>(R.id.btn_1).setOnClickListener(this)
        findViewById<Button>(R.id.btn_2).setOnClickListener(this)
        findViewById<Button>(R.id.btn_3).setOnClickListener(this)
        findViewById<Button>(R.id.btn_4).setOnClickListener(this)
        findViewById<Button>(R.id.btn_5).setOnClickListener(this)
        findViewById<Button>(R.id.btn_6).setOnClickListener(this)
        findViewById<Button>(R.id.btn_7).setOnClickListener(this)
        findViewById<Button>(R.id.btn_8).setOnClickListener(this)
        findViewById<Button>(R.id.btn_9).setOnClickListener(this)
        findViewById<Button>(R.id.btn_period).setOnClickListener(this)
        findViewById<Button>(R.id.btn_x).setOnClickListener(this)
        findViewById<Button>(R.id.btn_divide).setOnClickListener(this)
        findViewById<Button>(R.id.btn_minus).setOnClickListener(this)
        findViewById<Button>(R.id.btn_plus).setOnClickListener(this)
        findViewById<Button>(R.id.btn_equals).setOnClickListener(this)

    }

    override fun setResult(result: String) {
        // TODO("Not yet implemented")
    }

    override fun getText(): String {
        return findViewById<TextView>(R.id.textView2).text.toString()
    }

    override fun recolor(operator: CalculatorModel.Operator) {
        Log.d("recolor", "its recoloring!")
        // Reset all operator colors
        findViewById<Button>(R.id.btn_divide).setTextColor(resources.getColor(R.color.white))
        findViewById<Button>(R.id.btn_divide).setBackgroundColor(resources.getColor(R.color.purple))
        findViewById<Button>(R.id.btn_plus).setTextColor(resources.getColor(R.color.white))
        findViewById<Button>(R.id.btn_plus).setBackgroundColor(resources.getColor(R.color.purple))
        findViewById<Button>(R.id.btn_minus).setTextColor(resources.getColor(R.color.white))
        findViewById<Button>(R.id.btn_minus).setBackgroundColor(resources.getColor(R.color.purple))
        findViewById<Button>(R.id.btn_x).setTextColor(resources.getColor(R.color.white))
        findViewById<Button>(R.id.btn_x).setBackgroundColor(resources.getColor(R.color.purple))
        // Recolor the passed in operator
        when (operator) {
            CalculatorModel.Operator.DIVIDE -> {
                findViewById<Button>(R.id.btn_divide).setBackgroundColor(resources.getColor(R.color.white))
                findViewById<Button>(R.id.btn_divide).setTextColor(resources.getColor(R.color.purple))
            }
            CalculatorModel.Operator.PLUS -> {
                findViewById<Button>(R.id.btn_plus).setBackgroundColor(resources.getColor(R.color.white))
                findViewById<Button>(R.id.btn_plus).setTextColor(resources.getColor(R.color.purple))
            }
            CalculatorModel.Operator.MINUS -> {
                findViewById<Button>(R.id.btn_minus).setBackgroundColor(resources.getColor(R.color.white))
                findViewById<Button>(R.id.btn_minus).setTextColor(resources.getColor(R.color.purple))
            }
            CalculatorModel.Operator.TIMES -> {
                findViewById<Button>(R.id.btn_x).setBackgroundColor(resources.getColor(R.color.white))
                findViewById<Button>(R.id.btn_x).setTextColor(resources.getColor(R.color.purple))
            }
            else -> {}
        }
    }

    override fun onClick(v: View?) {
        if(v != null) {
            val btnClick: Button = v as Button
            when (btnClick.id) {
                R.id.btn_c -> {
                    findViewById<TextView>(R.id.textView2).text = mPresenter.clear()
                }
                R.id.btn_pm -> {
                    findViewById<TextView>(R.id.textView2).text = mPresenter.switchPos()
                }
                R.id.btn_back -> {
                    findViewById<TextView>(R.id.textView2).text = mPresenter.backspace()
                }
                R.id.btn_period -> {
                    findViewById<TextView>(R.id.textView2).text = mPresenter.decimal()
                }
                else -> {
                    findViewById<TextView>(R.id.textView2).text = mPresenter.button(btnClick.text.toString())
                }
            }
        }
    }
}