webSocket = null;
isMsgOn = true;
chatList = new Object();
select = "";
waits = 0
function init() {
    // check permission
    if (pass != "true") {
        let verify = new XMLHttpRequest();
        verify.open("GET", "/get/verify?phone="+phone+"&zeroCount="+zeroCount+"&chain=true&step=2&code="+code+"&uuid="+uuid)
        verify.onload = (data) => {
            res = data.currentTarget.response
            if (res == "false" || rest == null) {
                history.back()
                location.href = "/admin/fail"
            }
        }
        verify.send()
    }

    cSts = stsTool(uuid)

    // websocket
    let isConnected = false
    webSocket = new WebSocket("ws://lyj.kr:8006")
    webSocket.onmessage = (e) => {
        if (!isConnected) {
            console.log(e.data)
            webSocket.send("verify|" + SHA256(e.data))
            isConnected = true
        }else {
            let data = e.data.split("|")
            if (data[0] == "evt") {
                console.log(data[1])
                if (data[1] == "lineUp" || data[1] == "deLineUp") {
                    webSocket.send("evt|waitsUpdate|"+uuid+"|"+waits)
                    updateUI()
                }
            }else if (data[0] == "msg") {
                handleMsg(JSON.parse(data[1]))
            }
        }
    }
    webSocket.onopen = () => { updateUI(); webSocket.send("connect|"+uuid) }

    $("#sendMsg").on("click", (e) => {
        if ($(".chat2Send").val() == "") return
        handleMsgSend(rest.name)
        $(".chat2Send").val("")
    })
    $(".chat2Send").on("keypress", (e) => {
        if ($(".chat2Send").val() == "") return
        var keyCode = e.keyCode || e.which;
        if (keyCode === 13) {
            handleMsgSend(rest.name)
            $(".chat2Send").val("")
        }
    })
}

function updateUI() {
    let getBookers = new XMLHttpRequest();
    getBookers.open("get", "/get/bookers?uuid="+uuid)
    getBookers.onload = (e) => {
        let data = e.currentTarget.response
        data = JSON.parse(data)
        waits = data.length
        webSocket.send("evt|waitsUpdate|"+uuid+"|"+waits)
        $(".window .ui .table .list").html("")
        data.forEach((e) => {
            $(".window .ui .table .list").append(
                "<tr>" +
                "   <td>"+e.waitNum+"</td>>" +
                "   <td>"+e.phone+"</td>" +
                "   <td><button class='btn btn-sm btn-outline-success in' id='in_"+e.uuid+"'>입장시키기</button></td>" +
                "   <td><button class='btn btn-sm btn-outline-danger out' id='out_"+e.uuid+"'>취소시키기</button></td>" +
                "</tr>")
        })
        $(".in").on("click", function (e) {
            let u_uuid = e.currentTarget.id.replace("in_", "")
            let sendPushAct = new XMLHttpRequest();
            sendPushAct.open("get", "/do/sendNotification?uuid="+u_uuid+"&r_uuid="+uuid+"&data="+rest.name+" - 고객님의 순번이 왔습니다!")
            sendPushAct.onload = (e) => {
                if (JSON.parse(e.currentTarget.responseText).success == true) {
                    info("알림전송 성공!")
                    let deLineUpAct = new XMLHttpRequest();
                    deLineUpAct.open("GET", "/do/deLineUp?uuid="+u_uuid+"&r_uuid="+uuid)
                    deLineUpAct.onload = (res) => {
                        info(res.currentTarget.responseText == "true" ? "예약이 제거되었습니다!" : "예약제거에 실패하였습니다.")
                        updateUI()
                    }
                    deLineUpAct.send()
                }else {
                    info("알림전송 실패")
                }
            }
            sendPushAct.send()
        })
        $(".out").on("click", function (e) {
            let u_uuid = e.currentTarget.id.replace("out_", "")
            let sendPushAct = new XMLHttpRequest();
            sendPushAct.open("get", "/do/sendNotification?uuid="+u_uuid+"&r_uuid="+uuid+"&data="+rest.name+" - 고객님의 예약이 반려되었습니다.")
            sendPushAct.onload = (e) => {
                if (JSON.parse(e.currentTarget.responseText).success == true) {
                    info("알림전송 성공!")
                    let deLineUpAct = new XMLHttpRequest();
                    deLineUpAct.open("GET", "/do/deLineUp?uuid="+u_uuid+"&r_uuid="+uuid)
                    deLineUpAct.onload = (res) => {
                        info(res.currentTarget.responseText == "true" ? "예약이 강제 취소되었습니다!" : "예약취소에 실패하였습니다.")
                        updateUI()
                    }
                    deLineUpAct.send()
                }else {
                    info("알림전송 실패")
                }
            }
            sendPushAct.send()
        })
    }
    getBookers.send()
}
function handleMsg(s) {
    let sender = s.phone
    let msgContent = s.msg
    console.log(chatList)
    if (!isMsgOn) {
        return webSocket.send("msg|{'destination': '" + sender + "', 'msg': '문의가 지원되지 않는 매점입니다!'}")
    } else {
        if (chatList[sender]) {
            chatList[sender].push([sender, msgContent])
        }else {
            chatList[sender] = [[sender, msgContent]]
        }
    }
    refreshMsgGroup()
}

function refreshMsgGroup() {
    $(".chat .list").html("")
    for (k in chatList) {
        $(".chat .list").append("<button class='btn btn-lg "+(k == select ? "btn-outline-primary" : "btn-outline-secondary")+" chatGroup' id='"+k+"'>"+k+"</button>")
        if (k == select) {
            let i = 0
            $(".chatText").html("")
            chatList[k].forEach(e => {
                $(".chatText").append("<div class='chatLine' id='chatLine_"+i+"'>"+e[0]+": "+e[1]+"</div>")
                if (i == chatList[k].length-2)
                    document.getElementById("chatLine_"+i).scrollIntoView()
                else if (i == chatList[k].length-1)
                    document.getElementById("chatLine_"+i).scrollIntoView({ behavior: 'smooth' })
                i++
            })
        }
    }
    $(".chatGroup").on("click", (e) => {
        if (select != "") {
            select = ""
            $(".chatText").html("")
            return refreshMsgGroup()
        }
        select = e.currentTarget.id
        $(".chat .list").html("")
        refreshMsgGroup()
    })
}

function handleMsgSend(name) {
    sendMsg(select, $(".chat2Send").val(), name)
    if (chatList[select]) {
        chatList[select].push([name, $(".chat2Send").val()])
    }else {
        chatList[select] = [[name, $(".chat2Send").val()]]
    }
    refreshMsgGroup()
}

function sendMsg(p, s, n) {
    webSocket.send('msg|'+p+'|{"name": "'+n+'", "msg": "'+s+'"}')
}