package cc.hiroga.mavenSearch

import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object Search {
    private val gsonBintray =
        GsonBuilder().serializeNulls().registerTypeAdapter(BintrayPackage::class.java, BintrayPackageDeserializer())
            .create()

    fun jCenter(keyword: String): List<BintrayPackage> {
        val res = Fuel.get("https://api.bintray.com/search/packages/maven?g=*${keyword}*").responseString().third.get()
        return gsonBintray.fromJson<List<BintrayPackage>>(res, List::class.java)
    }
}