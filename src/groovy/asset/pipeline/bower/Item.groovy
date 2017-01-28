package asset.pipeline.bower

class Item {
    String name
    String url

    Item(name, url){
        this.name = name
        this.url = url
    }

    String toString() {
        return "Name: " + this.name + " " + "Url: " + this.url +  " "
    }
}
