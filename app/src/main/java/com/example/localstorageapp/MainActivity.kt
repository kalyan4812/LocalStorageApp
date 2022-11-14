package com.example.localstorageapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.dataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.localstorageapp.ProtoDataStore.PrefSettings
import com.example.localstorageapp.ProtoDataStore.PrefSettingsSerializer
import com.example.localstorageapp.ProtoDataStore.UserName
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

val Context.dataStore by dataStore("pref-settings.json", PrefSettingsSerializer)

class MainActivity : AppCompatActivity() {

    private lateinit var lng: EditText
    private lateinit var fname: EditText
    private lateinit var lname: EditText
    private lateinit var save: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_main
        )
        lng = findViewById(R.id.lng)
        fname = findViewById(R.id.fname)
        lname = findViewById(R.id.lname)
        save = findViewById(R.id.submit)


        save.setOnClickListener {
            saveData();
        }

        // read data
        val prefSettings: Flow<PrefSettings> = dataStore.data
        collectLifecycleAwareFlow(prefSettings) {
            lng.setText(it.lng.toString())
            if (it.list.size >= 1) {
                fname.setText(it.list.get(0).firstName)
                lname.setText(it.list.get(0).lastName)
            }
        }


    }

    fun <T> ComponentActivity.collectLifecycleAwareFlow(
        flow: Flow<T>,
        collect: suspend (T) -> Unit
    ) {
        lifecycleScope.launch {

            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(collect)
            }


        }
    }

    private fun saveData() {
        lifecycleScope.launch(Dispatchers.Main) {
            dataStore.updateData {
                it.copy(
                    lng = lng.text.toString(),
                    list = persistentListOf(
                        UserName(
                            fname.text.toString(),
                            lname.text.toString()
                        ),
                        UserName(lname.text.toString(), fname.text.toString())
                    )
                )

            }
        }
        Toast.makeText(applicationContext, "Saved Details....", Toast.LENGTH_SHORT).show()
    }
}