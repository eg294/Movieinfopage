package com.codepath.bestsellerlistapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray

// --------------------------------//
// CHANGE THIS TO BE YOUR API KEY  //
// --------------------------------//
private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"

/*
 * The class for the only fragment in the app, which contains the progress bar,
 * recyclerView, and performs the network calls to the NY Times API.
 */
class MoviesFragment : Fragment(), OnListFragmentInteractionListener {

    /*
     * Constructing the view
     */

    lateinit var recyclerView : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        recyclerView = view.findViewById(R.id.list)
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        updateAdapter(progressBar)
        return view
    }

    /*
     * Updates the RecyclerView adapter with new data.  This is where the
     * networking magic happens!
     */
    private fun updateAdapter(progressBar: ContentLoadingProgressBar) {
        progressBar.show()

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY

        //https://api.themoviedb.org/3/movie/now_playing
        //https://api.themoviedb.org/3/person/popular
        client[
            "https://api.themoviedb.org/3/trending/person/day",
            params,
            object : JsonHttpResponseHandler()

            // Uncomment me once you complete the above sections!
            {
                /*
                 * The onSuccess function gets called when
                 * HTTP response status is "200 OK"
                 */
                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JsonHttpResponseHandler.JSON
                ) {
                    // The wait for a response is over
                    progressBar.hide()

                    val resultsJSON: String = json.jsonObject.get("results").toString()
//                val gson = Gson()
//                val arrayMovieType = object : TypeToken<List<Movie>>() {}.type
//                val models : List<Movie> = gson.fromJson(resultsJSON,arrayMovieType)
//                recyclerView.adapter = MoviesRecyclerViewAdapter(models, this@MoviesFragment)


                    setData(resultsJSON)

                    // Look for this in Logcat:
                    Log.d("BestSellerBooksFragment", "response successful")
                }

                /*
                 * The onFailure function gets called when
                 * HTTP response status is "4XX" (eg. 401, 403, 404)
                 */
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    // The wait for a response is over
                    progressBar.hide()

                    // If the error is not null, log it!
                    t?.message?.let {
                        Log.e("BestSellerBooksFragment", errorResponse)
                    }
                }
            }]


    }

    fun setData(results: String) {

        val resultArray = JSONArray(results)
        val models = ArrayList<Movie>()

        for (i in 0 until resultArray.length()) {
            val tmp = resultArray.getJSONObject(i)
            models.add(
                Movie(
                    tmp.getString("original_name"),
                    tmp.getString("profile_path"),
                    tmp.getString("media_type"),
                    tmp.getString("known_for")

                )
            )

        }
        recyclerView.adapter = MoviesRecyclerViewAdapter(models, this@MoviesFragment)


    }

    /*
     * What happens when a particular book is clicked.
     */
    override fun onItemClick(item: Movie) {
        Toast.makeText(context, "test: " + item.title, Toast.LENGTH_LONG).show()

        val i = Intent(activity, DetailActivity::class.java)
        i.putExtra(ARTICLE_EXTRA, item)
        startActivity(i)

    }

}
