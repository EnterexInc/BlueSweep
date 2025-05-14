package com.example.bluesweepmock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.bluesweepmock.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchDialog(
    onDismiss: () -> Unit,
    onLocationSelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var searchResults by remember { mutableStateOf<List<LocationSuggestion>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    
    // In a real app, this would be connected to Google Places API
    // For this mock, we'll use predefined locations in Malaysia
    val mockLocations = remember {
        listOf(
            LocationSuggestion("Kuala Lumpur City Centre, Kuala Lumpur", "City center with KLCC and Petronas Towers"),
            LocationSuggestion("Batu Caves, Selangor", "Hindu temple and cave site"),
            LocationSuggestion("Penang Island, Penang", "Island known for food and culture"),
            LocationSuggestion("Langkawi, Kedah", "Archipelago with beaches and rainforests"),
            LocationSuggestion("Malacca City, Malacca", "Historic colonial city"),
            LocationSuggestion("Kota Kinabalu, Sabah", "Coastal city with Mount Kinabalu nearby"),
            LocationSuggestion("Kuching, Sarawak", "Capital city of Sarawak"),
            LocationSuggestion("Cameron Highlands, Pahang", "Hill station with tea plantations"),
            LocationSuggestion("Johor Bahru, Johor", "Southern city near Singapore"),
            LocationSuggestion("Ipoh, Perak", "City known for food and colonial architecture"),
            LocationSuggestion("Putrajaya", "Federal administrative center"),
            LocationSuggestion("Port Dickson, Negeri Sembilan", "Coastal town with beaches"),
            LocationSuggestion("Kuala Selangor, Selangor", "Coastal town with fireflies"),
            LocationSuggestion("Taman Tasik Shah Alam, Selangor", "Large lake and park"),
            LocationSuggestion("Cyberjaya, Selangor", "Tech hub city"),
            LocationSuggestion("Klang River, Kuala Lumpur", "River flowing through KL"),
            LocationSuggestion("Bagan Lalang Beach, Selangor", "Beach area in Sepang"),
            LocationSuggestion("Kuala Selangor Nature Park, Selangor", "Mangrove forest reserve")
        )
    }
    
    // Mock search function
    fun performSearch(query: String) {
        if (query.isBlank()) {
            searchResults = emptyList()
            return
        }
        
        isSearching = true
        coroutineScope.launch {
            // Simulate network delay
            delay(500)
            searchResults = mockLocations.filter { 
                it.name.contains(query, ignoreCase = true) || 
                it.description?.contains(query, ignoreCase = true) == true 
            }
            isSearching = false
        }
    }
    
    // Initial search results for common locations
    LaunchedEffect(Unit) {
        searchResults = mockLocations.take(5)
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
                // Header with close button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Location",
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
                
                // Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { 
                        searchQuery = it
                        performSearch(it)
                    },
                    label = { Text("Search location") },
                    placeholder = { Text("Enter city, area, or landmark") },
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
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = OceanBlue
                        )
                    }
                )
                
                // Loading indicator or results
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = OceanBlue
                        )
                    } else {
                        if (searchResults.isEmpty() && searchQuery.isNotBlank()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "No locations found",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextGray
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = "Try a different search term",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextGray
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (searchQuery.isBlank()) {
                                    item {
                                        Text(
                                            text = "Popular Locations",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = OceanBlue,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                                
                                items(searchResults) { location ->
                                    LocationItem(
                                        location = location,
                                        onClick = { 
                                            onLocationSelected(location.name)
                                            onDismiss()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Custom location entry
                Button(
                    onClick = {
                        if (searchQuery.isNotBlank()) {
                            onLocationSelected(searchQuery)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OceanBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = searchQuery.isNotBlank()
                ) {
                    Text("Use Custom Location")
                }
            }
        }
    }
}

@Composable
fun LocationItem(
    location: LocationSuggestion,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = OceanBlue,
            modifier = Modifier.padding(end = 12.dp)
        )
        
        Column {
            Text(
                text = location.name,
                style = MaterialTheme.typography.bodyLarge,
                color = TextGray
            )
            
            location.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray.copy(alpha = 0.7f)
                )
            }
        }
    }
}

data class LocationSuggestion(
    val name: String,
    val description: String? = null
) 