import { useEffect, useState } from "react";
import Cookies from "js-cookie";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import '../styles/Task.css'
import { Helmet } from "react-helmet";

function Task(props) {
    const [task, setTask] = useState([])
    const navigate = useNavigate();
    const [taskDetails, setTaskDetails] = useState('')
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [showErrorPopup, setShowErrorPopup] = useState(false);
    
    const handleDetailsButton = async (id) => {
        try {
            const response = await axios.get(props.taskUrl + '/task/' + id, {
                headers: { 'Authorization': Cookies.get('token') }
            })
            setTaskDetails(response.data)
            console.log(response.data)
            handleOpenModal();
        } catch (error) {
            if (error.response && error.response.status === 403) {
                navigate('/login')
            }
        }
    }

    const handleOpenModal = () => {
        setIsModalOpen(true);
    }

    const handleCloseModal = () => {
        setIsModalOpen(false);
    }


    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(props.taskUrl + "/task", {
                headers: { 'Authorization': Cookies.get('token') }
                });
                console.log(response)
                setTask(response.data)
            } catch(error) {
                if (error.response && error.response.status === 403) {
                    navigate('/login')
                }
            }
        }; fetchData();
    }, []);

    const handleBecomeExecutor = async (id) => {
        try {
            const response = await axios.post(props.taskUrl + '/task/executor/' + id, {}, {
                headers: {'Authorization': Cookies.get('token')}
            })
            window.location.reload();
        } catch (error) {
            if (error.response && error.response.status === 403) {
                setErrorMessage('You are not executor')
                setShowErrorPopup(true)
            }
        }
    }

    const closeErrorPopup = () => {
        setShowErrorPopup(false);
    };

    const handleLogout = async () => {
        try {
            await axios.post(props.authUrl + '/blacklist', {}, {headers: {'Authorization': Cookies.get('token')}})
            navigate('/login')
        } catch (error) {
            console.log(error.response)
        }
    }

    return (
        <div className="task-container">
            <Helmet>
                <title>TSM</title>
            </Helmet>
            <header className="task-header">TMS</header>
            <div className="under-header">
                <a href='/accounts' className="navbar">Account</a>
                <a href='/' className="navbar">Tasks</a>
                <a href='/myAccount' className="navbar">My Account</a>
                <a href='/createTask' className="navbar">Create Task</a>
                <a onClick={handleLogout} className="navbar">Logout</a>
            </div>
            <div className="task-list">
                <h2>TASKS</h2>
                <div className="task-grid">
                    {task.map(t => (
                        <div className="task-details">
                            <h3>{t.title}</h3>
                            <p>Priority: {t.priority}</p>
                            <p>Status: {t.status}</p>
                            <p>Author: {t.authorId}</p>
                            <p>Executors count: {t.executorsCount}</p>
                            <button onClick={() => handleDetailsButton(t.id)} className="details">View Details</button>
                        </div>
                    ))}
                    {isModalOpen &&
                        <div className="modal-overlay">
                            <div className="modal-content">
                                <h3>{taskDetails.title}</h3>
                                <p>Task ID: {taskDetails.id}</p>
                                <p>Description: {taskDetails.description}</p>
                                <p>Priority: {taskDetails.priority}</p>
                                <p>Status: {taskDetails.status}</p>
                                <p>Author: {taskDetails.authorId}</p>
                                <p>Executors:</p>
                                {taskDetails.executors.length === 0 ? (
                                    <p>No executors</p>
                                ) : (
                                    <div>
                                        <ul className="no-markers">
                                            {taskDetails.executors.map(executor => (
                                                <li key={executor.id}>{executor.email}</li>
                                            ))}
                                        </ul>
                                    </div>
                                )}
                                <div className="modal-buttons">
                                    <button style={{width: '150px'}} onClick={() => handleBecomeExecutor(taskDetails.id)} className="close-button">Become executor</button>
                                    <button onClick={handleCloseModal} className="close-button">Close</button>
                                </div>
                            </div>
                            {showErrorPopup && (
                                <div className="error-popup">
                                    <div className="error-popup-content">
                                        <span>{errorMessage}</span>
                                        <button onClick={closeErrorPopup}>Close</button>
                                    </div>
                                </div>
                            )}
                        </div>
                    }
                </div>
            </div>
        </div>
    )

}

export default Task;