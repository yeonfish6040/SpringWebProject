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
        }else {
            console.log(e.data)
        }
    }
    webSocket.onopen = () => webSocket.send("connect|"+uuid)
}