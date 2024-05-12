import React, { useState } from 'react';
import "./login.css";
import BackButton from '../backButton';

import user_icon from '../../assets/person.png';
import email_icon from '../../assets/email.png';
import password_icon from '../../assets/password.png';

import authService from '../../services/authService'; // Import authService

import CONFIG from '../../config'

export const LoginRegister = () => {
    const [action, setAction] = useState("Login");
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');

    const handleSubmit = (type) => {
        if (action === 'Login' && type==="Login") {
            authService.login(email, password)
                .then((data) => {
                    window.location.href = `${CONFIG.SERVER_URI}/home`
                })
                .catch((error) => {
                    console.error('Login failed:', error);
                    // Handle login failure
                });
        }else if(action === 'Login' && type==="Register") {
            setAction("Register")
        } else if (action === 'Register' && type==='Register') {
            authService.register(email, password, username)
                .then((data) => {
                    alert("Your accounts has been Created!\nYou can now Login :)")
                    setAction("Login")
                })
                .catch((error) => {
                    console.error('Registration failed:', error);
                    // Handle registration failure
                });
        }else{
            setAction("Login")
        }
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
                        <input type="text" name="username" maxLength={20} value={username} onChange={(e) => setUsername(e.target.value)} placeholder='Username' />
                    </div>
                )}
                <div className="input">
                    <img src={email_icon} alt="Email Icon" />
                    <input type="email" name="email" maxLength={255} value={email} onChange={(e) => setEmail(e.target.value)} placeholder='Email' />
                </div>
                <div className='input'>
                    <img src={password_icon} alt="Password Icon" />
                    <input type="password" name="password" maxLength={255} value={password} onChange={(e) => setPassword(e.target.value)} placeholder='Password' />
                </div>
            </div>
            <div className={action === "Register" ? "hidden" : "forgot-password"}><span>Lost Password?</span></div>
            <div className='submit-container'>
                <div className={action === "Login" ? "submit gray" : "submit"} onClick={() => { handleSubmit("Register") }}>Register</div>
                <div className={action === "Register" ? "submit gray" : "submit"} onClick={() => { handleSubmit("Login") }}>Login</div>
            </div>
        </div>
    );
};

export default LoginRegister;
