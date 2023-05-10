import React, { useState } from "react";
import axios from "axios";

interface RegisterProps {
    onAuth: () => void
}

interface UserCredentialsRegister {
    login: string
    firstName: string
    lastName: string
    password: string
}

export default function Register({ onAuth }: RegisterProps) {
    const [login, setLogin] = useState('')
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [password, setPassword] = useState('')
    const [errLogin, setErrLogin] = useState('')
    const [errFirstName, setErrFirstName] = useState('')
    const [errLastName, setErrLastName] = useState('')
    const [errPassword, setErrPassword] = useState('')
    const [errors, setErrors] = useState('')


    function register(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault()
        setErrLogin('')
        setErrFirstName('')
        setErrLastName('')
        setErrPassword('')
        setErrors('')

        let userCredentials: UserCredentialsRegister = {
            login: login,
            firstName: firstName,
            lastName: lastName,
            password: password
        }

        axios.post<string>('http://localhost:8080/api/1/users/new', userCredentials)
            .then(response => response.data)
            .then(data => {
                localStorage.setItem('jwt', data)
                onAuth()
            })
            .catch(response => {
                let errData = response.response.data
                for (let key in errData) {
                    switch (key) {
                        case 'login':
                            setErrLogin(errData[key])
                            break
                        case 'firstName':
                            setErrFirstName(errData[key])
                            break
                        case 'lastName':
                            setErrLastName(errData[key])
                            break
                        case 'password':
                            setErrPassword(errData[key])
                            break
                        case 'errors':
                            setErrors(errData[key].join('\n'))
                            break
                    }
                }
            })
    }

    return (
        <form onSubmit={ register } className={ "register" }>
            <p><b>Register</b></p>
            <label>
                Login:
                <input
                    type="text"
                    value={ login }
                    onChange={ e => setLogin(e.target.value) }
                    required
                />
                <div className="error">{ errLogin }</div>
            </label>
            <label>
                Firstname:
                <input
                    type="text"
                    value={ firstName }
                    onChange={ e => setFirstName(e.target.value) }
                    required
                />
                <div className="error">{ errFirstName }</div>
            </label>
            <label>
                Lastname:
                <input
                    type="text"
                    value={ lastName }
                    onChange={ e => setLastName(e.target.value) }
                    required
                />
                <div className="error">{ errLastName }</div>
            </label>
            <label>
                Password:
                <input
                    type="password"
                    value={ password }
                    onChange={ e => setPassword(e.target.value) }
                    required
                />
                <div className="error">{ errPassword }</div>
            </label>
            <div className="error">{ errors }</div>
            <input type="submit" value="Register"/>
        </form>
    )
}