import React from 'react'
import './welcome.css'

function Welcome() {
  return (
    <div className='welcome-container'> 
      <div className="welcome-header">
        <ul style={{display:'flex'}}>
          <li><a href="/login">Login</a></li>
        </ul>
      </div>
      <div className="welcomeBody">
        <h1>Die Beste ChatApp die es gibt oder so</h1>
        <h2><a href="/login">Jetzt Kostenlos einen Account erstellen!</a></h2>
         
      </div>
    </div>
  )
}

export default Welcome