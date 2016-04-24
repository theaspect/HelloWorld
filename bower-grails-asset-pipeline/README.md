CoffeeScript Grails
=====================
The Grails `coffee-grails` is a plugin that provides coffee-script support for the asset-pipeline static asset management plugin.

For more information on how to use asset-pipeline, visit [here](http://www.github.com/bertramdev/asset-pipeline).


Usage
-----

Simply create files in your standard `assets/javascripts` folder with extension `.coffee` or `.js.coffee`. You also may require other files by using the following requires syntax at the top of each file:

```coffee
#= require test
#= require_self
#= require_tree .
```

*NOTE:* If the command line node coffee command is detected on your system. The application will attempt to use node to compile your javascript instead.