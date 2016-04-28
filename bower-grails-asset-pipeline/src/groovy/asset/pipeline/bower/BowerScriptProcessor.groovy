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

import asset.pipeline.AbstractProcessor
import asset.pipeline.AssetCompiler
import asset.pipeline.AssetFile
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable


class BowerScriptProcessor extends AbstractProcessor {

    static Boolean NODE_SUPPORTED
    Scriptable globalScope
    ClassLoader classLoader

    BowerScriptProcessor(AssetCompiler precompiler) {
        super(precompiler)
    }

    /**
     * Processes an input string from a given AssetFile implementation of bower and converts it to javascript
     * @param input String input bower script text to be converted to javascript
     * @param AssetFile instance of the asset file from which this file came from. Not actually used currently for this implementation.
     * @return String of compiled javascript
     */
    String process(String input, AssetFile assetFile) {

            /*try {
                def cx = Context.enter()
                def compileScope = cx.newObject(globalScope)
                compileScope.setParentScope(globalScope)
                compileScope.put("bowerScriptSrc", compileScope, input)
                def result = cx.evaluateString(compileScope, "BowerScript.compile(bowerScriptSrc)", "BowerScript compile command", 0, null)
                return result
            } catch (Exception e) {
                throw new Exception("""
				BowerScript Engine compilation of coffeescript to javascript failed.
				$e
				""")
            } finally {
                Context.exit()
            }*/

    }

}
