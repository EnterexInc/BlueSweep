package com.example.bluesweepmock.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.bluesweepmock.ui.theme.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import java.util.*

// Data class for pollution markers
data class PollutionMarker(
    val id: String = UUID.randomUUID().toString(),
    val position: LatLng,
    val type: PollutionType
)

// Enum for pollution types
enum class PollutionType(val displayName: String, val color: Color) {
    PLASTIC("Plastic", Color.Red),
    CHEMICAL("Chemical", Color.Yellow),
    ORGANIC("Organic", Color.Green)
}

@SuppressLint("MissingPermission")
@Composable
fun PollutionMapDialog(
    location: String,
    onDismiss: () -> Unit,
    onLocationSelected: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    
    // State for map
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
    
    // Convert location string to LatLng (in a real app, use geocoding)
    // For this mock, we'll use predefined coordinates for Malaysian locations
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
    
    // Camera position
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(locationCoordinates, 15f)
    }
    
    // State for markers
    var markers by remember { mutableStateOf<List<PollutionMarker>>(emptyList()) }
    
    // State for marker placement
    var isPlacingMarker by remember { mutableStateOf(false) }
    var selectedMarkerType by remember { mutableStateOf(PollutionType.PLASTIC) }
    
    // State for marker deletion
    var markerToDelete by remember { mutableStateOf<PollutionMarker?>(null) }
    
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
            ) {
                // Header with title and close button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Pollution Map",
                            style = MaterialTheme.typography.titleLarge,
                            color = OceanBlue,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = location,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextGray
                        )
                    }
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextGray
                        )
                    }
                }
                
                // Map with markers
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        properties = mapProperties,
                        uiSettings = mapUiSettings,
                        cameraPositionState = cameraPositionState,
                        onMapClick = { latLng ->
                            if (isPlacingMarker) {
                                // Add marker at clicked position
                                markers = markers + PollutionMarker(
                                    position = latLng,
                                    type = selectedMarkerType
                                )
                                isPlacingMarker = false
                            } else if (onLocationSelected != null) {
                                // If location selection callback is provided, allow selecting location
                                // Get address from the clicked location (in a real app, use geocoding)
                                // For this mock, we'll use predefined locations
                                val newLocation = when {
                                    latLng.latitude > 5.0 -> "Penang, Malaysia"
                                    latLng.latitude > 4.0 -> "Ipoh, Malaysia"
                                    latLng.latitude > 3.0 -> "Kuala Lumpur, Malaysia"
                                    latLng.latitude > 2.0 -> "Putrajaya, Malaysia"
                                    else -> "Johor, Malaysia"
                                }
                                onLocationSelected(newLocation)
                                onDismiss()
                            }
                        }
                    ) {
                        // Draw existing markers
                        markers.forEach { marker ->
                            Marker(
                                state = MarkerState(position = marker.position),
                                title = marker.type.displayName,
                                snippet = "Tap to remove",
                                icon = BitmapDescriptorFactory.defaultMarker(
                                    when (marker.type) {
                                        PollutionType.PLASTIC -> BitmapDescriptorFactory.HUE_RED
                                        PollutionType.CHEMICAL -> BitmapDescriptorFactory.HUE_YELLOW
                                        PollutionType.ORGANIC -> BitmapDescriptorFactory.HUE_GREEN
                                    }
                                ),
                                onClick = {
                                    markerToDelete = marker
                                    true
                                }
                            )
                        }
                    }
                    
                    // Floating action buttons for adding markers
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FloatingActionButton(
                            onClick = {
                                isPlacingMarker = true
                                selectedMarkerType = PollutionType.PLASTIC
                            },
                            containerColor = PollutionType.PLASTIC.color,
                            contentColor = Color.White,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Plastic Waste Marker"
                            )
                        }
                        
                        FloatingActionButton(
                            onClick = {
                                isPlacingMarker = true
                                selectedMarkerType = PollutionType.CHEMICAL
                            },
                            containerColor = PollutionType.CHEMICAL.color,
                            contentColor = Color.Black,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Chemical Waste Marker"
                            )
                        }
                        
                        FloatingActionButton(
                            onClick = {
                                isPlacingMarker = true
                                selectedMarkerType = PollutionType.ORGANIC
                            },
                            containerColor = PollutionType.ORGANIC.color,
                            contentColor = Color.White,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Organic Waste Marker"
                            )
                        }
                    }
                    
                    // Instruction text when placing marker
                    if (isPlacingMarker) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 16.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Tap on map to place ${selectedMarkerType.displayName} marker",
                                color = Color.White
                            )
                        }
                    }
                }
                
                // Legend
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PollutionType.values().forEach { type ->
                        PollutionLegendItem(type = type)
                    }
                }
            }
        }
    }
    
    // Confirmation dialog for marker deletion
    if (markerToDelete != null) {
        AlertDialog(
            onDismissRequest = { markerToDelete = null },
            title = { Text("Remove Marker") },
            text = { Text("Remove this ${markerToDelete?.type?.displayName} waste marker?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        markers = markers.filter { it.id != markerToDelete?.id }
                        markerToDelete = null
                    }
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { markerToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun PollutionLegendItem(type: PollutionType) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(type.color)
                .border(0.5.dp, Color.White, CircleShape)
        )
        
        Text(
            text = type.displayName,
            style = MaterialTheme.typography.bodySmall,
            color = TextGray
        )
    }
} 