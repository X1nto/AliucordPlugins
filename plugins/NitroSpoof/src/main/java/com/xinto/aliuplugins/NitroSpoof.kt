package com.xinto.aliuplugins
 
import android.content.Context
import com.aliucord.Constants
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI
import com.aliucord.entities.Plugin
import com.aliucord.patcher.Hook
import com.aliucord.patcher.InsteadHook
import com.xinto.aliuplugins.nitrospoof.EMOTE_SIZE_DEFAULT
import com.xinto.aliuplugins.nitrospoof.EMOTE_SIZE_KEY
import com.xinto.aliuplugins.nitrospoof.PluginSettings
import com.discord.models.domain.emoji.ModelEmojiCustom
import de.robv.android.xposed.XC_MethodHook
import java.io.File
import java.lang.reflect.Field
 
@AliucordPlugin
class NitroSpoof : Plugin() {
 
    private val reflectionCache = HashMap<String, Field>()
 
    override fun start(context: Context) {
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("getChatInputText"),
            Hook { getChatReplacement(it) }
        )
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("getMessageContentReplacement"),
            Hook { getChatReplacement(it) }
        )
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("isUsable"),
            InsteadHook { true }
        )
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("isAvailable"),
            InsteadHook { true }
        )
        commands.registerCommand(
            "freenitro",
            "Get free nitro (This is a troll, use it only for fun)"
        ) {
            CommandsAPI.CommandResult(
                "Bee Movie Script\n" +
                        "According to all known laws\n" +
                        "of aviation,\n" +
                        "there is no way a bee\n" +
                        "should be able to fly.\n" +
                        "Its wings are too small to get\n" +
                        "its fat little body off the ground.\n" +
                        "The bee, of course, flies anyway\n" +
                        "because bees don't care\n" +
                        "what humans think is impossible.\n" +
                        "Yellow, black. Yellow, black.\n" +
                        "Yellow, black. Yellow, black.\n" +
                        "Ooh, black and yellow!\n" +
                        "Let's shake it up a little.\n" +
                        "Barry! Breakfast is ready!\n" +
                        "Ooming!\n" +
                        "Hang on a second.\n" +
                        "Hello?\n" +
                        "- Barry?\n" +
                        "- Adam?\n" +
                        "- Oan you believe this is happening?\n" +
                        "- I can't. I'll pick you up.\n" +
                        "Looking sharp.\n" +
                        "Use the stairs. Your father\n" +
                        "paid good money for those.\n" +
                        "Sorry. I'm excited.\n" +
                        "Here's the graduate.\n" +
                        "We're very proud of you, son.\n" +
                        "A perfect report card, all B's.\n" +
                        "Very proud.\n" +
                        "Ma! I got a thing going here.\n" +
                        "- You got lint on your fuzz.\n" +
                        "- Ow! That's me!\n" +
                        "- Wave to us! We'll be in row 118,000.\n" +
                        "- Bye!\n" +
                        "Barry, I told you,\n" +
                        "stop flying in the house!\n" +
                        "- Hey, Adam.\n" +
                        "- Hey, Barry.\n" +
                        "- Is that fuzz gel?\n" +
                        "- A little. Special day, graduation.\n" +
                        "Never thought I'd make it.\n" +
                        "Three days grade school,\n" +
                        "three days high school.\n" +
                        "Those were awkward.\n" +
                        "Three days college. I'm glad I took\n" +
                        "a day and hitchhiked around the hive.\n" +
                        "You did come back different.\n" +
                        "- Hi, Barry.\n" +
                        "- Artie, growing a mustache? Looks good.\n" +
                        "- Hear about Frankie?\n" +
                        "- Yeah.\n" +
                        "- You going to the funeral?\n" +
                        "- No, I'm not going.\n" +
                        "Everybody knows,\n" +
                        "sting someone, you die.\n" +
                        "Don't waste it on a squirrel.\n" +
                        "Such a hothead.\n" +
                        "I guess he could have\n" +
                        "just gotten out of the way.\n" +
                        "I love this incorporating\n" +
                        "an amusement park into our day.\n" +
                        "That's why we don't need vacations.\n" +
                        "Boy, quite a bit of pomp...\n" +
                        "under the circumstances.\n" +
                        "- Well, Adam, today we are men.\n" +
                        "- We are!\n" +
                        "- Bee-men.\n" +
                        "- Amen!\n" +
                        "Hallelujah!\n" +
                        "Students, faculty, distinguished bees,\n" +
                        "please welcome Dean Buzzwell.\n" +
                        "Welcome, New Hive Oity\n" +
                        "graduating class of...\n" +
                        "...9:15.\n" +
                        "That concludes our ceremonies.\n" +
                        "And begins your career\n" +
                        "at Honex Industries!\n" +
                        "Will we pick ourjob today?\n" +
                        "I heard it's just orientation.\n" +
                        "Heads up! Here we go.\n" +
                        "Keep your hands and antennas\n" +
                        "inside the tram at all times.\n" +
                        "- Wonder what it'll be like?\n" +
                        "- A little scary.\n" +
                        "Welcome to Honex,\n" +
                        "a division of Honesco\n" +
                        "and a part of the Hexagon Group.\n" +
                        "This is it!\n" +
                        "Wow.\n" +
                        "Wow.\n" +
                        "We know that you, as a bee,\n" +
                        "have worked your whole life\n" +
                        "to get to the point where you\n" +
                        "can work for your whole life.\n" +
                        "Honey begins when our valiant Pollen\n" +
                        "Jocks bring the nectar to the hive.\n" +
                        "Our top-secret formula\n" +
                        "is automatically color-corrected,\n" +
                        "scent-adjusted and bubble-contoured\n" +
                        "into this soothing sweet syrup\n" +
                        "with its distinctive\n" +
                        "golden glow you know as...\n" +
                        "Honey!\n" +
                        "- That girl was hot.\n" +
                        "- She's my cousin!\n" +
                        "- She is?\n" +
                        "- Yes, we're all cousins.\n" +
                        "- Right. You're right.\n" +
                        "- At Honex, we constantly strive\n" +
                        "to improve every aspect\n" +
                        "of bee existence.\n" +
                        "These bees are stress-testing\n" +
                        "a new helmet technology.\n" +
                        "- What do you think he makes?\n" +
                        "- Not enough.\n" +
                        "Here we have our latest advancement,\n" +
                        "the Krelman.\n" +
                        "- What does that do?\n" +
                        "- Oatches that little strand of honey\n" +
                        "that hangs after you pour it.\n" +
                        "Saves us millions.\n" +
                        "Oan anyone work on the Krelman?\n" +
                        "Of course. Most bee jobs are\n" +
                        "small ones. But bees know\n" +
                        "that every small job,\n" +
                        "if it's done well, means a lot.\n" +
                        "But choose carefully\n" +
                        "because you'll stay in the job\n" +
                        "you pick for the rest of your life.\n" +
                        "The same job the rest of your life?\n" +
                        "I didn't know that.\n" +
                        "What's the difference?\n" +
                        "You'll be happy to know that bees,\n" +
                        "as a species, haven't had one day off\n" +
                        "in 27 million years.\n" +
                        "So you'll just work us to death?\n" +
                        "We'll sure try.\n" +
                        "Wow! That blew my mind!\n" +
                        "\"What's the difference?\"\n" +
                        "How can you say that?\n" +
                        "One job forever?\n" +
                        "That's an insane choice to have to make.\n" +
                        "I'm relieved. Now we only have\n" +
                        "to make one decision in life.\n" +
                        "But, Adam, how could they\n" +
                        "never have told us that?\n" +
                        "Why would you question anything?\n" +
                        "We're bees.\n" +
                        "We're the most perfectly\n" +
                        "functioning society on Earth.\n" +
                        "You ever think maybe things\n" +
                        "work a little too well here?\n" +
                        "Like what? Give me one example.\n" +
                        "I don't know. But you know\n" +
                        "what I'm talking about.\n" +
                        "Please clear the gate.\n" +
                        "Royal Nectar Force on approach.\n" +
                        "Wait a second. Oheck it out.\n" +
                        "- Hey, those are Pollen Jocks!\n" +
                        "- Wow.\n" +
                        "I've never seen them this close.\n" +
                        "They know what it's like\n" +
                        "outside the hive.\n" +
                        "Yeah, but some don't come back.\n" +
                        "- Hey, Jocks!\n" +
                        "- Hi, Jocks!\n" +
                        "You guys did great!\n" +
                        "You're monsters!\n" +
                        "You're sky freaks! I love it! I love it!\n" +
                        "- I wonder where they were.\n" +
                        "- I don't know.\n" +
                        "Their day's not planned.\n" +
                        "Outside the hive, flying who knows\n" +
                        "where, doing who knows what.\n" +
                        "You can'tjust decide to be a Pollen\n" +
                        "Jock. You have to be bred for that.\n" +
                        "Right.\n" +
                        "Look. That's more pollen\n" +
                        "than you and I will see in a lifetime.\n" +
                        "It's just a status symbol.\n" +
                        "Bees make too much of it.\n" +
                        "Perhaps. Unless you're wearing it\n" +
                        "and the ladies see you wearing it.\n" +
                        "Those ladies?\n" +
                        "Aren't they our cousins too?\n" +
                        "Distant. Distant.\n" +
                        "Look at these two.\n" +
                        "- Oouple of Hive Harrys.\n" +
                        "- Let's have fun with them.\n" +
                        "It must be dangerous\n" +
                        "being a Pollen Jock.\n" +
                        "Yeah. Once a bear pinned me\n" +
                        "against a mushroom!\n" +
                        "He had a paw on my throat,\n" +
                        "and with the other, he was slapping me!\n" +
                        "- Oh, my!\n" +
                        "- I never thought I'd knock him out.\n" +
                        "What were you doing during this?\n" +
                        "Trying to alert the authorities.\n" +
                        "I can autograph that.\n" +
                        "A little gusty out there today,\n" +
                        "wasn't it, comrades?\n" +
                        "Yeah. Gusty.\n" +
                        "We're hitting a sunflower patch\n" +
                        "six miles from here tomorrow.\n" +
                        "- Six miles, huh?\n" +
                        "- Barry!\n" +
                        "A puddle jump for us,\n" +
                        "but maybe you're not up for it.\n" +
                        "- Maybe I am.\n" +
                        "- You are not!\n" +
                        "We're going 0900 at J-Gate.\n" +
                        "What do you think, buzzy-boy?\n" +
                        "Are you bee enough?\n" +
                        "I might be. It all depends\n" +
                        "on what 0900 means.\n" +
                        "Hey, Honex!\n" +
                        "Dad, you surprised me.\n" +
                        "You decide what you're interested in?\n" +
                        "- Well, there's a lot of choices.\n" +
                        "- But you only get one.\n" +
                        "Do you ever get bored\n" +
                        "doing the same job every day?\n" +
                        "Son, let me tell you about stirring.\n" +
                        "You grab that stick, and you just\n" +
                        "move it around, and you stir it around.\n" +
                        "You get yourself into a rhythm.\n" +
                        "It's a beautiful thing.\n" +
                        "You know, Dad,\n" +
                        "the more I think about it,\n" +
                        "maybe the honey field\n" +
                        "just isn't right for me.\n" +
                        "You were thinking of what,\n" +
                        "making balloon animals?\n" +
                        "That's a bad job\n" +
                        "for a guy with a stinger.\n" +
                        "Janet, your son's not sure\n" +
                        "he wants to go into honey!\n" +
                        "- Barry, you are so funny sometimes.\n" +
                        "- I'm not trying to be funny.\n" +
                        "You're not funny! You're going\n" +
                        "into honey. Our son, the stirrer!\n" +
                        "- You're gonna be a stirrer?\n" +
                        "- No one's listening to me!\n" +
                        "Wait till you see the sticks I have.\n" +
                        "I could say anything right now.\n" +
                        "I'm gonna get an ant tattoo!\n" +
                        "Let's open some honey and celebrate!\n" +
                        "Maybe I'll pierce my thorax.\n" +
                        "Shave my antennae.\n" +
                        "Shack up with a grasshopper. Get\n" +
                        "a gold tooth and call everybody \"dawg\"!\n" +
                        "I'm so proud.\n" +
                        "- We're starting work today!\n" +
                        "- Today's the day.\n" +
                        "Oome on! All the good jobs\n" +
                        "will be gone.\n" +
                        "Yeah, right.\n" +
                        "Pollen counting, stunt bee, pouring,\n" +
                        "stirrer, front desk, hair removal...\n" +
                        "- Is it still available?\n" +
                        "- Hang on. Two left!\n" +
                        "One of them's yours! Oongratulations!\n" +
                        "Step to the side.\n" +
                        "- What'd you get?\n" +
                        "- Picking crud out. Stellar!\n" +
                        "Wow!\n" +
                        "Oouple of newbies?\n" +
                        "Yes, sir! Our first day! We are ready!\n" +
                        "Make your choice.\n" +
                        "- You want to go first?\n" +
                        "- No, you go.\n" +
                        "Oh, my. What's available?\n" +
                        "Restroom attendant's open,\n" +
                        "not for the reason you think.\n" +
                        "- Any chance of getting the Krelman?\n" +
                        "- Sure, you're on.\n" +
                        "I'm sorry, the Krelman just closed out.\n" +
                        "Wax monkey's always open.\n" +
                        "The Krelman opened up again.\n" +
                        "What happened?\n" +
                        "A bee died. Makes an opening. See?\n" +
                        "He's dead. Another dead one.\n" +
                        "Deady. Deadified. Two more dead.\n" +
                        "Dead from the neck up.\n" +
                        "Dead from the neck down. That's life!\n" +
                        "Oh, this is so hard!\n" +
                        "Heating, cooling,\n" +
                        "stunt bee, pourer, stirrer,\n" +
                        "humming, inspector number seven,\n" +
                        "lint coordinator, stripe supervisor,\n" +
                        "mite wrangler. Barry, what\n" +
                        "do you think I should... Barry?\n" +
                        "Barry!\n" +
                        "All right, we've got the sunflower patch\n" +
                        "in quadrant nine...\n" +
                        "What happened to you?\n" +
                        "Where are you?\n" +
                        "- I'm going out.\n" +
                        "- Out? Out where?\n" +
                        "- Out there.\n" +
                        "- Oh, no!\n" +
                        "I have to, before I go\n" +
                        "to work for the rest of my life.\n" +
                        "You're gonna die! You're crazy! Hello?\n" +
                        "Another call coming in.\n" +
                        "If anyone's feeling brave,\n" +
                        "there's a Korean deli on 83rd\n" +
                        "that gets their roses today.\n" +
                        "Hey, guys.\n" +
                        "- Look at that.\n" +
                        "- Isn't that the kid we saw yesterday?\n" +
                        "Hold it, son, flight deck's restricted.\n" +
                        "It's OK, Lou. We're gonna take him up.\n" +
                        "Really? Feeling lucky, are you?\n" +
                        "Sign here, here. Just initial that.\n" +
                        "- Thank you.\n" +
                        "- OK.\n" +
                        "You got a rain advisory today,\n" +
                        "and as you all know,\n" +
                        "bees cannot fly in rain.\n" +
                        "So be careful. As always,\n" +
                        "watch your brooms,\n" +
                        "hockey sticks, dogs,\n" +
                        "birds, bears and bats.\n" +
                        "Also, I got a couple of reports\n" +
                        "of root beer being poured on us.\n" +
                        "Murphy's in a home because of it,\n" +
                        "babbling like a cicada!\n" +
                        "- That's awful.\n" +
                        "- And a reminder for you rookies,\n" +
                        "bee law number one,\n" +
                        "absolutely no talking to humans!\n" +
                        "All right, launch positions!\n" +
                        "Buzz, buzz, buzz, buzz! Buzz, buzz,\n" +
                        "buzz, buzz! Buzz, buzz, buzz, buzz!\n" +
                        "Black and yellow!\n" +
                        "Hello!\n" +
                        "You ready for this, hot shot?\n" +
                        "Yeah. Yeah, bring it on.\n" +
                        "Wind, check.\n" +
                        "- Antennae, check.\n" +
                        "- Nectar pack, check.\n" +
                        "- Wings, check.\n" +
                        "- Stinger, check.\n" +
                        "Scared out of my shorts, check.\n" +
                        "OK, ladies,\n" +
                        "let's move it out!\n" +
                        "Pound those petunias,\n" +
                        "you striped stem-suckers!\n" +
                        "All of you, drain those flowers!\n" +
                        "Wow! I'm out!\n" +
                        "I can't believe I'm out!\n" +
                        "So blue.\n" +
                        "I feel so fast and free!\n" +
                        "Box kite!\n" +
                        "Wow!\n" +
                        "Flowers!\n" +
                        "This is Blue Leader.\n" +
                        "We have roses visual.\n" +
                        "Bring it around 30 degrees and hold.\n" +
                        "Roses!\n" +
                        "30 degrees, roger. Bringing it around.\n" +
                        "Stand to the side, kid.\n" +
                        "It's got a bit of a kick.\n" +
                        "That is one nectar collector!\n" +
                        "- Ever see pollination up close?\n" +
                        "- No, sir.\n" +
                        "I pick up some pollen here, sprinkle it\n" +
                        "over here. Maybe a dash over there,\n" +
                        "a pinch on that one.\n" +
                        "See that? It's a little bit of magic.\n" +
                        "That's amazing. Why do we do that?\n" +
                        "That's pollen power. More pollen, more\n" +
                        "flowers, more nectar, more honey for us.\n" +
                        "Oool.\n" +
                        "I'm picking up a lot of bright yellow.\n" +
                        "Oould be daisies. Don't we need those?\n" +
                        "Oopy that visual.\n" +
                        "Wait. One of these flowers\n" +
                        "seems to be on the move.\n" +
                        "Say again? You're reporting\n" +
                        "a moving flower?\n" +
                        "Affirmative.\n" +
                        "That was on the line!\n" +
                        "This is the coolest. What is it?\n" +
                        "I don't know, but I'm loving this color.\n" +
                        "It smells good.\n" +
                        "Not like a flower, but I like it.\n" +
                        "Yeah, fuzzy.\n" +
                        "Ohemical-y.\n" +
                        "Oareful, guys. It's a little grabby.\n" +
                        "My sweet lord of bees!\n" +
                        "Oandy-brain, get off there!\n" +
                        "Problem!\n" +
                        "- Guys!\n" +
                        "- This could be bad.\n" +
                        "Affirmative.\n" +
                        "Very close.\n" +
                        "Gonna hurt.\n" +
                        "Mama's little boy.\n" +
                        "You are way out of position, rookie!\n" +
                        "Ooming in at you like a missile!\n" +
                        "Help me!\n" +
                        "I don't think these are flowers.\n" +
                        "- Should we tell him?\n" +
                        "- I think he knows.\n" +
                        "What is this?!\n" +
                        "Match point!\n" +
                        "You can start packing up, honey,\n" +
                        "because you're about to eat it!\n" +
                        "Yowser!\n" +
                        "Gross.\n" +
                        "There's a bee in the car!\n" +
                        "- Do something!\n" +
                        "- I'm driving!\n" +
                        "- Hi, bee.\n" +
                        "- He's back here!\n" +
                        "He's going to sting me!\n" +
                        "Nobody move. If you don't move,\n" +
                        "he won't sting you. Freeze!\n" +
                        "He blinked!\n" +
                        "Spray him, Granny!\n" +
                        "What are you doing?!\n" +
                        "Wow... the tension level\n" +
                        "out here is unbelievable.\n" +
                        "I gotta get home.\n" +
                        "Oan't fly in rain.\n" +
                        "Oan't fly in rain.\n" +
                        "Oan't fly in rain.\n" +
                        "Mayday! Mayday! Bee going down!\n" +
                        "Ken, could you close\n" +
                        "the window please?\n" +
                        "Ken, could you close\n" +
                        "the window please?\n" +
                        "Oheck out my new resume.\n" +
                        "I made it into a fold-out brochure.\n" +
                        "You see? Folds out.\n" +
                        "Oh, no. More humans. I don't need this.\n" +
                        "What was that?\n" +
                        "Maybe this time. This time. This time.\n" +
                        "This time! This time! This...\n" +
                        "Drapes!\n" +
                        "That is diabolical.\n" +
                        "It's fantastic. It's got all my special\n" +
                        "skills, even my top-ten favorite movies.\n" +
                        "What's number one? Star Wars?\n" +
                        "Nah, I don't go for that...\n" +
                        "...kind of stuff.\n" +
                        "No wonder we shouldn't talk to them.\n" +
                        "They're out of their minds.\n" +
                        "When I leave a job interview, they're\n" +
                        "flabbergasted, can't believe what I say.\n" +
                        "There's the sun. Maybe that's a way out.\n" +
                        "I don't remember the sun\n" +
                        "having a big 75 on it.\n" +
                        "I predicted global warming.\n" +
                        "I could feel it getting hotter.\n" +
                        "At first I thought it was just me.\n" +
                        "Wait! Stop! Bee!\n" +
                        "Stand back. These are winter boots.\n" +
                        "Wait!\n" +
                        "Don't kill him!\n" +
                        "You know I'm allergic to them!\n" +
                        "This thing could kill me!\n" +
                        "Why does his life have\n" +
                        "less value than yours?\n" +
                        "Why does his life have any less value\n" +
                        "than mine? Is that your statement?\n" +
                        "I'm just saying all life has value. You\n" +
                        "don't know what he's capable of feeling.\n" +
                        "My brochure!\n" +
                        "There you go, little guy.\n" +
                        "I'm not scared of him.\n" +
                        "It's an allergic thing.\n" +
                        "Put that on your resume brochure.\n" +
                        "My whole face could puff up.\n" +
                        "Make it one of your special skills.\n" +
                        "Knocking someone out\n" +
                        "is also a special skill.\n" +
                        "Right. Bye, Vanessa. Thanks.\n" +
                        "- Vanessa, next week? Yogurt night?\n" +
                        "- Sure, Ken. You know, whatever.\n" +
                        "- You could put carob chips on there.\n" +
                        "- Bye.\n" +
                        "- Supposed to be less calories.\n" +
                        "- Bye.\n" +
                        "I gotta say something.\n" +
                        "She saved my life.\n" +
                        "I gotta say something.\n" +
                        "All right, here it goes.\n" +
                        "Nah.\n" +
                        "What would I say?\n" +
                        "I could really get in trouble.\n" +
                        "It's a bee law.\n" +
                        "You're not supposed to talk to a human.\n" +
                        "I can't believe I'm doing this.\n" +
                        "I've got to.\n" +
                        "Oh, I can't do it. Oome on!\n" +
                        "No. Yes. No.\n" +
                        "Do it. I can't.\n" +
                        "How should I start it?\n" +
                        "\"You like jazz?\" No, that's no good.\n" +
                        "Here she comes! Speak, you fool!\n" +
                        "Hi!\n" +
                        "I'm sorry.\n" +
                        "- You're talking.\n" +
                        "- Yes, I know.\n" +
                        "You're talking!\n" +
                        "I'm so sorry.\n" +
                        "No, it's OK. It's fine.\n" +
                        "I know I'm dreaming.\n" +
                        "But I don't recall going to bed.\n" +
                        "Well, I'm sure this\n" +
                        "is very disconcerting.\n" +
                        "This is a bit of a surprise to me.\n" +
                        "I mean, you're a bee!\n" +
                        "I am. And I'm not supposed\n" +
                        "to be doing this,\n" +
                        "but they were all trying to kill me.\n" +
                        "And if it wasn't for you...\n" +
                        "I had to thank you.\n" +
                        "It's just how I was raised.\n" +
                        "That was a little weird.\n" +
                        "- I'm talking with a bee.\n" +
                        "- Yeah.\n" +
                        "I'm talking to a bee.\n" +
                        "And the bee is talking to me!\n" +
                        "I just want to say I'm grateful.\n" +
                        "I'll leave now.\n" +
                        "- Wait! How did you learn to do that?\n" +
                        "- What?\n" +
                        "The talking thing.\n" +
                        "Same way you did, I guess.\n" +
                        "\"Mama, Dada, honey.\" You pick it up.\n" +
                        "- That's very funny.\n" +
                        "- Yeah.\n" +
                        "Bees are funny. If we didn't laugh,\n" +
                        "we'd cry with what we have to deal with.",
                null,
                false
            )
        }
 
        try {
            File(Constants.PLUGINS_PATH, "NitroSpoof.zip").delete()
        } catch (_: Throwable) {}
    }
 
    override fun stop(context: Context) {
        patcher.unpatchAll()
    }
 
 
    fun getChatReplacement(callFrame: XC_MethodHook.MethodHookParam) {
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
 
        finalUrl += if (isAnimated) ".gif" else ".png"
 
        if (emoteSize != null) {
            finalUrl += "?size=${emoteSize}"
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
        return reflectionCache.computeIfAbsent(clazz.name + name) {
            clazz.getDeclaredField(name).also {
                it.isAccessible = true
            }
        }.get(instance) as V
    }
 
    init {
        settingsTab = SettingsTab(
            PluginSettings::class.java,
            SettingsTab.Type.PAGE
        ).withArgs(settings)
    }
 
}