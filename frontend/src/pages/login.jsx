import { useState } from "react";
import '../styles/login.css';

function Login() {
    const [getUsername, setUsername] = useState("");
    const [getPassword, setPassword] = useState("");

    const onSubmit = async (e) => {
        e.preventDefault();
        const apiURL = 'http://localhost:8080/auth/login';
        
        try {
            const response = await fetch(apiURL, {
                method: 'POST',
                mode: "cors",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ 
                    email: getUsername, 
                    password: getPassword 
                }),
            });
            const data = await response.json();
            console.log("Server Response:", data);
        } catch (error) {
            console.error("Login Error:", error);
        }
    };

    return (
    <div className="login-container">
        {/* LEFT SIDE SECTION */}
        <div className="branding-section">
            <h1 className="hero-text">Return to your quiet place.</h1>
            <p className="hero-subtext">
                Welcome back. Step into your personal sanctuary and continue your journey toward calmer, more intentional reflection.
            </p>
            <ul className="feature-list">
                <li><div className="icon-circle">✨</div> Guided reflection, one question at a time</li>
                <li><div className="icon-circle">🔒</div> Private and personal space</li>
                <li><div className="icon-circle">🌿</div> Built to help you slow down and feel lighter</li>
            </ul>
        </div>

        {/* RIGHT SIDE SECTION */}
        <div className="form-section">
            <form className='login-form' onSubmit={onSubmit}>
                <div className="form-header">
                    <h1>Welcome back</h1>
                    <p>Log in to continue reflecting.</p>
                </div>
                
                <div className="input-group">
                    <label>Username or email</label>
                    <input type="text" placeholder="Enter your username" onChange={(e)=>setUsername(e.target.value)}/>
                </div>

                <div className="input-group">
                    <label>Password</label>
                    <input type="password" placeholder="Enter your password" onChange={(e)=>setPassword(e.target.value)}/>
                </div>

                <button type="submit" className="btn-login">Log in</button>
                {/* ... existing Google button and footer links ... */}
            </form>
        </div>
    </div>
);
}

export default Login;