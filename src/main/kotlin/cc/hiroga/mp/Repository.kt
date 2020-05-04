package cc.hiroga.mp

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.google.gson.GsonBuilder
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.net.URL

private typealias Query = Pair<String, String>

private val gson = GsonBuilder().serializeNulls()
    .registerTypeAdapter(MavenCentralResponse::class.java, MavenCentralResponseDeserializer())
    .registerTypeAdapter(MavenCentralResponseBody::class.java, MavenCentralResponseBodyDeserializer())
    .registerTypeAdapter(MavenCentralPackage::class.java, MavenCentralPackageDeserializer())
    .registerTypeAdapter(BintrayPackage::class.java, BintrayPackageDeserializer()).create()

interface Repository {
    suspend fun search(keyword: String): SearchPackageResult
}

object MavenRepository : Repository {
    private const val name = "Central"
    private val url = URL("https://repo1.maven.org/maven2/")
    override suspend fun search(keyword: String): SearchPackageResult {
        // API Guide: https://search.maven.org/classic/#api
        val (_, _, result) =
            Fuel.get("https://search.maven.org/solrsearch/select", listOf(Pair("q", keyword), Pair("rows", 100)))
                .awaitStringResponseResult()
        return result.fold(
            { data ->
                SearchPackageResult(
                    name = name,
                    url = url,
                    packages = gson.fromJson(data, MavenCentralResponse::class.java).response.docs
                        .map { it.asPackage }
                        .sortedWith(compareBy({ it.groupId }, { it.artifactId }))
                )
            },
            { error ->
                println("When request on $name, an error of type ${error.exception} happened: ${error.message}")
                SearchPackageResult(name = name, url = url, packages = emptyList())
            }
        )
    }
}

object JCenter : Repository {
    private const val name = "Bintray's JCenter"
    private val url = URL("https://jcenter.bintray.com/")

    override suspend fun search(keyword: String): SearchPackageResult = coroutineScope {
        // API Guide: https://bintray.com/docs/api/#_maven_package_search
        val byGroupId = async {getPackages(keyword, listOf(Pair("g", "*${keyword}*")))}
        val byArtifactId = async {getPackages(keyword, listOf(Pair("g", "*${keyword}*")))}
        return@coroutineScope SearchPackageResult(
            name = name,
            url = url,
            packages = (byGroupId.await() + byArtifactId.await())
                .distinct()
                .sortedWith(compareBy({ it.groupId }, { it.artifactId }))
        )
    }

    private suspend fun getPackages(keyword: String, queries: List<Query>): List<Package> {
        val (_, _, result) =
            Fuel.get("https://api.bintray.com/search/packages/maven", queries)
                .awaitStringResponseResult()
        return result.fold(
            { data ->
                gson.fromJson<List<BintrayPackage>>(data, bintrayPackageListType).map { it.asPackages }.flatten()
            },
            { error ->
                println("When request on $name, an error of type ${error.exception} happened: ${error.message}")
                emptyList()
            }
        )

    }
}
