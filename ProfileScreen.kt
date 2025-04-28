package com.example.bluesweepmock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bluesweepmock.R
import com.example.bluesweepmock.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit
) {
    // Sample user data
    val user = UserProfile(
        name = "Shairyishuanda bin Raj",
        username = "@raj_ocean",
        bio = "Marine conservation enthusiast | Ocean cleanup volunteer | Love diving and protecting marine life",
        location = "Kuala Lumpur, Malaysia",
        joinDate = "Joined March 2023",
        eventsAttended = 12,
        wasteReports = 28,
        impactScore = 85,
        profileImageResId = R.drawable.mascot
    )
    
    // Sample badges
    val badges = listOf(
        Badge(
            name = "Ocean Guardian",
            description = "Participated in 10+ cleanup events",
            iconResId = R.drawable.beach,
            isUnlocked = true
        ),
        Badge(
            name = "Waste Warrior",
            description = "Reported 25+ waste items",
            iconResId = R.drawable.plastic_pollution,
            isUnlocked = true
        ),
        Badge(
            name = "Community Leader",
            description = "Organized 3+ cleanup events",
            iconResId = R.drawable.mangrove,
            isUnlocked = true
        ),
        Badge(
            name = "Marine Expert",
            description = "Completed all marine education modules",
            iconResId = R.drawable.ocean_acidification,
            isUnlocked = false
        ),
        Badge(
            name = "Global Impact",
            description = "Contributed to international cleanup efforts",
            iconResId = R.drawable.river,
            isUnlocked = false
        )
    )
    
    // Sample achievements
    val achievements = listOf(
        Achievement(
            title = "First Cleanup",
            description = "Participated in your first ocean cleanup event",
            date = "March 15, 2023",
            iconResId = R.drawable.beach
        ),
        Achievement(
            title = "Waste Reporter",
            description = "Reported your first waste item",
            date = "April 2, 2023",
            iconResId = R.drawable.plastic_pollution
        ),
        Achievement(
            title = "Event Organizer",
            description = "Organized your first cleanup event",
            date = "June 10, 2023",
            iconResId = R.drawable.mangrove
        ),
        Achievement(
            title = "Community Builder",
            description = "Invited 5 friends to join BlueSweep",
            date = "August 22, 2023",
            iconResId = R.drawable.lake
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
        ) {
            // Top Bar with Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
                    text = "Profile",
                    style = MaterialTheme.typography.titleLarge,
                    color = OceanBlue,
                    fontWeight = FontWeight.Bold
                )
                
                IconButton(onClick = { /* TODO: Implement settings */ }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = OceanBlue
                    )
                }
            }
            
            // Profile Header
            ProfileHeader(user)
            
            // Stats Section
            StatsSection(user)
            
            // Badges Section
            BadgesSection(badges)
            
            // Achievements Section
            AchievementsSection(achievements)
            
            // Settings Options
            SettingsOptions()
            
            // Logout Button
            LogoutButton()
        }
    }
}

@Composable
fun ProfileHeader(user: UserProfile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(3.dp, OceanBlue, CircleShape)
                .background(LightBlue),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = user.profileImageResId),
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Name and Username
        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineMedium,
            color = OceanBlue,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = user.username,
            style = MaterialTheme.typography.bodyLarge,
            color = TextGray
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Bio
        Text(
            text = user.bio,
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Location and Join Date
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = OceanBlue,
                modifier = Modifier.size(16.dp)
            )
            
            Spacer(modifier = Modifier.width(4.dp))
            
            Text(
                text = user.location,
                style = MaterialTheme.typography.bodySmall,
                color = TextGray
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = user.joinDate,
                style = MaterialTheme.typography.bodySmall,
                color = TextGray
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Edit Profile Button
        OutlinedButton(
            onClick = { /* TODO: Implement edit profile */ },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = OceanBlue
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = SolidColor(OceanBlue)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile",
                modifier = Modifier.size(16.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text("Edit Profile")
        }
    }
}

@Composable
fun StatsSection(user: UserProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
            Text(
                text = "Your Impact",
                style = MaterialTheme.typography.titleMedium,
                color = OceanBlue,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Impact Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Impact Score",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray
                )
                
                Text(
                    text = "${user.impactScore}/100",
                    style = MaterialTheme.typography.titleMedium,
                    color = OceanBlue,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress Bar
            LinearProgressIndicator(
                progress = user.impactScore / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = OceanBlue,
                trackColor = LightBlue
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.Event,
                    value = user.eventsAttended.toString(),
                    label = "Events"
                )
                
                StatItem(
                    icon = Icons.Default.Report,
                    value = user.wasteReports.toString(),
                    label = "Reports"
                )
                
                StatItem(
                    icon = Icons.Default.VolunteerActivism,
                    value = "3",
                    label = "Organized"
                )
            }
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(LightBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = OceanBlue
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = OceanBlue,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextGray
        )
    }
}

@Composable
fun BadgesSection(badges: List<Badge>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Badges",
            style = MaterialTheme.typography.titleMedium,
            color = OceanBlue,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(badges) { badge ->
                BadgeItem(badge)
            }
        }
    }
}

@Composable
fun BadgeItem(badge: Badge) {
    Card(
        modifier = Modifier
            .width(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(if (badge.isUnlocked) LightBlue else Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = badge.iconResId),
                    contentDescription = badge.name,
                    modifier = Modifier.size(40.dp),
                    alpha = if (badge.isUnlocked) 1f else 0.5f
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = badge.name,
                style = MaterialTheme.typography.bodyMedium,
                color = if (badge.isUnlocked) OceanBlue else TextGray,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = badge.description,
                style = MaterialTheme.typography.bodySmall,
                color = TextGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AchievementsSection(achievements: List<Achievement>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Achievements",
            style = MaterialTheme.typography.titleMedium,
            color = OceanBlue,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            achievements.forEach { achievement ->
                AchievementItem(achievement)
            }
        }
    }
}

@Composable
fun AchievementItem(achievement: Achievement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(LightBlue),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = achievement.iconResId),
                    contentDescription = achievement.title,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OceanBlue,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray
                )
                
                Text(
                    text = achievement.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray
                )
            }
        }
    }
}

@Composable
fun SettingsOptions() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleMedium,
            color = OceanBlue,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        SettingsItem(
            icon = Icons.Default.Notifications,
            title = "Notifications",
            subtitle = "Manage your notification preferences"
        )
        
        SettingsItem(
            icon = Icons.Default.Lock,
            title = "Privacy",
            subtitle = "Control your privacy settings"
        )
        
        SettingsItem(
            icon = Icons.Default.Help,
            title = "Help & Support",
            subtitle = "Get help with using BlueSweep"
        )
        
        SettingsItem(
            icon = Icons.Default.Info,
            title = "About",
            subtitle = "Learn more about BlueSweep"
        )
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* TODO: Implement settings action */ }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = OceanBlue
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OceanBlue,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = TextGray
            )
        }
    }
}

@Composable
fun LogoutButton() {
    Button(
        onClick = { /* TODO: Implement logout */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red.copy(alpha = 0.1f),
            contentColor = Color.Red
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Logout,
            contentDescription = "Logout",
            modifier = Modifier.size(16.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text("Logout")
    }
}

// Data classes for the profile screen
data class UserProfile(
    val name: String,
    val username: String,
    val bio: String,
    val location: String,
    val joinDate: String,
    val eventsAttended: Int,
    val wasteReports: Int,
    val impactScore: Int,
    val profileImageResId: Int
)

data class Badge(
    val name: String,
    val description: String,
    val iconResId: Int,
    val isUnlocked: Boolean
)

data class Achievement(
    val title: String,
    val description: String,
    val date: String,
    val iconResId: Int
) 