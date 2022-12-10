verifing = false
adminClick = {}

function init() {
    info("로딩 완료!")
    if (feedback != "") {
        info(feedback)
    }
    $("#searchRun").on("click", search)
    $(document).on("click", function (e) {
        if (e.currentTarget.activeElement.className.includes("book")) {
            book(e)
        }
    })
    window.addEventListener("keydown", function(event) {
        if (event.key == "Escape"){
            location.href = "/"
        }
    });

    if (Notification.permission !== 'granted'){
        Notification.requestPermission();
        info("내차례 알림을 받고싶으시다면,<br>알림을 허용해주시기 바랍니다.")
    }



}

function initMap() {
    let myStyles =[
        {
            featureType: "poi",
            elementType: "labels",
            stylers: [
                { visibility: "off" }
            ]
        }
    ];

    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 18,
        center: {lat: lat, lng: lng},
        disableDefaultUI: true,
        styles: myStyles,
    });

    list.forEach(e => {
        adminClick[e.uuid] = 0
        let marker = new google.maps.Marker({
            position: {lat: parseFloat(e.location1), lng: parseFloat(e.location2)},
            map,
            title: e.name,
            // label: e.name,
        });
        let addr;
        getAddr(lat, lng, (data) => {
            data = JSON.parse(data.currentTarget.response)
            let addr = data.results[4].formatted_address
            contentString = "<span>" +
                "<span class='h6 restTitle' id='rest_"+e.uuid+"'>" + e.name+"</span> - <button class='book btn btn-sm btn-outline-secondary' id='"+e.uuid+"'>예약하기</button>" +
                "<br> " + addr +
                "<br>"+e.call.replace(/\D+/g, '')
                    .replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3')
                    .replace(/(\d{2})(\d{4})(\d{4})/, '$1-$2-$3')
                    .replace(/(\d{2})(\d{3})(\d{4})/, '$1-$2-$3')
                    .replace(/(\d{4})(\d{4})/, '$1-$2')+
                " - "+e.waits+"명 대기중, "+Math.floor((e.c_wait_time*e.waits)/60)+"시간 "+(e.c_wait_time*e.waits)%60+"분 예상</span>"
            let infoWindow = new google.maps.InfoWindow({
                content: contentString,
                ariaLabel: e.name,
            });
            if (e.uuid == uuid) {
                infoWindow.open({
                    anchor: marker,
                })
                $(".book").on("click", book)
                map.panTo({lat: parseFloat(e.location1), lng: parseFloat(e.location2)})
            }
            $(".restTitle").on("click", function (evt) {
                evt_uuid = evt.currentTarget.id.replace("rest_", "")
                if (adminClick[evt_uuid] == NaN) {
                    adminClick[evt_uuid] = 0
                }else{
                    adminClick[evt_uuid] += 1
                }
                if (!adminClick[evt_uuid+"_time"]) {
                    adminClick[evt_uuid+"_time"] = new Date().getTime()
                }else {
                    if (new Date().getTime() > adminClick[evt_uuid+"_time"]+1000) {
                        adminClick[evt_uuid+"_time"] = new Date().getTime()
                        adminClick[evt_uuid] = 1
                    }else {
                        if (adminClick[evt_uuid] == 5) {
                            verifyAdmin(evt)
                        }
                    }
                }
            })
            marker.addListener("click", () => {
                infoWindow.open({
                    anchor: marker,
                })
                setTimeout(() => {
                    $(".restTitle").on("click", function (evt) {
                        evt_uuid = evt.currentTarget.id.replace("rest_", "")
                        if (adminClick[evt_uuid] == NaN) {
                            adminClick[evt_uuid] = 0
                        }else{
                            adminClick[evt_uuid] += 1
                        }
                        if (!adminClick[evt_uuid+"_time"]) {
                            adminClick[evt_uuid+"_time"] = new Date().getTime()
                        }else {
                            if (new Date().getTime() > adminClick[evt_uuid+"_time"]+1000) {
                                adminClick[evt_uuid+"_time"] = new Date().getTime()
                                adminClick[evt_uuid] = 1
                            }else {
                                if (adminClick[evt_uuid] == 5) {
                                    verifyAdmin(evt_uuid)
                                }
                            }
                        }
                    })
                }, 100)
            })
        })
    })
}

function search() {
    let text = $(".search").val()
    location.href = "/search?q="+text
}

function book(e) {
    if (!verifing) {
        verifing = true
        let uuid = e.currentTarget.activeElement.id;
        let tel = info("<input type='tel' class='form-control phone' id='phone' placeholder='사용자의 휴대폰번호'><button id='phone_submit' class='btn btn-outline-info'>예약</button>", { manual: true })
        $("#phone_submit").on("click", () => {
            if (!isNaN($("#phone").val()) && $("#phone").val() != "") {
                $("#phone_submit").attr("disabled", true);
                let phone = $("#phone").val();
                tel()
                let user_uuid;
                let getUser = new XMLHttpRequest();
                getUser.open("get", "/get/user?phone="+phone)
                getUser.onload = (data) => user_uuid = data.currentTarget.responseText
                getUser.send()
                let getVerifyCode = new XMLHttpRequest();
                let zeroCount = 0
                for (e in phone.split("")) {
                    if (e == 0) {
                        zeroCount += 1
                    }else {
                        break
                    }
                }
                let verifyCode
                getVerifyCode.open("GET", "/get/verify?phone="+phone+"&zeroCount="+zeroCount+"&chain=false")
                getVerifyCode.onload = (data) => verifyCode = data.currentTarget.responseText;
                getVerifyCode.send()
                let verify = info("<input type='password' class='form-control verify' id='verify' placeholder='인증번호'><button id='verify_submit' class='btn btn-outline-success'>인증</button>", { manual: true });
                $("#verify_submit").on("click", () => {
                    if (SHA256($("#verify").val()) == verifyCode) {
                        $("#verify_submit").attr("disabled", true);
                        verify()
                        info("인증 성공.")
                        verifing = false;
                        let bookRun = new XMLHttpRequest();
                        bookRun.open("get", "/do/book?r_uuid="+uuid+"&uuid="+user_uuid)
                        bookRun.onload = (data) => {
                            res = data.currentTarget.responseText
                            if (res >= 0 && !isNaN(res)) {
                                info("예약번호: "+res+"<br>"+"예약에 성공하셨습니다.")
                            }else {
                                if (res == -1) {
                                    info("예약실패<br>알 수 없는 오류로 인하야 예약에 실패하였습니다.")
                                }else if (res == -2) {
                                    let DLWindow = info("예약실패<br>이미 예약이 되어있습니다. <button id='deLineUp_"+uuid+"' class='btn btn-outline-success'>예약취소</button>", { manual: true })
                                    $("#deLineUp_"+uuid).on("click", function() {
                                        $("#deLineUp_"+uuid).attr("disabled", true);
                                        deLineUp(user_uuid, uuid, DLWindow)
                                    })
                                }else if (res == -3) {
                                    info("인증실패<br>사용자의 인증요청이 피어에 의해 거부당했습니다.")
                                }
                            }
                        }
                        bookRun.send()
                    }else {
                        info("인증번호가 매치되지 않습니다.")
                    }
                })
            }else {
                info("형식이 맞지 않습니다.")
            }
        })
    }
}

function verifyAdmin(e) {
    if (verifing) {
        return false;
    }
    verifing = true
    let uuid = e;
    let tel = info("<input type='tel' class='form-control phone' id='phone' placeholder='사용자의 휴대폰번호'><button id='phone_submit' class='btn btn-outline-info'>접속</button>", { manual: true })
    $("#phone_submit").on("click", () => {
        if (!isNaN($("#phone").val()) && $("#phone").val() != "") {
            $("#phone_submit").attr("disabled", true);
            let phone = $("#phone").val();
            tel()
            let user_uuid;
            let getVerifyCode = new XMLHttpRequest();
            let verifyCode
            let zeroCount = 0
            for (e in phone.split("")) {
                if (e == 0) {
                    zeroCount += 1
                }else {
                    break
                }
            }
            getVerifyCode.open("GET", "/get/verify?phone="+phone+"&zeroCount="+zeroCount+"&chain=true&step=1")
            getVerifyCode.send()
            let verify = info("<input type='password' class='form-control verify' id='verify' placeholder='인증번호'><button id='verify_submit' class='btn btn-outline-success'>인증</button>", { manual: true });
            $("#verify_submit").on("click", () => {
                location.href = "/admin?uuid="+this.uuid+"&code="+$("#verify").val()+"&phone="+phone+"&zeroCount="+zeroCount
            })
        }else {
            info("형식이 맞지 않습니다.")
        }
    })
}


function deLineUp(uuid, r_uuid, deLineUpWindow) {
    deLineUpWindow()
    let deLineUpAct = new XMLHttpRequest();
    deLineUpAct.open("GET", "/do/deLineUp?uuid="+uuid+"&r_uuid="+r_uuid)
    deLineUpAct.onload = (res) => {
        info(res.currentTarget.responseText == "true" ? "예약이 취소되었습니다!" : "예약취소에 실패하였습니다.")
    }
    deLineUpAct.send()
}

function pauseEvent(e){
    if(e.stopPropagation) e.stopPropagation();
    if(e.preventDefault) e.preventDefault();
    e.cancelBubble=true;
    e.returnValue=false;
    return false;
}