rootProject.name = "AliucordPlugins"

include(":Gnuify")
project(":Gnuify").projectDir = File("./plugins/Gnuify")

include(":LayoutController")
project(":LayoutController").projectDir = File("./plugins/LayoutController")

include(":Lyrics")
project(":Lyrics").projectDir = File("./plugins/Lyrics")

include(":NitroSpoof")
project(":NitroSpoof").projectDir = File("./plugins/NitroSpoof")

include(":ShowHiddenChannels")
project(":ShowHiddenChannels").projectDir = File("./plugins/ShowHiddenChannels")

include(":Stallman")
project(":Stallman").projectDir = File("./plugins/Stallman")
