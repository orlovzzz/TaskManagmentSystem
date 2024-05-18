import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "../styles/Login.css"
import "../styles/Reset.css"
import Cookies from 'js-cookie';
import axios from 'axios';
import { Helmet } from "react-helmet";

const Login = (props) => {
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
            const response = await axios.post(props.url + "/login", 
            { email: email, password: password })
            Cookies.set('token', response.data.message)
            navigate('/')
        } catch (error) {
            setShowErrorPopup(true)
            console.log(error)
            console.log(error.response)
            setErrorMessage(error.response.data.message)
        }
    };

    const handleRegisterRedirect = () => {
        navigate('/registration');
    };

    const closeErrorPopup = () => {
        setShowErrorPopup(false);
    };

    return (
        <div className="login-container">
            <Helmet>
                <title>Login</title>
            </Helmet>
            <header className="login-header">LOGIN</header>
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
                <button type="submit">Login</button>
                <div className="register-link">
                Don't have an account yet? <a href="/registration" onClick={handleRegisterRedirect}>Register</a>
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

export default Login;
