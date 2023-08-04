package com.menesdurak.e_ticaret_uygulamasi.presentation.user

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.menesdurak.e_ticaret_uygulamasi.R

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
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
    }

    override fun onMapLongClick(p0: LatLng) {
        mMap.clear()
        mMap.addMarker(
            MarkerOptions()
                .position(p0)
                .title("Long-clicked Location")
        )
    }

}