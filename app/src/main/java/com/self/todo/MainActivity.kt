package com.self.todo

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.self.todo.navigation.SetupNavGraph
import com.self.todo.ui.theme.ToDoTheme
import com.self.todo.ui.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().apply {
            setOnExitAnimationListener { screen ->
                val fadeOut = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.ALPHA,
                    1.0f,
                    1.0f,
                    0.0f,
                )
                fadeOut.duration = 500
                val fadeOut2 = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.TRANSLATION_Y,
                    0.0f,
                    0.0f,
                    300.0f
                )
                fadeOut2.duration = 500

                val fadeOut3 = ObjectAnimator.ofFloat(
                    screen.view,
                    View.ALPHA,
                    1.0f,
                    1.0f,
                    0.0f
                )
                fadeOut3.duration = 500

                val animatorSet = AnimatorSet()
                animatorSet.play(fadeOut)
                animatorSet.play(fadeOut2)
                animatorSet.play(fadeOut3)
                animatorSet.doOnEnd { screen.remove() }
                animatorSet.start()
            }
        }
        setContent {
            ToDoTheme {
                navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    sharedViewModel
                )
            }
        }
    }
}