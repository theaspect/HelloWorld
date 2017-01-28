package asset.pipeline.bower

import asset.pipeline.AssetFile
import asset.pipeline.GenericAssetFile
import asset.pipeline.fs.AssetResolver
import grails.util.Holders
import org.springframework.stereotype.Component

@Component
class BowerAssetResolver implements AssetResolver {

    def bowerDownloadService = new BowerDownloadService(Holders.config.grails.assets.bowerjs ?: "target/bower/")

    @Override
    String getName() {
        return "BowerAssetResolver"
    }
    /**
     * Called from asset.pipeline.AssetHelper#fileForUri(java.lang.String, java.lang.String, java.lang.String, asset.pipeline.AssetFile)
     */
    @Override
    AssetFile getAsset(String relativePath, String contentType, String extension, AssetFile baseFile) {
        File f = new File(bowerDownloadService.dirFile, relativePath)
        if (f.exists()) {
            return new GenericAssetFile(path: f.getPath(), inputStreamSource: { return new FileInputStream(f) })
        } else {
            return null
        }
    }

    @Override
    AssetFile getAsset(String relativePath, String contentType) {
        return null
    }

    @Override
    AssetFile getAsset(String relativePath) {
        return null
    }

    @Override
    List<AssetFile> getAssets(String basePath, String contentType, String extension, Boolean recursive, AssetFile relativeFile, AssetFile baseFile) {
        if (basePath.endsWith(".bower.js")) {
            File relative = new File(basePath)
            String[] parseFileName = relative.getName().replace(".bower.js", "").split("-")
            String lib = parseFileName[0]
            String version = parseFileName.size() == 1 ? 'master' : parseFileName[1]

            return bowerDownloadService.getFiles(lib, version).collect { name, stream ->
                new BowerAssetFile(path: name.replaceAll('.js$',''), inputStreamSource: { return stream })
            }
        } else {
            return null
        }
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
        return []
    }
}
