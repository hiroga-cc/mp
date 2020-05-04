package cc.hiroga.mp

import com.github.kittinunf.fuel.Fuel
import com.google.gson.GsonBuilder
import java.net.URL

object Search {
    private val gson =
        GsonBuilder().serializeNulls()
            .registerTypeAdapter(MavenCentralResponse::class.java, MavenCentralResponseDeserializer())
            .registerTypeAdapter(MavenCentralResponseBody::class.java, MavenCentralResponseBodyDeserializer())
            .registerTypeAdapter(MavenCentralPackage::class.java, MavenCentralPackageDeserializer())
            .registerTypeAdapter(BintrayPackage::class.java, BintrayPackageDeserializer())
            .create()

    fun search(keyword: String): List<Repository> {
        return listOf(mavenCentral(keyword),jCenter(keyword))
    }

    fun mavenCentral(keyword: String): Repository {
        // API Guide: https://search.maven.org/classic/#api
        val res =
            Fuel.get("https://search.maven.org/solrsearch/select", listOf(Pair("q","${keyword}"), Pair("rows",100))).responseString().third.get()
        return Repository(
            name = "Central",
            url = URL("https://repo1.maven.org/maven2/"),
            packages = gson.fromJson<MavenCentralResponse>(
                res,
                MavenCentralResponse::class.java
            ).response.docs.map { it.asPackage }
        )

    }

    fun jCenter(keyword: String): Repository {
        // API Guide: https://bintray.com/docs/api/#_maven_package_search
        val res1 = Fuel.get("https://api.bintray.com/search/packages/maven", listOf(Pair("g","*${keyword}*"))).responseString().third.get()
        val obj1 = gson.fromJson<List<BintrayPackage>>(res1, bintrayPackageListType)
        val res2 = Fuel.get("https://api.bintray.com/search/packages/maven", listOf(Pair("a","*${keyword}*"))).responseString().third.get()
        val obj2 = gson.fromJson<List<BintrayPackage>>(res2, bintrayPackageListType)
        return Repository(
            name = "Bintray's JCenter",
            url = URL("https://jcenter.bintray.com/"),
            packages = (obj1 + obj2).map { it.asPackages }.flatten()
        )
    }
}