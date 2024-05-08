import MainNavigation from "./main-nav"
import React, { useState } from 'react';
import "../styles/login.css";
function Login({ onLogin }) {
 const [username, setUsername] = useState('');
 const [password, setPassword] = useState('');
 const handleSubmit = (event) => {
 event.preventDefault();
 onLogin(username, password);
 };
 return (
 <div className="login-container">
 <form onSubmit={handleSubmit}>
 <h2>Login</h2>
 <div className="form-group">
 <label htmlFor="username">Username:</label>
 <input type="text" id="username"
 name="username" value={username}
 onChange={(e) => setUsername(e.target.value)}
 required
 />
 </div>
 <div className="form-group">
 <label htmlFor="password">Password:</label>
 <input type="password" id="password"
 name="password" value={password}
 onChange={(e) => setPassword(e.target.value)}
 required
 />
 </div>
 <button type="submit">Login</button>
 </form>
 </div>
 );
}
export default Login;