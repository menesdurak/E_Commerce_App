package com.menesdurak.e_ticaret_uygulamasi.presentation.user.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.datatransport.BuildConfig
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.common.calculateDistance
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.Location
import com.menesdurak.e_ticaret_uygulamasi.data.remote.api.DirectionsApi
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.DirectionsResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap

    private val mapsViewModel: MapsViewModel by viewModels()

    private var myLocation: Marker? = null

    private var polyline: Polyline? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val location1 = Location(36.59, 31.26, "Location1")
        val location2 = Location(38.59, 38.26, "Location2")
        val location3 = Location(38.59, 28.26, "Location3")
        mapsViewModel.addLocation(location1)
        mapsViewModel.addLocation(location2)
        mapsViewModel.addLocation(location3)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        view.findViewById<ImageButton>(R.id.btnMyLocation).setOnClickListener {
            coroutineScope.launch {
                val currentLocation = getLocation()
                addMarker(currentLocation)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(this)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(39.0, 35.0), 5.0f))

        mapsViewModel.getAllLocations()
        mapsViewModel.locations.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    for (index in 0 until it.data.size) {
                        val latLng = LatLng(it.data[index].latitude, it.data[index].longitude)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(it.data[index].name)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        )
                    }
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Maps Fragment")
                }

                Resource.Loading -> {

                }
            }
        }
    }

    override fun onMapLongClick(p0: LatLng) {
        addMarker(p0)
    }

    private fun addMarker(p0: LatLng) {
        if (myLocation != null) {
            myLocation!!.remove()
        }
        myLocation = mMap.addMarker(
            MarkerOptions()
                .position(p0)
                .title("My Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        )
        mapsViewModel.locations.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    var closestMarker = Pair<Int, Double>(0, 0.0)
                    for (index in 0 until it.data.size) {
                        closestMarker = it.data[index].calculateDistance(index, p0, closestMarker)
                    }
                    polyline?.remove()
                    polyline = mMap.addPolyline(
                        PolylineOptions()
                            .clickable(true)
                            .add(
                                LatLng(
                                    it.data[closestMarker.first].latitude,
                                    it.data[closestMarker.first].longitude
                                ),
                                LatLng(p0.latitude, p0.longitude)
                            )
                    )
                    val source = LatLng(p0.latitude, p0.longitude)
                    val destination = LatLng(
                        it.data[closestMarker.first].latitude,
                        it.data[closestMarker.first].longitude
                    )

//                    drawRoute(source, destination)

                }

                is Resource.Error -> {
                    Log.e("error", "Error in Maps Fragment")
                }

                Resource.Loading -> {

                }
            }
        }
    }

    private suspend fun getLocation(): LatLng {
        var latLng = LatLng(30.0, 30.0)
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            withContext(Dispatchers.IO) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            latLng = LatLng(location.latitude, location.longitude)
                        } else {
                            println("Location is null.")
                        }
                    }
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
        return latLng
    }

    //    private fun drawRoute(origin: LatLng, destination: LatLng) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://maps.googleapis.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val directionsApi = retrofit.create(DirectionsApi::class.java)
//        val call = directionsApi.getDirections(
//            origin = "${origin.latitude},${origin.longitude}",
//            destination = "${destination.latitude},${destination.longitude}",
//            apiKey = resources.getString(R.string.google_api_key)
//        )
//
//        call.enqueue(object : Callback<DirectionsResponse> {
//            override fun onResponse(
//                call: Call<DirectionsResponse>,
//                response: Response<DirectionsResponse>,
//            ) {
//                val routes = response.body()?.routes
//                if (!routes.isNullOrEmpty()) {
//                    val points = decodePolyline(routes[0].overviewPolyline.points)
//                    val polylineOptions = PolylineOptions()
//                        .addAll(points)
//                        .color(resources.getColor(R.color.sub2)) // Customize route color
//
//                    mMap.addPolyline(polylineOptions)
//                }
//            }
//
//            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
//                // Handle failure
//            }
//        })
//    }
//
//    private fun decodePolyline(encoded: String): List<LatLng> {
//        val poly = ArrayList<LatLng>()
//        var index = 0
//        val len = encoded.length
//        var lat = 0
//        var lng = 0
//
//        while (index < len) {
//            var b: Int
//            var shift = 0
//            var result = 0
//            do {
//                b = encoded[index++].toInt() - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlat = if (result and 1 != 0) -(result shr 1) else result shr 1
//            lat += dlat
//
//            shift = 0
//            result = 0
//            do {
//                b = encoded[index++].toInt() - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlng = if (result and 1 != 0) -(result shr 1) else result shr 1
//            lng += dlng
//
//            val latLng = LatLng(
//                lat.toDouble() / 1e5,
//                lng.toDouble() / 1e5
//            )
//            poly.add(latLng)
//        }
//        return poly
//    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                coroutineScope.launch {
                    val currentLocation = getLocation()
                    addMarker(currentLocation)
                }
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}