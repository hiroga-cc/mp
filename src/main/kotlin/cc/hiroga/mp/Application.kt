package cc.hiroga.mp

import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    Command().subcommands(SearchCommand()).main(args)
}
