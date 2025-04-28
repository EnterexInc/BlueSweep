package com.example.bluesweepmock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.bluesweepmock.R
import com.example.bluesweepmock.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

// Data class for waste reports
data class WasteReport(
    val id: Int,
    val description: String,
    val location: String,
    val date: Date,
    val photoUri: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WasteTrackingScreen(
    onNavigateBack: () -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Report", "History")
    
    // State for photo upload
    var showPhotoOptions by remember { mutableStateOf(false) }
    var hasPhoto by remember { mutableStateOf(false) }
    
    // State for description
    var description by remember { mutableStateOf("") }
    
    // State for location
    var location by remember { mutableStateOf("") }
    var showLocationDialog by remember { mutableStateOf(false) }
    
    // State for map dialog
    var showMapDialog by remember { mutableStateOf(false) }
    
    // Sample waste reports for history
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val sampleReports = remember {
        listOf(
            WasteReport(
                1,
                "Plastic bottles and food wrappers found near the shoreline",
                "Taman Tasik Shah Alam, Selangor",
                Date(System.currentTimeMillis() - 86400000 * 2), // 2 days ago
                "plasticpollution"
            ),
            WasteReport(
                2,
                "Large amount of microplastics in the water",
                "Klang River, Kuala Lumpur",
                Date(System.currentTimeMillis() - 86400000 * 5), // 5 days ago
                "microplastic_pollution"
            ),
            WasteReport(
                3,
                "Fishing nets and debris washed ashore",
                "Port Dickson Beach, Negeri Sembilan",
                Date(System.currentTimeMillis() - 86400000 * 10), // 10 days ago
                "beach"
            )
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Bar with Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = OceanBlue
                    )
                }
                
                Text(
                    text = "Waste Tracking",
                    style = MaterialTheme.typography.titleLarge,
                    color = OceanBlue,
                    fontWeight = FontWeight.Bold
                )
                
                // Placeholder for symmetry
                Box(modifier = Modifier.size(48.dp))
            }
            
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = White,
                contentColor = OceanBlue
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
            
            // Tab Content
            when (selectedTabIndex) {
                0 -> ReportTab(
                    hasPhoto = hasPhoto,
                    onPhotoClick = { showPhotoOptions = true },
                    onRemovePhoto = { hasPhoto = false },
                    description = description,
                    onDescriptionChange = { description = it },
                    location = location,
                    onLocationClick = { showLocationDialog = true },
                    onSubmit = {
                        // In a real app, this would save the report
                        // For now, just show a success message
                        showMapDialog = true
                    }
                )
                1 -> HistoryTab(
                    reports = sampleReports,
                    dateFormat = dateFormat,
                    onMapClick = { showMapDialog = true }
                )
            }
        }
        
        // Photo Options Dialog
        if (showPhotoOptions) {
            Dialog(onDismissRequest = { showPhotoOptions = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Add Photo",
                            style = MaterialTheme.typography.titleLarge,
                            color = OceanBlue,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Camera Button
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    hasPhoto = true
                                    showPhotoOptions = false
                                }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(OceanBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Camera,
                                        contentDescription = "Take Photo",
                                        tint = White
                                    )
                                }
                                Text(
                                    text = "Camera",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextGray,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                            
                            // Gallery Button
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    hasPhoto = true
                                    showPhotoOptions = false
                                }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(OceanBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.PhotoLibrary,
                                        contentDescription = "Choose from Gallery",
                                        tint = White
                                    )
                                }
                                Text(
                                    text = "Gallery",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextGray,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                        
                        TextButton(
                            onClick = { showPhotoOptions = false },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
        
        // Location Dialog
        if (showLocationDialog) {
            Dialog(onDismissRequest = { showLocationDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Select Location",
                            style = MaterialTheme.typography.titleLarge,
                            color = OceanBlue,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        // Location Input
                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location") },
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
                                    imageVector = Icons.Outlined.LocationOn,
                                    contentDescription = "Location",
                                    tint = OceanBlue
                                )
                            }
                        )
                        
                        // Map Placeholder
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(LightBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Map View",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextGray
                            )
                        }
                        
                        // Buttons
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showLocationDialog = false }) {
                                Text("Cancel")
                            }
                            
                            Button(
                                onClick = {
                                    if (location.isEmpty()) {
                                        location = "Taman Tasik Shah Alam, Selangor"
                                    }
                                    showLocationDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = OceanBlue
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Confirm")
                            }
                        }
                    }
                }
            }
        }
        
        // Map Dialog
        if (showMapDialog) {
            Dialog(onDismissRequest = { showMapDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Pollution Map",
                                style = MaterialTheme.typography.titleLarge,
                                color = OceanBlue,
                                fontWeight = FontWeight.Bold
                            )
                            
                            IconButton(onClick = { showMapDialog = false }) {
                                Icon(
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = "Close",
                                    tint = TextGray
                                )
                            }
                        }
                        
                        // Map Placeholder with Pollution Pins
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                                .background(LightBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Interactive Map",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextGray
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Sample Pollution Pins
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    PollutionPin("Plastic", Color.Red)
                                    PollutionPin("Chemical", Color.Yellow)
                                    PollutionPin("Organic", Color.Green)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportTab(
    hasPhoto: Boolean,
    onPhotoClick: () -> Unit,
    onRemovePhoto: () -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    location: String,
    onLocationClick: () -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Photo Upload Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = onPhotoClick),
                contentAlignment = Alignment.Center
            ) {
                if (hasPhoto) {
                    // Photo Preview
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Placeholder for photo
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(LightBlue)
                        )
                        
                        // Remove Photo Button
                        IconButton(
                            onClick = onRemovePhoto,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Remove Photo",
                                tint = Color.Red
                            )
                        }
                    }
                } else {
                    // Upload Prompt
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Camera,
                            contentDescription = "Add Photo",
                            tint = OceanBlue,
                            modifier = Modifier.size(48.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Tap to add a photo",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextGray
                        )
                    }
                }
            }
        }
        
        // Description Input
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            placeholder = { Text("Describe the waste you found...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OceanBlue,
                unfocusedBorderColor = LightBlue
            ),
            maxLines = 5
        )
        
        // Location Selection
        OutlinedTextField(
            value = location,
            onValueChange = { /* Read-only */ },
            label = { Text("Location") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OceanBlue,
                unfocusedBorderColor = LightBlue
            ),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = onLocationClick) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Select Location",
                        tint = OceanBlue
                    )
                }
            }
        )
        
        // Submit Button
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OceanBlue
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Submit Report")
        }
    }
}

@Composable
fun HistoryTab(
    reports: List<WasteReport>,
    dateFormat: SimpleDateFormat,
    onMapClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        reports.forEach { report ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMapClick() },
                colors = CardDefaults.cardColors(
                    containerColor = White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Report Image
                    report.photoUri?.let { photoUri ->
                        Image(
                            painter = painterResource(
                                id = when (photoUri) {
                                    "plastic_pollution" -> R.drawable.plastic_pollution
                                    "microplastic_pollution" -> R.drawable.microplastic_pollution
                                    "beach" -> R.drawable.beach
                                    else -> R.drawable.ocean_mascot
                                }
                            ),
                            contentDescription = "Waste Report Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    Text(
                        text = report.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextGray
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = report.location,
                            style = MaterialTheme.typography.bodyMedium,
                            color = OceanBlue
                        )
                        
                        Text(
                            text = dateFormat.format(report.date),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PollutionPin(
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(color)
                .border(1.dp, Color.White, CircleShape)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextGray
        )
    }
} 