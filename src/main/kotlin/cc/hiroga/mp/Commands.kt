package cc.hiroga.mp

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple

class Command : CliktCommand() {
    override fun run() = Unit
}

class SearchCommand : CliktCommand(name = "search") {
    private val keywords: List<String> by argument().multiple()
    override fun run() {
        listOf(MavenRepository, JCenter)
            .map { it.search(keywords[0]) }
            .map { Out.out(it) }
    }
}
