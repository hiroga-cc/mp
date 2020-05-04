package cc.hiroga.mp

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.net.URL

data class Repository(
    val name: String,
    val url: URL,
    val packages: List<Package>
)

data class Package(
    val groupId: String,
    val artifactId: String,
    val latestVersion: String
)

data class MavenCentralResponse(
    // val responseHeader // not used in this tool.
    val response: MavenCentralResponseBody
    // val spellcheck // not used in this tool
)

data class MavenCentralResponseBody(
    val numFound: Int,
    val start: Int,
    val docs: List<MavenCentralPackage>
)

data class MavenCentralPackage(
    val id: String,
    val g: String,
    val a: String,
    val latestVersion: String,
    val repositoryId: String,
    val p: String,
    val timestamp: String,
    val versionCount: String,
    val text: List<String>,
    val ec: List<String>
) {
    val asPackage: Package
        get() = Package(groupId = g, artifactId = a, latestVersion = latestVersion)
}

class MavenCentralResponseDeserializer : JsonDeserializer<MavenCentralResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MavenCentralResponse {
        val obj = json as JsonObject
        return MavenCentralResponse(
            response = context!!.deserialize(
                obj.get("response").asJsonObject, MavenCentralResponseBody::class.java
            )
        )
    }
}

class MavenCentralResponseBodyDeserializer : JsonDeserializer<MavenCentralResponseBody> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MavenCentralResponseBody {
        val obj = json as JsonObject
        return MavenCentralResponseBody(
            numFound = obj.get("numFound").asInt,
            start = obj.get("start").asInt,
            docs = context!!.deserialize(
                obj.get("docs").asJsonArray, object : TypeToken<List<MavenCentralPackage>>() {}.type
            )
        )
    }
}

class MavenCentralPackageDeserializer : JsonDeserializer<MavenCentralPackage> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MavenCentralPackage {
        val obj = json as JsonObject
        return MavenCentralPackage(
            id = obj.get("id").asString,
            g = obj.get("g").asString,
            a = obj.get("a").asString,
            latestVersion = obj.get("latestVersion").asString,
            repositoryId = obj.get("repositoryId").asString,
            p = obj.get("p").asString,
            timestamp = obj.get("timestamp").asString,
            versionCount = obj.get("versionCount").asString,
            text = context!!.deserialize(
                obj.get("text").asJsonArray, object : TypeToken<List<String>>() {}.type
            ),
            ec = context!!.deserialize(
                obj.get("ec").asJsonArray, object : TypeToken<List<String>>() {}.type
            )
        )
    }
}

data class BintrayPackage(
    val name: String,
    val repo: String,
    val owner: String,
    val desc: Boolean?,
    val systemIds: List<String>,
    val versions: List<String>,
    val latestVersion: String
) {
    val asPackages: List<Package>
        get() {
            return systemIds.map {
                val (groupId, artifactId) = it.split(":")
                Package(groupId = groupId, artifactId = artifactId, latestVersion = latestVersion)
            }
        }
}

val bintrayPackageListType: Type = object: TypeToken<List<BintrayPackage>>() {}.type

class BintrayPackageDeserializer : JsonDeserializer<BintrayPackage> {
    override fun deserialize(
        json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?
    ): BintrayPackage {
       val obj =  json as JsonObject
        return BintrayPackage(
            obj.get("name").asString,
            obj.get("repo").asString,
            obj.get("owner").asString,
            obj.get("desc").let {
                when (it) {
                    is JsonNull -> null
                    else -> it.asBoolean
                }
            },
            context!!.deserialize(
                obj.get("system_ids").asJsonArray, object : TypeToken<List<String>>() {}.type
            ),
            context!!.deserialize(obj.get("versions").asJsonArray, object : TypeToken<List<String>>() {}.type),
            obj.get("latest_version").asString
        )
    }
}
