package com.example.favoritecolors.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.favoritecolors.ui.state.ColorsState
import com.example.favoritecolors.ui.viewModel.FavoriteColorsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthDialog(viewModel: FavoriteColorsViewModel, state: ColorsState) {
    BasicAlertDialog(
        onDismissRequest = { viewModel.toggleRegistrationDialog() },
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp
        ) {
            Column {
                PrimaryTabRow(selectedTabIndex = if (state.signUpTab) 0 else 1) {
                    Tab(
                        selected = state.signUpTab,
                        onClick = { viewModel.setSignupTab(true) },
                        text = { Text(text = "Sign Up") })
                    Tab(
                        selected = !state.signUpTab,
                        onClick = { viewModel.setSignupTab(false) },
                        text = { Text(text = "Login") })
                }
                if (state.signUpTab) {
                    Column(
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 5.dp),
                        verticalArrangement = Arrangement.spacedBy(7.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        EmailField(viewModel, state)
                        PasswordField(viewModel, state)
                        ConfirmPasswordField(viewModel, state)
                        Text(
                            text = state.authDialogMessage,
                            color = if (state.authDialogError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                        Button(
                            onClick = { viewModel.signupHandler() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp)
                                .padding(bottom = 5.dp)
                        ) {
                            Text(text = "Sign Up")
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        EmailField(viewModel, state)
                        PasswordField(viewModel, state)
                        Button(
                            onClick = { viewModel.loginHandler() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp, vertical = 5.dp)
                        ) {
                            Text(text = "Login")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmailField(viewModel: FavoriteColorsViewModel, state: ColorsState) {
    OutlinedTextField(
        value = state.email,
        onValueChange = { viewModel.updateEmail(it) },
        label = { Text("Enter email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = state.authDialogError
    )
}

@Composable
fun PasswordField(viewModel: FavoriteColorsViewModel, state: ColorsState) {
    OutlinedTextField(
        value = state.password,
        onValueChange = { viewModel.updatePassword(it) },
        label = { Text("Enter password") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = state.authDialogError
    )
}

@Composable
fun ConfirmPasswordField(viewModel: FavoriteColorsViewModel, state: ColorsState) {
    OutlinedTextField(
        value = state.confirmPassword,
        onValueChange = { viewModel.updateConfirmPassword(it) },
        label = { Text("Confirm Password") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = state.authDialogError
    )
}