import React, { useState } from 'react';
import "./login.css";
import BackButton from '../backButton'

import user_icon from '../../assets/person.png';
import email_icon from '../../assets/email.png';
import password_icon from '../../assets/password.png';

export const LoginRegister = ({onLogin}) => {
    const [action, setAction] = useState("Login");
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        onLogin(username, email, password);
    };

    return (
        <div className='container'>
            <div className='header'>
                <div className="top">
                    <BackButton />
                    <div className='text'>{action}</div>
                </div>
                <div className='underline'></div>
            </div>
            <div className='inputs'>
                {action === "Register" && (
                    <div className="input">
                        <img src={user_icon} alt="User Icon" />
                        <input type="text" name="username" value={username} onChange={(e) => setUsername(e.target.value)} placeholder='Username' />
                    </div>
                )}
                <div className="input">
                    <img src={email_icon} alt="Email Icon" />
                    <input type="email" name="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder='Email' />
                </div>
                <div className='input'>
                    <img src={password_icon} alt="Password Icon" />
                    <input type="password" name="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder='Password' />
                </div>
            </div>
            <div className={action === "Register" ? "hidden" : "forgot-password"}><span>Lost Password?</span></div>
            <div className='submit-container'>
                <div className={action === "Login" ? "submit gray" : "submit"} onClick={() => {setAction("Register");console.log(username+email+password);handleSubmit}}>Register</div>
                <div className={action === "Register" ? "submit gray" : "submit"} onClick={() => {setAction("Login"); console.log(username+email+password);handleSubmit}}>Login</div>
            </div>
        </div>
    );
};

export default LoginRegister;