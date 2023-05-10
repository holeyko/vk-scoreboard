import Enter from "./Enter";
import './Auth.css'

interface LoginProps {
    onAuth: () => void
}

export default function Login({ onAuth }: LoginProps) {
    return (
        <div className="auth">
            <Enter onAuth={ onAuth }/>
        </div>
    )
}