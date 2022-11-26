rests = null;
page = 1
pageTotel = null

function init() {
    navigator.geolocation.getCurrentPosition(getRestaurants, error, {enableHighAccuracy: true})
}

function getRestaurants(pos) {
    let crd = pos.coords
    let lat = crd.latitude
    let lon = crd.longitude
    let acc = crd.accuracy

    console.log(lat)
    console.log(lon)

    $("#loc1").val(lat);
    $("#loc2").val(lon);

    currentAddress = new XMLHttpRequest()
    currentAddress.open("GET", "https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lon+"&sensor=true&key=AIzaSyAexLg6oFaOmd99G_jytyHpmlKi1pmnxwg")
    currentAddress.onload = (e) => {
        res = JSON.parse(e.currentTarget.response)
        document.title = "Waiter - "+res.results[4].address_components[0].long_name
        $(".currentPosition").text(res.results[4].formatted_address)
    }
    currentAddress.send()


    getRest = new XMLHttpRequest();
    getRest.open("get", "/get/restaurant/"+lat+"/"+lon)
    getRest.onload = (e) => {
        res = JSON.parse(e.currentTarget.response)
        this.rests = res;
        this.pageTotel = res.length%4 != 0 ? res.length/4+1 : res.length/4
        res = res.slice(0, 4)
        res.forEach(e => {
            $(".rest_lists").append(
                "<div class='rest "+e.uuid+"'>" +
                "   <img src='/img/"+e.pictures+"' class='rest_img'>" +
                "   <span class='info'>" +
                "       <div class='name' id='"+e.uuid+"'>"+e.name+"</div>" +
                "   </span>" +
                "</div>" +
                "<hr>"
            )
        })

        $(".rest .info .name").on("click", toMap)

        for (i=0;i<pageTotel;i++) {
            $(".numBar").append("<span class='icon num'>"+(i+1)+"</span>")
            $(".icon.num").on("click", changePage)
        }
    }
    getRest.send()
}

function toMap(e) {
    uuid = e.currentTarget.id
    $("#toMap #uuid").val(uuid)
    $("#toMap").submit()
}

function changePage(e) {
    page = parseInt(e.currentTarget.innerText)
    console.log(page)
    console.log((page*4)-4, (page*4))
    $(".rest_lists").html("")
    res = rests
    res = res.slice((page*4)-4, (page*4))
    res.forEach(e => {
        $(".rest_lists").append(
            "<div class='rest "+e.uuid+"'>" +
            "   <img src='/img/"+e.pictures+"' class='rest_img'>" +
            "   <span class='info'>" +
            "       <div class='name'>"+e.name+"</div>" +
            "   </span>" +
            "</div>" +
            "<hr>"
        )
    })
}


function error(e) {
    console.error(e)
}