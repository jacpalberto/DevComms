package com.jacpalberto.devcomms.address

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.sponsors.models.Location
import kotlinx.android.synthetic.main.activity_address.*
import org.koin.android.viewmodel.ext.android.viewModel

class AddressActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        fun newIntent(context: Context) = Intent(context, AddressActivity::class.java)
    }

    private val viewModel: AddressViewModel by viewModel()
    private val gdlLocation = LatLng(20.659698, -103.349609)
    private var gMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initToolbar()
        viewModel.fetchLocations()?.observe(this, Observer { handleLocations(it) })
    }

    private fun handleLocations(locations: DataResponse<List<Location>>?) {
        val locationList = locations?.data
        val builder = LatLngBounds.Builder()
        locationList?.forEach {
            builder.include(LatLng(it.lat.toDouble(), it.lon.toDouble()))
            gMap?.addMarker(MarkerOptions()
                    .position(LatLng(it.lat.toDouble(), it.lon.toDouble()))
                    .title(it.name))
        }

        val bounds = builder.build()
        if (locations?.data?.size == 1) {
            val location = locations?.data?.get(0)
            val cu = CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.lat.toDouble(), location.lon.toDouble()), 15F)
            gMap?.animateCamera(cu)
        } else {
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 400)
            gMap?.moveCamera(cu)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap?.isMyLocationEnabled = true
            gMap?.uiSettings?.isMyLocationButtonEnabled = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 123)
        }
        gMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(gdlLocation, 12F))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    gMap?.isMyLocationEnabled = true
                    gMap?.uiSettings?.isMyLocationButtonEnabled = true
                }
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_white)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
