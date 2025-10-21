package com.fatec.camerajetpack.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import com.fatec.camerajetpack.ui.camera.CameraView
import com.fatec.camerajetpack.ui.camera.ImagePreviewScreen
import com.fatec.camerajetpack.utils.PermissionUtils
import com.fatec.camerajetpack.utils.ImageUtils
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun MainScreen(
    modifier: Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // 1. ESTADO CHAVE: Controla qual tela estamos vendo.
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Executor de thread para as operações de I/O do CameraX
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    // 2. Gerenciamento de Permissões
    var hasCameraPermission by remember {
        mutableStateOf(
            PermissionUtils.isCameraPermissionGranted(
                context
            )
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasCameraPermission = isGranted }
    )

    // Solicita a permissão na primeira vez que o Composable é composto
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(PermissionUtils.CAMERA_PERMISSION)
        }
    }

    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            if (hasCameraPermission) {
                // 3. LÓGICA DE FLUXO
                if (capturedImageUri == null) {
                    // MODO CÂMERA
                    CameraMode(
                        context = context,
                        lifecycleOwner = lifecycleOwner,
                        cameraExecutor = cameraExecutor,
                        onImageCaptured = { file ->
                            capturedImageUri = file.toUri()
                        } // Muda para o Preview
                    )
                } else {
                    // MODO PREVIEW
                    PreviewMode(
                        uri = capturedImageUri!!,
                        onSave = { uri ->
                            ImageUtils.saveImageToGallery(context, uri)
                            Toast.makeText(context, "Imagem salva na galeria!", Toast.LENGTH_LONG)
                                .show()
                            capturedImageUri = null // Volta para Câmera
                        },
                        onDiscard = {
                            capturedImageUri = null // Volta para Câmera
                        }
                    )
                }
            } else {
                Text(
                    "Aguardando Permissão da Câmera...",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    // Limpeza: Garante que o Executor de thread seja desligado
    DisposableEffect(Unit) {
        onDispose { cameraExecutor.shutdown() }
    }
}

@Composable
fun CameraMode(
    context: Context, lifecycleOwner: LifecycleOwner, cameraExecutor: ExecutorService,
    onImageCaptured: (File) -> Unit
) {
    val cameraViewRef = remember { mutableStateOf<CameraView?>(null) }

    // Variável de conveniência para verificar se a câmera está pronta
    val isCameraReady = cameraViewRef.value != null

    Box(modifier = Modifier.fillMaxSize()) {
        CameraView(
            modifier = Modifier.fillMaxSize(),
            lifecycleOwner = lifecycleOwner,
            executor = cameraExecutor,
            cameraViewRef = cameraViewRef
        )

        Button(
            onClick = {
                android.util.Log.d("CameraClick", "Botão Clicado. Câmera Pronta: $isCameraReady")

                if (cameraViewRef.value != null) {
                    cameraViewRef.value!!.takePhoto(context, onImageCaptured)
                } else {
                    android.util.Log.e("CameraClick", "cameraViewRef está NULO, não pode tirar a foto.")
                }
            },
            enabled = isCameraReady, // Desabilita se for null
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
        ) {
            if (isCameraReady) {
                Text("Tirar Foto")
            } else {
                Text("Câmera a carregar...")
            }
        }
    }
}

@Composable
fun PreviewMode(uri: Uri, onSave: (Uri) -> Unit, onDiscard: () -> Unit) {
    ImagePreviewScreen(uri = uri, onSave = onSave, onDiscard = onDiscard)
}