import axios from 'axios'
import authHeader from '../api/authHeader'
import {API_URL} from '../config'

const getAllChats = () => {
    const headersConfig = authHeader();
    console.log('Sending headers:', headersConfig); // Check what headers are actually being sent
    return axios.post(`${API_URL}/v1/chat/get`, {}, { headers: headersConfig });
};

const ChatService = {
    getAllChats
}

export default ChatService