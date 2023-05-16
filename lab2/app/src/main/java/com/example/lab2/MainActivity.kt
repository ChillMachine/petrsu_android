package com.example.lab2

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var buttonRock: Button
    private lateinit var buttonScissors: Button
    private lateinit var buttonPaper: Button
    private lateinit var textResult: TextView
    private lateinit var textScore: TextView
    private lateinit var editMaxScore: EditText
    private lateinit var buttonSave: Button
    private lateinit var textMaxScore: TextView
    private lateinit var buttonRestart: Button

    private var playerScore: Int = 0
    private var computerScore: Int = 0
    private var maxScore: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        textResult = findViewById(R.id.textResult)
        buttonRock = findViewById(R.id.buttonRock)
        buttonScissors = findViewById(R.id.buttonScissors)
        buttonPaper = findViewById(R.id.buttonPaper)
        textScore = findViewById(R.id.textScore)
        editMaxScore = findViewById(R.id.editMaxScore)
        buttonSave = findViewById(R.id.buttonSave)
        textMaxScore = findViewById(R.id.textMaxScore)
        buttonRestart = findViewById(R.id.buttonRestart)

        buttonRock.setOnClickListener { playGame(Choice.ROCK) }
        buttonScissors.setOnClickListener { playGame(Choice.SCISSORS) }
        buttonPaper.setOnClickListener { playGame(Choice.PAPER) }

        buttonRestart.setOnClickListener {

            resetGame()
        }

        buttonSave.setOnClickListener {
            val maxScoreValue = editMaxScore.text.toString().toIntOrNull()
            if (maxScoreValue != null) {
                maxScore = maxScoreValue
                textMaxScore.text = "До $maxScoreValue побед"
                textMaxScore.visibility = TextView.VISIBLE
                editMaxScore.visibility = EditText.INVISIBLE
                buttonSave.visibility = Button.INVISIBLE
                hideKeyboard()

                buttonPaper.visibility = Button.VISIBLE
                buttonRock.visibility = Button.VISIBLE
                buttonScissors.visibility = Button.VISIBLE
                textScore.visibility = TextView.VISIBLE
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }


    private fun playGame(playerChoice: Choice) {
        val computerChoice = generateComputerChoice()
        val result = determineWinner(playerChoice, computerChoice)

        when (result) {
            Result.PLAYER_WINS -> {
                playerScore++
                textScore.text = "Счет: $playerScore : $computerScore"
                textResult.text = "Вы победили! Ваш выбор: $playerChoice, выбор компьютера: $computerChoice"
                textResult.visibility = TextView.VISIBLE

            }
            Result.COMPUTER_WINS -> {
                computerScore++
                textScore.text = "Счет: $playerScore : $computerScore"
                textResult.text = "Компьютер победил! Ваш выбор: $playerChoice, выбор компьютера: $computerChoice"
                textResult.visibility = TextView.VISIBLE

            }
            Result.DRAW -> {
                textResult.text = "Ничья! Ваш выбор: $playerChoice, выбор компьютера: $computerChoice"
                textResult.visibility = TextView.VISIBLE
            }
        }

        // Проверяем, достигли ли кто-то максимального количества очков
        if (playerScore >= maxScore) {
            showGameOverMessage("Вы победили!")
        } else if (computerScore >= maxScore) {
            showGameOverMessage("Компьютер победил!")
        }
    }

    private fun generateComputerChoice(): Choice {
        val choices = arrayOf(Choice.ROCK, Choice.SCISSORS, Choice.PAPER)
        return choices.random()
    }

    private fun determineWinner(playerChoice: Choice, computerChoice: Choice): Result {
        if (playerChoice == computerChoice) {
            return Result.DRAW
        }

        return when (playerChoice) {
            Choice.ROCK -> if (computerChoice == Choice.SCISSORS) Result.PLAYER_WINS else Result.COMPUTER_WINS
            Choice.SCISSORS -> if (computerChoice == Choice.PAPER) Result.PLAYER_WINS else Result.COMPUTER_WINS
            Choice.PAPER -> if (computerChoice == Choice.ROCK) Result.PLAYER_WINS else Result.COMPUTER_WINS
        }
    }

    private fun showGameOverMessage(message: String) {
        textResult.text = "$message Игра окончена. Ваши очки: $playerScore, очки компьютера: $computerScore"
        textScore.visibility = TextView.INVISIBLE
        textResult.visibility = TextView.VISIBLE
        buttonRestart.visibility = Button.VISIBLE
        buttonRock.isEnabled = false
        buttonScissors.isEnabled = false
        buttonPaper.isEnabled = false
    }



    private fun resetGame(){
        playerScore = 0
        computerScore = 0


        buttonRestart.visibility = Button.INVISIBLE
        textResult.visibility = TextView.INVISIBLE
        buttonScissors.visibility = Button.INVISIBLE
        buttonRock.visibility = Button.INVISIBLE
        buttonPaper.visibility = Button.INVISIBLE
        textMaxScore.visibility = TextView.INVISIBLE

        editMaxScore.visibility = EditText.VISIBLE
        buttonSave.visibility = Button.VISIBLE



        // Скрыть результат и сообщение о победе/поражении
        textResult.visibility = TextView.INVISIBLE

        // Вернуть состояние кнопок выбора в исходное состояние
        buttonRock.isEnabled = true
        buttonPaper.isEnabled = true
        buttonScissors.isEnabled = true

        // Очистить текст счета игры
        textScore.text = ""

        // Сбросить счет игры
        playerScore = 0
        computerScore = 0
    }
}

enum class Choice {
    ROCK,
    SCISSORS,
    PAPER
}

enum class Result {
    PLAYER_WINS,
    COMPUTER_WINS,
    DRAW
}