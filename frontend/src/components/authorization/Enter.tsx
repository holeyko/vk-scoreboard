import React, { useState } from "react";
import axios from "axios";
import './Auth.css'

interface EnterProps {
    onAuth: () => void
}

interface UserCredentialEnter {
    login: string
    password: string
}

export default function Enter({ onAuth }: EnterProps) {
    const [login, setLogin] = useState('')
    const [password, setPassword] = useState('')
    const [error, setErrors] = useState('')

    function enter(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault()
        setErrors('')

        let userCredentials: UserCredentialEnter = {
            login: login,
            password: password
        }
        axios.post<string>('http://localhost:8080/api/1/jwt/auth', userCredentials)
            .then(response => response.data)
            .then(data => {
                localStorage.setItem('jwt', data)
                onAuth()
            })
            .catch(response => {
                let errData = response.response.data
                setErrors(errData['errors']?.join('\n'))
            })
    }

    return (
        <form onSubmit={ enter } className="enter">
            <p><b>Enter</b></p>
            <label>
                Login:
                <input
                    type="text"
                    value={ login }
                    onChange={ e => setLogin(e.target.value) }
                    required
                />
            </label>
            <label>
                Password:
                <input
                    type="password"
                    value={ password }
                    onChange={ e => setPassword(e.target.value) }
                    required
                />
            </label>
            <div className="error">{ error }</div>
            <input type="submit" value="Enter"/>
        </form>
    )
}