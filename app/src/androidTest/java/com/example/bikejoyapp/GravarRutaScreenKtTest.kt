
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bikejoyapp.MainActivity
import com.example.bikejoyapp.ui.GravarRutaScreen
import com.example.bikejoyapp.viewmodel.GravarRutaViewModel
import com.example.bikejoyapp.viewmodel.MainViewModel
import org.hamcrest.Matchers.not
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.example.bikejoyapp.ui.FakeGravarRutaViewModel
import com.google.android.gms.maps.model.LatLng

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

private val REQUEST_LOCATION_PERMISSION = 123
@RunWith(AndroidJUnit4::class)
class GravarRutaScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var gravarviewModel: GravarRutaViewModel? = null
    private var mainviewModel: MainViewModel? = null

    @Before
    fun setUp() {

        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(
            "pm grant ${InstrumentationRegistry.getInstrumentation().targetContext.packageName} " +
                    "android.permission.ACCESS_FINE_LOCATION"
        )
        // Inicializa tus ViewModels aquí si es necesario
        gravarviewModel = GravarRutaViewModel()
        mainviewModel = MainViewModel()

        composeTestRule.setContent {
            // Establece el contenido de tu pantalla Compose
            GravarRutaScreen(gravarviewModel!!, mainviewModel!!)
        }
        requestLocationPermission()
    }
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                InstrumentationRegistry.getInstrumentation().targetContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                InstrumentationRegistry.getInstrumentation().targetContext as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }/*
    @Test
    fun test_isDialogDisplayedWhenGravarRutaButtonClicked() {

        composeTestRule.onNodeWithText("Gravar Ruta").performClick()
        composeTestRule.onNodeWithText("Introdueix nom de la ruta").assertExists()
    }*/
    @Test
    fun test_areButtonsDisabledAtStart() {
        composeTestRule.onNodeWithText("Refer Inici").assertIsNotEnabled()
        composeTestRule.onNodeWithText("Gravar Ruta").assertIsNotEnabled()
        composeTestRule.onNodeWithText("Desfer Punt").assertIsNotEnabled()
    }
}