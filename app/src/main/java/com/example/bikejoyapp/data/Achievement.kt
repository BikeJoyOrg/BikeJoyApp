package com.example.bikejoyapp.data

data class Level(
    val level: Int,
    val description: String,
    val valueRequired: Int,
    val coinReward: Int,
    val xpReward: Int,
    val petReward: String? = null,
    var isAchieved: Boolean = false,
    var isRedeemed: Boolean = false
)

data class Achievement (
    val name: String,
    var currentValue: Int,
    val levels: Array<Level>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Achievement

        if (name != other.name) return false
        if (currentValue != other.currentValue) return false
        if (!levels.contentEquals(other.levels)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + currentValue
        result = 31 * result + levels.contentHashCode()
        return result
    }
}