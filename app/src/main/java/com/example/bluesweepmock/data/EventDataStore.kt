package com.example.bluesweepmock.data

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.bluesweepmock.R
import com.example.bluesweepmock.ui.screens.CleanupEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

private val Context.eventDataStore: DataStore<Preferences> by preferencesDataStore(name = "custom_events")

class EventDataStore(private val context: Context) {
    
    companion object {
        private val EVENT_COUNT = intPreferencesKey("event_count")
        
        // Creates keys for each event field
        private fun eventIdKey(id: Int) = intPreferencesKey("event_${id}_id")
        private fun eventTitleKey(id: Int) = stringPreferencesKey("event_${id}_title")
        private fun eventDescriptionKey(id: Int) = stringPreferencesKey("event_${id}_description")
        private fun eventDateKey(id: Int) = longPreferencesKey("event_${id}_date")
        private fun eventTimeKey(id: Int) = stringPreferencesKey("event_${id}_time")
        private fun eventLocationKey(id: Int) = stringPreferencesKey("event_${id}_location")
        private fun eventParticipantsKey(id: Int) = intPreferencesKey("event_${id}_participants")
        private fun eventMaxParticipantsKey(id: Int) = intPreferencesKey("event_${id}_max_participants")
        private fun eventImageTypeKey(id: Int) = intPreferencesKey("event_${id}_image_type")
        private fun eventCustomImageUriKey(id: Int) = stringPreferencesKey("event_${id}_custom_image_uri")
    }
    
    // Get all custom events
    val eventsFlow: Flow<List<CleanupEvent>> = context.eventDataStore.data
        .map { preferences ->
            val eventCount = preferences[EVENT_COUNT] ?: 0
            val events = mutableListOf<CleanupEvent>()
            
            for (i in 1..eventCount) {
                val id = preferences[eventIdKey(i)] ?: continue
                val title = preferences[eventTitleKey(i)] ?: ""
                val description = preferences[eventDescriptionKey(i)] ?: ""
                val date = preferences[eventDateKey(i)]?.let { Date(it) } ?: Date()
                val time = preferences[eventTimeKey(i)] ?: ""
                val location = preferences[eventLocationKey(i)] ?: ""
                val participants = preferences[eventParticipantsKey(i)] ?: 0
                val maxParticipants = preferences[eventMaxParticipantsKey(i)] ?: 0
                
                // Handle either default image resource or custom image URI
                val customImageUri = preferences[eventCustomImageUriKey(i)]
                val imageResId = if (customImageUri != null) {
                    // If there's a custom image URI, use a placeholder ID that will be ignored
                    -1
                } else {
                    // Otherwise use the stored image resource ID
                    preferences[eventImageTypeKey(i)] ?: R.drawable.beach
                }
                
                events.add(
                    CleanupEvent(
                        id = id,
                        title = title,
                        description = description,
                        date = date,
                        time = time,
                        location = location,
                        participants = participants,
                        maxParticipants = maxParticipants,
                        imageResId = imageResId,
                        customImageUri = customImageUri
                    )
                )
            }
            
            // Sort by date (soonest first)
            events.sortBy { it.date }
            events
        }
    
    // Add a new custom event with default image
    suspend fun addEvent(
        title: String,
        description: String,
        date: Date,
        time: String,
        location: String,
        maxParticipants: Int,
        imageType: Int
    ) {
        addEvent(title, description, date, time, location, maxParticipants, imageType, null)
    }
    
    // Add a new custom event with custom image URI
    suspend fun addEvent(
        title: String,
        description: String,
        date: Date,
        time: String,
        location: String,
        maxParticipants: Int,
        imageType: Int? = null,
        customImageUri: String? = null
    ) {
        context.eventDataStore.edit { preferences ->
            val currentCount = preferences[EVENT_COUNT] ?: 0
            val newId = currentCount + 1
            
            preferences[EVENT_COUNT] = newId
            preferences[eventIdKey(newId)] = newId
            preferences[eventTitleKey(newId)] = title
            preferences[eventDescriptionKey(newId)] = description
            preferences[eventDateKey(newId)] = date.time
            preferences[eventTimeKey(newId)] = time
            preferences[eventLocationKey(newId)] = location
            preferences[eventParticipantsKey(newId)] = 0 // Start with 0 participants
            preferences[eventMaxParticipantsKey(newId)] = maxParticipants
            
            if (customImageUri != null) {
                preferences[eventCustomImageUriKey(newId)] = customImageUri
            } else if (imageType != null) {
                preferences[eventImageTypeKey(newId)] = imageType
            } else {
                preferences[eventImageTypeKey(newId)] = R.drawable.beach // Default image
            }
        }
    }
    
    // Register for an event (increase participant count)
    suspend fun registerForEvent(id: Int) {
        context.eventDataStore.edit { preferences ->
            val currentParticipants = preferences[eventParticipantsKey(id)] ?: 0
            val maxParticipants = preferences[eventMaxParticipantsKey(id)] ?: 0
            
            if (currentParticipants < maxParticipants) {
                preferences[eventParticipantsKey(id)] = currentParticipants + 1
            }
        }
    }
    
    // Delete an event
    suspend fun deleteEvent(id: Int) {
        context.eventDataStore.edit { preferences ->
            preferences.remove(eventIdKey(id))
            preferences.remove(eventTitleKey(id))
            preferences.remove(eventDescriptionKey(id))
            preferences.remove(eventDateKey(id))
            preferences.remove(eventTimeKey(id))
            preferences.remove(eventLocationKey(id))
            preferences.remove(eventParticipantsKey(id))
            preferences.remove(eventMaxParticipantsKey(id))
            preferences.remove(eventImageTypeKey(id))
            preferences.remove(eventCustomImageUriKey(id))
            
            // We don't decrement the count to maintain ID uniqueness
        }
    }
    
    // Clear all events (for testing)
    suspend fun clearAllEvents() {
        context.eventDataStore.edit { preferences ->
            preferences[EVENT_COUNT] = 0
        }
    }
} 