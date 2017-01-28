import asset.pipeline.AssetPipelineConfigHolder
import asset.pipeline.bower.BowerAssetResolver

class BowerAssetPipelineGrailsPlugin {
    def version = "0.3"
    def grailsVersion = "2.4 > *"
    def title = "Bower Asset-Pipeline Plugin"
    def description = "Provides bower support for the asset-pipeline static asset management plugin."
    def license = "APACHE"

    def organization = []
    def developers = [
            [name: 'Valery Butuzov', email: 'valera-bapbap56@mail.ru'],
            [name: 'Constantine Linnick', email: 'theaspect@gmail.com']
    ]

    def documentation = "https://github.com/theaspect/grails-bower-asset-pipeline"
    def issueManagement = [ system: "GITHUB", url: "https://github.com/theaspect/grails-bower-asset-pipeline/issues" ]
    def scm = [ url: "https://github.com/theaspect/grails-bower-asset-pipeline" ]

    def doWithApplicationContext = { ctx ->
        AssetPipelineConfigHolder.registerResolver(new BowerAssetResolver())
    }
}
