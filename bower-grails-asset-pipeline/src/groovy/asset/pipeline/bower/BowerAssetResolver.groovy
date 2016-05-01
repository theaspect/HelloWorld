package asset.pipeline.bower

import asset.pipeline.AssetFile
import asset.pipeline.fs.AbstractAssetResolver
import org.springframework.stereotype.Component

@Component
class BowerAssetResolver extends AbstractAssetResolver<Object> {
    BowerAssetResolver() {
        super("SomeUnusedName")
    }

    @Override
    protected String relativePathToResolver(Object file, String scanDirectoryPath) {
        return null
    }

    @Override
    protected Object getRelativeFile(String relativePath, String name) {
        return null
    }

    @Override
    protected Closure<InputStream> createInputStreamClosure(Object file) {
        return null
    }

    @Override
    protected String getFileName(Object file) {
        return null
    }

    /**
     * Called from asset.pipeline.AssetHelper#fileForUri(java.lang.String, java.lang.String, java.lang.String, asset.pipeline.AssetFile)
     * @param relativePath
     * @param contentType
     * @param extension
     * @param baseFile
     * @return
     */
    @Override
    AssetFile getAsset(String relativePath, String contentType, String extension, AssetFile baseFile) {
        if (relativePath.endsWith(".bower.js")) {
            return new BowerAssetFile(
                    path: relativePath.split("//.")[0],
                    inputStreamSource: {
                        return new ByteArrayInputStream("Woo-Hoo".bytes)
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
