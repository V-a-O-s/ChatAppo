import * as React from "react";
import * as ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider, }
from "react-router-dom";
import "./styles/index.css";
import Home from "./components/home/homepage";
import Login from "./components/login/login";
import Welcome from "./components/welcome/welcome";
const router = createBrowserRouter([
  {
    path: "/",
    element: <Welcome /> //public for all
  },
  {
    path: "/login",
    element: <Login /> //public for all
  },
  {
    path: "/home",
    element: <Home /> //private for users
  }
]);
  ReactDOM.createRoot(document.getElementById("root"))
  .render(
    <React.StrictMode>
      <RouterProvider router={router} />
    </React.StrictMode>
);