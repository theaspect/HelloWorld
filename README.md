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
compile ":asset-pipeline:2.7.2"
compile ":bower-asset-pipeline:0.3"
```

In any javascript asset you can specify library with version or latest available from repo.
Just add bower.js and it will be looked-up in bower repository:

```javascript
//= require jquery
//= require datatables.bower.js
//= require underscore-1.8.3.bower.js
```

*NOTE:* If you do not specify version, it will download latest version every day. Version should be exactly the same as in git-repo tag.
*NOTE:* In case of multiple js files, css'es and other assets only first js will be downloaded
