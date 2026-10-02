package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TrackDictionary
import com.example.ui.MainViewModel
import com.example.ui.theme.ImmersiveAmberWarm
import com.example.ui.theme.ImmersiveBackground
import com.example.ui.theme.ImmersiveCardBorder
import com.example.ui.theme.ImmersiveCardBorderSubtle
import com.example.ui.theme.ImmersiveLilac
import com.example.ui.theme.ImmersiveLilacContainer
import com.example.ui.theme.ImmersiveMint
import com.example.ui.theme.ImmersiveMintDark
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.util.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun ExperimentsScreen(
    viewModel: MainViewModel? = null,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    var parentDirectoryPath by remember { mutableStateOf(TrackDictionary.BASE_PLUGINS_DIR) }
    var currentFolderName by remember { mutableStateOf("") }
    var newFolderName by remember { mutableStateOf("") }

    var isRenaming by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isSuccessStatus by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .widthIn(max = 720.dp)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .testTag("experiments_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
            border = BorderStroke(1.dp, ImmersiveCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = ImmersiveLilacContainer,
                        border = BorderStroke(1.dp, ImmersiveLilac.copy(alpha = 0.4f)),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Science,
                                contentDescription = null,
                                tint = ImmersiveLilac,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "ЛАБОРАТОРИЯ ЭКСПЕРИМЕНТОВ",
                            color = TextTertiary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Экспериментальные функции",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Здесь находятся экспериментальные утилиты и прототипы для управления конфигурациями и файлами.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }

        // Basic Folder Rename Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("folder_rename_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
            border = BorderStroke(1.dp, ImmersiveCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ImmersiveLilacContainer,
                        border = BorderStroke(1.dp, ImmersiveLilac.copy(alpha = 0.4f)),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.DriveFileRenameOutline,
                                contentDescription = null,
                                tint = ImmersiveLilac,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "ПЕРЕИМЕНОВАНИЕ ПАПОК",
                            color = TextTertiary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Изменение названия директории",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Parent directory path input
                OutlinedTextField(
                    value = parentDirectoryPath,
                    onValueChange = { parentDirectoryPath = it },
                    label = { Text("Путь к родительской папке", fontSize = 11.sp) },
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = TextPrimary
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ImmersiveLilac,
                        unfocusedBorderColor = ImmersiveCardBorderSubtle,
                        focusedLabelColor = ImmersiveLilac,
                        unfocusedLabelColor = TextTertiary,
                        focusedContainerColor = ImmersiveBackground.copy(alpha = 0.5f),
                        unfocusedContainerColor = ImmersiveBackground.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_parent_dir_path")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Current folder name input
                OutlinedTextField(
                    value = currentFolderName,
                    onValueChange = { currentFolderName = it },
                    label = { Text("Текущее имя папки", fontSize = 11.sp) },
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = TextPrimary
                    ),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            tint = ImmersiveLilac,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ImmersiveLilac,
                        unfocusedBorderColor = ImmersiveCardBorderSubtle,
                        focusedLabelColor = ImmersiveLilac,
                        unfocusedLabelColor = TextTertiary,
                        focusedContainerColor = ImmersiveBackground.copy(alpha = 0.5f),
                        unfocusedContainerColor = ImmersiveBackground.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_current_folder_name")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // New folder name input (Max 9 chars)
                OutlinedTextField(
                    value = newFolderName,
                    onValueChange = { 
                        if (it.length <= 9) newFolderName = it 
                    },
                    label = { Text("Новое имя папки", fontSize = 11.sp) },
                    supportingText = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "${newFolderName.length}/9",
                                fontSize = 10.sp,
                                color = TextTertiary,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    },
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = TextPrimary
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ImmersiveLilac,
                        unfocusedBorderColor = ImmersiveCardBorderSubtle,
                        focusedLabelColor = ImmersiveLilac,
                        unfocusedLabelColor = TextTertiary,
                        focusedContainerColor = ImmersiveBackground.copy(alpha = 0.5f),
                        unfocusedContainerColor = ImmersiveBackground.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_new_folder_name")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action button
                Button(
                    onClick = {
                        val parentPath = parentDirectoryPath.trim()
                        val oldName = currentFolderName.trim()
                        val newName = newFolderName.trim()

                        if (oldName.isEmpty() || newName.isEmpty()) {
                            statusMessage = "Заполните текущее и новое имя папки"
                            isSuccessStatus = false
                            return@Button
                        }

                        if (oldName == newName) {
                            statusMessage = "Новое имя совпадает с текущим"
                            isSuccessStatus = false
                            return@Button
                        }

                        isRenaming = true

                        if (viewModel != null) {
                            viewModel.renameDirectory(parentPath, oldName, newName) { success, msg ->
                                isRenaming = false
                                isSuccessStatus = success
                                statusMessage = msg
                                if (success) {
                                    currentFolderName = newName
                                    newFolderName = ""
                                }
                            }
                        } else {
                            coroutineScope.launch {
                                val result = withContext(Dispatchers.IO) {
                                    val parent = File(parentPath)
                                    val source = File(parent, oldName)
                                    val target = File(parent, newName)

                                    if (!source.exists()) {
                                        return@withContext Pair(false, "Папка '$oldName' не найдена в $parentPath")
                                    }
                                    if (target.exists()) {
                                        return@withContext Pair(false, "Папка с именем '$newName' уже существует")
                                    }

                                    val success = source.renameTo(target)
                                    if (success) {
                                        AppLogger.s("Переименование директории: '$oldName' -> '$newName'")
                                        Pair(true, "Папка успешно переименована в '$newName'")
                                    } else {
                                        AppLogger.e("Не удалось переименовать '$oldName' в '$newName'")
                                        Pair(false, "Ошибка переименования (проверьте права доступа)")
                                    }
                                }

                                if (result.first) {
                                    TrackDictionary.notifyDirectoryRenamed(parentPath, oldName, newName)
                                    currentFolderName = newName
                                    newFolderName = ""
                                }

                                isRenaming = false
                                isSuccessStatus = result.first
                                statusMessage = result.second
                            }
                        }
                    },
                    enabled = !isRenaming,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ImmersiveLilac,
                        contentColor = ImmersiveBackground
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_execute_rename")
                ) {
                    if (isRenaming) {
                        CircularProgressIndicator(
                            color = ImmersiveBackground,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DriveFileRenameOutline,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ПЕРЕИМЕНОВАТЬ ПАПКУ",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }

                // Status message
                AnimatedVisibility(visible = statusMessage != null) {
                    statusMessage?.let { msg ->
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSuccessStatus) ImmersiveMintDark else ImmersiveAmberWarm.copy(alpha = 0.15f),
                            border = BorderStroke(
                                1.dp,
                                if (isSuccessStatus) ImmersiveMint.copy(alpha = 0.5f) else ImmersiveAmberWarm.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (isSuccessStatus) Icons.Default.CheckCircle else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (isSuccessStatus) ImmersiveMint else ImmersiveAmberWarm,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = msg,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
