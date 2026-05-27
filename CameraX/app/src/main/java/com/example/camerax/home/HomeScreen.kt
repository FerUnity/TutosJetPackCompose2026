package com.example.camerax.home

import android.Manifest
import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.util.concurrent.Executor

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )
    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                expandedHeight = 80.dp,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                ),
                title = {
                    Text(text = "Camara")
                },
                actions = {
                    Text(text = "Opciones")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color(0xFFDC294C),
                contentColor = Color.White,
                onClick = {
                    tomarFoto(
                        cameraController = cameraController,
                        executor = ContextCompat.getMainExecutor(context),
                        context = context
                    )
                }
            )
            {
                Text(text = "Capturar")
            }
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                color = Color(0xFF7171D0)
            ) {
                Text(text = "Camara")
            }
        }

    ) {
        if (cameraPermissionState.status.isGranted) {
            Toast.makeText(context, "Permiso de camara aceptado", Toast.LENGTH_SHORT).show()
            CamaraComposable(
                cameraController = cameraController,
                lifecycleOwner = LocalLifecycleOwner.current,
                modifier = Modifier.padding(it)
            )
        } else {
            Text(text = "Permiso de camara denegado", modifier = Modifier.padding(it))
            Toast.makeText(context, "Permiso de camara denegado", Toast.LENGTH_SHORT).show()
        }
    }

}
//Investigar fun coil, que sirve para cargar las imagenes de internet en el app.

//Creamos otra fun para tomar la foto y ademas la guarde en el movil.
// Fun se llama desde el boton FloatingActionButton():
private fun tomarFoto(
    cameraController: LifecycleCameraController,
    executor: Executor,
    context: Context
) {
//    Guardamos la foto en un arch teporal
    val file = File.createTempFile(
        "imageTest",
        ".jpg"
    )
    val outputDirectory = ImageCapture.OutputFileOptions.Builder(file).build()
    cameraController.takePicture(
        outputDirectory,
         executor,
        object : ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            Toast.makeText(context, "Foto guardada en ${outputFileResults.savedUri} y en ${file.absolutePath}",
                Toast.LENGTH_SHORT).show()
            println("Foto guardada en ${outputFileResults.savedUri} y en ${file.absolutePath}")
        }

            override fun onError(p0: ImageCaptureException) {
                Toast.makeText(context, "Error al guardar la foto", Toast.LENGTH_SHORT).show()
            }
        }

    )
}

//Creamos aparte la fun para hacer funcionar la camara en esta app. Se llama desde el composable HomeScreen():
@Composable
private fun CamaraComposable(
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier
) {
//    val context = LocalContext.current
/*    val cameraController = remember {
        LifecycleCameraController(context)
    }*/
//       Por si se abre otra app que use camara, por ej Wassap, que se cierre la camara:
//    val lifecycleOwner = LocalLifecycleOwner.current
    cameraController.bindToLifecycle(lifecycleOwner)
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            previewView.controller = cameraController
            previewView

        })
}
