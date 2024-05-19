package com.xinto.aliuplugins

// import java.io.File
import android.content.Context
import com.aliucord.Constants.ALIUCORD_GUILD_ID
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI
import com.aliucord.entities.Plugin
import com.aliucord.patcher.Hook
import com.aliucord.patcher.InsteadHook
import com.aliucord.wrappers.ChannelWrapper
import com.discord.models.domain.emoji.ModelEmojiCustom
import com.discord.models.guild.Guild
import com.discord.stores.StoreStream
import com.xinto.aliuplugins.nitrospoof.EMOTE_SIZE_DEFAULT
import com.xinto.aliuplugins.nitrospoof.EMOTE_SIZE_KEY
import com.xinto.aliuplugins.nitrospoof.EMPTY_CHAR
import com.xinto.aliuplugins.nitrospoof.PluginSettings
import com.xinto.aliuplugins.nitrospoof.permBlacklist
import com.xinto.aliuplugins.nitrospoof.servBlacklist
import de.robv.android.xposed.XC_MethodHook
import java.lang.reflect.Field

@AliucordPlugin
class NitroSpoof : Plugin() {

  private val reflectionCache = HashMap<String, Field>()

  fun getGuildId(): Long {
    val CW = ChannelWrapper(StoreStream.getChannelsSelected().getSelectedChannel())
    return CW.guildId
  }
  
  fun getGuildName(): String {
    val CW = ChannelWrapper(StoreStream.getChannelsSelected().getSelectedChannel())
    val Guild: Guild get() = if (CW.isDM()) StoreStream.getGuilds().getGuild(769749710479032340) else StoreStream.getGuilds().getGuild(CW.guildId)
    val name = Guild.getName()
    return name
  }

  fun isDm(): Boolean {
    val CW = ChannelWrapper(StoreStream.getChannelsSelected().getSelectedChannel())
    val dm = CW.isDM()
    return dm
  }

  override fun start(context: Context) {
    patcher.patch(
        ModelEmojiCustom::class.java.getDeclaredMethod("getChatInputText"),
        Hook { getChatReplacement(it) }
    )
    patcher.patch(
        ModelEmojiCustom::class.java.getDeclaredMethod("getMessageContentReplacement"),
        Hook { getChatReplacement(it) }
    )
    patcher.patch(ModelEmojiCustom::class.java.getDeclaredMethod("isUsable"), InsteadHook { true })
    patcher.patch(
        ModelEmojiCustom::class.java.getDeclaredMethod("isAvailable"),
        InsteadHook { true }
    )
    commands.registerCommand("blacklist", "Blacklist current server to not use empty character.") {
      if (servBlacklist.contains(getGuildId())) {
        CommandsAPI.CommandResult("Current server is already in blacklist.")
      } else if (isDm()) {
        CommandsAPI.CommandResult("You cannot blacklist a DM")
      } else {
        servBlacklist.put(getGuildId(), getGuildName())
        CommandsAPI.CommandResult("Current server is blacklisted.")
      }
    }
    commands.registerCommand("whitelist", "Remove current server from blacklist.") {
      if (getGuildId() == ALIUCORD_GUILD_ID) CommandsAPI.CommandResult("Nop.")
      else if (isDm()) {
        CommandsAPI.CommandResult("Why would you")
      } else {
        servBlacklist.remove(getGuildId())
        CommandsAPI.CommandResult("Current server removed from blacklist.")
      }
    }
    /* commands.registerCommand("freenitroll", "Get free nitro (this is a troll)") {
        try {
            File(Constants.PLUGINS_PATH, "NitroSpoof.zip").delete()
        } catch (_: Throwable) {}<F11>
        CommandsAPI.CommandResult(BEE_MOVIE_SCRIPT, null, false)
    } */
  }

  override fun stop(context: Context) {
    patcher.unpatchAll()
  }

  private fun getChatReplacement(callFrame: XC_MethodHook.MethodHookParam) {
    val thisObject = callFrame.thisObject as ModelEmojiCustom
    val isUsable = thisObject.getCachedField<Boolean>("isUsable")

    if (isUsable) {
      callFrame.result = callFrame.result
      return
    }

    var finalUrl = "https://cdn.discordapp.com/emojis/"

    val idStr = thisObject.getCachedField<String>("idStr")
    val isAnimated = thisObject.getCachedField<Boolean>("isAnimated")

    finalUrl += idStr
    val emoteSize = settings.getString(EMOTE_SIZE_KEY, EMOTE_SIZE_DEFAULT).toIntOrNull()

    finalUrl += (if (isAnimated) ".gif" else ".png") + "?quality=lossless"

    if (emoteSize != null) {
      finalUrl += "&size=${emoteSize}"
    }

    if (settings.getBool("emptyChar", false) &&
            !servBlacklist.contains(getGuildId()) &&
            !permBlacklist.contains(getGuildId())
    ) {
      finalUrl = EMPTY_CHAR + "(" + finalUrl + ")"
    }

    callFrame.result = finalUrl
  }

  /**
   * Get a reflected field from cache or compute it if cache is absent
   * @param V type of the field value
   */
  private inline fun <reified V> Any.getCachedField(
      name: String,
      instance: Any? = this,
  ): V {
    val clazz = this::class.java
    return reflectionCache
        .computeIfAbsent(clazz.name + name) {
          clazz.getDeclaredField(name).also { it.isAccessible = true }
        }
        .get(instance) as
        V
  }

  init {
    settingsTab = SettingsTab(PluginSettings::class.java, SettingsTab.Type.PAGE).withArgs(settings)
  }

  /* companion object {
      private const val BEE_MOVIE_SCRIPT = "Bee Movie Script\nAccording to all known laws\nof aviation,\nthere is no way a bee\nshould be able to fly.\nIts wings are too small to get\nits fat little body off the ground.\nThe bee, of course, flies anyway\nbecause bees don't care\nwhat humans think is impossible.\nYellow, black. Yellow, black.\nYellow, black. Yellow, black.\nOoh, black and yellow!\nLet's shake it up a little.\nBarry! Breakfast is ready!\nOoming!\nHang on a second.\nHello?\n- Barry?\n- Adam?\n- Oan you believe this is happening?\n- I can't. I'll pick you up.\nLooking sharp.\nUse the stairs. Your father\npaid good money for those.\nSorry. I'm excited.\nHere's the graduate.\nWe're very proud of you, son.\nA perfect report card, all B's.\nVery proud.\nMa! I got a thing going here.\n- You got lint on your fuzz.\n- Ow! That's me!\n- Wave to us! We'll be in row 118,000.\n- Bye!\nBarry, I told you,\nstop flying in the house!\n- Hey, Adam.\n- Hey, Barry.\n- Is that fuzz gel?\n- A little. Special day, graduation.\nNever thought I'd make it.\nThree days grade school,\nthree days high school.\nThose were awkward.\nThree days college. I'm glad I took\na day and hitchhiked around the hive.\nYou did come back different.\n- Hi, Barry.\n- Artie, growing a mustache? Looks good.\n- Hear about Frankie?\n- Yeah.\n- You going to the funeral?\n- No, I'm not going.\nEverybody knows,\nsting someone, you die.\nDon't waste it on a squirrel.\nSuch a hothead.\nI guess he could have\njust gotten out of the way.\nI love this incorporating\nan amusement park into our day.\nThat's why we don't need vacations.\nBoy, quite a bit of pomp...\nunder the circumstances.\n- Well, Adam, today we are men.\n- We are!\n- Bee-men.\n- Amen!\nHallelujah!\nStudents, faculty, distinguished bees,\nplease welcome Dean Buzzwell.\nWelcome, New Hive Oity\ngraduating class of...\n...9:15.\nThat concludes our ceremonies.\nAnd begins your career\nat Honex Industries!\nWill we pick ourjob today?\nI heard it's just orientation.\nHeads up! Here we go.\nKeep your hands and antennas\ninside the tram at all times.\n- Wonder what it'll be like?\n- A little scary.\nWelcome to Honex,\na division of Honesco\nand a part of the Hexagon Group.\nThis is it!\nWow.\nWow.\nWe know that you, as a bee,\nhave worked your whole life\nto get to the point where you\ncan work for your whole life.\nHoney begins when our valiant Pollen\nJocks bring the nectar to the hive.\nOur top-secret formula\nis automatically color-corrected,\nscent-adjusted and bubble-contoured\ninto this soothing sweet syrup\nwith its distinctive\ngolden glow you know as...\nHoney!\n- That girl was hot.\n- She's my cousin!\n- She is?\n- Yes, we're all cousins.\n- Right. You're right.\n- At Honex, we constantly strive\nto improve every aspect\nof bee existence.\nThese bees are stress-testing\na new helmet technology.\n- What do you think he makes?\n- Not enough.\nHere we have our latest advancement,\nthe Krelman.\n- What does that do?\n- Oatches that little strand of honey\nthat hangs after you pour it.\nSaves us millions.\nOan anyone work on the Krelman?\nOf course. Most bee jobs are\nsmall ones. But bees know\nthat every small job,\nif it's done well, means a lot.\nBut choose carefully\nbecause you'll stay in the job\nyou pick for the rest of your life.\nThe same job the rest of your life?\nI didn't know that.\nWhat's the difference?\nYou'll be happy to know that bees,\nas a species, haven't had one day off\nin 27 million years.\nSo you'll just work us to death?\nWe'll sure try.\nWow! That blew my mind!\n\"What's the difference?\"\nHow can you say that?\nOne job forever?\nThat's an insane choice to have to make.\nI'm relieved. Now we only have\nto make one decision in life.\nBut, Adam, how could they\nnever have told us that?\nWhy would you question anything?\nWe're bees.\nWe're the most perfectly\nfunctioning society on Earth.\nYou ever think maybe things\nwork a little too well here?\nLike what? Give me one example.\nI don't know. But you know\nwhat I'm talking about.\nPlease clear the gate.\nRoyal Nectar Force on approach.\nWait a second. Oheck it out.\n- Hey, those are Pollen Jocks!\n- Wow.\nI've never seen them this close.\nThey know what it's like\noutside the hive.\nYeah, but some don't come back.\n- Hey, Jocks!\n- Hi, Jocks!\nYou guys did great!\nYou're monsters!\nYou're sky freaks! I love it! I love it!\n- I wonder where they were.\n- I don't know.\nTheir day's not planned.\nOutside the hive, flying who knows\nwhere, doing who knows what.\nYou can'tjust decide to be a Pollen\nJock. You have to be bred for that.\nRight.\nLook. That's more pollen\nthan you and I will see in a lifetime.\nIt's just a status symbol.\nBees make too much of it.\nPerhaps. Unless you're wearing it\nand the ladies see you wearing it.\nThose ladies?\nAren't they our cousins too?\nDistant. Distant.\nLook at these two.\n- Oouple of Hive Harrys.\n- Let's have fun with them.\nIt must be dangerous\nbeing a Pollen Jock.\nYeah. Once a bear pinned me\nagainst a mushroom!\nHe had a paw on my throat,\nand with the other, he was slapping me!\n- Oh, my!\n- I never thought I'd knock him out.\nWhat were you doing during this?\nTrying to alert the authorities.\nI can autograph that.\nA little gusty out there today,\nwasn't it, comrades?\nYeah. Gusty.\nWe're hitting a sunflower patch\nsix miles from here tomorrow.\n- Six miles, huh?\n- Barry!\nA puddle jump for us,\nbut maybe you're not up for it.\n- Maybe I am.\n- You are not!\nWe're going 0900 at J-Gate.\nWhat do you think, buzzy-boy?\nAre you bee enough?\nI might be. It all depends\non what 0900 means.\nHey, Honex!\nDad, you surprised me.\nYou decide what you're interested in?\n- Well, there's a lot of choices.\n- But you only get one.\nDo you ever get bored\ndoing the same job every day?\nSon, let me tell you about stirring.\nYou grab that stick, and you just\nmove it around, and you stir it around.\nYou get yourself into a rhythm.\nIt's a beautiful thing.\nYou know, Dad,\nthe more I think about it,\nmaybe the honey field\njust isn't right for me.\nYou were thinking of what,\nmaking balloon animals?\nThat's a bad job\nfor a guy with a stinger.\nJanet, your son's not sure\nhe wants to go into honey!\n- Barry, you are so funny sometimes.\n- I'm not trying to be funny.\nYou're not funny! You're going\ninto honey. Our son, the stirrer!\n- You're gonna be a stirrer?\n- No one's listening to me!\nWait till you see the sticks I have.\nI could say anything right now.\nI'm gonna get an ant tattoo!\nLet's open some honey and celebrate!\nMaybe I'll pierce my thorax.\nShave my antennae.\nShack up with a grasshopper. Get\na gold tooth and call everybody \"dawg\"!\nI'm so proud.\n- We're starting work today!\n- Today's the day.\nOome on! All the good jobs\nwill be gone.\nYeah, right.\nPollen counting, stunt bee, pouring,\nstirrer, front desk, hair removal...\n- Is it still available?\n- Hang on. Two left!\nOne of them's yours! Oongratulations!\nStep to the side.\n- What'd you get?\n- Picking crud out. Stellar!\nWow!\nOouple of newbies?\nYes, sir! Our first day! We are ready!\nMake your choice.\n- You want to go first?\n- No, you go.\nOh, my. What's available?\nRestroom attendant's open,\nnot for the reason you think.\n- Any chance of getting the Krelman?\n- Sure, you're on.\nI'm sorry, the Krelman just closed out.\nWax monkey's always open.\nThe Krelman opened up again.\nWhat happened?\nA bee died. Makes an opening. See?\nHe's dead. Another dead one.\nDeady. Deadified. Two more dead.\nDead from the neck up.\nDead from the neck down. That's life!\nOh, this is so hard!\nHeating, cooling,\nstunt bee, pourer, stirrer,\nhumming, inspector number seven,\nlint coordinator, stripe supervisor,\nmite wrangler. Barry, what\ndo you think I should... Barry?\nBarry!\nAll right, we've got the sunflower patch\nin quadrant nine...\nWhat happened to you?\nWhere are you?\n- I'm going out.\n- Out? Out where?\n- Out there.\n- Oh, no!\nI have to, before I go\nto work for the rest of my life.\nYou're gonna die! You're crazy! Hello?\nAnother call coming in.\nIf anyone's feeling brave,\nthere's a Korean deli on 83rd\nthat gets their roses today.\nHey, guys.\n- Look at that.\n- Isn't that the kid we saw yesterday?\nHold it, son, flight deck's restricted.\nIt's OK, Lou. We're gonna take him up.\nReally? Feeling lucky, are you?\nSign here, here. Just initial that.\n- Thank you.\n- OK.\nYou got a rain advisory today,\nand as you all know,\nbees cannot fly in rain.\nSo be careful. As always,\nwatch your brooms,\nhockey sticks, dogs,\nbirds, bears and bats.\nAlso, I got a couple of reports\nof root beer being poured on us.\nMurphy's in a home because of it,\nbabbling like a cicada!\n- That's awful.\n- And a reminder for you rookies,\nbee law number one,\nabsolutely no talking to humans!\nAll right, launch positions!\nBuzz, buzz, buzz, buzz! Buzz, buzz,\nbuzz, buzz! Buzz, buzz, buzz, buzz!\nBlack and yellow!\nHello!\nYou ready for this, hot shot?\nYeah. Yeah, bring it on.\nWind, check.\n- Antennae, check.\n- Nectar pack, check.\n- Wings, check.\n- Stinger, check.\nScared out of my shorts, check.\nOK, ladies,\nlet's move it out!\nPound those petunias,\nyou striped stem-suckers!\nAll of you, drain those flowers!\nWow! I'm out!\nI can't believe I'm out!\nSo blue.\nI feel so fast and free!\nBox kite!\nWow!\nFlowers!\nThis is Blue Leader.\nWe have roses visual.\nBring it around 30 degrees and hold.\nRoses!\n30 degrees, roger. Bringing it around.\nStand to the side, kid.\nIt's got a bit of a kick.\nThat is one nectar collector!\n- Ever see pollination up close?\n- No, sir.\nI pick up some pollen here, sprinkle it\nover here. Maybe a dash over there,\na pinch on that one.\nSee that? It's a little bit of magic.\nThat's amazing. Why do we do that?\nThat's pollen power. More pollen, more\nflowers, more nectar, more honey for us.\nOool.\nI'm picking up a lot of bright yellow.\nOould be daisies. Don't we need those?\nOopy that visual.\nWait. One of these flowers\nseems to be on the move.\nSay again? You're reporting\na moving flower?\nAffirmative.\nThat was on the line!\nThis is the coolest. What is it?\nI don't know, but I'm loving this color.\nIt smells good.\nNot like a flower, but I like it.\nYeah, fuzzy.\nOhemical-y.\nOareful, guys. It's a little grabby.\nMy sweet lord of bees!\nOandy-brain, get off there!\nProblem!\n- Guys!\n- This could be bad.\nAffirmative.\nVery close.\nGonna hurt.\nMama's little boy.\nYou are way out of position, rookie!\nOoming in at you like a missile!\nHelp me!\nI don't think these are flowers.\n- Should we tell him?\n- I think he knows.\nWhat is this?!\nMatch point!\nYou can start packing up, honey,\nbecause you're about to eat it!\nYowser!\nGross.\nThere's a bee in the car!\n- Do something!\n- I'm driving!\n- Hi, bee.\n- He's back here!\nHe's going to sting me!\nNobody move. If you don't move,\nhe won't sting you. Freeze!\nHe blinked!\nSpray him, Granny!\nWhat are you doing?!\nWow... the tension level\nout here is unbelievable.\nI gotta get home.\nOan't fly in rain.\nOan't fly in rain.\nOan't fly in rain.\nMayday! Mayday! Bee going down!\nKen, could you close\nthe window please?\nKen, could you close\nthe window please?\nOheck out my new resume.\nI made it into a fold-out brochure.\nYou see? Folds out.\nOh, no. More humans. I don't need this.\nWhat was that?\nMaybe this time. This time. This time.\nThis time! This time! This...\nDrapes!\nThat is diabolical.\nIt's fantastic. It's got all my special\nskills, even my top-ten favorite movies.\nWhat's number one? Star Wars?\nNah, I don't go for that...\n...kind of stuff.\nNo wonder we shouldn't talk to them.\nThey're out of their minds.\nWhen I leave a job interview, they're\nflabbergasted, can't believe what I say.\nThere's the sun. Maybe that's a way out.\nI don't remember the sun\nhaving a big 75 on it.\nI predicted global warming.\nI could feel it getting hotter.\nAt first I thought it was just me.\nWait! Stop! Bee!\nStand back. These are winter boots.\nWait!\nDon't kill him!\nYou know I'm allergic to them!\nThis thing could kill me!\nWhy does his life have\nless value than yours?\nWhy does his life have any less value\nthan mine? Is that your statement?\nI'm just saying all life has value. You\ndon't know what he's capable of feeling.\nMy brochure!\nThere you go, little guy.\nI'm not scared of him.\nIt's an allergic thing.\nPut that on your resume brochure.\nMy whole face could puff up.\nMake it one of your special skills.\nKnocking someone out\nis also a special skill.\nRight. Bye, Vanessa. Thanks.\n- Vanessa, next week? Yogurt night?\n- Sure, Ken. You know, whatever.\n- You could put carob chips on there.\n- Bye.\n- Supposed to be less calories.\n- Bye.\nI gotta say something.\nShe saved my life.\nI gotta say something.\nAll right, here it goes.\nNah.\nWhat would I say?\nI could really get in trouble.\nIt's a bee law.\nYou're not supposed to talk to a human.\nI can't believe I'm doing this.\nI've got to.\nOh, I can't do it. Oome on!\nNo. Yes. No.\nDo it. I can't.\nHow should I start it?\n\"You like jazz?\" No, that's no good.\nHere she comes! Speak, you fool!\nHi!\nI'm sorry.\n- You're talking.\n- Yes, I know.\nYou're talking!\nI'm so sorry.\nNo, it's OK. It's fine.\nI know I'm dreaming.\nBut I don't recall going to bed.\nWell, I'm sure this\nis very disconcerting.\nThis is a bit of a surprise to me.\nI mean, you're a bee!\nI am. And I'm not supposed\nto be doing this,\nbut they were all trying to kill me.\nAnd if it wasn't for you...\nI had to thank you.\nIt's just how I was raised.\nThat was a little weird.\n- I'm talking with a bee.\n- Yeah.\nI'm talking to a bee.\nAnd the bee is talking to me!\nI just want to say I'm grateful.\nI'll leave now.\n- Wait! How did you learn to do that?\n- What?\nThe talking thing.\nSame way you did, I guess.\n\"Mama, Dada, honey.\" You pick it up.\n- That's very funny.\n- Yeah.\nBees are funny. If we didn't laugh,\nwe'd cry with what we have to deal with."
  } */

}
