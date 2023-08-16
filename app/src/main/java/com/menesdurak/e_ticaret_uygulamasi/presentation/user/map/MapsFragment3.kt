package com.menesdurak.e_ticaret_uygulamasi.presentation.user.map

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Constants.REQUEST_LOCATION_PERMISSION
import com.menesdurak.e_ticaret_uygulamasi.common.DrawMapRoute
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.common.addMarker
import com.menesdurak.e_ticaret_uygulamasi.common.calculateDistance
import com.menesdurak.e_ticaret_uygulamasi.common.changeCameraPosition
import com.menesdurak.e_ticaret_uygulamasi.common.setStartingZoomArea
import com.menesdurak.e_ticaret_uygulamasi.common.zoomArea
import dagger.hilt.android.AndroidEntryPoint
import io.nlopez.smartlocation.SmartLocation

@AndroidEntryPoint
class MapsFragment3 : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap

    private val mapsViewModel: MapsViewModel by viewModels()

    private var myLocation: Marker? = null

    private var polyline: Polyline? = null

    private var supportMapFragment: SupportMapFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        requestLocationPermissions { isGranted ->
            if (isGranted) {
                setupMap()
            } else {
                Toast.makeText(requireContext(), "Konum izinleri verilmedi", Toast.LENGTH_SHORT).show()
            }
        }
//        val location1 = Location(36.59, 31.26, "Location1")
//        val location2 = Location(38.59, 38.26, "Location2")
//        val location3 = Location(38.59, 28.26, "Location3")
//        mapsViewModel.addLocation(location1)
//        mapsViewModel.addLocation(location2)
//        mapsViewModel.addLocation(location3)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onMapReady(map: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), "Konum izinleri verilmedi", Toast.LENGTH_SHORT).show()
            return
        }

        with(map) {
            setStartingZoomArea(
                startLatLng = LatLng(42.216071, 26.389816), endLatLng = LatLng(36.690183, 44.747969)
            )

            setOnMapLoadedCallback {
                isMyLocationEnabled = true
                SmartLocation.with(requireContext()).location().start { location ->
                    map.changeCameraPosition(LatLng(location.latitude, location.longitude))
                    println("$location.latitude $location.longitude")
                    drawRoute(
                        map = map, startLocation = LatLng(location.latitude, location.longitude)
                    )
                }
            }
        }

//        mMap = map
//        mMap.setOnMapLongClickListener(this)
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(39.0, 35.0), 5.0f))
//
//        mapsViewModel.getAllLocations()
//        mapsViewModel.locations.observe(viewLifecycleOwner) {
//            when (it) {
//                is Resource.Success -> {
//                    for (index in 0 until it.data.size) {
//                        val latLng = LatLng(it.data[index].latitude, it.data[index].longitude)
//                        mMap.addMarker(
//                            MarkerOptions()
//                                .position(latLng)
//                                .title(it.data[index].name)
//                                .icon(
//                                    BitmapDescriptorFactory.defaultMarker(
//                                        BitmapDescriptorFactory.HUE_ORANGE
//                                    )
//                                )
//                        )
//                    }
//                }
//
//                is Resource.Error -> {
//                    Log.e("error", "Error in Maps Fragment")
//                }
//
//                Resource.Loading -> {
//
//                }
//            }
//        }

//        requireView().findViewById<ImageButton>(R.id.btnMyLocation).setOnClickListener {
//            SmartLocation.with(requireContext()).location().start {
//                val latLng = LatLng(it.latitude, it.longitude)
//                addMarker(latLng)
//            }
//        }
    }

    override fun onMapLongClick(p0: LatLng) {
        addMarker(p0)
    }

    private fun setupMap() {
        supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment?.getMapAsync(this)
    }

    private fun addMarker(p0: LatLng) {
        mapsViewModel.locations.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    var closestMarker = Pair<Int, Double>(0, 0.0)
                    for (index in 0 until it.data.size) {
                        closestMarker =
                            it.data[index].calculateDistance(index, p0, closestMarker)
                    }
                    val source = LatLng(p0.latitude, p0.longitude)
                    val destination = LatLng(
                        it.data[closestMarker.first].latitude,
                        it.data[closestMarker.first].longitude
                    )

//                    drawRoute(mMap, source, destination)

                }

                is Resource.Error -> {
                    Log.e("error", "Error in Maps Fragment")
                }

                Resource.Loading -> {

                }
            }
        }
    }

    private fun drawRoute(map: GoogleMap, startLocation: LatLng) {
        map.zoomArea(listOf(startLocation, LatLng(40.9834373, 28.7309099)))

        map.addMarker(startLocation, "başlangıç")

        map.addMarker(LatLng(40.9834373, 28.7309099), "bitiş")

        DrawMapRoute(
            context = requireContext(),
            map = map,
            startPoint = startLocation,
            endPoint = LatLng(40.9834373, 28.7309099),
        ).apply {
            drawRoute()

//            getTime().observe() { time ->
//                binding.txtTime.text = time
//            }
//
//            getDistance().observe() { distance ->
//                binding.txtDistance.text = distance
//            }
        }
    }

    private fun requestLocationPermissions(onRequestPermissionsResult: (Boolean) -> Unit) {
        val coarsePermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val finePermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (coarsePermissionGranted && finePermissionGranted) {
            onRequestPermissionsResult(true)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), REQUEST_LOCATION_PERMISSION
            )
        }
    }

}