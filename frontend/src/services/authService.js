import axios from 'axios';
import CONFIG from '../config';
import authHeader from '../api/authHeader';

const register = (email, password, username) => {
    console.log("reg1")
    const req = axios.post(CONFIG.API_URL+"/auth/register", {
        email,
        password,
        username
    }).then((response) => {
        if (response.data.token) {
            console.log("reg2")
            localStorage.setItem("user", JSON.stringify(response.data));
        }
        console.log("reg3")
        return response.data;
    });
    console.log(req);
    return req;
}

const login = (email, password) => {
    return axios.post(CONFIG.API_URL+"/auth/login", {
        email,
        password,
    }).then((response) => {
        if (response.data.token) {
            localStorage.setItem("user", JSON.stringify(response.data));
        }
        return response.data;
    });
}

const logout = () => {
    localStorage.removeItem("user");
}

const getCurrentUser = () => {
    return JSON.parse(localStorage.getItem("user"));
}

const isLoggedIn = () => {
    const headersConfig = authHeader(); // Call authHeader() to get the actual headers configuration
    return axios.get(API_URL + "/verified", { headers: headersConfig })
        .then((response) => {
            return response.status === 200;
        })
        .catch((error) => {
            return false;
        });
}

const authService = {
    register,
    login,
    logout,
    isLoggedIn,
    getCurrentUser
}

export default authService;
