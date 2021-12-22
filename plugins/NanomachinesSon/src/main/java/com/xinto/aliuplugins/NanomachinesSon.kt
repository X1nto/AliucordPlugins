package com.xinto.aliuplugins

import android.content.Context
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI.CommandResult
import com.aliucord.entities.Plugin

@AliucordPlugin
class NanomachinesSon : Plugin() {

    override fun start(context: Context) {
        commands.registerCommand(
            "nanomachines",
            "Nanomachines, son!"
        ) {
            CommandResult(speech)
        }
    }

    override fun stop(context: Context) {
        commands.unregisterAll()
    }

    companion object {
        private const val speech = """I have a dream. That one day every person in this nation will control their own destiny. A nation of the truly free, dammit. A nation of action, not words, ruled by strength, not committee! Where the law changes to suit the individual, not the other way around. Where power and justice are back where they belong: in the hands of the people! Where every man is free to think - to act - for himself! FUCK all these limp-dick lawyers and chickenshit bureaucrats. FUCK this 24-hour Internet spew of trivia and celebrity bullshit! FUCK American pride! FUCK the media! FUCK ALL OF IT! America is diseased. Rotten to the core. There's no saving it - we need to pull it out by the roots. Wipe the slate clean. BURN IT DOWN! And from the ashes, a new America will be born. Evolved, but untamed! The weak will be purged and the strongest will thrive - free to live as they see fit, they'll make America great again!... In my new America, people will die and kill for what they BELIEVE! Not for money. not for oil! Not for what they're told is right. Every man will be free to fight his own wars!"""
    }
}