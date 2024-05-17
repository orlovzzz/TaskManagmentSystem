import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "../styles/Login.css"
import "../styles/Reset.css"
import axios from 'axios';
import { Helmet } from "react-helmet";

const Registration = (props) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [showErrorPopup, setShowErrorPopup] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        setEmail('')
        setPassword('')
        try {
            const response = await axios.post(props.url + "/registration", { email, password })
            navigate('/login')
        } catch (error) {
            setErrorMessage(error.response.data.message)
            setShowErrorPopup(true)
            console.log(error.response)
        }
    };

    const handleRegisterRedirect = () => {
        navigate('/login');
    };

    const closeErrorPopup = () => {
        setShowErrorPopup(false);
    };

    return (
        <div className="login-container">
            <Helmet>
                <title>Registration</title>
            </Helmet>
            <header className="login-header">REGISTRATION</header>
            <form className="login-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Email</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Register</button>
                <div className="register-link">
                Already have an account? <a href="/login" onClick={handleRegisterRedirect}>Login</a>
                </div>
            </form>
            {showErrorPopup && (
                <div className="error-popup">
                    <div className="error-popup-content">
                        <span>{errorMessage}</span>
                        <button onClick={closeErrorPopup}>Close</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Registration;
