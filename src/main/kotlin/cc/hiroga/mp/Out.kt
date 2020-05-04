package cc.hiroga.mp

object Out{
    private const val FORMAT = "%-40s %-32s %s"

    fun out(repository: Repository){
        println("${repository.name} (${repository.url})")
        println("")
        println(FORMAT.format("GroupId", "ArtifactId", "LatestVersion"))
        println("----------------------------------------------------------------------------------------------------")
        repository.packages.map{
            println(FORMAT.format(it.groupId, it.artifactId, it.latestVersion))
        }
        println("")
    }
}