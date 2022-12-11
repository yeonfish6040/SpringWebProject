webSocket = null;

function init() {
    // check permission
    if (pass != "true") {
        let verify = new XMLHttpRequest();
        verify.open("GET", "/get/verify?phone="+phone+"&zeroCount="+zeroCount+"&chain=true&step=2&code="+code)
        verify.onload = (data) => {
            res = data.currentTarget.response
            if (res == "false") {
                history.back()
                location.href = "/admin/fail"
            }
        }
        verify.send()
    }

    cSts = stsTool(uuid)

    // websocket
    let isConnected = false
    webSocket = new WebSocket("ws://lyj.kr:8005")
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
                    updateUI()
                }
            }else if (data[0] == "msg") {
                console.log(JSON.parse(data[1]))
            }
        }
    }
    webSocket.onopen = () => webSocket.send("connect|"+uuid)

    updateUI()
}

function updateUI() {
    let getBookers = new XMLHttpRequest();
    getBookers.open("get", "/get/bookers?uuid="+uuid)
    getBookers.onload = (e) => {
        let data = e.currentTarget.response
        data = JSON.parse(data)
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
            let deLineUpAct = new XMLHttpRequest();
            deLineUpAct.open("GET", "/do/deLineUp?uuid="+u_uuid+"&r_uuid="+uuid)
            deLineUpAct.onload = (res) => {
                info(res.currentTarget.responseText == "true" ? "예약이 취소되었습니다!" : "예약취소에 실패하였습니다.")
                updateUI()
            }
            deLineUpAct.send()
        })
    }
    getBookers.send()
}