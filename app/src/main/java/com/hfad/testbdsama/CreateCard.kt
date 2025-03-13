package com.hfad.testbdsama

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hfad.testbdsama.Data.MainDB
import com.hfad.testbdsama.Data.prod
import com.hfad.testbdsama.ui.theme.LiteGray
import com.hfad.testbdsama.ui.theme.gray
import com.hfad.testbdsama.ui.theme.green
import kotlinx.coroutines.launch

@Composable
fun CreateCard(
    mainDB: MainDB,
    onSave: () -> Unit,
    editingProduct: prod? = null // Параметр для редактирования
) {
    var text1 by remember {
        mutableStateOf(
            editingProduct?.name ?: ""
        )
    } // Заполняем поле, если редактируем
    var text2 by remember {
        mutableStateOf(
            editingProduct?.descriptoin ?: ""
        )
    } // Заполняем поле, если редактируем
    val coroutineScope = rememberCoroutineScope()

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
                label = {
                    Text(
                        text = "Название",
                        fontSize = 17.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(12.dp))
                    .border(
                        width = 3.dp,
                        brush = SolidColor(green),
                        shape = RoundedCornerShape(12.dp)
                    )

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
                label = {
                    Text(
                        text = "Описание",
                        fontSize = 17.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(12.dp))
                    .border(
                        width = 3.dp,
                        brush = SolidColor(green),
                        shape = RoundedCornerShape(12.dp)
                    )

            )
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
                            } else {
                                // Создаем новую карточку
                                mainDB.dao.insertProd(
                                    prod(
                                        null,
                                        text1,
                                        text2
                                    )
                                )
                            }
                            onSave()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = green,

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