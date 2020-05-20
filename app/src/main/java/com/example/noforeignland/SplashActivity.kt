package com.example.noforeignland

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        GlobalScope.launch { checkDatabase() }
    }

    private  fun checkDatabase() {
        val splashTimer = System.currentTimeMillis()

        val dbHandler = DatabaseHelper(this)
        if (!dbHandler.databaseExist()) {
            getPlacesFromAPI()
        }

        val sleepTime = 2000 - (System.currentTimeMillis() - splashTimer)
        if (sleepTime > 0){
            Thread.sleep(sleepTime)
        }

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getPlacesFromAPI(){
        var data: String = ""
        val httpGet =  "https://www.noforeignland.com/home/api/v1/places/".httpGet().responseString { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    throw ex
                }
                is Result.Success -> {
                    data = result.get()
                }
            }
        }
        httpGet.join()
        addPlacesToDatabase(data)
    }

    private fun addPlacesToDatabase(data: String){
        val gson = Gson()

        try {
            val featureCollection: FeatureCollection = gson.fromJson(data, FeatureCollection::class.java)
            
            val dbHandler = DatabaseHelper(this)
            dbHandler.addManyFeatures(featureCollection.features)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}






