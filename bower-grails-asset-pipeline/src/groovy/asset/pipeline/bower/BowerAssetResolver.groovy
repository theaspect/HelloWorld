package asset.pipeline.bower

import asset.pipeline.AssetFile
import asset.pipeline.fs.AssetResolver
import org.springframework.stereotype.Component

@Component
class BowerAssetResolver implements AssetResolver {

    @Override
    String getName() {
        return "BowerAssetResolver"
    }

    /**
     * Called from asset.pipeline.AssetHelper#fileForUri(java.lang.String, java.lang.String, java.lang.String, asset.pipeline.AssetFile)
     */
    @Override
    AssetFile getAsset(String relativePath, String contentType, String extension, AssetFile baseFile) {
        if (relativePath.endsWith(".bower.js")) {
            File relative = new File(relativePath)
            def parseFileName = new ArrayList()
            (parseFileName = relative.getName().replace(".bower.js", "").split("-")) as AssetFile
            //println(parseFileName)
            return new BowerAssetFile(
                    path: relativePath,// парс
                    inputStreamSource: {
                        if(parseFileName.size() == 1){
                            return new ByteArrayInputStream(BowerDownload.work("-url ${parseFileName[0].toString()}".split(" ")) as byte)
                        }
                        else{
                            return new ByteArrayInputStream(BowerDownload.work("-url ${parseFileName[0]} -version ${parseFileName[1]}".split(" ")) as byte)
                        }

                    })
        } else {
            return null
        }
    }

    @Override
    AssetFile getAsset(String relativePath, String contentType) {
        // FIXME
        return null
    }

    @Override
    AssetFile getAsset(String relativePath) {
        // FIXME
        return null
    }

    @Override
    List<AssetFile> getAssets(String basePath, String contentType, String extension, Boolean recursive, AssetFile relativeFile, AssetFile baseFile) {
        return null
    }

    @Override
    List<AssetFile> getAssets(String basePath, String contentType, String extension) {
        return null
    }

    @Override
    List<AssetFile> getAssets(String basePath, String contentType) {
        return null
    }

    @Override
    List<AssetFile> getAssets(String basePath) {
        return null
    }

    @Override
    Collection<AssetFile> scanForFiles(List<String> excludePatterns, List<String> includePatterns) {
        return null
    }
}
