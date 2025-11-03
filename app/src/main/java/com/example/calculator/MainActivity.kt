package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private var currentInput = "0"
    private var operator = ""
    private var operand1 = 0.0
    private var isNewInput = true
    private var hasOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)
        setupNumberButtons()
        setupOperatorButtons()
        setupFunctionButtons()
    }

    private fun setupNumberButtons() {
        val numberButtonIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        numberButtonIds.forEach { id ->
            val button = findViewById<Button>(id)
            button.setOnClickListener {
                if (isNewInput) {
                    currentInput = ""
                    isNewInput = false
                }
                val number = button.text.toString()

                // Ngăn chặn số 0 ở đầu
                currentInput = if (currentInput == "0") {
                    number
                } else {
                    currentInput + number
                }
                updateDisplay()
            }
        }
    }

    private fun setupOperatorButtons() {
        // Nút chia
        findViewById<Button>(R.id.btnDivide).setOnClickListener { setOperator("/") }

        // Nút nhân
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { setOperator("×") }

        // Nút trừ
        findViewById<Button>(R.id.btnMinus).setOnClickListener { setOperator("-") }

        // Nút cộng
        findViewById<Button>(R.id.btnPlus).setOnClickListener { setOperator("+") }

        // Nút bằng
        findViewById<Button>(R.id.btnEquals).setOnClickListener { calculateResult() }
    }

    private fun setupFunctionButtons() {
        // CE - Clear Entry
        findViewById<Button>(R.id.btnCE).setOnClickListener {
            currentInput = "0"
            updateDisplay()
            isNewInput = true
        }

        // C - Clear All
        findViewById<Button>(R.id.btnC).setOnClickListener {
            currentInput = "0"
            operator = ""
            operand1 = 0.0
            hasOperator = false
            updateDisplay()
            isNewInput = true
        }

        // BS - Backspace
        findViewById<Button>(R.id.btnBS).setOnClickListener {
            currentInput = if (currentInput.length > 1) {
                currentInput.substring(0, currentInput.length - 1)
            } else {
                "0"
            }
            if (currentInput == "0") {
                isNewInput = true
            }
            updateDisplay()
        }

        // +/- - Plus/Minus
        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener {
            if (currentInput != "0") {
                currentInput = if (currentInput.startsWith("-")) {
                    currentInput.substring(1)
                } else {
                    "-$currentInput"
                }
                updateDisplay()
            }
        }

        // Dot - Dấu thập phân
        findViewById<Button>(R.id.btnDot).setOnClickListener {
            when {
                isNewInput -> {
                    currentInput = "0."
                    isNewInput = false
                }
                !currentInput.contains(".") -> {
                    currentInput += "."
                }
            }
            updateDisplay()
        }
    }

    private fun setOperator(newOperator: String) {
        if (!isNewInput || hasOperator) {
            if (operator.isNotEmpty()) {
                calculateResult()
            }
            try {
                operand1 = currentInput.toDouble()
                operator = newOperator
                hasOperator = true
                isNewInput = true
            } catch (e: NumberFormatException) {
                currentInput = "Error"
                updateDisplay()
                resetCalculator()
            }
        } else {
            operator = newOperator
            hasOperator = true
        }
    }

    private fun calculateResult() {
        if (operator.isNotEmpty() && hasOperator) {
            try {
                val operand2 = currentInput.toDouble()
                val result = when (operator) {
                    "+" -> operand1 + operand2
                    "-" -> operand1 - operand2
                    "×" -> operand1 * operand2
                    "/" -> if (operand2 != 0.0) {
                        operand1 / operand2
                    } else {
                        tvDisplay.text = "Error"
                        resetCalculator()
                        return
                    }
                    else -> 0.0
                }

                currentInput = if (result == result.toInt().toDouble()) {
                    result.toInt().toString()
                } else {

                    String.format("%.6f", result).removeSuffix("0").removeSuffix(".")
                }

                operator = ""
                hasOperator = false
                updateDisplay()
                isNewInput = true

            } catch (e: NumberFormatException) {
                currentInput = "Error"
                updateDisplay()
                resetCalculator()
            }
        }
    }

    private fun updateDisplay() {
        tvDisplay.text = currentInput
    }

    private fun resetCalculator() {
        currentInput = "0"
        operator = ""
        operand1 = 0.0
        hasOperator = false
        isNewInput = true
    }
}