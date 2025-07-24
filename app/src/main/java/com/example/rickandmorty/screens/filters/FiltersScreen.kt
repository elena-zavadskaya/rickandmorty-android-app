package com.example.rickandmorty.screens.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.rickandmorty.navigation.NavRoutes
import com.example.rickandmorty.presentation.filters.FiltersViewModel
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(
    navController: NavHostController? = null,
    viewModel: FiltersViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val statusOptions = listOf("Alive", "Dead", "Unknown")
    val genderOptions = listOf("Female", "Male", "Genderless", "Unknown")

    var statusError by remember { mutableStateOf(false) }
    var genderError by remember { mutableStateOf(false) }

    fun String.urlEncoded(): String = URLEncoder.encode(this, "UTF-8")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Фильтры",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    if (navController != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Назад",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = viewModel.name ?: "",
                        onValueChange = { viewModel.setName(it) },
                        label = { Text("Имя", style = MaterialTheme.typography.bodyMedium) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        trailingIcon = {
                            if (viewModel.name?.isNotEmpty() == true) {
                                IconButton(onClick = { viewModel.setName(null) }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Очистить",
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = viewModel.species ?: "",
                        onValueChange = { viewModel.setSpecies(it) },
                        label = { Text("Вид", style = MaterialTheme.typography.bodyMedium) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        trailingIcon = {
                            if (viewModel.species?.isNotEmpty() == true) {
                                IconButton(onClick = { viewModel.setSpecies(null) }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Очистить",
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = viewModel.type ?: "",
                        onValueChange = { viewModel.setType(it) },
                        label = { Text("Тип", style = MaterialTheme.typography.bodyMedium) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        trailingIcon = {
                            if (viewModel.type?.isNotEmpty() == true) {
                                IconButton(onClick = { viewModel.setType(null) }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Очистить",
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Статус",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, bottom = 8.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup()
                        .padding(16.dp)
                ) {
                    statusOptions.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (viewModel.status == option),
                                    onClick = {
                                        viewModel.setStatus(option)
                                        statusError = false
                                    },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (viewModel.status == option),
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                    unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            )
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }

                    if (statusError) {
                        Text(
                            text = "Недопустимое значение статуса",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, start = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Пол",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, bottom = 8.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup()
                        .padding(16.dp)
                ) {
                    genderOptions.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (viewModel.gender == option),
                                    onClick = {
                                        viewModel.setGender(option)
                                        genderError = false
                                    },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (viewModel.gender == option),
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                    unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            )
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }

                    if (genderError) {
                        Text(
                            text = "Недопустимое значение пола",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, start = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        viewModel.resetAllFilters()
                        statusError = false
                        genderError = false
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Сбросить")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        statusError = viewModel.status != null && viewModel.status !in statusOptions
                        genderError = viewModel.gender != null && viewModel.gender !in genderOptions

                        if (!statusError && !genderError) {
                            val queryParams = listOfNotNull(
                                viewModel.name?.takeIf { it.isNotBlank() }?.let { "name=${it.urlEncoded()}" },
                                viewModel.status?.takeIf { it.isNotBlank() }?.let { "status=${it.urlEncoded()}" },
                                viewModel.species?.takeIf { it.isNotBlank() }?.let { "species=${it.urlEncoded()}" },
                                viewModel.type?.takeIf { it.isNotBlank() }?.let { "type=${it.urlEncoded()}" },
                                viewModel.gender?.takeIf { it.isNotBlank() }?.let { "gender=${it.urlEncoded()}" }
                            ).joinToString("&")

                            navController?.navigate("${NavRoutes.HOME}?$queryParams") {
                                popUpTo(NavRoutes.HOME) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Применить")
                }
            }
        }
    }
}