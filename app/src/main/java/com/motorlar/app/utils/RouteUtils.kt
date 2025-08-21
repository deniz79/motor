package com.motorlar.app.utils

import com.google.android.gms.maps.model.LatLng
import kotlin.math.*

object RouteUtils {
    
    fun calculateDistance(points: List<LatLng>): Double {
        if (points.size < 2) return 0.0
        var totalDistance = 0.0
        for (i in 0 until points.size - 1) {
            totalDistance += calculateDistanceBetweenPoints(points[i], points[i + 1])
        }
        return totalDistance
    }
    
    fun calculateDistanceBetweenPoints(point1: LatLng, point2: LatLng): Double {
        val R = 6371.0 // Dünya'nın yarıçapı (km)
        val lat1 = Math.toRadians(point1.latitude)
        val lat2 = Math.toRadians(point2.latitude)
        val deltaLat = Math.toRadians(point2.latitude - point1.latitude)
        val deltaLng = Math.toRadians(point2.longitude - point1.longitude)
        
        val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                cos(lat1) * cos(lat2) *
                sin(deltaLng / 2) * sin(deltaLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        return R * c
    }
    
    fun calculateDuration(points: List<LatLng>): Long {
        val distance = calculateDistance(points)
        val averageSpeed = 60.0 // km/h
        return ((distance / averageSpeed) * 3600000).toLong() // milisaniye
    }
}
