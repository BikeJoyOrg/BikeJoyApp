import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bikejoyapp.data.Achievement
import com.example.bikejoyapp.data.Level

class AchievementViewModel : ViewModel() {
    private val _achievements = MutableLiveData<Map<String, Achievement>>()
    val achievements: LiveData<Map<String, Achievement>> = _achievements

    init {
        getAchievementsData()
    }

    fun getAchievementsData() {
        _achievements.value = mapOf(
            "Aventurero" to Achievement(
                name = "Aventurero",
                currentValue = 15,
                levels = arrayOf(
                    Level(1, "Viaja un total de 20 km", 20, 50, isAchieved = false, isRedeemed = false),
                    Level(2, "Viaja un total de 60 km", 60, 200, isAchieved = false, isRedeemed = false),
                    Level(3, "Viaja un total de 150 km", 150, 200, isAchieved = false, isRedeemed = false)
                )
            ),
            "Creador" to Achievement(
                name = "Creador",
                currentValue = 10,
                levels = arrayOf(
                    Level(1, "Crea un total de 10 rutas", 10, 200, isAchieved = true, isRedeemed = false),
                    Level(2, "Crea un total de 25 rutas", 25, 200, isAchieved = false, isRedeemed = false),
                    Level(3, "Crea un total de 50 rutas", 50, 200, isAchieved = false, isRedeemed = false)
                )
            ),
            "Explorador" to Achievement(
                name = "Explorador",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Explora un total del 15% del mapa", 15, 200, isAchieved = true, isRedeemed = true),
                    Level(2, "Explora un total del 50% del mapa", 50, 200, isAchieved = true, isRedeemed = true),
                    Level(3, "Explora un total del 100% del mapa", 100, 200, isAchieved = false, isRedeemed = false)
                )
            ),
            "Entusiasta" to Achievement(
                name = "Entusiasta",
                currentValue = 50,
                levels = arrayOf(
                    Level(1, "Completa un total de 10 rutas", 10, 200, isAchieved = true, isRedeemed = true),
                    Level(2, "Completa un total de 25 rutas", 25, 200, isAchieved = true, isRedeemed = true),
                    Level(3, "Completa un total de 50 rutas", 50, 200, isAchieved = true, isRedeemed = true)
                )
            ),
            "Apasionado" to Achievement(
                name = "Apasionado",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Visita un total de 10 estaciones", 10, 200, isAchieved = false, isRedeemed = false),
                    Level(2, "Visita un total de 25 estaciones", 25, 200, isAchieved = false, isRedeemed = false),
                    Level(3, "Visita un total de 50 estaciones", 50, 200, isAchieved = false, isRedeemed = false)
                )
            ),
            "Sociable" to Achievement(
                name = "Sociable",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Haz 10 amigos", 10, 200, isAchieved = false, isRedeemed = false),
                    Level(2, "Haz 25 amigos", 25, 200, isAchieved = false, isRedeemed = false),
                    Level(3, "Haz 50 amigos", 50, 200, isAchieved = false, isRedeemed = false)
                )
            ),
            "Crítico" to Achievement(
                name = "Crítico",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Comenta en 5 rutas que hayas completado", 5, 200, isAchieved = false, isRedeemed = false),
                    Level(2, "Comenta en 15 rutas que hayas completado", 15, 200, isAchieved = false, isRedeemed = false),
                    Level(3, "Comenta en 30 rutas que hayas completado", 30, 200, isAchieved = false, isRedeemed = false)
                )
            ),
            "Navegante" to Achievement(
                name = "Navegante",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Completa una navegación 20 veces", 20, 200, isAchieved = false, isRedeemed = false),
                    Level(2, "Completa una navegación 40 veces", 40, 200, isAchieved = false, isRedeemed = false),
                    Level(3, "Completa una navegación 70 veces", 70, 200, isAchieved = false, isRedeemed = false)
                )
            ),
            "Derrochador" to Achievement(
                name = "Derrochador",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Gasta 100 monedas en la tienda", 100, 200, isAchieved = false, isRedeemed = false),
                    Level(2, "Gasta 400 monedas en la tienda", 400, 200, isAchieved = false, isRedeemed = false),
                    Level(3, "Gasta 1000 monedas en la tienda", 1000, 200, isAchieved = false, isRedeemed = false)
                )
            ),
            "Criador" to Achievement(
                name = "Criador",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Sube 1 mascota a su nivel máximo", 1, 200, isAchieved = false, isRedeemed = false),
                    Level(2, "Sube 4 mascotas a su nivel máximo", 4, 200, isAchieved = false, isRedeemed = false),
                    Level(3, "Sube 9 mascotas a su nivel máximo", 9, 200, isAchieved = false, isRedeemed = false)
                )
            ),
            "Ecologista" to Achievement(
                name = "Ecologista",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Estalvia un total de 50 de c02", 50, 200, isAchieved = false, isRedeemed = false),
                    Level(2, "Estalvia un total de 100 de c02", 100, 200, isAchieved = false, isRedeemed = false),
                    Level(3, "Estalvia un total de 200 de c02", 200, 200, isAchieved = false, isRedeemed = false)
                )
            ),
        )
    }

    fun updateAchievement(achievementName: String, value: Int) {
        _achievements.value?.get(achievementName)?.let { achievement ->
            // Si ya ha alcanzado el nivel 3, no hace nada
            if (!achievement.levels[2].isAchieved) {
                achievement.currentValue = value
                achievement.levels.forEach { level ->
                    if (!level.isAchieved && value >= level.valueRequired) {
                        if (level.level == 3) {
                            // Si ya ha alcanzado el nivel 3, el valor se queda en el limite
                            achievement.currentValue = level.valueRequired
                        }
                        level.isAchieved = true
                    }
                }
                _achievements.value = _achievements.value // Trigger LiveData update
            }
        }
    }

    fun claimReward(achievementName: String, levelIndex: Int) {
        val currentAchievements = _achievements.value.orEmpty().toMutableMap()
        val achievement = currentAchievements[achievementName]
        achievement?.levels?.get(levelIndex)?.isRedeemed = true
        _achievements.value = currentAchievements
    }
}