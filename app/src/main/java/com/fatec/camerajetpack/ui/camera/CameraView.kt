package com.fatec.camerajetpack.ui.camera

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService

/**
 * CLASSE DE CONTROLE: Contém a função takePhoto() que será chamada pelo botão do Compose.
 */
class CameraView(
    private val imageCapture: ImageCapture?,
    private val executor: ExecutorService
) {
    fun takePhoto(context: Context, onImageCaptured: (File) -> Unit) {
        // 🚨 PONTO DE VERIFICAÇÃO 1: O ImageCapture foi configurado corretamente?
        val imageCaptureInstance = imageCapture
        if (imageCaptureInstance == null) {
            Log.e("CameraX", "ERRO FATAL: ImageCapture é NULO no momento da foto.")
            Toast.makeText(context, "Câmera não inicializada corretamente.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("CameraX", "ImageCapture OK. Iniciando processo de foto.")

        // 1. Cria um nome e local (cache) para o arquivo temporário
        val photoFile = File(
            context.cacheDir,
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // 2. Aciona o ImageCapture.takePicture
        imageCaptureInstance.takePicture(
            outputOptions,
            executor, // Usa a thread específica para salvar a imagem
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    // 🚨 PONTO DE VERIFICAÇÃO 2: Captura de Erro
                    Log.e("CameraX", "Falha na captura (ImageCaptureException): ${exc.message}", exc)
                    Toast.makeText(context, "Falha ao salvar a imagem: ${exc.imageCaptureError}", Toast.LENGTH_LONG).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    // 🚨 PONTO DE VERIFICAÇÃO 3: Sucesso
                    Log.i("CameraX", "Foto salva com sucesso no cache: ${photoFile.absolutePath}")
                    onImageCaptured(photoFile)
                }
            }
        )
    }
}