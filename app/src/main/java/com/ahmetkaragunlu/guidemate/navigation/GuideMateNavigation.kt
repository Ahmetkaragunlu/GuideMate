import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahmetkaragunlu.guidemate.features.auth_graph.authNavGraph
import com.ahmetkaragunlu.guidemate.features.graph.Graph
import com.ahmetkaragunlu.guidemate.features.tourist_graph.TouristNavGraphScaffold

@Composable
fun GuideMateNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Graph.AuthGraph.route
    ) {
        authNavGraph(navController = navController)

        composable(route = Graph.TouristGraph.route) {
            TouristNavGraphScaffold()
        }
    }
}