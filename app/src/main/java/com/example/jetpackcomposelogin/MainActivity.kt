package com.example.jetpackcomposelogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.jetpackcomposelogin.ui.theme.JetpackComposeLoginTheme
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.DarkGray.toArgb()),
            navigationBarStyle = SystemBarStyle.light(Color.Blue.toArgb(), Color.Cyan.toArgb())
        )
        setContent {
            JetpackComposeLoginTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    // Here is the updated code - new view to be declared
                    MainView()
                }
            }
        }
    }
}

@Composable
fun MainView() {

    // Preserve and observe the state of the UI
    var userIsAuthenticated by remember { mutableStateOf(false) }
    var appJustLaunched by remember { mutableStateOf(true) }

    // Scaffold for Compose helps manage window insets for me,
    // ensuring that your content is properly padded
    // and not obscured by system UI.
    Scaffold { innerPadding ->

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // TODO add title logic here next!!!!

            val title = if (userIsAuthenticated) {
                stringResource(R.string.logged_in_title)
            } else {
                if (appJustLaunched) {
                    stringResource(R.string.initial_title)
                } else {
                    stringResource(R.string.logged_out_title)
                }
            }

            Title(
//                text = stringResource(R.string.initial_title),
                text = title,
                modifier = Modifier.padding(innerPadding)
            )

            if (userIsAuthenticated) {

                UserInfoRow(
                    label = stringResource(R.string.name_label),
                    value = "Name goes here"
                )

                UserInfoRow(
                    label = stringResource(R.string.email_label),
                    value = "Email goes here"
                )

                UserPicture(
                    url = "https://images.ctfassets.net/23aumh6u8s0i/5hHkO5DxWMPxDjc2QZLXYf/403128092dedc8eb3395314b1d3545ad/icon-user.png",
                    description = "Description goes here"
                )

            }

            val buttonText: String
            val onClickAction: () -> Unit
            if (userIsAuthenticated) {
                buttonText = stringResource(R.string.log_out_button)
                onClickAction = {
                    userIsAuthenticated = false
                    appJustLaunched = false
                }
            } else {
                buttonText = stringResource(R.string.log_in_button)
                onClickAction = {
                    userIsAuthenticated = true
                }

            }

            LogButton(
                text = buttonText,
                onClick = onClickAction
            )

        }

    }

}

@Composable
fun Title(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
        ),
        modifier = modifier
    )
}

@Composable
fun LogButton(
    text: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
        ) {
            Text(
                text = text,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun UserInfoRow(
    label: String,
    value: String
) {
    Row {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )
        Spacer(
            modifier = Modifier.width(10.dp)
        )
        Text(
            text = value,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontSize = 20.sp
            )
        )
    }
}

@Composable
fun UserPicture(
    url: String,
    description: String
) {

    val imgPainter = rememberAsyncImagePainter(
        model = url
    )

    val imgAsyncStateFlow = imgPainter.state

    val imgAsyncState = imgAsyncStateFlow.collectAsState()

    val transition by animateFloatAsState(
        targetValue = if (imgAsyncState.value is AsyncImagePainter.State.Success) 1f else 0f
    )

    if (imgAsyncState.value is AsyncImagePainter.State.Loading || imgAsyncState.value is AsyncImagePainter.State.Empty) {

        LoadingAnimation()

    } else {

        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = imgPainter,
                contentDescription = description,
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .scale(0.8f + (0.2f.times(transition)))
                    .graphicsLayer { rotationX = (1.0f * transition) + 5.0f }
                    .alpha(min(1.0f, transition / 0.2f))
            )
        }

    }
}

@Composable
fun LoadingAnimation() {
    val animation = rememberInfiniteTransition()
    val progress by animation.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Restart,
        )
    )

    Column(
        modifier = Modifier.requiredHeight(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(60.dp)
                .scale(progress)
                .alpha(1f - progress)
                .border(
                    5.dp,
                    color = Color.Black,
                    shape = CircleShape
                )
        )

    }
}
