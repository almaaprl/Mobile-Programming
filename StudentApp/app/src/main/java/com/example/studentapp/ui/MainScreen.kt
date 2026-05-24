package com.example.studentapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.studentapp.Data.Siswa
import com.example.studentapp.ViewModel.StudentViewModel

@Composable
fun MainScreen(
    viewModel: StudentViewModel
) {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // State siswa yang sedang diedit (null = dialog tutup)
    var siswaYangDiedit by remember { mutableStateOf<Siswa?>(null) }
    var editNama by remember { mutableStateOf("") }
    var editEmail by remember { mutableStateOf("") }

    val siswaList by viewModel.siswaList.collectAsState()

    if (siswaYangDiedit != null) {
        AlertDialog(
            onDismissRequest = { siswaYangDiedit = null },
            shape = RoundedCornerShape(20.dp),
            title = {
                Text("Edit Data Siswa", style = MaterialTheme.typography.titleLarge)
            },
            text = {
                val purpleColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurplePrimary,
                    unfocusedBorderColor = Color(0xFFCCBBEE),
                    focusedLabelColor = PurplePrimary,
                    unfocusedLabelColor = Color(0xFF9E9E9E),
                    cursorColor = PurplePrimary
                )
                Column {
                    OutlinedTextField(
                        value = editNama,
                        onValueChange = { editNama = it },
                        label = { Text("Nama") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = purpleColors,
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = purpleColors,
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editNama.isNotBlank() && editEmail.isNotBlank() && editEmail.contains("@")) {
                            viewModel.editSiswa(siswaYangDiedit!!.copy(nama = editNama, email = editEmail))
                            siswaYangDiedit = null
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { siswaYangDiedit = null },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Batal")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 28.dp)
    ) {
        Text(
            text = "Registrasi Siswa",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF1A1A1A)
        )
        Text(
            text = "Kelola data siswa",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF888888)
        )

        Spacer(modifier = Modifier.height(24.dp))

        FormInput(
            nama = nama,
            email = email,
            onNamaChange = { nama = it },
            onEmailChange = { email = it },
            onTambahClick = {
                if (nama.isBlank()) return@FormInput
                if (email.isBlank()) return@FormInput
                if (!email.contains("@")) return@FormInput
                viewModel.tambahSiswa(nama, email)
                nama = ""
                email = ""
            }
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Daftar Siswa",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF1A1A1A)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (siswaList.isEmpty()) {
            Text(
                text = "Belum ada data siswa",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFAAAAAA)
            )
        }

        LazyColumn {
            items(siswaList) { siswa ->
                StudentItem(
                    siswa = siswa,
                    onDelete = { viewModel.hapusSiswa(siswa) },
                    onEdit = {
                        siswaYangDiedit = siswa
                        editNama = siswa.nama
                        editEmail = siswa.email
                    }
                )
            }
        }
    }
}