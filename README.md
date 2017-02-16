Bower Grails Asset pipeline
===========================

The bower-grails-asset-pipeline plugin allow you load javascript libraries directly from bower.
You required to install [asset-pipeline](https://github.com/bertramdev/asset-pipeline) plugin.

For more information on how to use asset-pipeline, visit [repo](https://github.com/theaspect/grails-bower-asset-pipeline).
You can search libraries in bower repository [bower](https://bower.io/search).

Usage
------

Add this plugin to `BuildConfig.groovy`:

```groovy
compile ":asset-pipeline:2.13.1"
compile ":bower-asset-pipeline:0.4"
```

In any javascript asset you can specify library with version or latest available from repo.
Just add bower.js and it will be looked-up in bower repository:

```javascript
//= require jquery
//= require datatables.bower.js
//= require underscore-1.8.3.bower.js
```

Same for CSS
```css
/*
*= require datatables.bower.css
*/
```
If bower repository contains not only js and css you want to inline, but additional files like png,
you have to create anyname.bower file in grails/assets folder:
```
# Specify library or library-version
datatables
underscore-1.8.3
```

*NOTE:* If you do not specify version, it will download latest version every day. Version should be exactly the same as in git-repo tag.
*NOTE:* In case of multiple js files, css'es and other assets only first js will be inlined

Versions
--------
##0.4

Now you can inject js, css files and additional resources

##0.3

Support complex packages, allows only one js though

##0.2

First public release, allow download single js library

##0.1

Initial release
