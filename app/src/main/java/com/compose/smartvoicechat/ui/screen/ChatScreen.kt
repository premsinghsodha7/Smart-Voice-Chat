package com.compose.smartvoicechat.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.PauseCircle
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.compose.smartvoicechat.R
import com.compose.smartvoicechat.model.ChatMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.initializeTTS(context)
    }

    val messages by viewModel.chatMessages.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val isTyping by viewModel.isTyping.collectAsState()
    val readingMessageId by viewModel.readingMessageId.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Smart Voice Chat", style = MaterialTheme.typography.titleLarge)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ){

            if (isListening){
                Image(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                        .padding(24.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.circle_dots_horizontal_svgrepo_com),
                    contentDescription = "Listening"
                )
            }

            if (isTyping){
                Image(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                        .padding(24.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.thinking),
                    contentDescription = "Listening"
                )
            }

            /*LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                items(messages) { message ->
                    ChatMessageItem(
                        message = message,
                        isPlaying = readingMessageId == message.id,
                        onPlayClicked = {
                            if (readingMessageId == message.id) {
                                viewModel.stopReading()
                            } else {
                                viewModel.readMessage(message.id, message.text)
                            }
                        }
                    )
                }

                item {
                    Spacer(Modifier.height(100.dp))
                }
            }*/

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .align(alignment = Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isListening) {
                    ListeningIndicator("Listening...")
                }

                if (isTyping) {
                    ListeningIndicator("Thinking...")
                }

                FloatingActionButton(
                    modifier = Modifier,
                    shape = CircleShape,
                    onClick = { viewModel.onMicTapped(context) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic, contentDescription = "Mic", tint = Color.White)
                }
            }

            if (messages.isNotEmpty())
            IconButton(
                onClick = {
                    if (readingMessageId != null) {
                        viewModel.stopReading()
                    } else {
                        messages.firstOrNull()?.let { message ->
                            viewModel.readMessage(message.id, message.text)
                        }
                    }
                },
                modifier = Modifier
                    .padding(24.dp)
                    .align(Alignment.BottomStart)
                    .size(64.dp)
            ) {
                Icon(
                    imageVector = if (readingMessageId != null) Icons.Outlined.PauseCircle else Icons.Outlined.PlayCircle,
                    contentDescription = if (readingMessageId != null) "Stop reading" else "Play reading"
                )
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    isPlaying: Boolean,
    onPlayClicked: () -> Unit
) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val backgroundColor = if (message.isUser) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                IconButton(
                    onClick = onPlayClicked,
                    modifier = Modifier
                        .align(Alignment.End)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Stop reading" else "Play reading"
                    )
                }
            }
        }
    }
}

@Composable
fun ListeningIndicator(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .padding(8.dp),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleMedium
    )
}