package asset.pipeline.bower

import groovy.json.JsonSlurper
import groovyjarjarcommonscli.*
import org.apache.log4j.Logger
import org.apache.log4j.LogManager

class BowerDownload {

    Logger logger = LogManager.getLogger(BowerDownload.class);

    def parseData
    def urlList = new ArrayList<Item>()
    String gitHubUserContent
    String fileName
    String dirFile
    String separator
    private boolean fileNotFound = true
    private boolean fileExists = false
    def startUrl

    public boolean isFileNotFound(){
        return fileNotFound
    }

    public boolean isFileExists(){
        return fileExists
    }

    BowerDownload() {

        this.parseData = null
        this.gitHubUserContent = "https://raw.githubusercontent.com"
        this.fileName = "bower.json"
        this.startUrl = "https://bower.herokuapp.com/packages/lookup/"
        this.separator = System.getProperty("file.separator")
        this.dirFile = System.getProperty("user.dir") + this.separator + "target" +  this.separator + "bowerJS" + this.separator
    }

    void parseUrl(String url) {

        if (url.charAt(0) != "[")
            url = '[' + url + ']'


        parseData = new JsonSlurper().parseText(url)
        parseData.each {
            aUrl ->
                def http = new Item(aUrl.name, aUrl.url)
                urlList.add(http)
        }
    }

    void conversionUrl(String version) {

        URL aURL = new URL(urlList[0].url)

        gitHubUserContent = gitHubUserContent + aURL.getPath().replaceAll("\\.git", "") + "/" + version

        try {
            parseData = new JsonSlurper().parseText((gitHubUserContent + '/' + fileName).toURL().text)
        }catch (FileNotFoundException e){
            fileNotFound = false
            return
        }


    }

    void downloadMainLibrary(String version) {

        String mainUrl = parseData.main

        String fileName = mainUrl.replaceAll("\\./", "")

        String newFileName
        def parseTest = new ArrayList()
        parseTest = fileName.split("\\.")

        if (version != "master") {
            newFileName = parseTest[0] + "-" + version + ".bower." + parseTest[1]
        } else {
            newFileName = parseTest[0] + ".bower." + parseTest[1]
        }

        newFileName = dirFile + newFileName

        if (!isFile(newFileName)) {
            //#  save to file  #//
            inFile(gitHubUserContent + '/' + fileName, newFileName)
        } else {
            fileExists = true
            return
        }


    }

    void inFile(String url, String fileName) {

        def file = new File(fileName).newOutputStream()
        file << new URL(url).openStream()
        file.close()
    }

    boolean isFile(String fileName) {

        boolean file = new File(fileName).exists()
        if (file) {
            File f = new File(fileName)
            if (f.length() == 0)
                file = false
        }
        return file

    }

    private static Option makeOptionWithArgument(String shortName, String description, boolean isRequired) {
        Option result = new Option(shortName, true, description);
        result.setArgs(1);
        result.setRequired(isRequired);

        return result;
    }

    static void printHelp(Options options) {
        final PrintWriter writer = new PrintWriter(System.out);
        final HelpFormatter helpFormatter = new HelpFormatter();

        helpFormatter.printHelp(
                writer,
                80,
                "[program]",
                "Options:",
                options,
                3,
                5,
                "-- HELP --",
                true);

        writer.flush();
    }

    public static int work(String[] args) {

        Options options = new Options()
                .addOption(makeOptionWithArgument("version", "Version", false))
                .addOption(makeOptionWithArgument("url", "Url", true))

        CommandLine commandLine = null;
        try {
            commandLine = new GnuParser().parse(options, args)
        } catch (ParseException e) {
            printHelp(options);
            return 255;
        }

        BowerDownload parseJson = new BowerDownload()
        File isDir = new File(parseJson.dirFile)
        isDir.mkdirs()
        def version
        def url

        if (commandLine.getOptionValue("version")) {
            version = commandLine.getOptionValue("version")
        } else {
            version = 'master'
        }

        try {
            url = (parseJson.startUrl + commandLine.getOptionValue("url")).toURL().text
        }catch (Exception e){
            parseJson.logger.error("Package not found: " + commandLine.getOptionValue("url"), e);
            return 1
        }
        //#  parse start url  #//

        parseJson.parseUrl(url)

        //#  conversion url  #//

        parseJson.conversionUrl(version)

        if(!parseJson.isFileNotFound()){
            parseJson.logger.error("File not found or incorrect version");
            return 2
        }

        //#  download main lib  #//

        parseJson.downloadMainLibrary(version)
        if(parseJson.isFileExists()){
            parseJson.logger.error("The file exists and it isn't empty");
            return 3
        }

        return 0
    }

    public static void main(String[] args) {
        System.exit(work(args))
    }

}