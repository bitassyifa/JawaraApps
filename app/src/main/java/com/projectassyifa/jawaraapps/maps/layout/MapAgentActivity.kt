package com.projectassyifa.jawaraapps.maps.layout



import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.projectassyifa.jawaraapps.databinding.ActivityMapAgentBinding
import java.io.IOException
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.util.*
import android.R
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.extra.Token
import com.projectassyifa.jawaraapps.maps.data.AgentVM
import javax.inject.Inject
import com.google.android.gms.maps.model.MarkerOptions
import com.projectassyifa.jawaraapps.home.layout.HomeActivity
import com.projectassyifa.jawaraapps.login.layout.LoginActivity
import com.projectassyifa.jawaraapps.pickup.data.PickModelInsert
import com.projectassyifa.jawaraapps.pickup.data.PickupRepo


class MapAgentActivity : AppCompatActivity(), OnMapReadyCallback,
    LocationListener,GoogleMap.OnCameraMoveListener,GoogleMap.OnCameraMoveStartedListener,GoogleMap.OnCameraIdleListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
//    private var service: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLastLocation: Location? = null
//    private var mCurrLocationMarker: Marker? = null
    private var mMap: GoogleMap?=null
//    private var REQUEST_LOCATION_CODE = 101
    private lateinit var binding: ActivityMapAgentBinding
    private var fusedLocationProviderClient : FusedLocationProviderClient? = null
    private val DEFAULT_ZOOM = 15f
    lateinit var mapView: MapView
    lateinit var addressTv : TextView
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    private var service: LocationManager? = null
    private var enabled: Boolean? = null
    private lateinit var tokenOuth: Token
    private val options = MarkerOptions()
    private val latlngs: ArrayList<LatLng> = ArrayList()
    private var idAgent : String? = null
    private var noTlp : String ? = null
    private var alamat : String ? = null
    private var longitude : Double? = null
    private var latitude : Double? = null
    var dataLogin: SharedPreferences? = null

    @Inject
    lateinit var agentVM: AgentVM

    @Inject
    lateinit var pickupRepo: PickupRepo

    override fun onLocationChanged(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses : List<Address>?= null
        try {
            addresses = geocoder.getFromLocation(location.latitude,location.longitude,1)

        } catch (e:IOException){
            e.printStackTrace()
        }


        setAddress(addresses!![0])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapAgentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (applicationContext as JawaraApps).applicationComponent.inject(this)
        dataLogin = this.getSharedPreferences(
            getString(com.projectassyifa.jawaraapps.R.string.sp),
            Context.MODE_PRIVATE
        )
        checkGps()

        mapView = binding.map
        addressTv = binding.addressPick
        var mapViewBundle : Bundle? =null
        if (savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

//        binding.buttonPick.isEnabled = id_agen != null
        binding.buttonPick.setOnClickListener(this)
    }

    private fun checkGps() {
        service = this.getSystemService(LOCATION_SERVICE) as LocationManager
        if (!service!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder(this)
                .setTitle("Lokasi tidak ditemukan") // GPS not found
                .setMessage("Aktifkan Lokasi ?") // Want to enable?
                .setPositiveButton(R.string.yes,
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        this.startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    })
                .setNegativeButton(R.string.no, null)
                .show()
        } else{
            println("MASUK ELSE CHECK GPS")
            getCurrentLocation()
        }
    }


    public override fun onSaveInstanceState(
        outState: Bundle
    ) {
        super.onSaveInstanceState(outState)
//        checkLocationPermission()
        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null){
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY,mapViewBundle)
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mapView.onResume()
        mMap = googleMap
        onCameraIdle()


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        )!= PackageManager.PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )!= PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            return
        }
        tokenOuth = Token(this)
        //agent
        agentVM.agentResponse.observe(this, androidx.lifecycle.Observer {

            it.forEach {
                latlngs.add(LatLng(it.latitude, it.longitude))

                for (point in latlngs) {
                    println("POINT $point")
                    options.position(point)
                    options.title(it.fullname)
                    options.snippet(it.id_agent)
                    options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_menu_myplaces))
                    googleMap.addMarker(options)

                }
            }

            googleMap.setOnMarkerClickListener { marker ->
                if (marker.isInfoWindowShown){
                    marker.hideInfoWindow()
                } else {
                    idAgent = marker.snippet
                    binding.agent.setText(marker.title)
                }
                true
            }




        })

//        mMap!!.setOnMarkerClickListener { marker ->
//            if (marker.isInfoWindowShown) {
//                marker.hideInfoWindow()
//            } else {
//                val builder = AlertDialog.Builder(this)
//                builder.setTitle("Konfirmasi ")
//                builder.setMessage("Pilih agent  ${marker.title} ? \n"
//
//                )
//                builder.setPositiveButton("Ya") { dialog, which ->
//                    binding.agent.setText(marker.title)
//                    idAgent = marker.snippet
////                            alamat = it.address
////                            noTlp = it.no_tlp
//                }
//                builder.setNegativeButton("Tidak"){dialog,which ->
//                }
//                val dialog: AlertDialog = builder.create()
//                dialog.show()
////                    println("${marker.title}")
//                marker.showInfoWindow()
//            }
//            true
//        }

        agentVM.agent("Bearer ${tokenOuth.fetchAuthToken()}",this)
        mMap!!.isMyLocationEnabled = true
        mMap!!.setOnCameraMoveListener(this)
        mMap!!.setOnCameraMoveStartedListener(this)
        mMap!!.setOnCameraIdleListener(this)


    }


private fun getCurrentLocation() {
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@MapAgentActivity)
    try {
        @SuppressLint("MissingPermission")
        val location = fusedLocationProviderClient!!.lastLocation
        location.addOnCompleteListener(object : OnCompleteListener<Location>{
            override fun onComplete(p0: Task<Location>) {
                if (p0.isSuccessful){
                    val currentLocation = p0.result as Location?
                    if (currentLocation != null){
                        moveCamera(
                            LatLng(currentLocation.latitude,currentLocation.longitude),DEFAULT_ZOOM
                        )
                    }
                }   else {
                    Toast.makeText(this@MapAgentActivity,"Current Location Not Found",Toast.LENGTH_SHORT).show()
                }
            }

        })
    } catch (se : Exception){
        Log.e("TAG","Security Exception")
    }
}

    private fun moveCamera(latLng: LatLng, defaultZoom: Float) {

        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,defaultZoom))
    }
    @Synchronized
    fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mGoogleApiClient!!.connect()
    }


    private fun setAddress(address: Address) {
        println("LEWAT SET ADDRESS")
        println("INI LONGITUDE ${address.longitude}")
        println("INI LATITUDE ${address.latitude}")
        longitude = address.longitude
        latitude = address.latitude
        if (address.getAddressLine(0)!=null){
            addressTv.setText(address.getAddressLine(0))
        }
        if (address.getAddressLine(1)!= null){
            addressTv.setText(addressTv.text.toString() + address.getAddressLine(1))
        }
    }

    override fun onCameraMove() {

    }

    override fun onCameraMoveStarted(p0: Int) {

    }

    override fun onCameraIdle() {
        var addresses : List<Address>? = null
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(mMap?.cameraPosition!!.target.latitude,
                mMap?.cameraPosition?.target!!.longitude,1)
            setAddress(addresses!![0])
        } catch (e:IndexOutOfBoundsException){
            e.printStackTrace()
        }catch (e:IOException){
            e.printStackTrace()
        }
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (!enabled!!) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
    private fun showLoad(state : Boolean){
        if (state){
            binding.laoding.visibility = View.VISIBLE
            binding.addressNote.isEnabled = false
            binding.buttonPick.isEnabled = false
        }else{
            binding.laoding.visibility = View.GONE
            binding.addressNote.isEnabled = true
            binding.buttonPick.isEnabled = true
        }
    }

    override fun onClick(v: View?) {
       when(v) {
           binding.buttonPick -> {
               showLoad(true)
               tokenOuth = Token(this)

               val id = dataLogin?.getString(
                   getString(com.projectassyifa.jawaraapps.R.string.id),
                   getString(com.projectassyifa.jawaraapps.R.string.default_value)
               )
            val dataPick = PickModelInsert(
                id_user = id.toString(),
                id_agent = idAgent.toString(),
                alamat = binding.addressPick.text.toString(),
                note = binding.addressNote.text.toString(),
                longitude = longitude!!,
                latitude = latitude!!
            )
               pickupRepo.resApi?.observe(this, androidx.lifecycle.Observer {
                   if (it.status){
                       startActivity(Intent(this,HomeActivity::class.java))
                   }
               })
               pickupRepo.insertPick("Bearer ${tokenOuth.fetchAuthToken()}",dataPick,this)
           }
       }
    }


}