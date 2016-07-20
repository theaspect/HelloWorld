import asset.pipeline.AssetPipelineConfigHolder

eventAssetPrecompileStart = {assetConfig ->
    // The only way to hook into WAR file generation
    classLoader.loadClass('asset.pipeline.bower.BowerAssetResolver')
    AssetPipelineConfigHolder.registerResolver(new asset.pipeline.bower.BowerAssetResolver())
}