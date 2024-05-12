import axios from 'axios'
import authHeader from '../api/authHeader'
import {API_URL} from '../config'

const joinChat = (gotInvite) => {
    const headersConfig = authHeader();
    if (invite==null) {
        alert("You can not join this Chat")
    }

    const inviteExists = InivteService.isValid(gotInvite);
    if (inviteExists){
        const data = {
            invite: gotInvite
        }
        return axios.post(`${API_URL}/v1/member/join`, data, { headers: headersConfig });
    }
    alert()    
};

const leaveChat = (chatid) => {
    const headersConfig = authHeader();
    if (chatid==null) {
        alert("You can not leave this Chat")
    }
    return axios.post(`${API_URL}/v1/member/leave/${chatid}`, {}, { headers: headersConfig });
};

const JoinService = {
    joinChat,
    leaveChat
}

export default JoinService