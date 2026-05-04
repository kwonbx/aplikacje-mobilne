package com.example.szlaki

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TrailEntity::class, UserEntity::class, RecordEntity::class, FavoriteTrailEntity::class], version = 6)
abstract class AppDatabase: RoomDatabase() {
    abstract fun trailDao(): TrailDao
    abstract fun userDao(): UserDao
    abstract fun recordDao(): RecordDao
    abstract fun favoriteTrailDao(): FavoriteTrailDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "szlaki_database"
                ).addCallback(object: RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.trailDao()?.insertTrails(
                                listOf(
                                    TrailEntity(
                                        name = "Szlak na Rysy (od Morskiego Oka)",
                                        type = "hiking",
                                        surface = "rock",
                                        difficulty = "hard",
                                        color = "red",
                                        operator = "TPN"
                                    ),
                                    TrailEntity(
                                        name = "Dolina Kościeliska",
                                        type = "hiking",
                                        surface = "gravel",
                                        difficulty = "easy",
                                        color = "green",
                                        operator = "TPN"
                                    ),
                                    TrailEntity(
                                        name = "Szlak na Giewont z Kuźnic",
                                        type = "hiking",
                                        surface = "rock",
                                        difficulty = "hard",
                                        color = "blue",
                                        operator = "TPN"
                                    ),
                                    TrailEntity(
                                        name = "Tarnica z Wołosatego",
                                        type = "hiking",
                                        surface = "dirt",
                                        difficulty = "medium",
                                        color = "blue",
                                        operator = "BdPN"
                                    ),
                                    TrailEntity(
                                        name = "Połonina Wetlińska z Przełęczy Wyżnej",
                                        type = "hiking",
                                        surface = "dirt",
                                        difficulty = "medium",
                                        color = "yellow",
                                        operator = "BdPN"
                                    ),
                                    TrailEntity(
                                        name = "Śnieżka przez Kocioł Łomniczki",
                                        type = "hiking",
                                        surface = "rock",
                                        difficulty = "hard",
                                        color = "red",
                                        operator = "KPN"
                                    ),
                                    TrailEntity(
                                        name = "Babia Góra (Perć Akademików)",
                                        type = "hiking",
                                        surface = "rock",
                                        difficulty = "hard",
                                        color = "yellow",
                                        operator = "BgPN"
                                    ),
                                    TrailEntity(
                                        name = "Trzy Korony ze Sromowiec Niżnych",
                                        type = "hiking",
                                        surface = "dirt",
                                        difficulty = "medium",
                                        color = "yellow",
                                        operator = "PIPN"
                                    ),
                                    TrailEntity(
                                        name = "Szczeliniec Wielki",
                                        type = "hiking",
                                        surface = "rock",
                                        difficulty = "easy",
                                        color = "yellow",
                                        operator = "PNGS"
                                    ),
                                    TrailEntity(
                                        name = "Turbacz z Łopusznej",
                                        type = "hiking",
                                        surface = "dirt",
                                        difficulty = "medium",
                                        color = "blue",
                                        operator = "GPN"
                                    ),
                                    TrailEntity(
                                        name = "Velo Dunajec (Zakopane - Nowy Targ)",
                                        type = "bicycle",
                                        surface = "asphalt",
                                        difficulty = "medium",
                                        color = "blue",
                                        operator = "ZDW Kraków"
                                    ),
                                    TrailEntity(
                                        name = "Velo Czorsztyn (Wokół Jeziora)",
                                        type = "bicycle",
                                        surface = "asphalt",
                                        difficulty = "easy",
                                        color = "red",
                                        operator = "ZDW Kraków"
                                    ),
                                    TrailEntity(
                                        name = "Wiślana Trasa Rowerowa (Małopolska)",
                                        type = "bicycle",
                                        surface = "asphalt",
                                        difficulty = "easy",
                                        color = "blue",
                                        operator = "ZDW Kraków"
                                    ),
                                    TrailEntity(
                                        name = "Rowerowy Szlak Orlich Gniazd",
                                        type = "bicycle",
                                        surface = "mixed",
                                        difficulty = "medium",
                                        color = "red",
                                        operator = "Związek Gmin Jurajskich"
                                    ),
                                    TrailEntity(
                                        name = "Green Velo - Puszcza Białowieska",
                                        type = "bicycle",
                                        surface = "gravel",
                                        difficulty = "easy",
                                        color = "orange",
                                        operator = "ROT"
                                    ),
                                    TrailEntity(
                                        name = "EuroVelo 10 (R10) - Półwysep Helski",
                                        type = "bicycle",
                                        surface = "mixed",
                                        difficulty = "easy",
                                        color = "green",
                                        operator = "Pomorskie"
                                    ),
                                    TrailEntity(
                                        name = "Kaszubska Marszruta - Szlak Żółty",
                                        type = "bicycle",
                                        surface = "gravel",
                                        difficulty = "easy",
                                        color = "yellow",
                                        operator = "Powiat Chojnicki"
                                    ),
                                    TrailEntity(
                                        name = "Singletrack Glacensis - Pętla Ostoja",
                                        type = "bicycle",
                                        surface = "dirt",
                                        difficulty = "hard",
                                        color = "black",
                                        operator = "Fundacja SG"
                                    ),
                                    TrailEntity(
                                        name = "Enduro Trails - Twister",
                                        type = "bicycle",
                                        surface = "dirt",
                                        difficulty = "medium",
                                        color = "blue",
                                        operator = "Miasto Bielsko-Biała"
                                    ),
                                    TrailEntity(
                                        name = "Szlak Doliny Baryczy",
                                        type = "bicycle",
                                        surface = "gravel",
                                        difficulty = "easy",
                                        color = "orange",
                                        operator = "Dolny Śląsk"
                                    )
                                )
                            )
                        }
                    }
                }).fallbackToDestructiveMigration(false).build()
                INSTANCE = instance
                instance
            }
        }
    }
}