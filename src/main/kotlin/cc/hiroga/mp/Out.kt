package cc.hiroga.mp

object Out{
    private const val FORMAT = "%-40s %-32s %s"

    fun out(searchPackageResult: SearchPackageResult){
        println("${searchPackageResult.name} (${searchPackageResult.url})")
        println("")
        println(FORMAT.format("GroupId", "ArtifactId", "LatestVersion"))
        println("--------------------------------------------------------------------------------")
        searchPackageResult.packages.map{
            println(FORMAT.format(it.groupId, it.artifactId, it.latestVersion))
        }
        println("")
    }
}