import asset.pipeline.AssetPipelineConfigHolder
import asset.pipeline.bower.BowerAssetResolver

eventAssetPrecompileStart = {assetConfig ->
    // The only way to hook into WAR file generation
    AssetPipelineConfigHolder.registerResolver(new BowerAssetResolver())
}