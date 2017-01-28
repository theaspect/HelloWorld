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

import asset.pipeline.AbstractAssetFile

import java.util.regex.Pattern

/**
* Specification for the Bower file extension which compiles into javascript
*/
class BowerJsAssetFile extends AbstractAssetFile {
	static final contentType = ['application/javascript','application/x-javascript','text/javascript']
	static extensions = ['bower.js']
	static final String compiledExtension = 'js'
	static processors = []
	Pattern directivePattern = ~/(?m)#=(.*)/
}
