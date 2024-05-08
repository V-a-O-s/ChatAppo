import { Link } from "react-router-dom"

const MainNavigation = () => {
    return <>
        <nav id="main-nav">
            <ul>
                <li>
                    <Link to="/public">Public</Link>
                </li>
                <li>
                    <Link to="/pubthicc">Pubthicc</Link>
                </li>
                <li>
                    <Link to="/login">Login</Link>/<Link to="/signup">Signup</Link>
                </li>
                <li>
                    <Link to="/privat">Private</Link>
                </li>
                <li>
                    <Link to="/app">App</Link>
                </li>
                <li>
                    <Link to="/">Home</Link>
                </li>
            </ul>
        </nav>
    </>
}

export default MainNavigation