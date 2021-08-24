rootProject.name = "AliucordPlugins"

listOf(
    "AttachmentUtils",
    "Gnuify",
    "LayoutController",
    "Lyrics",
    "NitroSpoof",
    "ShowHiddenChannels",
    "Stallman"
).forEach { plugin ->
    include(":$plugin")
    project(":$plugin").projectDir = File("./plugins/$plugin")
}