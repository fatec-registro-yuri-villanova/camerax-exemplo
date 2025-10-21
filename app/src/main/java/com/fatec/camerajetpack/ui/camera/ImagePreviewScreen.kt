package com.fatec.camerajetpack.ui.camera

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * TELA DE DECISÃO: Exibe a foto capturada e gerencia as ações do usuário.
 */
@Composable
fun ImagePreviewScreen(
    uri: Uri,
    onSave: (Uri) -> Unit,
    onDiscard: () -> Unit // Notifica a MainScreen para voltar ao modo Câmera
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Exibe a imagem usando a URI temporária
        AsyncImage(
            model = uri,
            contentDescription = "Pré-visualização da foto",
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentScale = ContentScale.Crop
        )

        // Controles de Ação
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onDiscard,
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) { Text("Descartar") }

            Button(
                onClick = { onSave(uri) },
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) { Text("Salvar") }
        }
    }
}