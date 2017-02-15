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

import asset.pipeline.AssetCompiler
import asset.pipeline.AssetFile
import asset.pipeline.DirectiveProcessor
import asset.pipeline.GenericAssetFile
import asset.pipeline.processors.AbstractUrlRewritingProcessor
import asset.pipeline.utils.net.Urls

import java.util.regex.Pattern

import static asset.pipeline.AssetHelper.*
import static asset.pipeline.utils.net.Urls.getSchemeWithColon
/**
 * This Processor iterates over relative image paths in a CSS file and
 * recalculates their path relative to the base file. In precompiler mode
 * the image URLs are also cache digested.
 *
 * @author David Estes
 * @author Ross Goldberg
 */
class BowerCssProcessor extends AbstractUrlRewritingProcessor {

    private static final Pattern URL_CALL_PATTERN = ~/url\((?:\s*)(['"]?)([a-zA-Z0-9\-_.:\/@#? &+%=]++)\1?(?:\s*)\)/


    BowerCssProcessor(final AssetCompiler precompiler) {
        super(precompiler)
    }


    String process(final String inputText, final AssetFile assetFile) {
        final Map<String, String> cachedPaths = [:]
        return \
            inputText.replaceAll(URL_CALL_PATTERN) { final String urlCall, final String quote, final String assetPath ->
                final String cachedPath = cachedPaths[assetPath]

                final String replacementPath
                if (cachedPath != null) {
                    replacementPath = cachedPath
                } else if (assetPath.size() > 0 && Urls.isRelative(assetPath)) {
                    replacementPath = replacementUrl(assetFile, assetPath, false)
                    if (replacementPath) {
                        cachedPaths[assetPath] = replacementPath
                    } else {
                        cachedPaths[assetPath] = assetPath
                        return urlCall
                    }
                } else {
                    return urlCall
                }

                return "url(${quote}${replacementPath}${quote})"
            }
    }

    /**
     * TODO Submit patch to Upstream make optimize optional
     */
    protected String replacementUrl(final AssetFile assetFile, final String url, boolean optimize = true) {
        final String schemeWithColon = getSchemeWithColon(url)

        final String urlSansScheme =
                schemeWithColon \
                ? url.substring(schemeWithColon.length())
                        : url

        final URL urlSplitter = new URL('http', 'hostname', urlSansScheme)

        final AssetFile baseFile = assetFile.baseFile ?: assetFile
        final AssetFile currFile =
                fileForFullName(
                        normalizePath(
                                assetFile.parentPath
                                        ? assetFile.parentPath + DIRECTIVE_FILE_SEPARATOR + urlSplitter.path
                                        : urlSplitter.path
                        )
                )

        if (! currFile) {
            return null
        }

        final StringBuilder replacementPathSb = new StringBuilder()

        // scheme (aka protocol)
        if (schemeWithColon) {
            replacementPathSb << schemeWithColon
        }

        if(optimize) {
            // relative parent path
            final String baseFileParentPath = baseFile.parentPath
            final String currFileParentPath = currFile.parentPath

            final List<String> baseRelativePath = baseFileParentPath ? baseFileParentPath.split(DIRECTIVE_FILE_SEPARATOR).findAll {
                it
            }.reverse() : []
            final List<String> currRelativePath = currFileParentPath ? currFileParentPath.split(DIRECTIVE_FILE_SEPARATOR).findAll {
                it
            }.reverse() : []

            int basePathIndex = baseRelativePath.size() - 1
            int currPathIndex = currRelativePath.size() - 1

            while (basePathIndex >= 0 && currPathIndex >= 0 && baseRelativePath[basePathIndex] == currRelativePath[currPathIndex]) {
                basePathIndex--
                currPathIndex--
            }

            // for each remaining level in the base path, add a ..
            for (; basePathIndex >= 0; basePathIndex--) {
                replacementPathSb << '..' << DIRECTIVE_FILE_SEPARATOR
            }

            for (; currPathIndex >= 0; currPathIndex--) {
                replacementPathSb << currRelativePath[currPathIndex] << DIRECTIVE_FILE_SEPARATOR
            }
        }else{
            replacementPathSb << currFile.parentPath << DIRECTIVE_FILE_SEPARATOR
        }

        // file
        final String fileName = nameWithoutExtension(currFile.name)
        if(precompiler && precompiler.options.enableDigests) {
            if(currFile instanceof GenericAssetFile) {
                replacementPathSb << fileName << '-' << currFile.getByteDigest() << '.' << extensionFromURI(currFile.name)
            } else {
                final String compiledExtension = currFile.compiledExtension
                replacementPathSb << fileName << '-' << getByteDigest(new DirectiveProcessor(currFile.contentType[0], precompiler).compile(currFile).bytes) << '.' << compiledExtension
            }
        } else {
            if(currFile instanceof GenericAssetFile) {
                replacementPathSb << currFile.name
            } else {
                replacementPathSb << fileName << '.' << currFile.compiledExtension
            }
        }

        // query
        if (urlSplitter.query != null) {
            replacementPathSb << '?' << urlSplitter.query
        }

        // fragment (aka reference; aka anchor)
        if (urlSplitter.ref) {
            replacementPathSb << '#' << urlSplitter.ref
        }

        return replacementPathSb.toString()
    }
}
