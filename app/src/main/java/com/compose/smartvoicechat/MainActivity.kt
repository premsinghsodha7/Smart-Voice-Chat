package com.compose.smartvoicechat

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.compose.smartvoicechat.ui.screen.ChatScreen
import com.compose.smartvoicechat.ui.screen.ChatViewModel

class MainActivity : ComponentActivity() {
    //private val viewModel: ChatViewModel by viewModels()

    private fun checkAndRequestPermissions() {
        val permission = Manifest.permission.RECORD_AUDIO
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request mic permission
        checkAndRequestPermissions()

        setContent {
            MaterialTheme {
                val viewModel: ChatViewModel = viewModel()
                ChatScreen(viewModel)
            }
        }
    }
}






