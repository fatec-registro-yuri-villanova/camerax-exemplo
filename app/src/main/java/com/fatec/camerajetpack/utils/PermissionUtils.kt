package com.fatec.camerajetpack.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * UTILITY OBJECT: Singleton para verificar o estado da permissão da Câmera.
 */
object PermissionUtils {
    val CAMERA_PERMISSION = Manifest.permission.CAMERA

    /**
     * Verifica se a permissão da Câmera já foi concedida ao aplicativo.
     * @param context: O contexto da Activity/Aplicação.
     * @return true se a permissão foi concedida.
     */
    fun isCameraPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            CAMERA_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }
}