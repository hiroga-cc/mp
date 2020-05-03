package cc.hiroga.mavenSearch

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

data class BintrayPackage(
    val name: String,
    val repo: String,
    val owner: String,
    val desc: Boolean?,
    val system_ids: List<String>,
    val versions: List<String>,
    val latest_version: String
)



class BintrayPackageDeserializer : JsonDeserializer<List<BintrayPackage>> {
    override fun deserialize(
        json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?
    ): List<BintrayPackage> {
        val array = json as JsonArray
        return array.map { obj ->
            obj as JsonObject
            BintrayPackage(
                obj.get("name").asString,
                obj.get("repo").asString,
                obj.get("owner").asString,
                obj.get("desc").let{
                    when(it){
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
}
