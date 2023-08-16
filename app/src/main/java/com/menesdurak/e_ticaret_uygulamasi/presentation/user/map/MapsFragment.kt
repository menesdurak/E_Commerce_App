package com.menesdurak.e_ticaret_uygulamasi.presentation.user.map

import android.Manifest
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
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Constants.REQUEST_LOCATION_PERMISSION
import com.menesdurak.e_ticaret_uygulamasi.common.DrawMapRoute
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.common.addMarker
import com.menesdurak.e_ticaret_uygulamasi.common.calculateDistance
import com.menesdurak.e_ticaret_uygulamasi.common.zoomArea
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap

    private val mapsViewModel: MapsViewModel by viewModels()

    private var myLocation: Marker? = null

    private var polyline: Polyline? = null

    private var supportMapFragment: SupportMapFragment? = null

    private var showMap = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
//        val location1 = Location(36.59, 31.26, "Location1")
//        val location2 = Location(38.59, 38.26, "Location2")
//        val location3 = Location(38.59, 28.26, "Location3")
//        mapsViewModel.addLocation(location1)
//        mapsViewModel.addLocation(location2)
//        mapsViewModel.addLocation(location3)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestLocationPermissions { isGranted ->
            if (isGranted) {
                setupMap()
            } else {
                Toast.makeText(requireContext(), "Konum izinleri verilmedi", Toast.LENGTH_SHORT)
                    .show()
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
                    drawRoute(mMap, LatLng(41.082533, 28.785525))
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

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun addMarker(p0: LatLng) {
        mMap.addMarker(
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
        map.zoomArea(listOf(LatLng(41.082533, 28.785525), LatLng(41.069075, 28.663302)))

        map.addMarker(LatLng(41.082533, 28.785525), "başlangıç")

        map.addMarker(LatLng(41.069075, 28.663302), "bitiş")

        DrawMapRoute(
            context = requireContext(),
            map = map,
            startPoint = LatLng(41.082533, 28.785525),
            endPoint = LatLng(41.069075, 28.663302),
        ).apply {
            drawRoute()

//            getTime().observe(this@MapsFragment) { time ->
//                binding.txtTime.text = time
//            }
//
//            getDistance().observe(this@MapsFragment) { distance ->
//                binding.txtDistance.text = distance
//            }
        }
    }

    private fun requestLocationPermissions(onRequestPermissionsResult: (Boolean) -> Unit) {
        val coarsePermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val finePermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (coarsePermissionGranted && finePermissionGranted) {
            onRequestPermissionsResult(true)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), REQUEST_LOCATION_PERMISSION
            )
        }
    }

}