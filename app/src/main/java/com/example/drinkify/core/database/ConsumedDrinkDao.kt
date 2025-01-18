package com.example.drinkify.core.database

import androidx.room.*
import com.example.drinkify.core.models.ConsumedDrink
import com.example.drinkify.core.models.ConsumedDrinkAdvanced
import com.example.drinkify.core.models.Drink
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumedDrinkDao {
    @Upsert
    suspend fun upsertConsumedDrink(consumedDrink: ConsumedDrink)

    @Delete
    suspend fun deleteConsumedDrink(consumedDrink: ConsumedDrink)

    // consumed drink count for user
    @Query("SELECT COUNT(*) FROM consumed_drink_table WHERE userId = :userId")
    fun getConsumedDrinkCount(userId: Int): Flow<Int>

    // all consumed drinks with drink information for user
    @Query("""
        SELECT *
        FROM consumed_drink_table
        WHERE userId = :userId
        ORDER BY timestamp DESC
    """)
    @Transaction
    fun getConsumedDrinksAdvanced(userId: Int): Flow<List<ConsumedDrinkAdvanced>>

    // total consumed alcohol for user
    @Query("""
        SELECT COALESCE(SUM(drink.alcoholPercentage), 0.0)
        FROM consumed_drink_table
        JOIN drink_table AS drink ON consumed_drink_table.drinkId = drink.id
        WHERE consumed_drink_table.userId = :userId
    """)
    fun getTotalConsumedAlcohol(userId: Int): Flow<Double>

    // most consumed drink for user
    @Query("""
        SELECT drink.*, COUNT(cd.id) AS drinkCount
        FROM consumed_drink_table AS cd
        JOIN drink_table AS drink ON cd.drinkId = drink.id
        WHERE cd.userId = :userId
        GROUP BY cd.drinkId
        ORDER BY drinkCount DESC
        LIMIT 1
    """)
    @Transaction
    fun getMostConsumedDrink(userId: Int): Flow<Drink?>
}
