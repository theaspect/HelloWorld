package groovy.asset.pipeline.bower

import asset.pipeline.bower.BowerDownloadService
import static org.junit.Assert.assertEquals
import org.junit.Test

class BowerAssetResolverTest {

    BowerDownloadService bowerDownloadService = new BowerDownloadService(null)

    @Test
    void necessaryFileNameWithVersion(){
        assertEquals("angular-v1.5.3.bower.js", bowerDownloadService.necessaryFileName("angular", "v1.5.3"))
    }

    @Test
    void necessaryFileNameWithOutVersion(){
        assertEquals("angular.bower.js", bowerDownloadService.necessaryFileName("angular", "master"))
    }

    @Test
    void getLibraryName(){
        assertEquals(bowerDownloadService.getAssetNames("{\n" +
                "  \"name\": \"angular\",\n" +
                "  \"version\": \"1.5.3\",\n" +
                "  \"license\": \"MIT\",\n" +
                "  \"main\": \"./angular.js\",\n" +
                "  \"ignore\": [],\n" +
                "  \"dependencies\": {\n" +
                "  }\n" +
                "}"), "angular.js")
    }

    @Test
    void getGitUrlWithVersion(){
        assertEquals(bowerDownloadService.getGitUrl("v1.5.3", "{\"name\":\"angular\",\"url\":\"https://github.com/angular/bower-angular.git\"}"),
                "https://raw.githubusercontent.com/angular/bower-angular/v1.5.3")
    }

    @Test
    void getGitUrlWithOutVersion(){
        assertEquals(bowerDownloadService.getGitUrl("master", "{\"name\":\"angular\",\"url\":\"https://github.com/angular/bower-angular.git\"}"),
                "https://raw.githubusercontent.com/angular/bower-angular/master")
    }

    @Test(expected = Exception)
    void getGitUrlManyUrl (){
        bowerDownloadService.getGitUrl("master", "{\"name\":\"angular\",\"url\":\"https://github.com/angular/bower-angular.git\"}," +
                "{\"name\":\"angular\",\"url\":\"https://github.com/angular/bower-angular.git\"}")

    }
}
