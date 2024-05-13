export default function authHeader() {
    const user = JSON.parse(localStorage.getItem("user"));
    console.log("test"+user+" - ")

    if (user && user.token) {
        return { Authorization: 'Bearer ' + user.token };
    } else {
        console.log("error with getting the auth Token");
        return {};
    }
}
