import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import '../styles/index.css'
import Demo from './demo.jsx'
import Public from './public.jsx'
const data = {
 name: "Ohio Rizzler",
 hobbies: ["rizz", "fanum taxed", "yappin'"]
}

const clickFun = () =>{
  console.log("frfr")
}


const root = document.getElementById("root")
ReactDOM.createRoot(root).render(
 <React.StrictMode>
 <Demo p1="skibidi" p2="gyat" fun={clickFun} person={data} />
 <App />
 <Public  />
 </React.StrictMode>,
)
