package com.codepath.bestsellerlistapp

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import java.io.Serializable

/**
 * The Model for storing a single book from the NY Times API
 *
 * SerializedName tags MUST match the JSON response for the
 * object to correctly parse with the gson library.
 */

class Movie(
    var title: String,
    var bookImageUrl: String,
    var description: String,
     known_for: String
) : Serializable {

    val listKnownFor = ArrayList<KnownFor>()

    init {

        val arr = JSONArray(known_for)
        for (i in 0 until arr.length()) {
            val tmpObj = arr.getJSONObject(i)
            listKnownFor.add(
                KnownFor(
                    tmpObj.getString("original_title"),
                    tmpObj.getString("overview")
                )
            )
        }

    }


    inner class KnownFor(var originalTitle: String, var overview: String) : Serializable
}


