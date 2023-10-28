package com.example.cicddemo

import android.content.SharedPreferences
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.Calendar

/**
 * Unit tests for the [SharedPreferencesHelper] that mocks [SharedPreferences].
 */
@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesHelperTest {
    private var mSharedPreferenceEntry: SharedPreferenceEntry? = null
    private var mMockSharedPreferencesHelper: SharedPreferencesHelper? = null
    private var mMockBrokenSharedPreferencesHelper: SharedPreferencesHelper? = null

    @Mock
    var mMockSharedPreferences: SharedPreferences? = null

    @Mock
    var mMockBrokenSharedPreferences: SharedPreferences? = null

    @Mock
    var mMockEditor: SharedPreferences.Editor? = null

    @Mock
    var mMockBrokenEditor: SharedPreferences.Editor? = null
    @Before
    fun initMocks() {
        // Create SharedPreferenceEntry to persist.
        mSharedPreferenceEntry = SharedPreferenceEntry(
            TEST_NAME, TEST_DATE_OF_BIRTH,
            TEST_EMAIL
        )
        // Create a mocked SharedPreferences.
        mMockSharedPreferencesHelper = createMockSharedPreference()
        // Create a mocked SharedPreferences that fails at saving data.
        mMockBrokenSharedPreferencesHelper = createBrokenMockSharedPreference()
    }

    @Test
    fun sharedPreferencesHelper_SaveAndReadPersonalInformation() {
        // Save the personal information to SharedPreferences
        val success = mMockSharedPreferencesHelper!!.savePersonalInfo(mSharedPreferenceEntry!!)
        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.save... returns true",
            success, CoreMatchers.`is`(true)
        )
        // Read personal information from SharedPreferences
        val savedSharedPreferenceEntry = mMockSharedPreferencesHelper!!.personalInfo
        // Make sure both written and retrieved personal information are equal.
        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.name has been persisted and read correctly",
            mSharedPreferenceEntry!!.name,
            CoreMatchers.`is`(CoreMatchers.equalTo(savedSharedPreferenceEntry.name))
        )
        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.dateOfBirth has been persisted and read "
                    + "correctly",
            mSharedPreferenceEntry!!.dateOfBirth,
            CoreMatchers.`is`(CoreMatchers.equalTo(savedSharedPreferenceEntry.dateOfBirth))
        )
        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.email has been persisted and read "
                    + "correctly",
            mSharedPreferenceEntry!!.email,
            CoreMatchers.`is`(CoreMatchers.equalTo(savedSharedPreferenceEntry.email))
        )
    }

    @Test
    fun sharedPreferencesHelper_SavePersonalInformationFailed_ReturnsFalse() {
        // Read personal information from a broken SharedPreferencesHelper
        val success =
            mMockBrokenSharedPreferencesHelper!!.savePersonalInfo(mSharedPreferenceEntry!!)
        MatcherAssert.assertThat(
            "Makes sure writing to a broken SharedPreferencesHelper returns false", success,
            CoreMatchers.`is`(false)
        )
    }

    /**
     * Creates a mocked SharedPreferences.
     */
    private fun createMockSharedPreference(): SharedPreferencesHelper? {
        mMockSharedPreferences?.let{
            // Mocking reading the SharedPreferences as if mMockSharedPreferences was previously written
            // correctly.
            `when`(it.getString(eq(SharedPreferencesHelper.KEY_NAME), anyString()))
                .thenReturn(mSharedPreferenceEntry!!.name)
            `when`(it.getString(eq(SharedPreferencesHelper.KEY_EMAIL), anyString()))
                .thenReturn(mSharedPreferenceEntry!!.email)
            `when`(it.getLong(eq(SharedPreferencesHelper.KEY_DOB), anyLong()))
                .thenReturn(mSharedPreferenceEntry!!.dateOfBirth.timeInMillis)
            // Mocking a successful commit.
            `when`(mMockEditor?.commit()).thenReturn(true)
            // Return the MockEditor when requesting it.
            `when`(mMockSharedPreferences?.edit()).thenReturn(mMockEditor)
            return SharedPreferencesHelper(it)
        }
        return null
    }

    /**
     * Creates a mocked SharedPreferences that fails when writing.
     */
    private fun createBrokenMockSharedPreference(): SharedPreferencesHelper? {
        // Mocking a commit that fails.
        `when`(mMockBrokenEditor?.commit()).thenReturn(false)
        // Return the broken MockEditor when requesting it.
        `when`(mMockBrokenSharedPreferences?.edit()).thenReturn(mMockBrokenEditor)
        return mMockBrokenSharedPreferences?.let { SharedPreferencesHelper(it) }
    }

    companion object {
        private const val TEST_NAME = "Test name"
        private const val TEST_EMAIL = "test@email.com"
        private val TEST_DATE_OF_BIRTH = Calendar.getInstance()

        init {
            TEST_DATE_OF_BIRTH[1980, 1] = 1
        }
    }
}