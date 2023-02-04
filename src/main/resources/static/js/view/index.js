verifing = false
adminClick = {}
ws = null
chatList = []

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
    $(document).on("click", function (e) {
        if (e.currentTarget.activeElement.className.includes("ask")) {
            openMsg(e)
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

    ws = new WebSocket("wss://lyj.kr:8006")
    ws.onmessage = (e) => {
        data = e.data.split("|")
        if (data[0] == "msg") {
            handleMsg(JSON.parse(data[1]))
        }else if (data[0] == "evt") {
            if (data[1] = "waitsUpdate") {
                $(".waits."+data[2]).text(data[3])
            }
        }
    }
    ws.onopen = () => {
        ws.send("connect|basic")
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
                "<span class='h6 restTitle' id='rest_"+e.uuid+"'>" + e.name+"</span> - " +
                "<span class='btn-group title_btn' role='group' "+(e.active != 2 ? (e.active == 1 ? "title='쉬는중...'" : "title='영업종료'") : "")+">" +
                "<button class='book btn btn-sm btn-outline-secondary' id='"+e.uuid+"' "+((e.active != 2) ? "disabled" : "")+">예약하기</button>" +
                "<button class='ask btn btn-sm btn-outline-secondary' id='ask_"+e.uuid+"' "+((e.active != 2) ? "disabled" : "")+">문의하기</button>" +
                "</span>" +
                "<br> " + addr +
                "<br>"+e.call.replace(/\D+/g, '')
                    .replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3')
                    .replace(/(\d{2})(\d{4})(\d{4})/, '$1-$2-$3')
                    .replace(/(\d{2})(\d{3})(\d{4})/, '$1-$2-$3')
                    .replace(/(\d{4})(\d{4})/, '$1-$2')+
                " - <span class='waits "+e.uuid+"'>"+e.waits+"</span>명 대기중, "+Math.floor((e.c_wait_time*e.waits)/60)+"시간 "+(e.c_wait_time*e.waits)%60+"분 예상</span>"
            let infoWindow = new google.maps.InfoWindow({
                content: contentString,
                ariaLabel: e.name,
            });
            if (e.uuid == uuid) {
                infoWindow.open({
                    anchor: marker,
                })
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
    let user_uuid;
    let uuid = e.currentTarget.activeElement.id;
    verify((phone) => {
        let getUser = new XMLHttpRequest();
        getUser.open("get", "/get/user?phone="+phone)
        getUser.onload = (data) => {
            user_uuid = data.currentTarget.responseText
            let bookRun = new XMLHttpRequest();
            bookRun.open("get", "/do/book?r_uuid="+uuid+"&uuid="+user_uuid+"&endpoint="+subscriptionGlobal.endpoint+"&p256dh="+subscriptionGlobal.p256dh+"&auth="+subscriptionGlobal.auth)
            bookRun.onload = (data) => {
                res = data.currentTarget.responseText
                if (res >= 0 && !isNaN(res)) {
                    info("예약번호: "+res+"<br>"+"예약에 성공하셨습니다.")
                    ws.send("evt|"+uuid+"|lineUp")
                }else {
                    if (res == -1) {
                        info("예약실패<br>알 수 없는 오류로 인하야 예약에 실패하였습니다.")
                    }else if (res == -2) {
                        let DLWindow = info("예약실패<br>이미 예약이 되어있습니다. <button id='deLineUp_"+uuid+"' class='btn btn-outline-success'>예약취소</button>", { manual: true })
                        $("#deLineUp_"+uuid).on("click", function() {
                            $("#deLineUp_"+uuid).attr("disabled", true);
                            deLineUp(user_uuid, uuid, DLWindow)
                        })
                    // }else if (res == -3) {
                        info("인증실패<br>사용자의 인증요청이 피어에 의해 거부당했습니다.")
                    }
                }
            }
            bookRun.send()
        }
        getUser.send()
    })
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
        ws.send("evt|"+r_uuid+"|deLineUp")
    }
    deLineUpAct.send()
}

function openMsg(e) {
    let uuid = e.currentTarget.activeElement.id.replace("ask_", "")
    verify((phone) => {
        ws.send("connect|"+phone)
        $(".chat").fadeIn()
        $("#sendMsg").on("click", (e) => {
            if ($(".chat2Send").val() == "") return
            sendMsg(uuid, $(".chat2Send").val(), phone)
            chatList.push([phone, $(".chat2Send").val()])
            refreshMsgGroup()
            $(".chat2Send").val("")
        })
        $(".chat2Send").on("keypress", (e) => {
            if ($(".chat2Send").val() == "") return
            var keyCode = e.keyCode || e.which;
            if (keyCode === 13) {
                sendMsg(uuid, $(".chat2Send").val(), phone)
                chatList.push([phone, $(".chat2Send").val()])
                refreshMsgGroup()
                $(".chat2Send").val("")
            }
        })
    })
}

function pauseEvent(e){
    if(e.stopPropagation) e.stopPropagation();
    if(e.preventDefault) e.preventDefault();
    e.cancelBubble=true;
    e.returnValue=false;
    return false;
}

function sendMsg(u, s, p) {
    ws.send('msg|'+u+'|{"phone": "'+p+'", "msg": "'+s+'"}')
}

function verify(callback) {
    let isOk = false
    if (!verifing) {
        verifing = true
        let tel = info("<input type='tel' class='form-control phone' id='phone' placeholder='사용자의 휴대폰번호'><button id='phone_submit' class='btn btn-outline-info'>전송</button>", { manual: true })
        $("#phone_submit").on("click", () => {
            if (!isNaN($("#phone").val()) && $("#phone").val() != "") {
                $("#phone_submit").attr("disabled", true);
                let phone = $("#phone").val();
                tel()
                let zeroCount = 0
                for (e in phone.split("")) {
                    if (e == 0) {
                        zeroCount += 1
                    }else {
                        break
                    }
                }
                let getVerifyCode = new XMLHttpRequest();
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
                        callback(phone)
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

function handleMsg(s) {
    let sender = s.name
    let msgContent = s.msg
    chatList.push([sender, msgContent])
    refreshMsgGroup()
}

function refreshMsgGroup() {
    $(".chat .chatText").html("")
    let i = 0
    chatList.forEach((e) => {
        $(".chatText").append("<div class='chatLine' id='chatLine_"+i+"'>"+e[0]+": "+e[1]+"</div>")
        if (i == chatList.length-2)
            document.getElementById("chatLine_"+i).scrollIntoView()
        else if (i == chatList.length-1)
            document.getElementById("chatLine_"+i).scrollIntoView({ behavior: 'smooth' })
        i++
    })
}