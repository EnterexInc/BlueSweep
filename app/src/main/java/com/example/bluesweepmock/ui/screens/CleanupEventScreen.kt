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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.bluesweepmock.R
import com.example.bluesweepmock.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleanupEventScreen(
    onNavigateBack: () -> Unit,
    onCreateEvent: () -> Unit
) {
    // Sample events data with Malaysian locations
    val events = listOf(
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
    
    // Date formatter
    val dateFormatter = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with back button
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
            
            // Create Event Button
            Button(
                onClick = onCreateEvent,
                colors = ButtonDefaults.buttonColors(
                    containerColor = OceanBlue
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Create Your Own Event",
                    style = MaterialTheme.typography.titleMedium,
                    color = White
                )
            }
            
            // Events List
            Text(
                text = "Upcoming Events in Selangor",
                style = MaterialTheme.typography.titleLarge,
                color = OceanBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                events.forEach { event ->
                    EventCard(
                        event = event,
                        formattedDate = dateFormatter.format(event.date)
                    )
                }
            }
        }
    }
}

@Composable
fun EventCard(
    event: CleanupEvent,
    formattedDate: String
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
            Image(
                painter = painterResource(id = event.imageResId),
                contentDescription = event.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            
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
                        painter = painterResource(id = R.drawable.ocean_mascot), // Placeholder, replace with calendar icon
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
                        painter = painterResource(id = R.drawable.ocean_mascot), // Placeholder, replace with time icon
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ocean_mascot), // Placeholder, replace with location icon
                            contentDescription = "Location",
                            tint = OceanBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = event.location,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextGray
                        )
                    }
                    
                    // Map Button
                    TextButton(
                        onClick = { showMapDialog = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = OceanBlue
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ocean_mascot), // Placeholder, replace with map icon
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
                        onClick = { /* TODO: Implement registration */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OceanBlue
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Register")
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
    val imageResId: Int
) 