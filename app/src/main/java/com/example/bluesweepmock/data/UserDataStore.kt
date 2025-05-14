package com.example.bluesweepmock.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")

class UserDataStore(private val context: Context) {
    
    companion object {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_BIO = stringPreferencesKey("user_bio")
        val USER_LOCATION = stringPreferencesKey("user_location")
        val USER_USERNAME = stringPreferencesKey("user_username")
    }
    
    // Get user profile data
    val userNameFlow: Flow<String> = context.dataStore.data
        .map { preferences -> 
            preferences[USER_NAME] ?: ""
        }
        
    val userBioFlow: Flow<String> = context.dataStore.data
        .map { preferences -> 
            preferences[USER_BIO] ?: "Ocean enthusiast and marine conservationist"
        }
        
    val userLocationFlow: Flow<String> = context.dataStore.data
        .map { preferences -> 
            preferences[USER_LOCATION] ?: "Earth"
        }
        
    val userUsernameFlow: Flow<String> = context.dataStore.data
        .map { preferences -> 
            preferences[USER_USERNAME] ?: ""
        }
    
    // Update user profile data
    suspend fun updateUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }
    
    suspend fun updateUserBio(bio: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_BIO] = bio
        }
    }
    
    suspend fun updateUserLocation(location: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_LOCATION] = location
        }
    }
    
    suspend fun updateUserUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_USERNAME] = username
        }
    }
    
    // Update all user profile data at once
    suspend fun updateUserProfile(name: String, bio: String, location: String, username: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
            preferences[USER_BIO] = bio
            preferences[USER_LOCATION] = location
            preferences[USER_USERNAME] = username
        }
    }
} 