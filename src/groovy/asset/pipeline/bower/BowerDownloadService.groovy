package asset.pipeline.bower

import groovy.json.JsonSlurper
import org.apache.commons.logging.LogFactory
import java.util.concurrent.TimeUnit

class BowerDownloadService {

    private static final log = LogFactory.getLog(BowerDownloadService);
    public static final String DEFAULT_VERSION = "master"
    public static final String FILE_EXTENSION = ".bower.js"
    private final String dirFile
    private static final String GIT_HUB_USER_CONTENT = "https://raw.githubusercontent.com"
    private static final String FILE_NAME = "bower.json"
    private static final String START_URL = "https://bower.herokuapp.com/packages/lookup/"

    private static final def LIFE_TIME_CACHE_IN_HOURS = 24

    BowerDownloadService(String dirFile) {
        this.dirFile = dirFile
    }

    public InputStream getLibrary(String libName, String version) {
        //проверка существования пути
        File isDir = new File(this.dirFile)
        isDir.mkdirs()

        String fileName = necessaryFileName(libName, version)

        //verification of the file on existence, not emptiness and prescription
        if (!(isFileActual(new File(this.dirFile, fileName), version))) {
            String gitHubUserContent = getGitUrl(version, downloadUrl(START_URL + libName))
            String libraryName = getLibraryName(downloadUrl(gitHubUserContent + '/' + FILE_NAME))
            writeUrlToFile(gitHubUserContent + '/' + libraryName, new File(dirFile, fileName))
        }
        return new FileInputStream(new File(this.dirFile, fileName))
    }

    /**
     * по имени библиотеки и версии получить имя файла
     */
    String necessaryFileName(String fileName, String version){
        if (version != DEFAULT_VERSION) {
            fileName = fileName + "-" + version + FILE_EXTENSION
        } else {
            fileName = fileName + FILE_EXTENSION
        }
        return fileName
    }

    /**
     * Проверяем, что файл существуте и не старше LIFE_TIME_CACHE_IN_HOURS
     */
    boolean isFileActual(File file, String version) {
        boolean fileExists = file.exists()
        if (fileExists) {
            long hours = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - new Date(file.lastModified()).getTime())
            if ((file.length() == 0) || ((hours > LIFE_TIME_CACHE_IN_HOURS) && (version == DEFAULT_VERSION))) {
                fileExists = false
            }
            log.error("File ${file.getName()} exists, it isn't empty and not old");
        }
        return fileExists
    }

    /**
     * Преобразование бовер ссылки на гит
     */
    String getGitUrl(String version, String text) {
        List<Item> urlList = parseText(text)
        if(urlList.size() > 1){
            throw new Exception()
        } else {
            URL aURL = new URL(urlList.first().url)
            String gitUrl = GIT_HUB_USER_CONTENT + aURL.getPath().replaceAll("\\.git", "") + "/" + version
            return gitUrl
        }

    }

    List<Item> parseText(String text) {
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
    String downloadUrl(String url){
        try {
            return (url).toURL().text
        } catch (Exception e) {
            log.error("Package or file not found: $url", e);
            throw e
        }
    }

    /**
     * Parse Bower Response
     */

    String getLibraryName(String text) {
        def parseData = new JsonSlurper().parseText(text)
        String fileName = parseData.main.replaceAll("\\./", "")
        return fileName
    }

    static void writeUrlToFile(String url, File file) {
        def fileOut = file.newOutputStream()
        fileOut << new URL(url).openStream()
        fileOut.close()
    }

}