import React, { useEffect, useState } from 'react';
import './home.css';
import Profile from '../profile/profile';
import ProfileOptions from '../profile/options';
import Chats from '../chats/chats';
import ChatWindow from '../chatWindow/chat';



function Home() {
    const [action, setAction] = useState("chat");
    const [activeChat, setActiveChat] = useState()
    return (
        <div className="home-container">
            <div className="home-header">
                <h1>JChat Chatapp</h1>
            </div>
            <div className='home-body'>
                <div className='left-container'>
                    <div className='switch'>
                        <div className='switch-buttons'>
                            <button className={(action==="chat")?"active-switch":"passive-switch"} onClick={() => {setAction("chat");setActiveChat(null)}}>Home</button>
                            <button className={(action==="profile")?"active-switch":"passive-switch hidden"} onClick={() => setAction("profile")}>Profile</button>
                        </div>
                    </div>
                    {(action==='chat')?<Chats setActiveChat={setActiveChat} activeChat={activeChat} />:<ProfileOptions />}
                </div>
                <div className='right-container'>
                    {(action==="chat")?<ChatWindow activeChat={activeChat} />:<Profile />}
                </div>
            </div>
        </div>
    );
}

export default Home