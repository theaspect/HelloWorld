package asset.pipeline.bower

import groovy.json.JsonSlurper
import org.apache.commons.logging.LogFactory

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Predicate

class BowerDownloadService {

    private static final log = LogFactory.getLog(BowerDownloadService)
    public static final String DEFAULT_VERSION = "master"
    public final String dirFile // target/bower/
    private static final String GIT_HUB_USER_CONTENT = "https://raw.githubusercontent.com"
    private static final String FILE_NAME = "bower.json"
    private static final String START_URL = "https://bower.herokuapp.com/packages/lookup/"

    private static final Long LIFE_TIME_CACHE_IN_HOURS = 24

    BowerDownloadService(String dirFile) {
        this.dirFile = dirFile
    }

    Map<String, Path> getFiles(String libName, String version, String extension = null) {
        String fileName = downloadIfNeeded(libName, version)

        LinkedHashMap<String, Path> files = getAllAssets(fileName, extension)

        return files
    }

    String downloadIfNeeded(String libName, String version) {
        //проверка существования пути
        File isDir = new File(this.dirFile)
        isDir.mkdirs()

        String fileName = necessaryFileName(libName, version)

        //verification of the file on existence, not emptiness and prescription
        if (!(isFileActual(new File(this.dirFile, fileName), version))) {
            String gitHubUserContent = getGitUrl(version, downloadUrl(START_URL + libName))
            List<String> libraryNames = getAssetNames(downloadUrl(gitHubUserContent + '/' + FILE_NAME))
            for (String lib : libraryNames) {
                def f = new File(dirFile, "$fileName/$lib")
                f.getParentFile().mkdirs()
                writeUrlToFile(gitHubUserContent + '/' + lib, f)
            }
        }
        fileName
    }

    LinkedHashMap<String, Path> getAllAssets(String fileName, extension = null) {
        // groovy isn't friendly with java8
        Map<String, Path> files = [:]
        if (Files.exists(Paths.get(this.dirFile, fileName))) {
            Files.walk(Paths.get(this.dirFile, fileName)).filter(new Predicate<Path>() {
                @Override
                boolean test(Path path) {
                    Files.isRegularFile(path) && ((extension == null) || path.toString().endsWith(".$extension"))
                }
            }).forEach(new Consumer<Path>() {
                @Override
                void accept(Path path) {
                    files[path.toString().substring(dirFile.length())] = path
                }
            })
        }
        files
    }

    /**
     * по имени библиотеки и версии получить имя файла
     */
    static String necessaryFileName(String fileName, String version) {
        if (version != DEFAULT_VERSION) {
            fileName = "$fileName-$version"
        } else {
            fileName = "$fileName-master"
        }
        return fileName
    }

    /**
     * Проверяем, что файл существуте и не старше LIFE_TIME_CACHE_IN_HOURS
     */
    static boolean isFileActual(File file, String version) {
        boolean fileExists = file.exists()
        if (fileExists) {
            long hours = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - new Date(file.lastModified()).getTime())
            if ((file.length() == 0) || ((hours > LIFE_TIME_CACHE_IN_HOURS) && (version == DEFAULT_VERSION))) {
                fileExists = false
            }
            log.debug("File ${file.getName()} exists, it isn't empty and not old")
        }
        return fileExists
    }

    /**
     * Преобразование бовер ссылки на гит
     */
    static String getGitUrl(String version, String text) {
        List<Item> urlList = parseText(text)
        if(urlList.size() > 1){
            throw new Exception()
        } else {
            URL aURL = new URL(urlList.first().url)
            String gitUrl = GIT_HUB_USER_CONTENT + aURL.getPath().replaceAll("\\.git", "") + "/" + version
            return gitUrl
        }

    }

    static List<Item> parseText(String text) {
        List<Item> urlList = []

        if (text.charAt(0) != "[")
            text = '[' + text + ']'

        def parseData = new JsonSlurper().parseText(text)
        parseData.each {
            aUrl ->
                def http = new Item(aUrl.name, aUrl.url)
                urlList.add(http)
        }
        return urlList
    }

    /**
     * Принимает на вход ссылку и записывает в стрингу содержимое
     */
    static String downloadUrl(String url) {
        try {
            return (url).toURL().text
        } catch (Exception e) {
            log.error("Package or file not found: $url", e)
            throw e
        }
    }

    /**
     * Parse Bower Response
     */

    static List<String> getAssetNames(String text) {
        def parseData = new JsonSlurper().parseText(text)
        List<String> fileNames
        switch (parseData.main) {
            case String:
                // Single js
                fileNames = [parseData.main.replaceAll("\\./", "")] as List<String>
                break
            case Collection:
                fileNames = parseData.main.collect { it.replaceAll("\\./", "") }
                break
            default:
                throw new IllegalStateException("Unknown type ${parseData.main}")
        }

        return fileNames
    }

    static void writeUrlToFile(String url, File file) {
        def fileOut = file.newOutputStream()
        fileOut << new URL(url).openStream()
        fileOut.close()
    }

}
