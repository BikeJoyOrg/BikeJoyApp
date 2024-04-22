import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.bikejoyapp.data.Feature
import com.example.bikejoyapp.data.Geometry
import com.example.bikejoyapp.data.RouteResponse
import com.example.bikejoyapp.data.RutaUsuari
import com.example.bikejoyapp.viewmodel.ApiRetrofit
import com.example.bikejoyapp.viewmodel.GravarRutaViewModel
import com.google.android.gms.maps.model.LatLng
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response

@ExperimentalCoroutinesApi
class GravarRutaViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: GravarRutaViewModel

    @RelaxedMockK
    private lateinit var mockApiRetrofit: ApiRetrofit
    @Mock
    private lateinit var mockCall: Response<RouteResponse>

    private val testDispatcher = TestCoroutineDispatcher()
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = GravarRutaViewModel()
        Dispatchers.setMain(Dispatchers.Unconfined)

    }
    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onselected should set first coordinate when start is true`() = runTest {
        val coordinate = "0.0,0.0"
        viewModel.onselected(coordinate)
        assertEquals(viewModel.posstart.value,LatLng(0.0, 0.0))
    }
    /*
        @Test
        fun `totalDistance should calculate correct distance`() {
            val points = listOf(LatLng(0.0, 0.0), LatLng(1.0, 1.0))

            val result = viewModel.totalDistance(points)

            assertEquals(157.22543203807225, result, 0.001)
        }*/

    @Test
    fun `totalDistance should return zero for empty list`() {
        val points = emptyList<LatLng>()

        val result = viewModel.totalDistance(points)

        assertEquals(0.0, result, 0.001)
    }
}