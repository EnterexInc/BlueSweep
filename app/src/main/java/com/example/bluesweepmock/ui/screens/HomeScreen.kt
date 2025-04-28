package com.example.bluesweepmock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Camera
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
import com.example.bluesweepmock.R
import com.example.bluesweepmock.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    username: String,
    onNavigateToEvents: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToPollution: () -> Unit,
    onNavigateToWasteTracking: () -> Unit
) {
    // State for eco tips rotation
    val ecoTips = listOf(
        "Use reusable water bottles to reduce plastic waste",
        "Participate in local beach cleanups",
        "Reduce single-use plastics in your daily life",
        "Support ocean conservation organizations",
        "Learn about marine ecosystems and their importance"
    )
    var currentTipIndex by remember { mutableStateOf(0) }
    
    // Rotate tips every 5 seconds
    LaunchedEffect(key1 = currentTipIndex) {
        delay(5000)
        currentTipIndex = (currentTipIndex + 1) % ecoTips.size
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlue)
            .systemBarsPadding() // Add padding for system bars
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 80.dp), // Increased bottom padding for navigation
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Navigation Row at the top
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mascot),
                        contentDescription = "BlueSweep Logo",
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "BlueSweep",
                        style = MaterialTheme.typography.headlineMedium,
                        color = OceanBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                IconButton(onClick = onNavigateToProfile) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Profile",
                        tint = OceanBlue
                    )
                }
            }

            // Greeting Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = OceanBlue
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.beach),
                        contentDescription = "Beach Background",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentScale = ContentScale.Crop
                    )
                    // Add a semi-transparent overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(Color.Black.copy(alpha = 0.3f))
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .padding(top = 8.dp), // Add some top padding
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Welcome, $username!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Together we can make our oceans cleaner",
                            style = MaterialTheme.typography.bodyLarge,
                            color = White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            // Quick Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Track Waste Button
                Button(
                    onClick = onNavigateToWasteTracking,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OceanBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Camera,
                        contentDescription = "Track Waste",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Track Waste")
                }
                
                // Events Button
                Button(
                    onClick = onNavigateToEvents,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OceanBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = "Events",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Events")
                }
            }
            
            // Stats Overview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Pollution Stats Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.plastic_pollution),
                            contentDescription = "Plastic Pollution",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "8M",
                            style = MaterialTheme.typography.headlineLarge,
                            color = OceanBlue,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tons of plastic in oceans yearly",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                // Impact Stats Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.microplastic_pollution),
                            contentDescription = "Microplastic Pollution",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "700+",
                            style = MaterialTheme.typography.headlineLarge,
                            color = OceanBlue,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Marine species affected",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            // Upcoming Event Card
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Local Cleanup Events",
                            style = MaterialTheme.typography.titleLarge,
                            color = OceanBlue,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Button(
                            onClick = onNavigateToEvents,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OceanBlue
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Join Event")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Beach Cleanup Day",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextGray
                    )
                    
                    Text(
                        text = "Saturday, 10:00 AM - 1:00 PM",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray
                    )
                    
                    Text(
                        text = "Taman Tasik Shah Alam, Selangor",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray
                    )
                }
            }
            
            // Ocean Mascot with Eco Tips
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Eco Tip of the Day",
                            style = MaterialTheme.typography.titleMedium,
                            color = OceanBlue,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Button(
                            onClick = onNavigateToPollution,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OceanBlue
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Learn More",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Learn More")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Eco Tip Image
                    Image(
                        painter = painterResource(id = R.drawable.eco_tip_of_the_day),
                        contentDescription = "Eco Tip of the Day",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = ecoTips[currentTipIndex],
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun OverviewCard(
    title: String,
    value: String,
    description: String,
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = LightBlue
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = OceanBlue,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = OceanBlue,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun NavigationButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = OceanBlue
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
} 