rootProject.name = "AliucordPlugins"

File("./plugins/")
    .listFiles()
    ?.forEach { plugin ->
        val pluginName = plugin.name
        include(":$pluginName")
        project(":$pluginName").projectDir = plugin
    }