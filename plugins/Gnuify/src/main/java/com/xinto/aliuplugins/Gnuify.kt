package com.xinto.aliuplugins

import android.content.Context
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.api.CommandsAPI.CommandResult
import com.discord.api.commands.ApplicationCommandType
import java.lang.StringBuilder

@AliucordPlugin
class Gnuify : Plugin() {

    private val prefix = "GNU/"

    override fun start(context: Context) {
        val argument = Utils.createCommandOption(
            type = ApplicationCommandType.STRING,
            name = "sentence",
            description = "Sentence to GNUify",
            required = true,
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