function initMap() {
    console.log("a")
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 18,
        center: {lat: lat, lng: lng},
        disableDefaultUI: true,
    });

    list.forEach(e => {
        let marker = new google.maps.Marker({
            position: {lat: parseFloat(e.location1), lng: parseFloat(e.location2)},
            map,
            title: e.name,
        });
        if (e.uuid == uuid) {
            contentString = "<span>"+e.name+"<br>"+e.info+"</span>"
            let infoWindow = new google.maps.InfoWindow({
                content: contentString,
                ariaLabel: e.name,
            });
            infoWindow.setPosition({lat: lat, lng: lng})
            infoWindow.setMap(map)
            map.panTo({lat: parseFloat(e.location1), lng: parseFloat(e.location2)})
        }
    })
}

function info(e) {
    let rand = getRand();
    $(".info").append("<div class='info_child "+rand+"'>"+e+"</div>")
    let temp = $("."+rand)
    temp.hide()
    temp.slideDown(300);

    setTimeout(() => {
        temp.slideUp(300, () => {
            temp.remove()
        })

    }, 4000)
}

const getRand = (len) => {
    return Math.random().toString().substring(2, 5)
}
