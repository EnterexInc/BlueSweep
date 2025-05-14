package com.example.bluesweepmock.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.bluesweepmock.ui.screens.WasteReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

private val Context.wasteReportDataStore: DataStore<Preferences> by preferencesDataStore(name = "waste_reports")

class WasteReportStore(private val context: Context) {
    
    companion object {
        private val REPORT_COUNT = intPreferencesKey("report_count")
        
        // Creates keys for each report field
        private fun reportIdKey(id: Int) = intPreferencesKey("report_${id}_id")
        private fun reportDescriptionKey(id: Int) = stringPreferencesKey("report_${id}_description")
        private fun reportLocationKey(id: Int) = stringPreferencesKey("report_${id}_location")
        private fun reportDateKey(id: Int) = longPreferencesKey("report_${id}_date")
        private fun reportPhotoUriKey(id: Int) = stringPreferencesKey("report_${id}_photo_uri")
    }
    
    // Get all waste reports
    val wasteReportsFlow: Flow<List<WasteReport>> = context.wasteReportDataStore.data
        .map { preferences ->
            val reportCount = preferences[REPORT_COUNT] ?: 0
            val reports = mutableListOf<WasteReport>()
            
            for (i in 1..reportCount) {
                val id = preferences[reportIdKey(i)] ?: continue
                val description = preferences[reportDescriptionKey(i)] ?: ""
                val location = preferences[reportLocationKey(i)] ?: ""
                val date = preferences[reportDateKey(i)]?.let { Date(it) } ?: Date()
                val photoUri = preferences[reportPhotoUriKey(i)]
                
                reports.add(
                    WasteReport(
                        id = id,
                        description = description,
                        location = location,
                        date = date,
                        photoUri = photoUri
                    )
                )
            }
            
            // Sort by date (newest first)
            reports.sortByDescending { it.date }
            reports
        }
    
    // Add a new waste report
    suspend fun addWasteReport(description: String, location: String, photoUri: String?) {
        context.wasteReportDataStore.edit { preferences ->
            val currentCount = preferences[REPORT_COUNT] ?: 0
            val newId = currentCount + 1
            
            preferences[REPORT_COUNT] = newId
            preferences[reportIdKey(newId)] = newId
            preferences[reportDescriptionKey(newId)] = description
            preferences[reportLocationKey(newId)] = location
            preferences[reportDateKey(newId)] = System.currentTimeMillis()
            if (photoUri != null) {
                preferences[reportPhotoUriKey(newId)] = photoUri
            }
        }
    }
    
    // Delete a waste report
    suspend fun deleteWasteReport(id: Int) {
        context.wasteReportDataStore.edit { preferences ->
            preferences.remove(reportIdKey(id))
            preferences.remove(reportDescriptionKey(id))
            preferences.remove(reportLocationKey(id))
            preferences.remove(reportDateKey(id))
            preferences.remove(reportPhotoUriKey(id))
            
            // We don't decrement the count to maintain ID uniqueness
            // But we could reorganize the IDs if needed
        }
    }
    
    // Clear all waste reports (for testing)
    suspend fun clearAllReports() {
        context.wasteReportDataStore.edit { preferences ->
            preferences[REPORT_COUNT] = 0
        }
    }
} 