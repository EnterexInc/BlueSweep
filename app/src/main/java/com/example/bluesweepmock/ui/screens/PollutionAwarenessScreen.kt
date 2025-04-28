package com.example.bluesweepmock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PollutionAwarenessScreen(
    onNavigateBack: () -> Unit
) {
    // State for the trivia dialog
    var showTriviaDialog by remember { mutableStateOf(false) }
    var currentTriviaIndex by remember { mutableStateOf(0) }
    
    // Trivia facts
    val triviaFacts = listOf(
        "Did you know? 8 million tons of plastic are dumped into the ocean every year.",
        "Did you know? By 2050, there could be more plastic than fish in the oceans by weight.",
        "Did you know? Only 9% of all plastic ever produced has been recycled.",
        "Did you know? A single plastic bottle can take up to 450 years to decompose.",
        "Did you know? Microplastics have been found in 90% of bottled water."
    )
    
    // Infographics for the carousel
    val infographics = listOf(
        InfographicItem(
            title = "Ocean Plastic Pollution",
            description = "Learn about the impact of plastic on marine ecosystems",
            imageResId = R.drawable.plastic_pollution
        ),
        InfographicItem(
            title = "Microplastics",
            description = "Tiny plastic particles that harm marine life and enter our food chain",
            imageResId = R.drawable.microplastic_pollution
        ),
        InfographicItem(
            title = "Ocean Acidification",
            description = "How CO2 emissions are changing ocean chemistry",
            imageResId = R.drawable.ocean_acidification
        )
    )
    
    // Pollutants list
    val pollutants = listOf(
        PollutantItem(
            name = "Plastic",
            description = "Non-biodegradable material that breaks down into microplastics",
            iconResId = R.drawable.plastic_pollution
        ),
        PollutantItem(
            name = "Microplastics",
            description = "Tiny plastic particles that enter the food chain",
            iconResId = R.drawable.microplastic_pollution
        ),
        PollutantItem(
            name = "Ocean Acidification",
            description = "Industrial chemicals that disrupt marine ecosystems",
            iconResId = R.drawable.ocean_acidification
        ),
        PollutantItem(
            name = "Marine Ecosystems",
            description = "Protecting our ocean's diverse ecosystems",
            iconResId = R.drawable.mangrove
        )
    )

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
                    text = "Pollution Awareness",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OceanBlue,
                    fontWeight = FontWeight.Bold
                )
                // Empty box for alignment
                Box(modifier = Modifier.size(48.dp))
            }
            
            // Infographics Carousel
            Text(
                text = "Ocean Pollution Infographics",
                style = MaterialTheme.typography.titleLarge,
                color = OceanBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(infographics) { infographic ->
                    InfographicCard(infographic)
                }
            }
            
            // Pollutants List
            Text(
                text = "Common Ocean Pollutants",
                style = MaterialTheme.typography.titleLarge,
                color = OceanBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pollutants.forEach { pollutant ->
                    PollutantCard(pollutant)
                }
            }
            
            // Did You Know Button
            Button(
                onClick = { 
                    currentTriviaIndex = (currentTriviaIndex + 1) % triviaFacts.size
                    showTriviaDialog = true 
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = OceanBlue
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Did You Know?",
                    style = MaterialTheme.typography.titleMedium,
                    color = White
                )
            }
        }
        
        // Trivia Dialog
        if (showTriviaDialog) {
            TriviaDialog(
                triviaFact = triviaFacts[currentTriviaIndex],
                onDismiss = { showTriviaDialog = false }
            )
        }
    }
}

@Composable
fun InfographicCard(infographic: InfographicItem) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(180.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = infographic.imageResId),
                contentDescription = infographic.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = infographic.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = OceanBlue,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = infographic.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray
                )
            }
        }
    }
}

@Composable
fun PollutantCard(pollutant: PollutantItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = pollutant.iconResId),
                contentDescription = pollutant.name,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(
                    text = pollutant.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = OceanBlue,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = pollutant.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray
                )
            }
        }
    }
}

@Composable
fun TriviaDialog(
    triviaFact: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = SoftBlue
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Did You Know?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = OceanBlue,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = triviaFact,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextGray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OceanBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Got it!")
                }
            }
        }
    }
}

// Data classes for the screen
data class InfographicItem(
    val title: String,
    val description: String,
    val imageResId: Int
)

data class PollutantItem(
    val name: String,
    val description: String,
    val iconResId: Int
) 