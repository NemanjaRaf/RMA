package com.nemanja02.rma.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import coil.compose.rememberImagePainter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberImagePainter
import com.nemanja02.rma.LocalAnalytics
import com.nemanja02.rma.auth.UserStore
import com.nemanja02.rma.cats.details.CatDetailsState
import com.nemanja02.rma.core.theme.EnableEdgeToEdge
import com.nemanja02.rma.drawer.AppDrawer
import com.nemanja02.rma.quiz.model.AnswerOption
import com.nemanja02.rma.quiz.model.Question
import kotlinx.coroutines.launch

fun NavGraphBuilder.quiz(
    route: String,
    onCatClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onCatsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onSettingsClick: () -> Unit,
    authData: UserStore?,
    onQuizCompleted: () -> Unit,
    onClose: () -> Unit,
    onPublishScore: () -> Unit,
) = composable(
    route = route,
) { navBackStackEntry ->

    val quizViewModel: QuizViewModel = hiltViewModel(navBackStackEntry)

    val state = quizViewModel.state.collectAsState()

    EnableEdgeToEdge(isDarkTheme = false)

    if (state.value.questions.isEmpty()) {
        ResultScreen(
            score = quizViewModel.calculateScore(),
            onFinish = onClose,
            onPublish = onPublishScore
        )
    } else {
        QuizScreen(
            state = state.value,
            onCatClick = onCatClick,
            onProfileClick = onProfileClick,
            onCatsClick = onCatsClick,
            onQuizClick = onQuizClick,
            onLeaderboardClick = onLeaderboardClick,
            onSettingsClick = onSettingsClick,
            authData = authData,
            onOptionSelected = { option -> quizViewModel.submitAnswer(option) },
            onQuizCompleted = onQuizCompleted,
            onClose = onClose
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    state: QuizContract.QuizUiState,
    onCatClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onCatsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onSettingsClick: () -> Unit,
    authData: UserStore? = null,
    onOptionSelected: (AnswerOption) -> Unit,
    onQuizCompleted: () -> Unit,
    onClose: () -> Unit,
) {
    val uiScope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)

    val analytics = LocalAnalytics.current
    SideEffect {
        analytics.log("Neka poruka")
    }

    BackHandler(enabled = drawerState.isOpen) {
        uiScope.launch { drawerState.close() }
    }

    AppDrawer(
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Time left: ${state.timeRemaining / 1000} seconds") },
                    )
                },
            ) { innerPadding ->

                Box(modifier = Modifier.padding(innerPadding)) {
                    if (state.updating) {
                        CircularProgressIndicator()
                    } else if (state.questions.isNotEmpty()) {
                        val question = state.questions[state.currentQuestionIndex]
                        QuestionScreen(
                            state = state,
                            onDrawerMenuClick = {
                                uiScope.launch {
                                    drawerState.open()
                                }
                            },
                            question = question,
                            onOptionSelected = onOptionSelected
                        )
                    } else {
                        onQuizCompleted()
//                    Text("No questions available")
                    }
                }
            }
        },
        drawerState = drawerState,
        onProfileClick = onProfileClick,
        onCatsClick = onCatsClick,
        onQuizClick = onQuizClick,
        onLeaderboardClick = onLeaderboardClick,
        onSettingsClick = onSettingsClick,
    )
}


@Composable
fun QuestionScreen(
    state: QuizContract.QuizUiState,
    onDrawerMenuClick: () -> Unit,
    question: Question,
    onOptionSelected: (AnswerOption) -> Unit
) {
    val quizViewModel: QuizViewModel = hiltViewModel()
    val state = quizViewModel.state.collectAsState().value

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Question ${state.currentQuestionIndex + 1} of ${state.questions.size}",
            style = MaterialTheme.typography.headlineSmall

        )
        Text(
            text = question.questionText,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (question.imageUrl != null) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = rememberImagePainter(question.imageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            question.options.forEach { option ->
                val buttonColor = when {
                    state.selectedOption == null -> MaterialTheme.colorScheme.primary
                    option == state.selectedOption && state.isOptionCorrect == true -> Color.Green
                    option == state.selectedOption && state.isOptionCorrect == false -> Color.Red
                    else -> MaterialTheme.colorScheme.primary
                }
                Button(
                    onClick = { if (state.selectedOption == null) onOptionSelected(option) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(option.text)
                }
            }
        }
    }
}