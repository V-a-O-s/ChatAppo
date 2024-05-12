import axios from 'axios'
import authHeader from '../api/authHeader'
import {API_URL} from '../config'

const headersConfig = authHeader();

const isValid = (toValidate) => {
    const data = {
        invite: toValidate
    }
    const resp = axios.get(`${API_URL}/v1/invite/validator`, data, { headers: headersConfig });

    console.log(resp.data);
    if (resp.data.isValid) {
        return true;
    }
    return false;
};

const join = (chatname) => {
    const data = {
        userLimit: (userlimit>255)?2:userlimit,
        chatName: (chatname==='')?"Chat":chatname
    };
    return axios.post(`${API_URL}/v1/chat/create`, data, { headers: headersConfig });
};

//get
//update
const InviteService = {
    isValid,
    join
}

export default InviteService;