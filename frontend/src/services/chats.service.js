import axios from 'axios'
import authHeader from '../api/authHeader'
import {API_URL} from '../config'

const headersConfig = authHeader();
const getAllChats = () => {
    return axios.post(`${API_URL}/v1/chat/get`, {}, { headers: headersConfig });
};

const createChat = (chatname, userlimit) => {
    const data = {
        userLimit: (userlimit>255)?2:userlimit,
        chatName: (chatname==='')?"Chat":chatname
    };
    return axios.post(`${API_URL}/v1/chat/create`, data, { headers: headersConfig });
};
const ChatService = {
    getAllChats,
    createChat
}

export default ChatService;