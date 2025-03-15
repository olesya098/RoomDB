package com.hfad.testbdsama

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceTheme.colors
import androidx.glance.appwidget.Tracing.enabled
import com.hfad.testbdsama.Data.MainDB
import com.hfad.testbdsama.Data.prod
import com.hfad.testbdsama.ui.theme.green
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun CreateCard(
    mainDB: MainDB,
    onSave: () -> Unit,
    editingProduct: prod? = null
) {
    var text1 by remember { mutableStateOf(editingProduct?.name ?: "") }
    var text2 by remember { mutableStateOf(editingProduct?.descriptoin ?: "") }
    var selectedImages by remember { mutableStateOf<List<ByteArray>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Состояние для хранения оригинальных изображений
    var originalImages by remember { mutableStateOf<List<ByteArray>>(emptyList()) }

    // Загрузка изображений при редактировании
    LaunchedEffect(editingProduct) {
        if (editingProduct != null) {
            // Загружаем изображения из базы данных
            val images = mainDB.dao.getImagesForProduct(editingProduct.id!!).firstOrNull()
            selectedImages = images?.map { it.imageData } ?: emptyList()
            originalImages = selectedImages // Сохраняем оригинальные изображения для сравнения
        }
    }

    // Лончер для выбора нескольких изображений
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            coroutineScope.launch {
                val newImages = uris.mapNotNull { uri ->
                    loadImageWithGlide(context, uri) // Используем Glide для загрузки изображений
                }
                selectedImages = selectedImages + newImages
            }
        }
    )

    // Проверка на наличие изменений
    val hasChanges = remember(editingProduct, text1, text2, selectedImages) {
        if (editingProduct == null) {
            // Если карточка новая, изменения есть, если поля не пустые
            text1.isNotEmpty() && text2.isNotEmpty()
        } else {
            // Если карточка редактируется, проверяем изменения
            text1 != editingProduct.name ||
                    text2 != editingProduct.descriptoin ||
                    selectedImages != originalImages
        }
    }

    Card(
        modifier = Modifier
            .fillMaxSize(0.9f)
            .verticalScroll(rememberScrollState())
            .padding(top = 40.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = text1,
                onValueChange = { text1 = it },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                ),
                label = { Text("Название", fontSize = 17.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = text2,
                onValueChange = { text2 = it },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                ),
                label = { Text("Описание", fontSize = 17.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Отображение выбранных изображений с горизонтальной прокруткой
            if (selectedImages.isNotEmpty()) {
                androidx.compose.foundation.lazy.LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedImages) { imageData ->
                        Image(
                            bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                                .asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }

            // Кнопка для выбора изображений
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = green),
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .size(height = 50.dp, width = 200.dp)
                ) {
                    Text("Выбрать несколько", fontSize = 17.sp, color = Color.White)
                }
            }
            Button(
                onClick = {
                    if (text1.isNotEmpty() && text2.isNotEmpty()) {
                        coroutineScope.launch {
                            if (editingProduct != null) {
                                // Обновляем существующую карточку
                                mainDB.dao.updateProd(
                                    editingProduct.copy(
                                        name = text1,
                                        descriptoin = text2
                                    )
                                )

                                // Удаляем старые изображения
                                mainDB.dao.deleteImagesForProduct(editingProduct.id!!)

                                // Сохраняем новые изображения
                                selectedImages.forEach { imageData ->
                                    mainDB.dao.insertImage(
                                        com.hfad.testbdsama.Data.Image(
                                            null,
                                            editingProduct.id!!,
                                            imageData
                                        )
                                    )
                                }
                            } else {
                                // Создаем новую карточку
                                val newProduct = prod(null, text1, text2)
                                val productId = mainDB.dao.insertProd(newProduct).toInt()

                                // Сохраняем изображения
                                selectedImages.forEach { imageData ->
                                    mainDB.dao.insertImage(
                                        com.hfad.testbdsama.Data.Image(
                                            null,
                                            productId,
                                            imageData
                                        )
                                    )
                                }
                            }

                            // Вызываем onSave для обновления состояния в MainActivity
                            onSave()
                        }
                    }
                },
                enabled = hasChanges, // Кнопка активна только при наличии изменений
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasChanges) green else Color.Gray // Изменение цвета кнопки
                ),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(height = 50.dp, width = 200.dp)
            ) {
                Text(
                    if (editingProduct != null) "Обновить" else "Сохранить",
                    fontSize = 17.sp,
                    color = Color.White
                )
            }
        }
    }
}