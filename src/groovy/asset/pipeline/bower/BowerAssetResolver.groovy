package asset.pipeline.bower

import asset.pipeline.AssetFile
import asset.pipeline.GenericAssetFile
import asset.pipeline.fs.AssetResolver
import grails.util.Holders
import org.springframework.stereotype.Component

import java.nio.file.Files
import java.nio.file.Paths

@Component
class BowerAssetResolver implements AssetResolver {

    public static final String BOWER = ".bower"
    public static final String BOWER_JS = ".bower.js"
    public static final String BOWER_CSS = ".bower.css"
    //TODO Should I put it in _Events.groovy and BowerAssetPipelineGrailsPlugin.groovy
    private final String assetsPath = "${new File("").getAbsolutePath()}/grails-app/assets"

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
        if (relativePath.endsWith(BOWER_CSS)) {
            String lib = getLibrary(relativePath, BOWER_CSS)
            String version = getVersion(relativePath, BOWER_CSS)
            bowerDownloadService.downloadIfNeeded(lib, version)
            scanForFiles([], []).find { it instanceof BowerCssAssetFile && it.path.startsWith("$lib-$version") }
        } else if (relativePath.endsWith(BOWER_JS)) {
            String lib = getLibrary(relativePath, BOWER_JS)
            String version = getVersion(relativePath, BOWER_JS)
            bowerDownloadService.downloadIfNeeded(lib, version)
            scanForFiles([], []).find { it instanceof BowerJsAssetFile && it.path.startsWith("$lib-$version") }
        } else {
            File f = new File(bowerDownloadService.dirFile, relativePath)
            if (f.exists()) {
                return new GenericAssetFile(path: f.getPath(), inputStreamSource: { return new FileInputStream(f) })
            } else {
                return null
            }
        }
    }

    private static String getLibrary(String path, String suffix) {
        def arr = path.substring("null/".length(), path.size() - suffix.length()).split("-")
        arr[0]
    }

    private static String getVersion(String path, String suffix) {
        def arr = path.substring("null/".length(), path.size() - suffix.length()).split("-")
        if (arr.size() > 1) {
            arr[1]
        } else {
            "master"
        }
    }


    @Override
    AssetFile getAsset(String relativePath, String contentType) {
        return null
    }

    @Override
    AssetFile getAsset(String relativePath) {
        File f = new File(bowerDownloadService.dirFile, relativePath)
        if (f.exists()) {
            return new GenericAssetFile(path: relativePath, inputStreamSource: { return new FileInputStream(f) })
        } else {
            return null
        }
    }

    @Override
    List<AssetFile> getAssets(String basePath, String contentType, String extension, Boolean recursive, AssetFile relativeFile, AssetFile baseFile) {
        if (basePath.endsWith(BOWER_JS)) {
            File relative = new File(basePath)
            String parseFileName = relative.getName().replace(BOWER_JS, "")

            return bowerDownloadService.getFiles(parseFileName, 'js').collect { name, path ->
                new BowerJsAssetFile(path: name.replaceAll('.js$', ''), inputStreamSource: {
                    return new FileInputStream(path.toFile())
                })
            }
        } else if (basePath.endsWith(BOWER_CSS)) {
            File relative = new File(basePath)
            String parseFileName = relative.getName().replace(BOWER_CSS, "")

            return bowerDownloadService.getFiles(parseFileName, 'css').collect { name, path ->
                new BowerCssAssetFile(path: name.replaceAll('.css$', ''), inputStreamSource: {
                    return new FileInputStream(path.toFile())
                })
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
        // Preload all scripts
        Files.newDirectoryStream(Paths.get(assetsPath)).toList()
                .findAll { Files.isRegularFile(it) && it.toString().endsWith(BOWER) }.each { file ->
            file.readLines().each { line ->
                if (!line.startsWith("#") && !line.startsWith("//")) {
                    bowerDownloadService.downloadIfNeeded(line)
                }
            }
        }

        return bowerDownloadService.getAllAssets('').collect { name, path ->
            if (name.endsWith(".js")) {
                new BowerJsAssetFile(path: name.replaceAll('.js$', ''), inputStreamSource: {
                    return new FileInputStream(path.toFile())
                })
            } else if (name.endsWith(".css")) {
                new BowerCssAssetFile(path: name.replaceAll('.css$', ''), inputStreamSource: {
                    return new FileInputStream(path.toFile())
                })
            } else {
                new GenericAssetFile(path: name, inputStreamSource: { return new FileInputStream(path.toFile()) })
            }
        }
    }
}
