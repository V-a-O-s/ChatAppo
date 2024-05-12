import axios from 'axios';
import authHeader from '../api/authHeader';
import CONFIG from '../config';

const getMessages = (chatId) => {
    if (chatId==null) {
        return;
    }
    const headersConfig = authHeader();
    return axios.get(`${CONFIG.API_URL}/v1/message/get/${chatId}`, { headers: headersConfig });
};

const sendMessage = (chatId, message) => {
    const headersConfig = authHeader();
    const data = {
        chatid: chatId,
        messageText: message
    };
    console.log(data);
    return axios.post(`${CONFIG.API_URL}/v1/message/send`, data, { headers: headersConfig });
};

const MessagesService = {
    getMessages,
    sendMessage
};

export default MessagesService;
