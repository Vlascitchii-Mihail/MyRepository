package com.bignerdranch.android.geoquiz_2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.TextView
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.core.app.ActivityOptionsCompat
import android.os.Build
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.geoquiz_2.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
const val cheat = "CheatActivity2"
private const val CHEAT_STATUS = "IS_CHEATER"

//AppCompatActivity - это подкласс, наследующий от класса Android Activity и
//обеспечивающий поддержку старых версий Android
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button
    private var cheatValue = 0


//    private val quizViewModel : QuizViewModel by lazy {
//        ViewModelProvider.AndroidViewModelFactory(application).create(QuizViewModel::class.java)
//        ViewModelProviders.of(this).get(QuizViewModel::class.java)
//        ViewModelProvider(this)[QuizViewModel::class.java]
//        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(QuizViewModel::class.java)
//    }
//    private val quizViewModel : QuizViewModel = QuizViewModel()

    private val quizViewModel : QuizViewModel by viewModels()


//вызывается при создании экземпляра подкласса activity.
    //savedInstanceState contains all the data from onSaveInstanceState()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        Log.d(TAG, "onCreate(Bundle?) called")

        //заполняет макет и выводит его на экран
    //R.layout.activity_main - идентификатор ресурса
        setContentView(binding.root)

    //this code commented because of using savedStateHandle in viewModel
//        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        cheatValue = savedInstanceState?.getInt(cheat, 0) ?: 0
        quizViewModel.cheatTries += savedInstanceState?.getInt(CHEAT_TRIES, 0) ?: 0
        quizViewModel.isCheater = savedInstanceState?.getBoolean(CHEAT_STATUS, false) ?: false

//        val isCheater = savedInstanceState?.getBoolean(KEY_INDEX_CHEAT, false) ?: false
//        quizViewModel.isCheater = isCheater

//        val provider: ViewModelProvider = ViewModelProviders.of(this)
//        val quizViewModel = provider.get(QuizViewModel::class.java)
////        val provider = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
////        val quizViewModel = provider.create(QuizViewModel::class.java)
//        Log.d(TAG, "Got a QuizViewModel:$quizViewModel")

    //getting reference on widgets
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatButton = findViewById(R.id.cheat_button)

    //filling interface  View.OnClickListener and creating listeners
        binding.trueButton.setOnClickListener {
            checkAnswer(true)
        }
        binding.falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            quizViewModel.isCheater = false
        }

    //moves to previous question
        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            //moves to next question after clicking on the textView
            quizViewModel.moveToNext()
            updateQuestion()
        }

    //receiving data from CheatActivity
        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                //it.data - intent from CheatActivity
                val data : Intent? = it.data

                //takes data from intent
                //is cheater or not?
                quizViewModel.isCheater = data?.let { it1 -> CheatActivity2.getBooleanIsCheater(it1) } == true
                Log.i(TAG, "${quizViewModel.isCheater}")
                cheatValue += data?.getIntExtra(cheat, 0) ?: 0
                quizViewModel.cheatTries = data?.getIntExtra(CHEAT_TRIES, 0) ?: 0
                Toast.makeText(this, "You cheated $cheatValue times", Toast.LENGTH_SHORT).show()

                Log.d(TAG, "CheatTries = ${quizViewModel.cheatTries}")


            }
        }


        cheatButton.setOnClickListener {
            if (quizViewModel.cheatTries == 3) {
                Toast.makeText(this, "You had 3 attempts", Toast.LENGTH_LONG).show()
            } else {
                Log.d(TAG, "UpdateQuestionText", Exception())
                quizViewModel.isCheater = true
                //calls the CheatActivity from MainActivity
                //@param this - package name
//            val intent = Intent(this, CheatActivity2::class.java)
//            startActivity(intent)

                val answerIsTrue = quizViewModel.currentQuestionAnswer
                //creating intent from function newIntent() from CheatActivity
                val intent = CheatActivity2.newIntent(
                    this@MainActivity,
                    answerIsTrue,
                    quizViewModel.cheatTries
                )
//            startActivity(intent)
//            startActivityForResult(intent, REQUEST_CODE_CHEAT)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val options =
                        ActivityOptionsCompat.makeClipRevealAnimation(it, 0, 0, it.width, it.height)

                    getContent.launch(intent, options)
                } else {
                    getContent.launch(intent)
                }
            }

        }


        updateQuestion()
    }

//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode != Activity.RESULT_OK) return
//        if (requestCode == REQUEST_CODE_CHEAT) {
//            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)?: false
//
//        }
//    }

//обновляет вопрос
    private fun updateQuestion() {
//        Log.d(TAG, "Updating question text", Exception())
         binding.trueButton.isEnabled = true
         binding.falseButton.isEnabled = true

    //receiving a text from ViewModel
            val questionTextResId = quizViewModel.currentQuestionText

        //setting the text in questionTextView
            questionTextView.setText(questionTextResId)
    }

    //проверка ответа пользователя
    private fun checkAnswer(userAnswer: Boolean) {
        //blocking buttons after checking a answer
        binding.trueButton.isEnabled = false
        binding.falseButton.isEnabled = false
            val correctAnswer = quizViewModel.currentQuestionAnswer
//            val messageResId = if (userAnswer == correctAnswer) {
//                R.string.correct_toast
//            }
//                else R.string.incorrect_toast

        //if user is cheater, shows Toast - "Cheating is wrong"
        val messageResId = when {
            quizViewModel.isCheater ->  R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        //this: Context - экземпляр MainActivity
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    //called when onStop() runs
    //saves all states and views outside the activity in OS
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.d(TAG, "onSaveInstanceState")
        //saves currentIndex outside of the app
//        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putInt(cheat, cheatValue)
        savedInstanceState.putInt(CHEAT_TRIES, quizViewModel.cheatTries)
        savedInstanceState.putBoolean(CHEAT_STATUS, quizViewModel.isCheater)
//        savedInstanceState.putBoolean(KEY_INDEX_CHEAT, quizViewModel.isCheater)
    }
}