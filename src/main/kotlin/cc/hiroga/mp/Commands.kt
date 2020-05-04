package cc.hiroga.mp

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class Command : CliktCommand() {
    override fun run() = Unit
}

class SearchCommand : CliktCommand(name = "search") {
    private val keywords: List<String> by argument().multiple()
    override fun run() {
        runBlocking {
            listOf(MavenRepository, JCenter)
                .map { async { it.search(keywords[0]) } }
                .map { it.await() }
                .map { Out.out(it) }
        }
    }
}
