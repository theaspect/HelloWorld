Bower Grails Asset pipeline
===========================

The bower-grails-asset-pipeline plugin allow you load javascript libraries directly from bower.

For more information on how to use asset-pipeline, visit [repo](https://github.com/theaspect/bower-grails-asset-pipeline).
You can search libraries in bower repository [bower](https://bower.io/search).

Usage
------

Add this plugin to `BuildConfig.groovy`:

```groovy
compile ":asset-pipeline:2.1.5"
compile ":bower-grails-asset-pipeline:0.1"
```

In any javascript asset you can specify library with version or latest available from repo.
Just add bower.js and it will be looked-up in bower repository:

```javascript
//= require angular-v1.5.3.bower.js
//= require underscore.bower.js
```

*NOTE:* If you do not specify version, it will download latest version every day. Version should be exactly the same as in git-repo tag.
