import * as React from "react";
import * as ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider, }
from "react-router-dom";
import "./styles/index.css";
import App from "./components/App";
import Public from "./components/public";
import MainNavigation from "./components/main-nav";
import Home from "./components/Home";
const router = createBrowserRouter([
  {
    path: "/app",
    element: <App />,
  },
  {
    path: "/public",
    element: <Public />
  },
  {
    path: "/pubthicc",
    element: <h1>Pipimann</h1>
  },
  {
    path: "/login",
    element: <h1>login oder so</h1>
  },
  {
    path: "/private",
    element: <h1>private</h1>
  },
  {
    path: "/",
    element: <Home />
  }
]);
  ReactDOM.createRoot(document.getElementById("root"))
  .render(
    <React.StrictMode>
      <RouterProvider router={router} />
    </React.StrictMode>
);