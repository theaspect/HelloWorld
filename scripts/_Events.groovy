import asset.pipeline.AssetPipelineConfigHolder

eventAssetPrecompileStart = {assetConfig ->
    // The only way to hook into WAR file generation
    def clazz = grailsApp.classLoader.loadClass('asset.pipeline.bower.BowerAssetResolver')
    AssetPipelineConfigHolder.registerResolver(clazz.newInstance())
}
