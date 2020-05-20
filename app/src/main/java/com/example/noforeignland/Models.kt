package com.example.noforeignland

data class FeatureCollection(var features: List<Feature>)

data class Feature(var properties: Properties, var geometry: Geometry){
    data class Properties(
        var name: String,
        var icon: String,
        var id: Long
    )
    data class Geometry(
        var coordinates: Array<Double>
    )
}

data class PlaceInfo(var place: Place)

data class Place(
    val id: Long,
    val name: String,
    val lat: Double,
    val lon: Double,
    val comments: String,
    val banner: String,
    val images: List<Image>

){
    data class Image(
        var servingUrl: String
    )
}



























