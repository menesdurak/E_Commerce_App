package com.menesdurak.e_ticaret_uygulamasi.presentation.user.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.gms.maps.model.PolylineOptions
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.common.calculateDistance
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap

    private val mapsViewModel: MapsViewModel by viewModels()

    private var myLocation: Marker? = null

    private var polyline: Polyline? = null

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
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
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
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Maps Fragment")
                }

                Resource.Loading -> {

                }
            }
        }

    }

}