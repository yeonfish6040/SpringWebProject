const stsTool = (uuid) => {
    tools = {
        stop : () => {
            let query = new XMLHttpRequest();
            query.open("POST", "/admin/csts?uuid="+uuid+"&type=0")
            query.onload = () => {
                location.reload()
            }
            query.send()
        },
        idle : () => {
            let query = new XMLHttpRequest();
            query.open("POST", "/admin/csts?uuid="+uuid+"&type=1")
            query.onload = () => {
                location.reload()
            }
            query.send()
        },
        run : () => {
            let query = new XMLHttpRequest();
            query.open("POST", "/admin/csts?uuid="+uuid+"&type=2")
            query.onload = () => {
                location.reload()
            }
            query.send()
        }
    }

    return tools
}