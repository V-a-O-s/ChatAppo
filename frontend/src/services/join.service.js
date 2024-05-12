import axios from 'axios'
import authHeader from '../api/authHeader'
import CONFIG from '../config'

const joinChat = (gotInvite) => {
    const headersConfig = authHeader();
    if (gotInvite=='') {
        alert("You can not join this Chat")
    }

    console.log(gotInvite)

    const data = {
        invite: gotInvite
    }

    return axios.post(`${CONFIG.API_URL}/v1/member/join`, data, { headers: headersConfig });
};

const leaveChat = (chatid) => {
    const headersConfig = authHeader();
    if (chatid==null) {
        alert("You can not leave this Chat")
    }
    return axios.post(`${CONFIG.API_URL}/v1/member/leave/${chatid}`, {}, { headers: headersConfig });
};

const JoinService = {
    joinChat,
    leaveChat
}

export default JoinService