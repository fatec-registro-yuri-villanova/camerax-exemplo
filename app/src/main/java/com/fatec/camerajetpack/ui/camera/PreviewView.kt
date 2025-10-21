package com.fatec.camerajetpack.ui.camera

import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.* // Importante para o LaunchedEffect, remember, etc.
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService

/**
 * COMPOSABLE DO PREVIEW: Hospeda a View nativa PreviewView do CameraX.
 */
@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner,
    executor: ExecutorService,
    cameraViewRef: MutableState<CameraView?> // Referência de retorno
) {
    val context = LocalContext.current

    // 1. Estados para o PreviewView e ImageCapture, usados para ligar a lógica no LaunchedEffect
    val previewView: PreviewView = remember {
        PreviewView(context).apply {
            this.scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    // O ImageCapture agora é um estado para que a referência possa ser usada externamente.
    val imageCapture: MutableState<ImageCapture?> = remember { mutableStateOf(null) }

    // LaunchedEffect para gerenciar o processo assíncrono.
    LaunchedEffect(lifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Configuração do Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // Configuração da Captura de Imagem
            val capture = ImageCapture.Builder().build()
            imageCapture.value = capture // Atribui o valor de captura

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, capture
                )

                cameraViewRef.value = CameraView(imageCapture.value, executor)
                Log.d("CameraX", "Casos de uso vinculados e CameraView ref atualizada.")

            } catch (exc: Exception) {
                Log.e("CameraX", "Falha ao vincular casos de uso", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }


    AndroidView(
        modifier = modifier,
        factory = { previewView } // Apenas retorna a View criada no remember
    )
}