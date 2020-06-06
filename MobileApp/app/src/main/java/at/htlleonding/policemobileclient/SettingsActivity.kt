package at.htlleonding.policemobileclient

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    companion object{
        private val LOG_TAG = this::class.java.toString()
    }
    private var district = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val spinner: Spinner = findViewById(R.id.spinner_settings_district)
        ArrayAdapter.createFromResource(
            this,
            R.array.districtNames,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
        bt_settings_save.setOnClickListener {
            onSave()
        }
        bt_settings_cancel.setOnClickListener {
            onCancel()
        }
        loadPreferencesIntoUI()
    }

    private fun onCancel() {
        finish()
    }

    private fun loadPreferencesIntoUI() {
        val preferences = this.applicationContext
            .getSharedPreferences(MainActivity.PREFERENCE_FILENAME, Context.MODE_PRIVATE)
        district = preferences.getInt(MainActivity.DISTRICT_KEY, -1)
        val name = preferences.getString(MainActivity.NAME_KEY, "Auto1")
        Log.d(LOG_TAG, "loaded district $district from preferences")
        if(district != -1)spinner_settings_district.setSelection(district)
        et_settings_name.setText(name)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        district = pos
        Log.d(LOG_TAG, "selected district: " + district)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        district = -1
    }

    private fun onSave(){
        var name = if(et_settings_name.text.toString() != ""){
            et_settings_name.text.toString()
        }
        else "Auto1"
        val preferences = this.applicationContext
            .getSharedPreferences(MainActivity.PREFERENCE_FILENAME, Context.MODE_PRIVATE)
            .edit()
        preferences.putInt(MainActivity.DISTRICT_KEY, district)
        preferences.putString(MainActivity.NAME_KEY, name)
        preferences.apply()
        MainActivity.district = district
        MainActivity.name = name
        Log.d(LOG_TAG, "saved preferences: $district   $name")
        finish()
    }
}
