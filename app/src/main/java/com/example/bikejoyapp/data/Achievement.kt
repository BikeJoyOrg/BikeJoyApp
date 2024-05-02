package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Level(
    val level: Int,
    val description: String,
    val valueRequired: Int,
    val coinReward: Int,
    val xpReward: Int,
    val petReward: String? = null
)

@Serializable
data class Achievement (
    val name: String,
    val levels: Array<Level>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Achievement

        if (name != other.name) return false
        if (!levels.contentEquals(other.levels)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + levels.contentHashCode()
        return result
    }
}

@Serializable
data class AchievementProgress(
    val achievement: String,
    val lastLevelAchieved: Int,
    val currentValue: Int,
    val isAchieved: Boolean,
    val isRedeemed: Boolean
)

@Serializable
data class AchievementResponse(
    val achievements: List<Achievement>
)


@Serializable
data class AchievementProgressResponse(
    val achievementsProgress: List<AchievementProgress>
)