package com.example.data

import com.example.model.TrackInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

object TrackDictionary {
    const val GAME_TEXTURES_DIR = "/sdcard/PSP/TEXTURES/UCUS98632/"
    const val BASE_PLUGINS_DIR = "/sdcard/PSP/PLUGINS/ND/"
    const val SOUND_READY_PATH = "/sdcard/PSP/PLUGINS/ND/Sound/Ww.mp3"
    const val SOUND_DONE_PATH = "/sdcard/PSP/PLUGINS/ND/Sound/Dd.mp3"

    const val TEXTURES_INI_CONTENT = """[options]
version = 1
hash = quick

[hashes]
"""

    val INITIAL_NORMAL_PACKS = listOf(
        "День1", "День2", "Полдень1", "Полдень2",
        "ОколоВечер1", "Вечер1", "Сумерки1", "Сумерки2",
        "Сумерки3", "ГлубокиеСумерки4", "ГлубокиеСумерки5", "Ночь1",
        "ГлубокаяНочь1", "ТемнаяНочь1", "ТемнаяНочь2", "ГлухаяНочь1",
        "Рассвет0", "Рассвет1", "Рассвет2", "РанееУтро1",
        "РанееУтро2", "РанееУтро3", "ОколоУтро1", "ПолУтро1"
    )

    private val INITIAL_RAIN_PACKS = listOf(
        "Гало1", "ПолГало1", "БолееСерое1", "СреднеСерое1",
        "Ливень1", "Ливень2", "Уход1", "ПредУход1",
        "Чисто1", "Чисто2"
    )

    private val INITIAL_DAY_PACKS = setOf("День1", "День2", "Полдень1", "Полдень2")

    private val _normalPacksFlow = MutableStateFlow(INITIAL_NORMAL_PACKS)
    val normalPacksFlow: StateFlow<List<String>> = _normalPacksFlow.asStateFlow()

    private val _rainPacksFlow = MutableStateFlow(INITIAL_RAIN_PACKS)
    val rainPacksFlow: StateFlow<List<String>> = _rainPacksFlow.asStateFlow()

    private val _dayPacksFlow = MutableStateFlow(INITIAL_DAY_PACKS)
    val dayPacksFlow: StateFlow<Set<String>> = _dayPacksFlow.asStateFlow()

    var resumePackAfterRain: String = "ГлубокиеСумерки5"
        private set

    val NORMAL_PACKS: List<String>
        get() = _normalPacksFlow.value

    val RAIN_PACKS: List<String>
        get() = _rainPacksFlow.value

    val DAY_PACKS_FOR_RAIN_CHECK: Set<String>
        get() = _dayPacksFlow.value

    val RESUME_PACK_AFTER_RAIN: String
        get() = resumePackAfterRain

    private val INITIAL_TRACKS = linkedMapOf(
        "SARTE" to TrackInfo(
            key = "SARTE",
            name = "Сарте / Le Mans Old",
            normalPath = "/sdcard/PSP/PLUGINS/ND/SARTE/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/SRW/",
            location = "Circuit de la Sarthe, France"
        ),
        "SARTE2" to TrackInfo(
            key = "SARTE2",
            name = "Сарте 2005 / Le Mans",
            normalPath = "/sdcard/PSP/PLUGINS/ND/SARTE2/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/SRW2/",
            location = "Circuit de la Sarthe 2005"
        ),
        "NURB" to TrackInfo(
            key = "NURB",
            name = "Нюрбургринг / Nürburgring",
            normalPath = "/sdcard/PSP/PLUGINS/ND/NURBURGRING/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/NRW/",
            location = "Nordschleife, Germany"
        ),
        "FUJI" to TrackInfo(
            key = "FUJI",
            name = "Фудзи / Fuji 2005",
            normalPath = "/sdcard/PSP/PLUGINS/ND/FUJI/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/FRW/",
            location = "Fuji Speedway, Japan"
        ),
        "LAGUNA" to TrackInfo(
            key = "LAGUNA",
            name = "Лагуна Сека / Laguna Seca",
            normalPath = "/sdcard/PSP/PLUGINS/ND/LAGUNA/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/LRW/",
            location = "California, USA"
        ),
        "SUZUKA" to TrackInfo(
            key = "SUZUKA",
            name = "Сузука / Suzuka",
            normalPath = "/sdcard/PSP/PLUGINS/ND/SUZUKA/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/SURW/",
            location = "Mie Prefecture, Japan"
        ),
        "BEGINNER" to TrackInfo(
            key = "BEGINNER",
            name = "Бигинер / Beginner",
            normalPath = "/sdcard/PSP/PLUGINS/ND/BEGINNER/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/BRW/",
            location = "Training Course"
        ),
        "COTA" to TrackInfo(
            key = "COTA",
            name = "Кота / COTA",
            normalPath = "/sdcard/PSP/PLUGINS/ND/COTA/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/CRW/",
            location = "Circuit of the Americas, USA"
        ),
        "PARIS" to TrackInfo(
            key = "PARIS",
            name = "Париж / Paris",
            normalPath = "/sdcard/PSP/PLUGINS/ND/PARIS/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/PRW/",
            location = "Paris Street Circuit, France"
        ),
        "VALENCIA" to TrackInfo(
            key = "VALENCIA",
            name = "Валенсия / Valencia",
            normalPath = "/sdcard/PSP/PLUGINS/ND/VALENCIA/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/VRW/",
            location = "Valencia Street Circuit, Spain"
        ),
        "HIGHSPEED" to TrackInfo(
            key = "HIGHSPEED",
            name = "ХайСпид / HighSpeed Ring",
            normalPath = "/sdcard/PSP/PLUGINS/ND/HIGHSPEED/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/HRW/",
            location = "High Speed Ring"
        ),
        "MIDFIELD" to TrackInfo(
            key = "MIDFIELD",
            name = "МидФилд / Midfield Raceway",
            normalPath = "/sdcard/PSP/PLUGINS/ND/MIDFIELD/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/MRW/",
            location = "Midfield Raceway"
        ),
        "AUTUMNRING" to TrackInfo(
            key = "AUTUMNRING",
            name = "Осеннее кольцо / Autumn Ring",
            normalPath = "/sdcard/PSP/PLUGINS/ND/AUTUMNRING/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/ARW/",
            location = "Autumn Ring Grand Prix"
        ),
        "AUTUMNMINI" to TrackInfo(
            key = "AUTUMNMINI",
            name = "Аутуммини / Autumn Ring Mini",
            normalPath = "/sdcard/PSP/PLUGINS/ND/AUTUMNMINI/",
            rainPath = "/sdcard/PSP/PLUGINS/ND/AMRW/",
            location = "Autumn Ring Mini"
        )
    )

    private val _tracksFlow = MutableStateFlow<Map<String, TrackInfo>>(INITIAL_TRACKS)
    val tracksFlow: StateFlow<Map<String, TrackInfo>> = _tracksFlow.asStateFlow()

    val TRACKS: Map<String, TrackInfo>
        get() = _tracksFlow.value

    fun getTrack(key: String): TrackInfo {
        val map = _tracksFlow.value
        return map[key] 
            ?: map.entries.firstOrNull { it.key.equals(key, ignoreCase = true) }?.value
            ?: map.values.firstOrNull()
            ?: TrackInfo(
                key = key,
                name = key,
                normalPath = "$BASE_PLUGINS_DIR$key/",
                rainPath = "${BASE_PLUGINS_DIR}${key}RW/",
                location = "Custom Track"
            )
    }

    fun getNextTrackKey(currentKey: String): String {
        val keys = _tracksFlow.value.keys.toList()
        if (keys.isEmpty()) return currentKey
        val index = keys.indexOfFirst { it.equals(currentKey, ignoreCase = true) }
        if (index == -1 || index >= keys.size - 1) {
            return keys.first()
        }
        return keys[index + 1]
    }

    /**
     * Dynamically updates the in-memory tracks dictionary when a directory is renamed at runtime.
     * Returns the new track key if a track key was modified, or null if no track key was changed.
     */
    fun notifyDirectoryRenamed(parentPath: String, oldName: String, newName: String): String? {
        val currentMap = _tracksFlow.value
        val newMap = LinkedHashMap<String, TrackInfo>()
        var updatedKey: String? = null

        val cleanOld = oldName.trim().trimEnd('/')
        val cleanNew = newName.trim().trimEnd('/')
        val normParent = if (parentPath.endsWith("/")) parentPath else "$parentPath/"

        currentMap.forEach { (key, info) ->
            val oldNormalFolder = File(info.normalPath.trimEnd('/')).name
            val oldRainFolder = File(info.rainPath.trimEnd('/')).name

            val matchesKey = key.equals(cleanOld, ignoreCase = true)
            val matchesNormalFolder = oldNormalFolder.equals(cleanOld, ignoreCase = true)
            val matchesRainFolder = oldRainFolder.equals(cleanOld, ignoreCase = true)

            if (matchesKey || matchesNormalFolder || matchesRainFolder) {
                val newKey = if (matchesKey || matchesNormalFolder) cleanNew else key
                if (matchesKey || matchesNormalFolder) {
                    updatedKey = newKey
                }

                val newNormalPath = if (matchesNormalFolder || matchesKey) {
                    "${normParent}$cleanNew/"
                } else {
                    info.normalPath
                }

                val newRainPath = if (matchesRainFolder) {
                    "${normParent}$cleanNew/"
                } else {
                    info.rainPath
                }

                // Dynamically update the display name if it contained the old name/key
                val updatedDisplayName = if (info.name.contains(cleanOld, ignoreCase = true)) {
                    info.name.replace(Regex(Regex.escape(cleanOld), RegexOption.IGNORE_CASE), cleanNew)
                } else if (matchesKey) {
                    cleanNew
                } else {
                    info.name
                }

                val updatedInfo = info.copy(
                    key = newKey,
                    name = updatedDisplayName,
                    normalPath = newNormalPath,
                    rainPath = newRainPath
                )
                newMap[newKey] = updatedInfo
            } else {
                newMap[key] = info
            }
        }

        _tracksFlow.value = newMap

        // Dynamically update Normal / Rain / Day packs if a cycle pack folder was renamed
        val currentNormal = _normalPacksFlow.value
        if (currentNormal.any { it.equals(cleanOld, ignoreCase = true) }) {
            _normalPacksFlow.value = currentNormal.map {
                if (it.equals(cleanOld, ignoreCase = true)) cleanNew else it
            }
        }

        val currentRain = _rainPacksFlow.value
        if (currentRain.any { it.equals(cleanOld, ignoreCase = true) }) {
            _rainPacksFlow.value = currentRain.map {
                if (it.equals(cleanOld, ignoreCase = true)) cleanNew else it
            }
        }

        val currentDay = _dayPacksFlow.value
        if (currentDay.any { it.equals(cleanOld, ignoreCase = true) }) {
            _dayPacksFlow.value = currentDay.map {
                if (it.equals(cleanOld, ignoreCase = true)) cleanNew else it
            }.toSet()
        }

        if (resumePackAfterRain.equals(cleanOld, ignoreCase = true)) {
            resumePackAfterRain = cleanNew
        }

        return updatedKey
    }
}
