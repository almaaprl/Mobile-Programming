package com.example.todolistmobileapp

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

// ─────────────────────────────────────────────
// DATA MODEL
// ─────────────────────────────────────────────

data class Task(
    val id: Int,
    val subject: String,
    val title: String,
    val description: String,
    val time: String,
    val date: LocalDate,
    val isCompleted: Boolean = false
)

data class DateItem(
    val date: LocalDate,
    val isSelected: Boolean = false
)

// ─────────────────────────────────────────────
// COLORS
// ─────────────────────────────────────────────

val PrimaryBlue   = Color(0xFF4F7FFF)
val GradientStart = Color(0xFFD6E4FF)
val GradientEnd   = Color(0xFFEFF4FF)
val TextDark      = Color(0xFF1A1A2E)
val TextGray      = Color(0xFF6B7280)
val SuccessGreen  = Color(0xFF22C55E)
val DangerRed     = Color(0xFFEF4444)
val CardWhite     = Color(0xFFFFFFFF)
val BorderLight   = Color(0xFFE5E7EB)
val SubjectBg     = Color(0xFFEEF2FF)

// ─────────────────────────────────────────────
// MAIN ACTIVITY
// ─────────────────────────────────────────────

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppRoot()
            }
        }
    }
}

// ─────────────────────────────────────────────
// APP ROOT — atur navigasi antar halaman
// ─────────────────────────────────────────────

@Composable
fun AppRoot() {
    var selectedNavIdx by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        when (selectedNavIdx) {
            0 -> HomeScreen(
                selectedNavIdx = selectedNavIdx,
                onNavSelected = { selectedNavIdx = it }
            )
            1 -> ToDoListScreen(
                selectedNavIdx = selectedNavIdx,
                onNavSelected = { selectedNavIdx = it }
            )
            2 -> Box(modifier = Modifier.fillMaxSize()) {
                ProfileScreen()
                BottomNavBar(
                    selectedIndex = selectedNavIdx,
                    onNavSelected = { selectedNavIdx = it },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────
// TO DO LIST SCREEN
// ─────────────────────────────────────────────

@Composable
fun ToDoListScreen(selectedNavIdx: Int, onNavSelected: (Int) -> Unit) {
    val today = LocalDate.now()

    var tasks by remember {
        mutableStateOf(
            listOf(
                Task(1, "Pancasila", "ETS: Buat artikel tentang pancasila", "Membuat artikel sesuai tema tugas 1", "23:59", today, false),
                Task(2, "Agama",     "Resume Bab 3 Kerukunan Umat",         "Buat resume 2 halaman A4",            "20:00", today, false),
                Task(3, "Bahasa",    "Makalah Ejaan Bahasa Indonesia",       "Makalah min. 10 halaman",             "23:59", today, true),
                Task(4, "Pancasila", "Tugas Presentasi Kelompok",            "Slide min. 15 halaman",               "08:00", today.plusDays(1), false),
                Task(5, "Agama",     "Ujian Tengah Semester",                "Materi bab 1-5",                      "10:00", today.plusDays(1), false),
                Task(6, "Bahasa",    "Tugas Membaca Puisi",                  "Rekam video 3 menit",                 "15:00", today.plusDays(2), false),
            )
        )
    }

    var searchQuery     by remember { mutableStateOf("") }
    var selectedDateIdx by remember { mutableStateOf(0) }
    var showAddDialog   by remember { mutableStateOf(false) }
    var taskToDelete    by remember { mutableStateOf<Task?>(null) }

    val dates = (0..6).map { offset ->
        DateItem(date = today.plusDays(offset.toLong()), isSelected = offset == selectedDateIdx)
    }

    val selectedDate = dates[selectedDateIdx].date

    val filteredTasks = tasks
        .filter { it.date == selectedDate }
        .filter {
            searchQuery.isEmpty() ||
                    it.title.contains(searchQuery, ignoreCase = true) ||
                    it.subject.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true)
        }
        .sortedWith(
            compareBy<Task> { it.isCompleted }.thenBy { it.time }
        )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(GradientStart, GradientEnd)))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // judul atas
            Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 52.dp)) {
                Text(
                    text = "My To Do List",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = TextDark
                )
                Text(
                    text = "Let's start making a to-do list for today!",
                    fontSize = 13.sp,
                    color = TextGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(dates) { index, dateItem ->
                    DateChip(
                        dateItem = dateItem,
                        onClick = { selectedDateIdx = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = BorderLight,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Tidak ada tugas untuk hari ini",
                            color = TextGray,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredTasks, key = { it.id }) { task ->
                        TaskCard(
                            task = task,
                            onToggle = {
                                tasks = tasks.map {
                                    if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it
                                }
                            },
                            onDelete = { taskToDelete = task }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }

        // FAB
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = PrimaryBlue,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 80.dp)
                .size(52.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Tambah Tugas")
        }

        BottomNavBar(
            selectedIndex = selectedNavIdx,
            onNavSelected = onNavSelected,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    if (showAddDialog) {
        AddTaskDialog(
            selectedDate = selectedDate,
            onDismiss = { showAddDialog = false },
            onSave = { subject, title, desc, time, date ->
                val newId = (tasks.maxOfOrNull { it.id } ?: 0) + 1
                tasks = tasks + Task(newId, subject, title, desc, time, date, false)
                showAddDialog = false
            }
        )
    }

    taskToDelete?.let { task ->
        AlertDialog(
            onDismissRequest = { taskToDelete = null },
            icon = { Icon(Icons.Filled.Delete, contentDescription = null, tint = DangerRed) },
            title = { Text("Hapus Tugas?", fontWeight = FontWeight.Bold, color = TextDark) },
            text = {
                Text(
                    text = "\"${task.title}\" akan dihapus secara permanen.",
                    color = TextGray,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        tasks = tasks.filter { it.id != task.id }
                        taskToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Hapus", color = Color.White) }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { taskToDelete = null },
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Batal", color = TextGray) }
            },
            containerColor = CardWhite,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

// ─────────────────────────────────────────────
// SEARCH BAR
// ─────────────────────────────────────────────

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val keyboard = LocalSoftwareKeyboardController.current
    Card(
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search Task", color = Color(0xFF9CA3AF), fontSize = 14.sp) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor   = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor   = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { keyboard?.hide() })
            )
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(12.dp))
                    .background(PrimaryBlue).clickable { keyboard?.hide() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Search, contentDescription = "Cari", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────
// DATE CHIP
// ─────────────────────────────────────────────

@Composable
fun DateChip(dateItem: DateItem, onClick: () -> Unit) {
    val bgColor   by animateColorAsState(if (dateItem.isSelected) PrimaryBlue else CardWhite, tween(200), label = "bg")
    val textColor by animateColorAsState(if (dateItem.isSelected) Color.White else TextDark,  tween(200), label = "txt")
    val subColor  by animateColorAsState(if (dateItem.isSelected) Color.White.copy(alpha = 0.85f) else TextGray, tween(200), label = "sub")

    Column(
        modifier = Modifier
            .width(60.dp).height(64.dp)
            .shadow(if (dateItem.isSelected) 4.dp else 2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = dateItem.date.dayOfMonth.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
        Text(
            text = dateItem.date.month.getDisplayName(TextStyle.SHORT, Locale("id")),
            fontSize = 12.sp, color = subColor, modifier = Modifier.padding(top = 2.dp)
        )
    }
}

// ─────────────────────────────────────────────
// TASK CARD
// ─────────────────────────────────────────────

@Composable
fun TaskCard(task: Task, onToggle: () -> Unit, onDelete: () -> Unit) {
    val statusBg by animateColorAsState(
        if (task.isCompleted) SuccessGreen else DangerRed, tween(300), label = "status"
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (task.isCompleted) Color(0xFFF8FFF8) else CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.width(72.dp).clip(RoundedCornerShape(10.dp))
                    .background(SubjectBg).padding(vertical = 6.dp, horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = task.subject, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                    color = PrimaryBlue, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title, fontSize = 13.sp, fontWeight = FontWeight.Bold,
                    color = if (task.isCompleted) TextGray else TextDark,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(text = task.description, fontSize = 11.sp, color = TextGray,
                    maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 2.dp))
                Text(text = "Pukul: ${task.time}", fontSize = 11.sp, color = TextGray,
                    modifier = Modifier.padding(top = 2.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(statusBg).clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (task.isCompleted) Icons.Filled.Check else Icons.Filled.Close,
                    contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape)
                    .background(Color(0xFFF3F4F6)).clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Hapus", tint = TextGray, modifier = Modifier.size(18.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────
// BOTTOM NAVIGATION
// ─────────────────────────────────────────────

@Composable
fun BottomNavBar(selectedIndex: Int, onNavSelected: (Int) -> Unit, modifier: Modifier = Modifier) {
    val navItems: List<Pair<ImageVector, String>> = listOf(
        Icons.Filled.Home        to "Beranda",
        Icons.Filled.CheckCircle to "Tugas",
        Icons.Filled.Person      to "Profil"
    )
    Surface(modifier = modifier.fillMaxWidth(), color = CardWhite, shadowElevation = 12.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().height(64.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEachIndexed { index, (icon, label) ->
                val isSelected = index == selectedIndex
                val iconColor by animateColorAsState(
                    if (isSelected) PrimaryBlue else TextGray, tween(200), label = "nav"
                )
                IconButton(onClick = { onNavSelected(index) }) {
                    Icon(icon, contentDescription = label, tint = iconColor, modifier = Modifier.size(26.dp))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────
// ADD TASK DIALOG
// ─────────────────────────────────────────────

@Composable
fun AddTaskDialog(
    selectedDate: LocalDate,
    onDismiss: () -> Unit,
    onSave: (subject: String, title: String, desc: String, time: String, date: LocalDate) -> Unit
) {
    val context   = LocalContext.current
    var subject   by remember { mutableStateOf("") }
    var title     by remember { mutableStateOf("") }
    var desc      by remember { mutableStateOf("") }
    var time      by remember { mutableStateOf("") }
    var taskDate  by remember { mutableStateOf(selectedDate) }
    var showError by remember { mutableStateOf(false) }

    fun openTimePicker() {
        val hour   = if (time.isNotEmpty()) time.split(":")[0].toIntOrNull() ?: 23 else 23
        val minute = if (time.isNotEmpty()) time.split(":").getOrNull(1)?.toIntOrNull() ?: 59 else 59
        TimePickerDialog(context, { _, h, m -> time = "%02d:%02d".format(h, m) }, hour, minute, true).show()
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Tambah Tugas Baru", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Spacer(modifier = Modifier.height(20.dp))
                DialogTextField(subject, { subject = it; showError = false }, "Mata Kuliah")
                Spacer(modifier = Modifier.height(12.dp))
                DialogTextField(title, { title = it; showError = false }, "Judul Tugas")
                Spacer(modifier = Modifier.height(12.dp))
                DialogTextField(desc, { desc = it }, "Deskripsi")
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = time,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Pukul", fontSize = 13.sp) },
                    placeholder = { Text("Pilih waktu", color = TextGray, fontSize = 13.sp) },
                    trailingIcon = {
                        Icon(Icons.Filled.Schedule, contentDescription = "Pilih waktu", tint = PrimaryBlue,
                            modifier = Modifier.clickable { openTimePicker() })
                    },
                    modifier = Modifier.fillMaxWidth().clickable { openTimePicker() },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = PrimaryBlue,
                        unfocusedBorderColor = BorderLight,
                        focusedLabelColor    = PrimaryBlue,
                        unfocusedLabelColor  = TextGray,
                        disabledBorderColor  = BorderLight,
                        disabledTextColor    = TextDark
                    )
                )
                if (showError) {
                    Text("Mata kuliah dan judul wajib diisi!", color = DangerRed, fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextGray)
                    ) { Text("Batal") }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (subject.isBlank() || title.isBlank()) showError = true
                            else onSave(subject.trim(), title.trim(), desc.trim(), time.ifEmpty { "23:59" }, taskDate)
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) { Text("Simpan", color = Color.White) }
                }
            }
        }
    }
}

@Composable
fun DialogTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(label, fontSize = 13.sp) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = PrimaryBlue,
            unfocusedBorderColor = BorderLight,
            focusedLabelColor    = PrimaryBlue,
            unfocusedLabelColor  = TextGray
        )
    )
}
