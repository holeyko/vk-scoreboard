import Register from "./Register";
import Enter from "./Enter";
import './Auth.css'

interface LoginProps {
    onAuth: () => void
}

export default function Login({ onAuth }: LoginProps) {
    return (
        <div className="auth">
            <Register onAuth={ onAuth }/>
            <Enter onAuth={ onAuth }/>
        </div>
    )
}