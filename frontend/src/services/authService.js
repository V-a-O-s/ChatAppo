import axios from 'axios'
import { API_URL } from '../config'

const register = (email, password, username) => {
    return axios.post(API_URL+"/auth/register", {
        email,
        password,
        username
    }).then((response) => {
        if (response.data.token) {
            localStorage.setItem("user", JSON.stringify(response.data));
        }

        return response.data;
    });
}

const login = (email, password) => {
    return axios.post(API_URL+"/auth/login", {
        email,
        password,
    }).then((response) => {
        if (response.data.token) {
            localStorage.setItem("user", JSON.stringify(response.data));
        }

        return response.data;
    });
}

const logout = (email, password, username) => {
    localStorage.removeItem("user")
}

const getCurrentUser = () => {
    return JSON.parse(localStorage.getItem(user))
}

const authService = {
    register,
    login,
    logout,
    getCurrentUser
}

export default authService;