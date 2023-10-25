package com.example.cicddemo

import android.content.SharedPreferences
import java.util.Calendar

/**
 * Helper class to manage access to [SharedPreferences].
 */
class SharedPreferencesHelper
/**
 * Constructor with dependency injection.
 *
 * @param sharedPreferences The [SharedPreferences] that will be used in this DAO.
 */( // The injected SharedPreferences implementation to use for persistence.
    private val mSharedPreferences: SharedPreferences
) {
    /**
     * Saves the given [SharedPreferenceEntry] that contains the user's settings to
     * [SharedPreferences].
     *
     * @param sharedPreferenceEntry contains data to save to [SharedPreferences].
     * @return `true` if writing to [SharedPreferences] succeeded. `false`
     * otherwise.
     */
    fun savePersonalInfo(sharedPreferenceEntry: SharedPreferenceEntry): Boolean {
        // Start a SharedPreferences transaction.
        val editor = mSharedPreferences.edit()
        editor.putString(KEY_NAME, sharedPreferenceEntry.name)
        editor.putLong(KEY_DOB, sharedPreferenceEntry.dateOfBirth.timeInMillis)
        editor.putString(KEY_EMAIL, sharedPreferenceEntry.email)
        // Commit changes to SharedPreferences.
        return editor.commit()
    }

    val personalInfo: SharedPreferenceEntry
        /**
         * Retrieves the [SharedPreferenceEntry] containing the user's personal information from
         * [SharedPreferences].
         *
         * @return the Retrieved [SharedPreferenceEntry].
         */
        get() {
            // Get data from the SharedPreferences.
            val name = mSharedPreferences.getString(KEY_NAME, "")
            val dobMillis = mSharedPreferences.getLong(KEY_DOB, Calendar.getInstance().timeInMillis)
            val dateOfBirth = Calendar.getInstance()
            dateOfBirth.timeInMillis = dobMillis
            val email = mSharedPreferences.getString(KEY_EMAIL, "")
            // Create and fill a SharedPreferenceEntry model object.
            return SharedPreferenceEntry(name!!, dateOfBirth, email!!)
        }

    companion object {
        // Keys for saving values in SharedPreferences.
        const val KEY_NAME = "key_name"
        const val KEY_DOB = "key_dob_millis"
        const val KEY_EMAIL = "key_email"
    }
}