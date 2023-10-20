package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quizapp.ui.theme.QuizAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuizApp()
                }
            }
        }

    }
}

data class Question(
    val text: String? = "test",
    val options: List<String> = emptyList(),
    val correctAnswer: String? = "correct Ans"
)

@Composable
fun QuizApp() {
    var isStartQuiz = remember {mutableStateOf(false)}

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }
    var correctAnswerCount by remember { mutableStateOf(0) }

    val questionState = remember { mutableMapOf<Int, Pair<String, Boolean>>() }


    val questions = listOf(
        Question(
            "What is an APK file in Android development?",
            listOf(
                "Android Package Kit",
                "Application Programming Kernel",
                "Android Programmed Key",
                "Application Preload Kit"),
            "Android Package Kit"
        ),
        Question(
            "Which programming language is primarily used for Android app development?",
            listOf(
                "Swift",
                "Java",
                "Python",
                "C#"),
            "Java"
        ),
        Question(
            "What is the purpose of the AndroidManifest.xml file in an Android app project?",
            listOf(
                "To store user data",
                "To define the app's user interface",
                "To specify the app's components and permissions",
                "To create animations for the app"),
            "To specify the app's components and permissions"
        ),
        Question(
            "Which component in Android is responsible for managing the user interface and handling user interactions?",
            listOf(
                "Activity",
                "Service",
                "BroadcastReceiver",
                "ContentProvider"),
            "Activity"
        ),
        Question(
            "What is the Android Support Library (AndroidX) used for?",
            listOf(
                "Providing compatibility features for older Android versions",
                "Managing user accounts and authentication",
                "Enhancing the device's hardware performance",
                "Creating augmented reality experiences"),
            "Providing compatibility features for older Android versions"
        )
    )

    val currentQuestion = questions.get(currentQuestionIndex)

    var isSubmitted = remember { mutableStateOf(false) }

    if (isStartQuiz.value){
        if (isSubmitted.value) {
            FinishScreen(
                correctAnswerCount = correctAnswerCount,
                totalMarks = questions.size,
                onClick = {
                    currentQuestionIndex = 0
                    correctAnswerCount = 0
                    isSubmitted.value = false
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Question(
                    currentQuestion = currentQuestion,
                    selectedAnswer = selectedAnswer,
                    onOptionSelected = { option ->
                        selectedAnswer = option
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            println("currentQuestionIndex1 => $currentQuestionIndex: $correctAnswerCount == $selectedAnswer")
                            if (currentQuestionIndex != (questions.size - 1)) {
                                println("check1")
                                currentQuestionIndex++
                                println("selectedAnswer: $selectedAnswer")
                                if (currentQuestion.correctAnswer == selectedAnswer) {
                                    println("check1.1")
                                    questionState[currentQuestionIndex] = Pair(selectedAnswer, true)
                                    correctAnswerCount++
                                }
                                questionState[currentQuestionIndex] = Pair(selectedAnswer, false)
                            } else {
                                println("check2")
                                if (currentQuestion.correctAnswer == selectedAnswer) {
                                    correctAnswerCount++
                                }
                                selectedAnswer = ""
                                isSubmitted.value = true
                            }
                            println("currentQuestionIndex2 => $currentQuestionIndex: $correctAnswerCount == $selectedAnswer")

                        },
                        enabled = selectedAnswer.isNotBlank()
                    ) {
                        Text(text = if (currentQuestionIndex != (questions.size - 1)) "Next" else "Submit")
                    }
                    if (currentQuestionIndex == 0){

                    }else{
                        Button(
                            onClick = {
                                println("currentQuestionIndex4 => $currentQuestionIndex: $correctAnswerCount == $selectedAnswer")
                                if (currentQuestionIndex != 0) {
                                    currentQuestionIndex--
                                    selectedAnswer = questionState[currentQuestionIndex]?.first ?: ""
                                    val isLastAnswerCorrect = questionState[currentQuestionIndex]?.second ?: false
                                    if (isLastAnswerCorrect) {
                                        correctAnswerCount--
                                    }
//
                                }
                                println("currentQuestionIndex4 => $currentQuestionIndex: $correctAnswerCount == $selectedAnswer")

                            },
                            enabled = true
                        ) {
                            Text(text = "Prev")
                        }
                    }
                }
            }
        }
    }else{
        StartScreen(
            onClick = {
                isStartQuiz.value = true
            })
    }


}

@Composable
fun Question(
    currentQuestion: Question,
    selectedAnswer: String,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Question", style = MaterialTheme.typography.headlineLarge)


        Text(text = currentQuestion.text!!, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(currentQuestion.options) { option ->
                OptionItem(
                    text = option,
                    selected = option == selectedAnswer,
                    onOptionSelected = {
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}

@Composable
fun OptionItem(text: String, selected: Boolean, onOptionSelected: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxSize()
        .clickable { onOptionSelected() }
        .background(if (selected) Color.LightGray else Color.White)
        .padding(10.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun StartScreen(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome", style = MaterialTheme.typography.headlineLarge)

        Button(
            onClick = onClick
        ) {
            Text(text = "Start quiz")
        }
    }
}

@Composable
fun FinishScreen(
    correctAnswerCount: Int,
    totalMarks: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Score", style = MaterialTheme.typography.headlineLarge)

        Text(text = "$correctAnswerCount / $totalMarks")

        if (totalMarks != correctAnswerCount){
            Button(
                onClick = onClick
            ) {
                Text(text = "Re-take")
            }
        }
    }
}

