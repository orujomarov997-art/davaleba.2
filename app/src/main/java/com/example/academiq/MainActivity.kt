package com.example.academiq
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                StudentFormScreen()
            }
        }
    }
}

@Composable
fun StudentFormScreen() {
    val context = LocalContext.current

    var nameState by remember { mutableStateOf("") }
    var lastNameState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var dateState by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    var isAgreed by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }
    var optionError by remember { mutableStateOf(false) }

    val directionOptions = listOf("Android", "iOS", "Web")
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val d = String.format("%02d", dayOfMonth)
            val m = String.format("%02d", month + 1)
            dateState = "$d/$m/$year"
            dateError = false
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val gradientBg = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F0C29), Color(0xFF1A1A3E), Color(0xFF24243E))
    )

    Box(modifier = Modifier.fillMaxSize().background(gradientBg)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 56.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // HEADER
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("AcademIQ", color = Color(0xFFD1B8FF), fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Student Registration Form", color = Color(0xFF8888BB), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Box(modifier = Modifier.width(60.dp).height(3.dp).clip(RoundedCornerShape(2.dp)).background(Brush.horizontalGradient(listOf(Color(0xFF9C77E0), Color(0xFF4A90D9)))))
            }

            Spacer(modifier = Modifier.height(28.dp))

            // CARD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF1E1E3F).copy(alpha = 0.9f))
                    .border(1.dp, Brush.verticalGradient(listOf(Color(0xFF7C5CBF), Color(0xFF3A3A6A))), RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    AcademIQTextField(value = nameState, onValueChange = { nameState = it; nameError = false }, label = "First Name", placeholder = "Enter your first name", isError = nameError)
                    AcademIQTextField(value = lastNameState, onValueChange = { lastNameState = it; lastNameError = false }, label = "Last Name", placeholder = "Enter your last name", isError = lastNameError)
                    AcademIQTextField(value = emailState, onValueChange = { emailState = it; emailError = false }, label = "Email Address", placeholder = "your@email.com", isError = emailError)

                    // DATE PICKER
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Select Date", color = Color(0xFFB39DDB), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        OutlinedTextField(
                            value = dateState,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("DD/MM/YYYY", color = Color(0xFF6E6E9F), fontSize = 14.sp) },
                            trailingIcon = {
                                IconButton(onClick = { datePickerDialog.show() }) {
                                    Icon(Icons.Default.DateRange, contentDescription = "Pick date", tint = Color(0xFF9C77E0))
                                }
                            },
                            modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF9C77E0),
                                unfocusedBorderColor = if (dateError) Color(0xFFCF6679) else Color(0xFF3D3D6B),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF252550),
                                unfocusedContainerColor = Color(0xFF252550)
                            ),
                            isError = dateError,
                            singleLine = true
                        )
                        if (dateError) {
                            Text("Please select a date", color = Color(0xFFCF6679), fontSize = 11.sp, modifier = Modifier.padding(start = 4.dp))
                        }
                    }

                    // RADIO BUTTONS
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Your Favorite Direction", color = Color(0xFFB39DDB), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF252550)).border(1.dp, if (optionError) Color(0xFFCF6679) else Color(0xFF3D3D6B), RoundedCornerShape(12.dp)).padding(horizontal = 16.dp, vertical = 8.dp)) {
                            Column {
                                directionOptions.forEach { option ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).clickable { selectedOption = option; optionError = false }.padding(vertical = 4.dp)
                                    ) {
                                        RadioButton(selected = selectedOption == option, onClick = { selectedOption = option; optionError = false }, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF9C77E0), unselectedColor = Color(0xFF6E6E9F)))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = option, color = if (selectedOption == option) Color(0xFFD1B8FF) else Color(0xFFAAAAAA), fontSize = 15.sp)
                                    }
                                }
                            }
                        }
                        if (optionError) {
                            Text("Please select an option", color = Color(0xFFCF6679), fontSize = 11.sp, modifier = Modifier.padding(start = 4.dp))
                        }
                    }

                    // SWITCH
                    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF252550)).border(1.dp, Color(0xFF3D3D6B), RoundedCornerShape(12.dp)).padding(horizontal = 16.dp, vertical = 12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("I agree to the terms\nand conditions", color = Color(0xFFCCCCCC), fontSize = 14.sp, modifier = Modifier.weight(1f))
                            Switch(checked = isAgreed, onCheckedChange = { isAgreed = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF9C77E0)))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // SUBMIT BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFF7C5CBF), Color(0xFF4A90D9))))
                    .clickable {
                        nameError = nameState.isBlank()
                        lastNameError = lastNameState.isBlank()
                        emailError = emailState.isBlank()
                        dateError = dateState.isBlank()
                        optionError = selectedOption.isBlank()

                        if (nameState.isNotBlank() && lastNameState.isNotBlank() && emailState.isNotBlank() && dateState.isNotBlank() && selectedOption.isNotBlank() && isAgreed) {
                            Toast.makeText(context, "Data sent!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Fill in all the boxes!", Toast.LENGTH_SHORT).show()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Submit", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AcademIQTextField(value: String, onValueChange: (String) -> Unit, label: String, placeholder: String, isError: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, color = Color(0xFFB39DDB), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color(0xFF6E6E9F), fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF9C77E0),
                unfocusedBorderColor = if (isError) Color(0xFFCF6679) else Color(0xFF3D3D6B),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF252550),
                unfocusedContainerColor = Color(0xFF252550)
            ),
            isError = isError,
            singleLine = true
        )
        if (isError) {
            Text("This field is required", color = Color(0xFFCF6679), fontSize = 11.sp, modifier = Modifier.padding(start = 4.dp))
        }
    }
}