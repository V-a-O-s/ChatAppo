import React, { useEffect, useState } from 'react';
import ChatService from '../../services/chats.service';
import JoinService from '../../services/join.service';
import './chats.css';

function Chats({ setActiveChat, activeChat }) {
  const [chats, setChats] = useState([]);
  const [showPopup, setShowPopup] = useState(false);
  const [popupType, setPopupType] = useState('invite'); // Default to 'invite'
  const [chatName, setChatName] = useState('');
  const [userLimit, setUserLimit] = useState('');
  const [inviteCode, setInviteCode] = useState('');

  useEffect(() => {
    // Fetch chats only if there is no active chat
    if (!activeChat) {
      ChatService.getAllChats().then(
        (response) => {
          setChats(response.data);
        },
        (error) => {
          console.error('Failed to fetch chats');
        }
      );
    }
  }, [activeChat]);

  const togglePopup = (type) => {
    if (showPopup && type != popupType) {
      setPopupType(type)
    }else if(type === popupType && showPopup) {
      setShowPopup(!showPopup);
    }else if(!showPopup) {
      setPopupType(type)
      setShowPopup(true)
    }else{
      setShowPopup(false);
      setPopupType(type);
    }
    
  };

  const handleCreateChat = () => {
    // Validate chat name and user limit
    if (chatName.trim().length > 20) {
      alert('Chat name cannot be longer than 20 characters.');
      return;
    }
    const userLimitNum = parseInt(userLimit);
    if (isNaN(userLimitNum) || userLimitNum > 255) {
      alert('Invalid user limit. Please enter a number between 1 and 255.');
      return;
    }
    
    ChatService.createChat(chatName, userLimitNum).then(
      (response) => {
        console.log('Chat created:', response.data);
        setShowPopup(false);
        ChatService.getAllChats().then((response) => {
          setChats(response.data);
        });
      },
      (error) => {
        console.error('Failed to create chat:', error);
      }
    );
  };

  const handleJoinChat = () => {
    JoinService.joinChat(inviteCode);
  };

  const handleLeaveChat = (chatId) => {
    JoinService.leaveChat(chatId)
      .then(() => {
        setActiveChat(null); // Set activeChat to null when leaving a chat
        // Refresh the chat list after leaving
        return ChatService.getAllChats();
      })
      .then((response) => {
        setChats(response.data);
      })
      .catch((error) => {
        console.error('Failed to leave chat:', error);
      });
    setActiveChat(null)
  };

  return (
    <div className="chats-container">
      
      <div className='chats-list'>
        <ul className="chats-ul">
          <li className='spec-chat-tab chat-tab' onClick={() => togglePopup('create')}>
            <div className='create-join chat-init'>
              Create a Chat
            </div>
          </li>
          <li className='spec-chat-tab chat-tab' onClick={() => togglePopup('invite')}>
            <div className='join-link chat-init' >
              Join a Chat
            </div>
          </li>
          <li className={(showPopup)?'chat-tab no-hover':"hidden"}>
          {showPopup && (
        <div className="popup-container" >
          <div className="popup">
            {popupType === 'create' ? (
              <div>
                <h3>Create a Chat</h3>
                <input
                  type="text"
                  placeholder="Chat Name"
                  value={chatName}
                  onChange={(e) => setChatName(e.target.value)}
                />
                <input
                  type="number"
                  placeholder="User Limit (1-255)"
                  value={userLimit}
                  onChange={(e) => setUserLimit(e.target.value)}
                />
                <button onClick={handleCreateChat}>Create</button>
              </div>
            ) : (
              <div>
                <h3>Join a Chat</h3>
                <input
                  type="text"
                  placeholder="Invite Code"
                  value={inviteCode}
                  onChange={(e) => setInviteCode(e.target.value)}
                />
                <div className='action-buttons'>
                  <button className='g-button' onClick={handleJoinChat}>Join</button>
                  <button className='b-button' onClick={() => setShowPopup(false)}>Exit</button>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
          </li>
          {chats.map((chat, index) => (
            <li key={chat.chatId} className={`chat-tab ${chat.chatId === activeChat ? 'active' : ''}`} onClick={() => setActiveChat(chat.chatId)}>
              <div className='chat-name' id={chat.owner+"-"+chat.chatId}>{chat.chatName}</div>
              <div className='misc-chat-button'>
                <div className='invite-button' onClick={() => handleInvite(chat.invite)}>+</div>
                <div className='leave-button' onClick={() => handleLeaveChat(chat.chatId)}>Leave</div>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default Chats;
