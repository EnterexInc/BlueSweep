package com.example.bluesweepmock.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bluesweepmock.data.WasteReportStore
import com.example.bluesweepmock.ui.screens.WasteReport
import com.example.bluesweepmock.utils.LocationManager
import com.example.bluesweepmock.utils.PhotoManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class WasteTrackingViewModel(
    private val context: Context,
    private val wasteReportStore: WasteReportStore,
    private val photoManager: PhotoManager,
    private val locationManager: LocationManager
) : ViewModel() {
    
    // UI State
    data class WasteTrackingUiState(
        val reports: List<WasteReport> = emptyList(),
        val currentPhotoUri: Uri? = null,
        val description: String = "",
        val location: String = "",
        val isLoading: Boolean = false,
        val isSubmitEnabled: Boolean = false,
        val showSuccessMessage: Boolean = false
    )
    
    private val _uiState = MutableStateFlow(WasteTrackingUiState())
    val uiState: StateFlow<WasteTrackingUiState> = _uiState.asStateFlow()
    
    init {
        loadReports()
    }
    
    private fun loadReports() {
        viewModelScope.launch {
            wasteReportStore.wasteReportsFlow.collect { reports ->
                _uiState.update { it.copy(reports = reports) }
            }
        }
    }
    
    // Update description
    fun updateDescription(description: String) {
        _uiState.update { 
            it.copy(
                description = description,
                isSubmitEnabled = description.isNotBlank() && it.location.isNotBlank()
            )
        }
    }
    
    // Set photo from Camera or Gallery
    fun setPhoto(uri: Uri?) {
        _uiState.update { 
            it.copy(
                currentPhotoUri = uri,
                isSubmitEnabled = it.description.isNotBlank() && it.location.isNotBlank()
            )
        }
    }
    
    // For testing purposes, use a sample image
    fun setSamplePhoto() {
        viewModelScope.launch {
            val uri = photoManager.getSampleImageUri()
            setPhoto(uri)
        }
    }
    
    // Update location
    fun updateLocation(location: String) {
        _uiState.update { 
            it.copy(
                location = location,
                isSubmitEnabled = it.description.isNotBlank() && location.isNotBlank()
            )
        }
    }
    
    // Get current location
    fun getCurrentLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                if (locationManager.hasLocationPermission()) {
                    // For real implementation
                    // val location = locationManager.getCurrentLocation()
                    // if (location != null) {
                    //     val address = locationManager.getAddressFromLocation(
                    //         location.latitude,
                    //         location.longitude
                    //     )
                    //     updateLocation(address)
                    // }
                    
                    // For testing purposes
                    val (_, address) = locationManager.getSampleLocation()
                    updateLocation(address)
                } else {
                    // Location permission not granted
                    // In a real app, you would request permission here
                    updateLocation("Location permission required")
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
    
    // Submit report
    fun submitReport() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val photoUriString = _uiState.value.currentPhotoUri?.toString()
                wasteReportStore.addWasteReport(
                    description = _uiState.value.description,
                    location = _uiState.value.location,
                    photoUri = photoUriString
                )
                
                // Reset form and show success message
                _uiState.update { 
                    it.copy(
                        description = "",
                        currentPhotoUri = null,
                        showSuccessMessage = true
                    )
                }
                
                // Hide success message after 3 seconds
                kotlinx.coroutines.delay(3000)
                _uiState.update { it.copy(showSuccessMessage = false) }
                
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
    
    // Clear success message
    fun clearSuccessMessage() {
        _uiState.update { it.copy(showSuccessMessage = false) }
    }
    
    // Factory for creating the ViewModel
    class Factory(
        private val context: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WasteTrackingViewModel::class.java)) {
                return WasteTrackingViewModel(
                    context = context,
                    wasteReportStore = WasteReportStore(context),
                    photoManager = PhotoManager(context),
                    locationManager = LocationManager(context)
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 