package com.example.quizgame

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quizgame.databinding.ActivityQuestionBinding
import com.example.quizgame.databinding.ScoreDialogBinding


class QuestionActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        var questionModelList : List<QuestionModel> = listOf()
        var time: String = ""
    }
    private lateinit var binding: ActivityQuestionBinding
    private var currentQuestionIndex = 0
    private var selectAnswer = ""
    private var score = 0
    private lateinit var timerViewModel: TimerViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timerViewModel = ViewModelProvider(this)[TimerViewModel::class.java]

        timerViewModel.countdownText.observe(this, Observer { countdownText ->
            binding.timerIndicate.text = countdownText
        })


        // Initialize ViewModel only if savedInstanceState is null
        if (savedInstanceState == null) {
            timerViewModel = ViewModelProvider(this).get(TimerViewModel::class.java)
            timerViewModel.startTimer()
        } else {
            // If savedInstanceState is not null, restore the ViewModel instance
            timerViewModel = ViewModelProvider(this).get(TimerViewModel::class.java)
        }

        // Observe countdown text
        timerViewModel.countdownText.observe(this, Observer { countdownText ->
            binding.timerIndicate.text = countdownText
            if (countdownText == "00:00") {
                finishTime()
            }
        })



        //set onclick listener to the buttons
        binding.apply {
            answerBtn1.setOnClickListener(this@QuestionActivity)
            answerBtn2.setOnClickListener(this@QuestionActivity)
            answerBtn3.setOnClickListener(this@QuestionActivity)
            answerBtn4.setOnClickListener(this@QuestionActivity)
            next.setOnClickListener(this@QuestionActivity)
        }
        loadQuestion()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }




    // to test question model list
    @SuppressLint("SetTextI18n")
    private fun loadQuestion(){
        selectAnswer = ""
        if(currentQuestionIndex == questionModelList.size){
            finishQuiz()
            return
        }
        binding.apply {
            questionIndicate.text = "Question ${currentQuestionIndex + 1 }/ ${questionModelList.size}"
            progressIndicate.progress = (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
            questionCard.text = questionModelList[currentQuestionIndex].question
            answerBtn1.text = questionModelList[currentQuestionIndex].options[0]
            answerBtn2.text = questionModelList[currentQuestionIndex].options[1]
            answerBtn3.text = questionModelList[currentQuestionIndex].options[2]
            answerBtn4.text = questionModelList[currentQuestionIndex].options[3]

        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save necessary data to handle configuration changes
        outState.putInt("currentQuestionIndex", currentQuestionIndex)
        outState.putInt("score", score)
        outState.putString("selectAnswer", selectAnswer)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore necessary data after configuration changes
        currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex")
        score = savedInstanceState.getInt("score")
        selectAnswer = savedInstanceState.getString("selectAnswer") ?: ""
    }




    override fun onClick(view: View?) {

        binding.apply {
            answerBtn1.setBackgroundColor(getColor(R.color.grey))
            answerBtn2.setBackgroundColor(getColor(R.color.grey))
            answerBtn3.setBackgroundColor(getColor(R.color.grey))
            answerBtn4.setBackgroundColor(getColor(R.color.grey))
        }
        val clickButton = view as Button
        if(clickButton.id == R.id.next){
            //next button clicked
            if(selectAnswer.isEmpty()){  //if not select answer can't continue game
                Toast.makeText(applicationContext,"Please Select Answer to Continue",Toast.LENGTH_SHORT).show()
                return
            }
            if(selectAnswer == questionModelList[currentQuestionIndex].correct){
                score++
                Log.i("Score of Quiz", score.toString())
            }
            currentQuestionIndex++
            loadQuestion()
        }else{
            //option button clicked
            selectAnswer = clickButton.text.toString()
            clickButton.setBackgroundColor(getColor(R.color.light_blue2))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun finishQuiz(){
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100 ).toInt()

        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicate.progress = percentage
            scoreProgress.text = "$percentage % "
            if(percentage >= 60){
                scoreTitle.text = "Congratulations! You have Successfully Passed"
                scoreTitle.setTextColor(Color.BLUE)
            }else{
                scoreTitle.text = "Sorry,You have Failed, Keep trying Better luck next time!"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score out of $totalQuestions are correct"
            finishButton.setOnClickListener{
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()

    }
    @SuppressLint("SetTextI18n")
    private fun finishTime(){

        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100 ).toInt()

        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicate.progress = percentage
            scoreProgress.text = "$percentage % "
            if(percentage >= 60){
                scoreTitle.text = "Time Over! but You have Successfully Passed"
                scoreTitle.setTextColor(Color.BLUE)
            }else{
                scoreTitle.text = "Time Over! Sorry,You have Failed, Keep trying Better luck next time!"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score out of $totalQuestions are correct"
            finishButton.setOnClickListener{
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()

    }

}