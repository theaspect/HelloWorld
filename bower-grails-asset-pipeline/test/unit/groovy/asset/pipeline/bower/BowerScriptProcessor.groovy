/*
* Copyright 2014 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package asset.pipeline.bower

import spock.lang.Specification

/**
**/
class BowerScriptProcessorSpec extends Specification {
	/*void "should compile bowerscript into js using rhino"() {
		given:
			def bowerScript = '''
				log = ->
					console.log "hello world"
			'''
			BowerScriptProcessor.NODE_SUPPORTED=false
			def processor = new BowerScriptProcessor()
		when:
			def output = processor.process(bowerScript, null)
		then:
			output.contains('console.log("hello world");')
	}

	void "should compile bowerscript into js using node"() {
		given:

			def bowerScript = '''
			log = ->
			console.log "hello world"
			'''
			BowerScriptProcessor.NODE_SUPPORTED=true
			def processor = new BowerScriptProcessor()
		when:
			def output = processor.process(bowerScript, null)
		then:
			output.contains('console.log("hello world");')
	}*/
}
