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
                                        operator = "TPN",
                                        imageUrl = "https://myownphotostory.pl/wp-content/uploads/2020/08/RYSY-1.jpg"
                                    ),
                                    TrailEntity(
                                        name = "Dolina Kościeliska",
                                        type = "hiking",
                                        surface = "gravel",
                                        difficulty = "easy",
                                        color = "green",
                                        operator = "TPN",
                                        imageUrl = "https://img.tatrytop.pl/site/tips/featured/koscieliska-valley-trail-162121-1280.jpeg"
                                    ),
                                    TrailEntity(
                                        name = "Szlak na Giewont z Kuźnic",
                                        type = "hiking",
                                        surface = "rock",
                                        difficulty = "hard",
                                        color = "blue",
                                        operator = "TPN",
                                        imageUrl = "https://tatromaniak.pl/wp-content/uploads/2021/01/MG_0071.jpg"
                                    ),
                                    TrailEntity(
                                        name = "Tarnica z Wołosatego",
                                        type = "hiking",
                                        surface = "dirt",
                                        difficulty = "medium",
                                        color = "blue",
                                        operator = "BdPN",
                                        imageUrl = "https://www.trasadlabobasa.pl/imagesm/17763.jpg"
                                    ),
                                    TrailEntity(
                                        name = "Połonina Wetlińska z Przełęczy Wyżnej",
                                        type = "hiking",
                                        surface = "dirt",
                                        difficulty = "medium",
                                        color = "yellow",
                                        operator = "BdPN",
                                        imageUrl = "https://mynaszlaku.pl/wp-content/uploads/2022/12/polonina-wetlinska-0026.jpg"
                                    ),
                                    TrailEntity(
                                        name = "Śnieżka przez Kocioł Łomniczki",
                                        type = "hiking",
                                        surface = "rock",
                                        difficulty = "hard",
                                        color = "red",
                                        operator = "KPN",
                                        imageUrl = "https://bliskocorazdalej.pl/wp-content/uploads/2020/10/DSC_0009-1170x777.jpg"
                                    ),
                                    TrailEntity(
                                        name = "Babia Góra (Perć Akademików)",
                                        type = "hiking",
                                        surface = "rock",
                                        difficulty = "hard",
                                        color = "yellow",
                                        operator = "BgPN",
                                        imageUrl = "https://hasajacezajace.com/wp-content/uploads/2018/12/PER%C4%86-AKADEMIK%C3%93W-3-1024x682.jpg"
                                    ),
                                    TrailEntity(
                                        name = "Trzy Korony ze Sromowiec Niżnych",
                                        type = "hiking",
                                        surface = "dirt",
                                        difficulty = "medium",
                                        color = "yellow",
                                        operator = "PIPN",
                                        imageUrl = "https://gorskiespacery.pl/wp-content/uploads/2023/12/P1420273_edited.jpg"
                                    ),
                                    TrailEntity(
                                        name = "Szczeliniec Wielki",
                                        type = "hiking",
                                        surface = "rock",
                                        difficulty = "easy",
                                        color = "yellow",
                                        operator = "PNGS",
                                        imageUrl = "https://8a.pl/8academy/wp-content/uploads/2025/05/Glowne-zdjecie-szczeliniec.webp"
                                    ),
                                    TrailEntity(
                                        name = "Turbacz z Łopusznej",
                                        type = "hiking",
                                        surface = "dirt",
                                        difficulty = "medium",
                                        color = "blue",
                                        operator = "GPN",
                                        imageUrl = "https://plannawypad.pl/wp-content/uploads/2024/04/turbacz-gorce-5.jpg"
                                    ),
                                    TrailEntity(
                                        name = "Velo Dunajec (Zakopane - Nowy Targ)",
                                        type = "bicycle",
                                        surface = "asphalt",
                                        difficulty = "medium",
                                        color = "blue",
                                        operator = "ZDW Kraków",
                                        imageUrl = "https://visitmalopolska.pl/documents/20194/1743722/Velo+Dunajec+Falsztyn+zjazd/30a84a1f-7714-48af-8629-aacc57a56aad?t=1575871864859&imageThumbnail=5"
                                    ),
                                    TrailEntity(
                                        name = "Velo Czorsztyn (Wokół Jeziora)",
                                        type = "bicycle",
                                        surface = "asphalt",
                                        difficulty = "easy",
                                        color = "red",
                                        operator = "ZDW Kraków",
                                        imageUrl = "https://www.pensjonat-niedzica.com.pl/files/velo-dunajec.jpg"
                                    ),
                                    TrailEntity(
                                        name = "Wiślana Trasa Rowerowa (Małopolska)",
                                        type = "bicycle",
                                        surface = "asphalt",
                                        difficulty = "easy",
                                        color = "blue",
                                        operator = "ZDW Kraków",
                                        imageUrl = "https://www.znajkraj.pl/files/styles/i/public/wislana-trasa-rowerowa-za-oswiecimiem-malopolska-2023-szymon-nitka-0437.jpg"
                                    ),
                                    TrailEntity(
                                        name = "Rowerowy Szlak Orlich Gniazd",
                                        type = "bicycle",
                                        surface = "mixed",
                                        difficulty = "medium",
                                        color = "red",
                                        operator = "Związek Gmin Jurajskich",
                                        imageUrl = "https://mambaonbike.pl/wp-content/uploads/2019/06/szlak-orlich-gniazd-9147.jpg"
                                    ),
                                    TrailEntity(
                                        name = "Green Velo - Puszcza Białowieska",
                                        type = "bicycle",
                                        surface = "gravel",
                                        difficulty = "easy",
                                        color = "orange",
                                        operator = "ROT",
                                        imageUrl = "https://greenvelo.pl/media/photos/6772/xl.jpg"
                                    ),
                                    TrailEntity(
                                        name = "EuroVelo 10 (R10) - Półwysep Helski",
                                        type = "bicycle",
                                        surface = "mixed",
                                        difficulty = "easy",
                                        color = "green",
                                        operator = "Pomorskie",
                                        imageUrl = "https://krokzahoryzont.pl/wp-content/uploads/2024/12/trasa-rowerowa-r10.webp"
                                    ),
                                    TrailEntity(
                                        name = "Kaszubska Marszruta - Szlak Żółty",
                                        type = "bicycle",
                                        surface = "gravel",
                                        difficulty = "easy",
                                        color = "yellow",
                                        operator = "Powiat Chojnicki",
                                        imageUrl = "https://polskanarowerze.pl/wp-content/uploads/2020/04/na-szlaku.jpg"
                                    ),
                                    TrailEntity(
                                        name = "Singletrack Glacensis - Pętla Ostoja",
                                        type = "bicycle",
                                        surface = "dirt",
                                        difficulty = "hard",
                                        color = "black",
                                        operator = "Fundacja SG",
                                        imageUrl = "https://www.alltrails.com/mugen/image/trail-app-router?url=https%3A%2F%2Fimages.alltrails.com%2FeyJidWNrZXQiOiJhc3NldHMuYWxsdHJhaWxzLmNvbSIsImtleSI6InVwbG9hZHMvcGhvdG8vaW1hZ2UvMTQ1NDk0NDAvNGZjNTllZGNkODcxYTEwMDk0YWRlNmVhYThmZGUzMTguanBnIiwiZWRpdHMiOnsidG9Gb3JtYXQiOiJ3ZWJwIiwicmVzaXplIjp7IndpZHRoIjoiMTA4MCIsImhlaWdodCI6IjcwMCIsImZpdCI6ImNvdmVyIn0sInJvdGF0ZSI6bnVsbCwianBlZyI6eyJ0cmVsbGlzUXVhbnRpc2F0aW9uIjp0cnVlLCJvdmVyc2hvb3REZXJpbmdpbmciOnRydWUsIm9wdGltaXNlU2NhbnMiOnRydWUsInF1YW50aXNhdGlvblRhYmxlIjozfX19&w=3840&q=75"
                                    ),
                                    TrailEntity(
                                        name = "Enduro Trails - Twister",
                                        type = "bicycle",
                                        surface = "dirt",
                                        difficulty = "medium",
                                        color = "blue",
                                        operator = "Miasto Bielsko-Biała",
                                        imageUrl = "https://www.slaskie.travel/Media/Default/.MainStorage/ContentItemDocumentTypeRecord/gonjffxw.mld/Enduro%20Trails%20BB2%20fot.%20Lucjusz%20Cykarski,%20Maciej%20Kopaniecki.jpg"
                                    ),
                                    TrailEntity(
                                        name = "Szlak Doliny Baryczy",
                                        type = "bicycle",
                                        surface = "gravel",
                                        difficulty = "easy",
                                        color = "orange",
                                        operator = "Dolny Śląsk",
                                        imageUrl = "https://www.znajkraj.pl/files/styles/i/public/z-lewej-barycz-z-prawej-zbiornik-ryczen-dolina-baryczy-2023-szymon-nitka-0215.jpg"
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