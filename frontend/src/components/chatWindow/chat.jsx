import React, { useEffect, useState, useRef } from 'react';
import MessagesService from '../../services/message.service';
import './chat.css';

function ChatWindow({ activeChat }) {
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState('');
  const chatWindowRef = useRef(null); // Reference to the chat window element
  const shouldAutoScrollRef = useRef(true); // Flag to determine whether to auto-scroll

  useEffect(() => {
    if (activeChat) {
      fetchMessages();
      const interval = setInterval(fetchMessages, 1000);
      return () => clearInterval(interval);
    }
  }, [activeChat]);

  useEffect(() => {
    const chatWindow = chatWindowRef.current;

    const handleScroll = () => {
      // Determine if the user has scrolled to the bottom
      const isAtBottom = chatWindow.scrollHeight - chatWindow.scrollTop === chatWindow.clientHeight;
      // Update auto-scroll flag based on user's scroll position
      shouldAutoScrollRef.current = isAtBottom;
    };

    // Add event listener for scroll
    chatWindow.addEventListener('scroll', handleScroll);

    // Cleanup function to remove event listener
    return () => {
      chatWindow.removeEventListener('scroll', handleScroll);
    };
  }, []);

  const fetchMessages = () => {
    MessagesService.getMessages(activeChat).then(
      (response) => {
        setMessages(response.data);
        if (shouldAutoScrollRef.current) {
          scrollToBottom();
        }
      },
      (error) => {
        console.error('Failed to fetch messages');
      }
    );
  };

  const handleSendMessage = () => {
    if (message.trim() === '') return;

    MessagesService.sendMessage(activeChat, message).then(
      (response) => {
        console.log('Message sent:');
        fetchMessages();
        setMessage('');
      },
      (error) => {
        console.error('Failed to send message');
        // Handle error as needed
      }
    );
  };

  const scrollToBottom = () => {
    const chatWindow = chatWindowRef.current;
    if (chatWindow) {
      chatWindow.scrollTop = chatWindow.scrollHeight;
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      e.preventDefault(); // Prevent default behavior of Enter key
      handleSendMessage();
    }
  };

  return (
    <div className='chat-window'>
      <ul className='text-ul' ref={chatWindowRef}>
        {messages.map((msg, index) => (
          <li key={index} id={msg.messageid} className={(activeChat!=null)?'message':'hidden'} >
            <div className='msg-info'>
              <div className='avatar'>
                <img src={`/src/assets/avatars/${msg.avatar}.png`} alt={msg.avatar + " avatar"} />
              </div>
              <div className='username info-item'>
                {msg.username}
              </div>
              <div className='time info-item'>
                {msg.sendingTime}
              </div>
            </div>
            <div className='msg-content'>
              {msg.messageText}
            </div>
          </li>
        ))}
      </ul>
      <div className='footer'>
        <div className='chat-input'>
          <textarea
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder='Your Message'
            rows="4"
            wrap="soft"
            onKeyPress={handleKeyPress} // Handle Enter key press
          />
        </div>
      </div>
    </div>
  );
}

export default ChatWindow;
