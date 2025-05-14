package com.example.bluesweepmock.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bluesweepmock.R
import com.example.bluesweepmock.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventDialog(
    onDismiss: () -> Unit,
    onCreateEvent: (
        title: String,
        description: String,
        date: Date,
        time: String,
        location: String,
        maxParticipants: Int,
        imageType: Int,
        customImageUri: String?
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var maxParticipants by remember { mutableStateOf("10") }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance().apply { 
        add(Calendar.DAY_OF_MONTH, 7) // Default to one week from now
    }) }
    var startTime by remember { mutableStateOf("09:00 AM") }
    var endTime by remember { mutableStateOf("12:00 PM") }
    
    // Image selection
    var selectedImageResId by remember { mutableStateOf<Int?>(R.drawable.beach) }
    var customImageUri by remember { mutableStateOf<Uri?>(null) }
    
    // Dialog visibility states
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showLocationSearch by remember { mutableStateOf(false) }
    var showImagePicker by remember { mutableStateOf(false) }
    
    // Date formatter
    val dateFormatter = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    
    // Validation
    val isFormValid = title.isNotBlank() && description.isNotBlank() && location.isNotBlank() && 
                      maxParticipants.toIntOrNull() != null && maxParticipants.toIntOrNull() ?: 0 > 0 &&
                      (selectedImageResId != null || customImageUri != null)
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with close button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Create New Event",
                        style = MaterialTheme.typography.titleLarge,
                        color = OceanBlue,
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextGray
                        )
                    }
                }
                
                // Event Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Event Title") },
                    placeholder = { Text("Enter event title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OceanBlue,
                        unfocusedBorderColor = LightBlue
                    ),
                    singleLine = true
                )
                
                // Event Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("Describe your cleanup event") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OceanBlue,
                        unfocusedBorderColor = LightBlue
                    ),
                    maxLines = 5
                )
                
                // Date Selection
                OutlinedTextField(
                    value = dateFormatter.format(selectedDate.time),
                    onValueChange = { /* Read-only */ },
                    label = { Text("Date") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OceanBlue,
                        unfocusedBorderColor = LightBlue
                    ),
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Date",
                            tint = OceanBlue
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Select Date",
                                tint = OceanBlue
                            )
                        }
                    }
                )
                
                // Time Selection
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Start Time
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { /* Read-only */ },
                        label = { Text("Start Time") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = OceanBlue,
                            unfocusedBorderColor = LightBlue
                        ),
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Start Time",
                                tint = OceanBlue
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { showStartTimePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "Select Start Time",
                                    tint = OceanBlue
                                )
                            }
                        }
                    )
                    
                    // End Time
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { /* Read-only */ },
                        label = { Text("End Time") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = OceanBlue,
                            unfocusedBorderColor = LightBlue
                        ),
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "End Time",
                                tint = OceanBlue
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { showEndTimePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "Select End Time",
                                    tint = OceanBlue
                                )
                            }
                        }
                    )
                }
                
                // Location
                OutlinedTextField(
                    value = location,
                    onValueChange = { /* Read-only */ },
                    label = { Text("Location") },
                    placeholder = { Text("Select event location") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OceanBlue,
                        unfocusedBorderColor = LightBlue
                    ),
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = OceanBlue
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { showLocationSearch = true }) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = "Search Location",
                                tint = OceanBlue
                            )
                        }
                    }
                )
                
                // Max Participants
                OutlinedTextField(
                    value = maxParticipants,
                    onValueChange = { 
                        // Only allow numeric input
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            maxParticipants = it
                        }
                    },
                    label = { Text("Maximum Participants") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OceanBlue,
                        unfocusedBorderColor = LightBlue
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Participants",
                            tint = OceanBlue
                        )
                    }
                )
                
                // Image Selection
                Text(
                    text = "Event Image",
                    style = MaterialTheme.typography.titleMedium,
                    color = OceanBlue,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                // Image preview or selection button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = LightBlue,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { showImagePicker = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (customImageUri != null) {
                        // Show custom image
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(customImageUri)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else if (selectedImageResId != null) {
                        // Show selected default image
                        Image(
                            painter = painterResource(id = selectedImageResId!!), // Force non-null
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Show placeholder
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = "Select Image",
                                tint = OceanBlue,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tap to select an image",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextGray
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Create Button
                Button(
                    onClick = {
                        val timeString = "$startTime - $endTime"
                        onCreateEvent(
                            title,
                            description,
                            selectedDate.time,
                            timeString,
                            location,
                            maxParticipants.toIntOrNull() ?: 10,
                            selectedImageResId ?: R.drawable.beach,
                            customImageUri?.toString()
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OceanBlue,
                        disabledContainerColor = LightBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = isFormValid
                ) {
                    Text("Create Event")
                }
            }
        }
    }
    
    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = selectedDate.timeInMillis
                ),
                showModeToggle = false,
                title = { Text("Select Date") },
                headline = { Text("Select Event Date") }
            )
        }
    }
    
    // Location Search Dialog
    if (showLocationSearch) {
        LocationSearchDialog(
            onDismiss = { showLocationSearch = false },
            onLocationSelected = { selectedLocation ->
                location = selectedLocation
            }
        )
    }
    
    // Image Picker Dialog
    if (showImagePicker) {
        ImagePickerDialog(
            onDismiss = { showImagePicker = false },
            onImageSelected = { imageResId, uri ->
                selectedImageResId = imageResId
                customImageUri = uri
            }
        )
    }
    
    // For simplicity, we're not implementing actual time pickers
    // In a real app, you would use TimePickerDialog
}

// Helper function for border is not needed since we're importing it directly 