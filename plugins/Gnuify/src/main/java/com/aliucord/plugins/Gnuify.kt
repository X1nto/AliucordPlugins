package com.aliucord.plugins

import android.content.Context
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.entities.Plugin.Manifest.Author
import com.aliucord.api.CommandsAPI.CommandResult
import com.discord.api.commands.ApplicationCommandType
import com.discord.models.commands.ApplicationCommandOption
import java.lang.StringBuilder

@AliucordPlugin
class Gnuify : Plugin() {

    private val prefix = "GNU/"

    override fun start(context: Context) {
        val argument = ApplicationCommandOption(
            ApplicationCommandType.STRING,
            "sentence",
            "A sentence to Gnuify",
            null,
            true,
            true,
            null,
            null
        )
        commands.registerCommand(
            "gnuify",
            "Append $prefix prefix to every word in the sentence.",
            listOf(argument)
        ) { ctx ->
            val sentence = ctx.getRequiredString("sentence")
            val gnuifiedSentence = StringBuilder()
            for (word in sentence.split("\\s+").toTypedArray()) {
                gnuifiedSentence.append(prefix).append(word).append(" ")
            }
            return@registerCommand CommandResult(gnuifiedSentence.toString())
        }
    }

    override fun stop(context: Context) {
        commands.unregisterAll()
    }
}