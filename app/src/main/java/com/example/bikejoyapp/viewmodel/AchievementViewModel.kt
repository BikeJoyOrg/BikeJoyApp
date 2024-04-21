import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bikejoyapp.data.Achievement
import com.example.bikejoyapp.data.Level

class AchievementViewModel: ViewModel() {
    private val _achievements = MutableLiveData(
        mapOf(
            "Aventurero" to Achievement(
                name = "Aventurero",
                actualValue = 0,
                levels = arrayOf(
                    Level(1,"Viaja un total de 20 km",20, false),
                    Level(2,"Viaja un total de 60 km",60, false),
                    Level(3,"Viaja un total de 150 km",150, false)
                )
            ),
            "Creador" to Achievement(
                name = "Creador",
                actualValue = 0,
                levels = arrayOf(
                    Level(1,"Crea un total de 10 rutas",10, false),
                    Level(2,"Crea un total de 25 rutas",25, false),
                    Level(3,"Crea un total de 50 rutas",50, false)
                )
            ),
            "Explorador" to Achievement(
                name = "Explorador",
                actualValue = 0,
                levels = arrayOf(
                    Level(1,"Explora un total del 15% del mapa",15, false),
                    Level(2,"Explora un total del 50% del mapa",50, false),
                    Level(3,"Explora un total del 100% del mapa",100, false)
                )
            ),
            "Entusiasta" to Achievement(
                name = "Entusiasta",
                actualValue = 0,
                levels = arrayOf(
                    Level(1,"Completa un total de 10 rutas",10, false),
                    Level(2,"Completa un total de 25 rutas",25, false),
                    Level(3,"Completa un total de 50 rutas",50, false)
                )
            ),
            "Apasionado" to Achievement(
                name = "Apasionado",
                actualValue = 0,
                levels = arrayOf(
                    Level(1,"Visita un total de 10 estaciones",10, false),
                    Level(2,"Visita un total de 25 estaciones",25, false),
                    Level(3,"Visita un total de 50 estaciones",50, false)
                )
            )
        )
    )
    val achievements: LiveData<Map<String, Achievement>> = _achievements

    fun updateAchievement(achievementName: String, value: Int) {
        _achievements.value?.get(achievementName)?.let { achievement ->
            // Si ya ha alcanzado el nivel 3, no hace nada
            if (!achievement.levels[2].isAchieved) {
                achievement.actualValue = value
                achievement.levels.forEach { level ->
                    if (!level.isAchieved && value >= level.valueRequired) {
                        if (level.level == 3) {
                            // Si ya ha alcanzado el nivel 3, el valor se queda en el limite
                            achievement.actualValue = level.valueRequired
                        }
                        level.isAchieved = true
                    }
                }
                _achievements.value = _achievements.value // Trigger LiveData update
            }
        }
    }
}