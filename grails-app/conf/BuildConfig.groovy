grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

	inherits 'global'
	log 'all'

	repositories {
		grailsCentral()
		mavenCentral()
	}

	dependencies {
		test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
	}
	plugins {
		runtime ":asset-pipeline:2.7.2"

		build ':release:3.1.2', ':rest-client-builder:2.0.1', {
            export = false
        }
		test(":spock:0.7") {
			exclude "spock-grails-support"
		}
	}
}
