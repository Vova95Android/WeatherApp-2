package com.example.patterntest.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.example.patterntest.MainActivity
import com.example.patterntest.R
import com.example.patterntest.Static
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.*
import kotlinx.android.synthetic.main.fragment_city.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_City : Fragment() {

    private val TAG = "1"
    private val cities = arrayOfNulls<String>(5)
    private val cities_id = arrayOfNulls<String>(100)
    private var button = false
    var placesClient: PlacesClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city, container, false)
        }

    override fun onStart() {
        super.onStart()
        imageButton.setOnClickListener { (activity as MainActivity).navController.navigate(R.id.action_Fragment_City_to_Fragment_Weather) }
        // Инициализация SDK
        // Инициализация SDK
        Places.initialize(
            (activity as MainActivity).getApplicationContext(),
            getString(R.string.api_key)
        )
        placesClient =
            Places.createClient(activity as MainActivity)
        val city_name: AutoCompleteTextView =
            autoCompleteTextView_city
        city_name.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                //Прлучение выбранного города
                val placeId = cities_id[position]!!
                val placeFields =
                    Arrays.asList(
                        Place.Field.ID,
                        Place.Field.NAME,
                        Place.Field.LAT_LNG
                    )
                val request = FetchPlaceRequest.newInstance(placeId, placeFields)
                placesClient!!.fetchPlace(request)
                    .addOnSuccessListener { response: FetchPlaceResponse ->
                        val place =
                            response.place
                        (activity as MainActivity).navController.navigate(R.id.action_Fragment_City_to_Fragment_Weather)
                        if (place.latLng != null) {
                            (activity as MainActivity).viewModel.liveDataCity.value=cities[position]
                            (activity as MainActivity).viewModel.liveDataLatLng.value=place.latLng.toString()

                            button = true
                        } else {
                            (activity as MainActivity).viewModel.liveDataLatLng.value="NULL"
                        }
                    }
                    .addOnFailureListener { exception: Exception ->
                        if (exception is ApiException) {
                            Log.e(
                                TAG,
                                "Place not found: " + exception.message
                            )
                            val statusCode = exception.statusCode
                            // TODO: Handle error with given status code.
                        }
                    }
            }
        city_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (s.toString().length > 0) {
                    // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
                    // and once again when the user makes a selection (for example when calling fetchPlace()).
                    val token = AutocompleteSessionToken.newInstance()

                    // Билдер для поиска совпадений городов
                    val request =
                        FindAutocompletePredictionsRequest.builder()
                            .setSessionToken(token)
                            .setQuery(s.toString())
                            .setTypeFilter(TypeFilter.CITIES)
                            .build()
                    placesClient!!.findAutocompletePredictions(request)
                        .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                            //Ответ о найденных городах
                            var ithem = 0
                            for (prediction in response.autocompletePredictions) {
                                cities[ithem] = prediction.getFullText(null).toString()
                                cities_id[ithem] = prediction.placeId
                                ithem++
                            }
                            val citiesList =
                                Arrays.asList(*cities)
                            val adapter = ArrayAdapter(
                                activity as MainActivity,
                                R.layout.support_simple_spinner_dropdown_item,
                                citiesList
                            )
                            city_name.setAdapter(adapter)
                        }
                        .addOnFailureListener { exception: Exception? ->
                            if (exception is ApiException) {
                                val apiException = exception
                                val cities =
                                    arrayOf("Не найдено")
                                val adapter = ArrayAdapter(
                                    activity as MainActivity,
                                    R.layout.support_simple_spinner_dropdown_item,
                                    cities
                                )
                                city_name.setAdapter(adapter)
                            }
                        }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }


}
