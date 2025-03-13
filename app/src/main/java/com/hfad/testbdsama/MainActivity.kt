package com.hfad.testbdsama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hfad.testbdsama.Data.MainDB
import com.hfad.testbdsama.Data.prod
import com.hfad.testbdsama.ui.theme.LiteGray
import com.hfad.testbdsama.ui.theme.LiteGrayMedium
import com.hfad.testbdsama.ui.theme.TestBDsamaTheme
import com.hfad.testbdsama.ui.theme.gray
import com.hfad.testbdsama.ui.theme.green
import com.hfad.testbdsama.ui.theme.grey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var mainDB: MainDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var showCreateCard by remember { mutableStateOf(false) }
            var editingProduct by remember { mutableStateOf<prod?>(null) } // Состояние для редактирования
            val prodlist = mainDB.dao.gettAll().collectAsState(initial = emptyList())
            val coroutineScope = rememberCoroutineScope()

            TestBDsamaTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (showCreateCard) {
                        CreateCard(
                            mainDB = mainDB,
                            onSave = {
                                showCreateCard = false
                                editingProduct = null // Сбрасываем состояние редактирования
                            },
                            editingProduct = editingProduct // Передаем карточку для редактирования
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.82f),
                        ) {
                            items(prodlist.value) { product ->
                                CardInf(
                                    product = product,
                                    onDelete = {
                                        coroutineScope.launch {
                                            mainDB.dao.deleteProd(product) // Удаляем карточку
                                        }
                                    },
                                    onEdit = {
                                        editingProduct =
                                            product // Устанавливаем карточку для редактирования
                                        showCreateCard = true // Открываем экран редактирования
                                    }
                                )
                            }
                        }
                        Button(
                            onClick = { showCreateCard = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,

                                ),
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 40.dp)
                                .size(height = 50.dp, width = 200.dp)
                        ) {
                            Text(
                                "Создать карточку",
                                fontSize = 17.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun CardInf(
    product: prod,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    collapsedMaxLine: Int = 3
) {

    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .heightIn(min = 200.dp) // Минимальная высота карточки
            .animateContentSize() // Анимация изменения размера
            .clickable { isExpanded = !isExpanded }, // Разворачивание/сворачивание по клику
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок и разделитель
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = product.name,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.Black
                )
            }

            // Текст заметки
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = product.descriptoin,
                textAlign = TextAlign.Start,
                maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
                lineHeight = 25.sp,
            )

            // Кнопка "Показать/Скрыть"
            Text(
                text = if (isExpanded) "Скрыть" else "Показать",
                color = green,
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .align(Alignment.End)
                    .padding(end = 8.dp, bottom = 8.dp)
            )

            // Кнопки внизу
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            tint = green,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = green,
                            modifier = Modifier.size(30.dp)

                        )
                    }
                }
            }
        }

    }

}
