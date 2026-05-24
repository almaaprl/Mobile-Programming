package com.example.studentapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val PurplePrimary = Color(0xFF6B3FA0)

@Composable
fun FormInput(
    nama: String,
    email: String,
    onNamaChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onTambahClick: () -> Unit
) {
    val purpleColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = PurplePrimary,
        unfocusedBorderColor = Color(0xFFCCBBEE),
        focusedLabelColor = PurplePrimary,
        unfocusedLabelColor = Color(0xFF9E9E9E),
        cursorColor = PurplePrimary
    )

    Column {
        OutlinedTextField(
            value = nama,
            onValueChange = onNamaChange,
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = purpleColors,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = purpleColors,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onTambahClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PurplePrimary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Tambah Siswa",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}