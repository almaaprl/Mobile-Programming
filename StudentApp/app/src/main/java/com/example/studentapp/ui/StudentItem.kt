package com.example.studentapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.Data.Siswa

val avatarColors = listOf(
    Color(0xFFD4BBFF), // ungu muda
    Color(0xFFB2E5C8), // hijau muda
    Color(0xFFFFCBA4), // oranye muda
    Color(0xFFFFB3C6), // pink muda
    Color(0xFFB3D9FF), // biru muda
    Color(0xFFFFEBA0)  // kuning muda
)

fun getAvatarColor(nama: String): Color {
    val index = nama.hashCode().and(0x7FFFFFFF) % avatarColors.size
    return avatarColors[index]
}

fun getInitials(nama: String): String {
    val parts = nama.trim().split(" ").filter { it.isNotEmpty() }
    return when {
        parts.size >= 2 -> "${parts[0].first()}${parts[1].first()}".uppercase()
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "?"
    }
}

@Composable
fun StudentItem(
    siswa: Siswa,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(getAvatarColor(siswa.nama)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getInitials(siswa.nama),
                    fontSize = 15.sp,
                    color = Color(0xFF333333),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = siswa.nama,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = siswa.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF888888)
                )
            }

            IconButton(
                onClick = onEdit,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color(0xFF7B52AB),
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}