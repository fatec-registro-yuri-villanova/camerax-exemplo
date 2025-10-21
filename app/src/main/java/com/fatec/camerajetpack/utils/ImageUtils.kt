package com.fatec.camerajetpack.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import android.util.Log

/**
 * UTILITY OBJECT: Singleton responsável por salvar o arquivo de imagem no MediaStore (Galeria).
 */
object ImageUtils {

    fun saveImageToGallery(context: Context, imageUri: Uri) {
        val file = File(imageUri.path ?: return)

        // 1. Define os metadados da imagem
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Organiza a imagem em uma pasta específica na Galeria
                put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/CameraJetpack")
            }
        }

        val resolver = context.contentResolver
        var uri: Uri? = null

        try {
            // 2. Insere o registro e obtém a URI final do local permanente
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            if (uri != null) {
                // 3. Copia os dados do arquivo temporário para o destino permanente
                resolver.openOutputStream(uri).use { outputStream ->
                    file.inputStream().use { inputStream ->
                        inputStream.copyTo(outputStream!!)
                    }
                }

                // 4. Limpeza: Deleta o arquivo temporário do cache.
                file.delete()
            }
        } catch (e: Exception) {
            Log.e("ImageUtils", "Falha ao salvar imagem na galeria: ${e.message}")
            // Em caso de falha, tenta reverter a criação do registro
            uri?.let { resolver.delete(it, null, null) }
        }
    }
}