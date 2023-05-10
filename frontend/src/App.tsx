import React, { useState } from 'react';
import Main from "./components/main/Main";
import Login from "./components/authorization/Login";

export default function App() {
    const [isAuth, setAuth] = useState(
        localStorage.getItem('jwt') === '' || localStorage.getItem('jwt') === null
    )

    function onAuth() {
        setAuth(true)
    }

    return (
        (isAuth ? <Main/> : <Login onAuth={ onAuth }/>)
    )
}
