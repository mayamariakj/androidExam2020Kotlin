package com.example.noforeignland

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        GlobalScope.launch { getDataAndCreateObject()}
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getDataAndCreateObject(){
        val place: Place? =getPlaceInformationFromAPI()

        runOnUiThread {
            if (place != null) {
                placeNameText.text = place.name
                latLonText.text = getString(R.string.latLon, place.lat, place.lon)
                if (Html.fromHtml(place.comments).toString() != "") {
                    placeInfoText.text = Html.fromHtml(place.comments)
                }
                placeInfoText.movementMethod = (ScrollingMovementMethod())
                createMapButton(place)
                getAndDisplayImage(place)
            } else {
                placeInfoText.text = getString(R.string.no_internet)
            }
            progressBar.visibility = GONE
        }
    }

    private fun getPlaceInformationFromAPI(): Place? {
        var place: Place? = null
        val featureId = intent.getLongExtra("featureId", 0).toString()
        val url:String = "https://www.noforeignland.com/home/api/v1/place?id=" + featureId.toString()

        val httpGet = url.httpGet().responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                    }
                    is Result.Success -> {

                        val jsonData = result.get()
                        val gson = Gson()

                        try{
                            place = gson.fromJson(jsonData, PlaceInfo::class.java).place

                        }
                        catch (ex: Exception){
                            ex.printStackTrace()
                            throw  ex
                        }
                    }
                }
            }
        httpGet.join()
        return place
    }

    private fun createMapButton(place: Place){
        mapImageButton.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("featureName", place.name)
            intent.putExtra("featureLat", place.lat)
            intent.putExtra("featureLon", place.lon)

            this.startActivity(intent)
        }
    }

    private fun getAndDisplayImage(place: Place) {

        if (!place.banner.equals("")) {
            Picasso.get().load(place.banner).fit().centerInside().into(placeImageView)
            placeImageView.clipToOutline = true
        }
        else if (!place.images.isEmpty()){
            Picasso.get().load(place.images.getOrNull(0)?.servingUrl).fit().centerInside().into(placeImageView)
            placeImageView.clipToOutline = true
        }
        else {
            placeImageView.visibility = GONE
        }
    }
}


