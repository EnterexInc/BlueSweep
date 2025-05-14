package com.example.bluesweepmock.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import java.util.*
import kotlin.coroutines.resume

class LocationManager(private val context: Context) {
    
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    
    // Check if location permissions are granted
    fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }
    
    // This method is marked with RequiresPermission to satisfy the lint check
    // but in our mock app, we'll use getSampleLocation() instead
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    suspend fun getCurrentLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            val cancellationTokenSource = CancellationTokenSource()
            
            try {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).addOnSuccessListener { location ->
                    continuation.resume(location)
                }.addOnFailureListener {
                    continuation.resume(null)
                }
            } catch (e: SecurityException) {
                continuation.resume(null)
            }
            
            continuation.invokeOnCancellation {
                cancellationTokenSource.cancel()
            }
        }
    }
    
    // Get address from location
    fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        if (!Geocoder.isPresent()) {
            return "$latitude, $longitude"
        }
        
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.isNullOrEmpty()) {
                "$latitude, $longitude"
            } else {
                val address = addresses[0]
                val addressParts = mutableListOf<String>()
                
                // Add the most specific address components
                if (address.thoroughfare != null) {
                    addressParts.add(address.thoroughfare)
                }
                if (address.subLocality != null) {
                    addressParts.add(address.subLocality)
                }
                if (address.locality != null) {
                    addressParts.add(address.locality)
                }
                if (address.adminArea != null) {
                    addressParts.add(address.adminArea)
                }
                
                if (addressParts.isEmpty()) {
                    "$latitude, $longitude"
                } else {
                    addressParts.joinToString(", ")
                }
            }
        } catch (e: IOException) {
            "$latitude, $longitude"
        }
    }
    
    // Get a sample location for testing - this is the primary method we'll use in the mock app
    suspend fun getSampleLocation(): Pair<Double, String> {
        // For a real app, we would check permissions and get the actual location
        // but for this mock, we'll use predefined locations
        val locations = listOf(
            Pair(3.139, "Kuala Lumpur, Malaysia"),
            Pair(3.0733, "Shah Alam, Malaysia"),
            Pair(5.4141, "Penang, Malaysia"),
            Pair(1.4927, "Johor Bahru, Malaysia"),
            Pair(2.9264, "Putrajaya, Malaysia")
        )
        
        // Return a random location from the list
        return locations.random()
    }
} 