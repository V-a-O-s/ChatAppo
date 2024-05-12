import axios from 'axios'
import authHeader from '../api/authHeader'
import CONFIG from '../config'

const headersConfig = authHeader();

const join = (chatname) => {
    const data = {
        userLimit: (userlimit>255)?2:userlimit,
        chatName: (chatname==='')?"Chat":chatname
    };
    return axios.post(`${CONFIG.API_URL}/v1/chat/create`, data, { headers: headersConfig });
};

//get
//update
const InviteService = {
    join
}

export default InviteService;