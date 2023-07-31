package com.example.testingapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    lateinit var txtvQuestion:TextView
    lateinit var txtvCategory:TextView
    lateinit var txtvDifficulty:TextView
    lateinit var txtAnswer:EditText
    lateinit var btnSubmitAnswer:Button
    lateinit var txtvAnswer:TextView
    lateinit var btnNextQuestion:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtvQuestion = findViewById(R.id.txtvQuestion)
        txtvCategory = findViewById(R.id.txtvCategory)
        txtvDifficulty = findViewById(R.id.txtvDifficulty)
        txtAnswer = findViewById(R.id.txtAnswer)
        btnSubmitAnswer = findViewById(R.id.btnSubmitAnswer)
        txtvAnswer = findViewById(R.id.txtvAnswer)
        btnNextQuestion = findViewById(R.id.btnNextQuestion)

        val btnGenerate:Button = findViewById(R.id.btnGenerate)
        btnGenerate.setOnClickListener{
            InfoGetter()
        }
        btnSubmitAnswer.setOnClickListener(){
            if(txtAnswer.text.toString().toLowerCase() == variablesList.get(0).answer.toString().toLowerCase()){
                Toast.makeText(this, "Your answer was correct", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Incorrect answer, you suck!!", Toast.LENGTH_SHORT).show()
                txtvAnswer.text = variablesList.get(0).answer.toString()
            }
        }
        btnNextQuestion.setOnClickListener(){
            InfoGetter()
            txtAnswer.setText("")
            txtvAnswer.setText("")
        }
    }
lateinit var answer:String
lateinit var variablesList:List<ApiVariables>
    private fun InfoGetter() {
        val executor =  Executors.newSingleThreadExecutor()
        executor.execute {
            val url = URL("https://jservice.io/api/random")
            val json = url.readText()
            variablesList = Gson().fromJson(json, Array<ApiVariables>::class.java).toList()

            Handler(Looper.getMainLooper()).post {
                Log.d("PullingInfo", "Plain Json Vars :" + json.toString())
                Log.d("ConvertingInfo", "Converted Json :" + variablesList.toString())

                txtvQuestion.text = variablesList[0].question
                txtvCategory.text = variablesList[0].category.title
                txtvDifficulty.text = variablesList[0].value.toString()
                answer=variablesList[0].answer
                Log.d("Display", "question: ${variablesList[0].question}, \nCategory: ${variablesList[0].category.title},\nDifficulty: ${variablesList[0].value.toString()}")
            }
        }
    }
}