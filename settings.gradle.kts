rootProject.name = "AliucordPlugins"

File(rootDir.path + "/plugins")
    .listFiles()
    ?.forEach { plugin ->
        val pluginName = plugin.name
        include(":$pluginName")
        project(":$pluginName").projectDir = plugin
    }