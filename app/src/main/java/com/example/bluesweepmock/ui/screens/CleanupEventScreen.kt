package com.example.bluesweepmock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.bluesweepmock.R
import com.example.bluesweepmock.data.EventDataStore
import com.example.bluesweepmock.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Schedule
import kotlinx.coroutines.launch
import android.widget.Toast
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleanupEventScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val eventDataStore = remember { EventDataStore(context) }
    
    // Sample events data with Malaysian locations
    val sampleEvents = listOf(
        CleanupEvent(
            id = 1,
            title = "Shah Alam Lake Cleanup",
            description = "Join us for a community cleanup at Taman Tasik Shah Alam to remove plastic waste and protect our local water ecosystem.",
            date = Calendar.getInstance().apply { 
                add(Calendar.DAY_OF_MONTH, 5) 
            }.time,
            time = "8:00 AM - 12:00 PM",
            location = "Taman Tasik Shah Alam, Selangor",
            participants = 24,
            maxParticipants = 50,
            imageResId = R.drawable.lake
        ),
        CleanupEvent(
            id = 2,
            title = "Klang River Protection",
            description = "Help clean up the Klang River and learn about the impact of marine debris on our local ecosystems. Organized by the Malaysian Nature Society.",
            date = Calendar.getInstance().apply { 
                add(Calendar.DAY_OF_MONTH, 12) 
            }.time,
            time = "9:00 AM - 2:00 PM",
            location = "Klang River, near Central Market, Kuala Lumpur",
            participants = 18,
            maxParticipants = 30,
            imageResId = R.drawable.river
        ),
        CleanupEvent(
            id = 3,
            title = "Port Klang Beach Cleanup",
            description = "Join marine conservationists to learn about Malaysia's coastal ecosystems and participate in a cleanup to protect our beaches.",
            date = Calendar.getInstance().apply { 
                add(Calendar.DAY_OF_MONTH, 20) 
            }.time,
            time = "7:00 AM - 1:00 PM",
            location = "Bagan Lalang Beach, Port Klang, Selangor",
            participants = 15,
            maxParticipants = 25,
            imageResId = R.drawable.beach
        ),
        CleanupEvent(
            id = 4,
            title = "Mangrove Conservation Day",
            description = "Help protect Malaysia's vital mangrove ecosystems at Kuala Selangor Nature Park. Learn about the importance of mangroves for marine life.",
            date = Calendar.getInstance().apply { 
                add(Calendar.DAY_OF_MONTH, 28) 
            }.time,
            time = "8:30 AM - 3:00 PM",
            location = "Kuala Selangor Nature Park, Selangor",
            participants = 22,
            maxParticipants = 40,
            imageResId = R.drawable.mangrove
        ),
        CleanupEvent(
            id = 5,
            title = "Cyberjaya Lake Cleanup",
            description = "Join tech professionals and environmentalists for a cleanup of Cyberjaya Lake. Learn about urban water conservation in Malaysia.",
            date = Calendar.getInstance().apply { 
                add(Calendar.DAY_OF_MONTH, 35) 
            }.time,
            time = "9:00 AM - 1:00 PM",
            location = "Cyberjaya Lake, Cyberjaya, Selangor",
            participants = 30,
            maxParticipants = 60,
            imageResId = R.drawable.lake
        )
    )
    
    // State for custom events
    var customEvents by remember { mutableStateOf<List<CleanupEvent>>(emptyList()) }
    var showCreateEventDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    
    // Load custom events
    LaunchedEffect(key1 = Unit) {
        eventDataStore.eventsFlow.collect { events ->
            customEvents = events
        }
    }
    
    // Combine sample and custom events, sorted by date
    val allEvents = remember(customEvents) {
        (sampleEvents + customEvents).sortedBy { it.date }
    }
    
    // Date formatter
    val dateFormatter = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlue)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with back button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
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
                        text = "Local Cleanup Events",
                        style = MaterialTheme.typography.headlineMedium,
                        color = OceanBlue,
                        fontWeight = FontWeight.Bold
                    )
                    // Empty box for alignment
                    Box(modifier = Modifier.size(48.dp))
                }
            }
            
            // Create Event Button
            item {
                Button(
                    onClick = { showCreateEventDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OceanBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Event",
                        tint = White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Create Your Own Event",
                        style = MaterialTheme.typography.titleMedium,
                        color = White
                    )
                }
            }
            
            // Events List Header
            item {
                Text(
                    text = "Upcoming Events in Selangor",
                    style = MaterialTheme.typography.titleLarge,
                    color = OceanBlue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Events List
            items(allEvents) { event ->
                EventCard(
                    event = event,
                    formattedDate = dateFormatter.format(event.date),
                    onRegister = { eventId ->
                        coroutineScope.launch {
                            eventDataStore.registerForEvent(eventId)
                            Toast.makeText(
                                context,
                                "Successfully registered for event!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
        }
        
        // Create Event Dialog
        if (showCreateEventDialog) {
            CreateEventDialog(
                onDismiss = { showCreateEventDialog = false },
                onCreateEvent = { title, description, date, time, location, maxParticipants, imageType, customImageUri ->
                    coroutineScope.launch {
                        isLoading = true
                        try {
                            eventDataStore.addEvent(
                                title = title,
                                description = description,
                                date = date,
                                time = time,
                                location = location,
                                maxParticipants = maxParticipants,
                                imageType = imageType,
                                customImageUri = customImageUri
                            )
                            
                            Toast.makeText(
                                context,
                                "Event created successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            
                            showCreateEventDialog = false
                        } finally {
                            isLoading = false
                        }
                    }
                }
            )
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
fun EventCard(
    event: CleanupEvent,
    formattedDate: String,
    onRegister: (Int) -> Unit
) {
    var showMapDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Event Image
            if (event.customImageUri != null) {
                // Display custom image
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(event.customImageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = event.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.beach) // Fallback image
                )
            } else {
                // Display default image
                Image(
                    painter = painterResource(id = event.imageResId),
                    contentDescription = event.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            }
            
            // Event Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = OceanBlue,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Date and Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Date",
                        tint = OceanBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Time",
                        tint = OceanBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = event.time,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Location with Map Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = OceanBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = event.location,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextGray,
                            maxLines = 2
                        )
                    }
                    
                    // Map Button
                    TextButton(
                        onClick = { showMapDialog = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = OceanBlue
                        ),
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "View Map",
                            tint = OceanBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Map")
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Participants and Register Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${event.participants}/${event.maxParticipants} participants",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray
                    )
                    
                    Button(
                        onClick = { onRegister(event.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OceanBlue
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = event.participants < event.maxParticipants
                    ) {
                        Text(if (event.participants < event.maxParticipants) "Register" else "Full")
                    }
                }
            }
        }
    }
    
    // Map Dialog
    if (showMapDialog) {
        MapDialog(
            location = event.location,
            onDismiss = { showMapDialog = false }
        )
    }
}

@Composable
fun MapDialog(
    location: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Location Map",
                    style = MaterialTheme.typography.titleLarge,
                    color = OceanBlue,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Map Image
                Image(
                    painter = painterResource(id = R.drawable.shah_alam_lake),
                    contentDescription = "Location Map",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OceanBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

// Data class for cleanup events
data class CleanupEvent(
    val id: Int,
    val title: String,
    val description: String,
    val date: Date,
    val time: String,
    val location: String,
    val participants: Int,
    val maxParticipants: Int,
    val imageResId: Int,
    val customImageUri: String? = null
) 