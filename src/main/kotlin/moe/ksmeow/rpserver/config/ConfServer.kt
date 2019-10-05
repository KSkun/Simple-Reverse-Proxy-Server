package moe.ksmeow.rpserver.config

import com.google.common.collect.HashMultimap

class ConfServer(_tokens: HashMultimap<String, ConfToken<*>> = HashMultimap.create(),
                 _locations: ArrayList<ConfLocation> = ArrayList()) : ConfSet("server", _tokens) {
    private var sorted = false
    val locations = _locations
    private val unsortedLocations: Array<ArrayList<ConfLocation>> =
        arrayOf(ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList())

    // hard-coded dirty implementation
    fun addLocation(location: ConfLocation) {
        when (location.option) {
            "=" -> unsortedLocations[0]
            "^~" -> unsortedLocations[1]
            "~", "~*" -> unsortedLocations[2]
            null -> unsortedLocations[3]
            "/" -> unsortedLocations[4]
            else -> throw ConfigInvalidException("Unknown location option ${location.option}.")
        }.add(location)
    }

    fun sortLocation() {
        if (sorted) return
        sorted = true
        for (arr in unsortedLocations) {
            for (loc in arr) {
                locations.add(loc)
            }
        }
    }
}