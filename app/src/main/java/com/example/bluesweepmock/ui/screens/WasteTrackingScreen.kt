package com.example.bluesweepmock.ui.screens

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bluesweepmock.R
import com.example.bluesweepmock.data.WasteReportStore
import com.example.bluesweepmock.ui.theme.*
import com.example.bluesweepmock.utils.LocationManager
import com.example.bluesweepmock.utils.PhotoManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
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
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // Initialize managers and view model
    val photoManager = remember { PhotoManager(context) }
    val locationManager = remember { LocationManager(context) }
    val wasteReportStore = remember { WasteReportStore(context) }
    
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Report", "History")
    
    // State for photo upload
    var showPhotoOptions by remember { mutableStateOf(false) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    
    // State for description
    var description by remember { mutableStateOf("") }
    
    // State for location
    var location by remember { mutableStateOf("") }
    var showLocationDialog by remember { mutableStateOf(false) }
    
    // State for reports
    var reports by remember { mutableStateOf<List<WasteReport>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    
    // State for pollution map
    var showPollutionMap by remember { mutableStateOf(false) }
    var selectedReportLocation by remember { mutableStateOf("") }
    
    // Load reports
    LaunchedEffect(key1 = Unit) {
        wasteReportStore.wasteReportsFlow.collect { reportsList ->
            reports = reportsList
        }
    }
    
    // Date formatter
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
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
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                    photoUri = photoUri,
                    onPhotoClick = { showPhotoOptions = true },
                    onRemovePhoto = { photoUri = null },
                    description = description,
                    onDescriptionChange = { description = it },
                    location = location,
                    onLocationClick = { showLocationDialog = true },
                    isLoading = isLoading,
                    onSubmit = {
                        if (description.isBlank()) {
                            Toast.makeText(context, "Please add a description", Toast.LENGTH_SHORT).show()
                            return@ReportTab
                        }
                        
                        if (location.isBlank()) {
                            Toast.makeText(context, "Please add a location", Toast.LENGTH_SHORT).show()
                            return@ReportTab
                        }
                        
                        coroutineScope.launch {
                            isLoading = true
                            try {
                                // Use the URI as a string
                                val photoUriString = photoUri?.toString()
                                wasteReportStore.addWasteReport(description, location, photoUriString)
                                
                                // Reset form
                                description = ""
                                photoUri = null
                                
                                // Show success message
                                showSuccessMessage = true
                                Toast.makeText(context, "Report submitted successfully!", Toast.LENGTH_SHORT).show()
                                
                                // Auto-switch to history tab
                                selectedTabIndex = 1
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    onShowMap = { loc ->
                        selectedReportLocation = loc
                        showPollutionMap = true
                    }
                )
                1 -> HistoryTab(
                    reports = reports,
                    dateFormat = dateFormat,
                    onMapClick = { location ->
                        selectedReportLocation = location
                        showPollutionMap = true
                    }
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
                            // Camera Button (For testing, we'll use a sample image)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    coroutineScope.launch {
                                        photoUri = photoManager.getSampleImageUri()
                                        showPhotoOptions = false
                                    }
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
                            
                            // Gallery Button (For testing, we'll use a sample image)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    coroutineScope.launch {
                                        photoUri = photoManager.getSampleImageUri()
                                        showPhotoOptions = false
                                    }
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
                        
                        // Get Current Location Button
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    // In a real app, check permissions and get real location
                                    // For testing, use a sample location
                                    val (_, address) = locationManager.getSampleLocation()
                                    location = address
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OceanBlue
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = "Get Current Location",
                                tint = White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Use Current Location")
                        }
                        
                        // Google Map instead of placeholder
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            // Convert location string to LatLng (using the same logic as in PollutionMapDialog)
                            val locationCoordinates = remember(location) {
                                when {
                                    location.contains("Kuala Lumpur", ignoreCase = true) -> LatLng(3.139, 101.6869)
                                    location.contains("Shah Alam", ignoreCase = true) -> LatLng(3.0733, 101.5185)
                                    location.contains("Klang", ignoreCase = true) -> LatLng(3.0449, 101.4455)
                                    location.contains("Cyberjaya", ignoreCase = true) -> LatLng(2.9188, 101.6520)
                                    location.contains("Putrajaya", ignoreCase = true) -> LatLng(2.9264, 101.6964)
                                    location.contains("Penang", ignoreCase = true) -> LatLng(5.4141, 100.3288)
                                    location.contains("Johor", ignoreCase = true) -> LatLng(1.4927, 103.7414)
                                    location.contains("Malacca", ignoreCase = true) -> LatLng(2.1896, 102.2501)
                                    location.contains("Ipoh", ignoreCase = true) -> LatLng(4.5975, 101.0901)
                                    location.contains("Kuching", ignoreCase = true) -> LatLng(1.5535, 110.3593)
                                    location.contains("Kota Kinabalu", ignoreCase = true) -> LatLng(5.9804, 116.0735)
                                    else -> LatLng(3.139, 101.6869) // Default to KL
                                }
                            }
                            
                            // Map properties and UI settings
                            val mapProperties by remember {
                                mutableStateOf(
                                    MapProperties(
                                        isMyLocationEnabled = false,
                                        mapType = MapType.NORMAL
                                    )
                                )
                            }
                            
                            val mapUiSettings by remember {
                                mutableStateOf(
                                    MapUiSettings(
                                        zoomControlsEnabled = true,
                                        myLocationButtonEnabled = false
                                    )
                                )
                            }
                            
                            // Camera position
                            val cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(locationCoordinates, 15f)
                            }
                            
                            // Effect to move camera to location
                            LaunchedEffect(locationCoordinates) {
                                cameraPositionState.animate(
                                    update = CameraUpdateFactory.newLatLngZoom(
                                        locationCoordinates,
                                        15f
                                    ),
                                    durationMs = 1000
                                )
                            }
                            
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                properties = mapProperties,
                                uiSettings = mapUiSettings,
                                cameraPositionState = cameraPositionState,
                                onMapClick = { latLng ->
                                    // Get address from location (in a real app)
                                    // For this mock, we'll use a predefined location name
                                    val newLocation = when {
                                        latLng.latitude > 5.0 -> "Penang, Malaysia"
                                        latLng.latitude > 4.0 -> "Ipoh, Malaysia"
                                        latLng.latitude > 3.0 -> "Kuala Lumpur, Malaysia"
                                        latLng.latitude > 2.0 -> "Putrajaya, Malaysia"
                                        else -> "Johor, Malaysia"
                                    }
                                    location = newLocation
                                }
                            ) {
                                // Add a marker at the current location
                                Marker(
                                    state = MarkerState(position = locationCoordinates),
                                    title = "Selected Location",
                                    snippet = location
                                )
                            }
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
                                onClick = { showLocationDialog = false },
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
        
        // Pollution Map Dialog
        if (showPollutionMap) {
            PollutionMapDialog(
                location = selectedReportLocation,
                onDismiss = { showPollutionMap = false },
                onLocationSelected = { newLocation -> 
                    location = newLocation
                }
            )
        }
        
        // Success Message Snackbar
        if (showSuccessMessage) {
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                action = {
                    TextButton(onClick = { showSuccessMessage = false }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text("Report submitted successfully!")
            }
            
            // Auto-dismiss after 3 seconds
            LaunchedEffect(showSuccessMessage) {
                kotlinx.coroutines.delay(3000)
                showSuccessMessage = false
            }
        }
        
        // Loading Indicator
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = OceanBlue)
            }
        }
    }
}

@Composable
fun ReportTab(
    photoUri: Uri?,
    onPhotoClick: () -> Unit,
    onRemovePhoto: () -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    location: String,
    onLocationClick: () -> Unit,
    isLoading: Boolean,
    onSubmit: () -> Unit,
    onShowMap: (String) -> Unit
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
                if (photoUri != null) {
                    // Photo Preview
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Display the selected photo
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photoUri)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Selected Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
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
                Row {
                    // Map button to open pollution map dialog
                    IconButton(onClick = {
                        if (location.isNotBlank()) {
                            onShowMap(location)
                        } else {
                            // If no location is selected, open location dialog
                            onLocationClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Map,
                            contentDescription = "View on Map",
                            tint = OceanBlue
                        )
                    }
                    
                    // Location button to open location dialog
                    IconButton(onClick = onLocationClick) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "Select Location",
                            tint = OceanBlue
                        )
                    }
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
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading && description.isNotBlank() && location.isNotBlank()
        ) {
            Text("Submit Report")
        }
    }
}

@Composable
fun HistoryTab(
    reports: List<WasteReport>,
    dateFormat: SimpleDateFormat,
    onMapClick: (String) -> Unit
) {
    if (reports.isEmpty()) {
        // Empty state
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "No Reports",
                    tint = OceanBlue,
                    modifier = Modifier.size(64.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "No reports yet",
                    style = MaterialTheme.typography.titleLarge,
                    color = OceanBlue
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Your submitted waste reports will appear here",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        // List of reports
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(reports) { report ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onMapClick(report.location) },
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
                            // Display either the photo from URI or a fallback image
                            val imageModel = if (photoUri.startsWith("content://") || photoUri.startsWith("file://")) {
                                photoUri
                            } else {
                                // Use a drawable resource as fallback
                                R.drawable.plastic_pollution
                            }
                            
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageModel)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Waste Report Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                                error = painterResource(id = R.drawable.plastic_pollution)
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