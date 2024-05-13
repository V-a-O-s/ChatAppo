import React from 'react';
import './welcome.css';

function Welcome() {
  return (
    <div className="welcome-container">
      <header>
        <h1>Welcome to ChatApp!</h1>
        <p>Connect with your friends and the world around you on ChatApp.</p>
      </header>
      <nav>
        <a href="/login" className="login-button">Log In</a>
      </nav>
    </div>
  );
}

export default Welcome;
