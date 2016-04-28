grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenCentral()
	}

	dependencies {

	}
	plugins {

		runtime ":asset-pipeline:2.7.2"

		build ':release:3.1.2', ':rest-client-builder:2.0.1', {
            export = false
        }
	}
}
