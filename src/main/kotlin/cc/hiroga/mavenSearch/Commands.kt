package cc.hiroga.mavenSearch

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple

class Command : CliktCommand() {
    override fun run() = Unit
}

class SearchCommand : CliktCommand(name = "search") {
    val keyword: List<String> by argument().multiple()
    override fun run() {
        val pacs = Search.jCenter(keyword[0])
        println(pacs)
    }
}
