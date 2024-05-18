import axios from "axios";
import '../styles/Task.css'
import '../styles/MyAccount.css'
import { Helmet } from "react-helmet";
import Cookies from "js-cookie";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function MyAccount(props) {
    const [accountInformation, setAccountInformation] = useState([])
    const [task, setTask] = useState([])
    const [executorInTasks, setExecutorInTasks] = useState([])
    const [taskDetails, setTaskDetails] = useState([])
    const [isModalOpen, setIsModalOpen] = useState(false);
    const navigate = useNavigate();
    const [selectedStatus, setSelectedStatus] = useState(taskDetails.status);
  
    const handleStatusChange = (event) => {
      setSelectedStatus(event.target.value);
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(props.url + "/accountApi/myAccount", {
                headers: { 'Authorization': Cookies.get('token') }
                });
                setAccountInformation(response.data)
                getAccountTasks(response.data.id)
                getTasksWhereAccountExecutor(response.data.id)
            } catch(error) {
                if (error.response && error.response.status === 403) {
                    navigate('/login')
                }
                console.log(error.response)
            }
        }; fetchData();
    }, []);

    
    const getAccountTasks = async (id) => {
        try {
            const response = await axios.get(props.url + "/taskApi/task/account/" + id, {
            headers: { 'Authorization': Cookies.get('token') }
            });

            setTask(response.data)
        } catch(error) {
            if (error.response && error.response.status === 403) {
                navigate('/login')
            }
            console.log(error.response)
        }
    }

    const getTasksWhereAccountExecutor = async (id) => {
        try {
            const response = await axios.get(props.url + '/taskApi/task/executor/' + id, {
                headers: { 'Authorization': Cookies.get('token') }
            })
            setExecutorInTasks(response.data)
        } catch (error) {
            if (error.response && error.response.status === 403) {
                navigate('/login')
            }
            console.log(error.response)
        }
    }

    const handleBecomeExecutor = async (id) => {
        try {
            const response = await axios.post(props.url + '/accountApi/accounts', {}, {
                headers: { 'Authorization': Cookies.get('token') }
            });
            window.location.reload();
        } catch(error) {
            if (error.response && error.response.status === 403) {
                navigate('/login')
            }
            console.log(error.response)
        }
    }

    const handleDetailsButton = async (id) => {
        try {
            const response = await axios.get(props.url + '/taskApi/task/' + id, {
                headers: { 'Authorization': Cookies.get('token') }
            })
            setTaskDetails(response.data)
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

    const handleConfirmClick = async (id) => {
        try {
            console.log(selectedStatus)
            const response = await axios.post(props.url + '/taskApi/task/status/' + id + '?status=' + selectedStatus, {}, { headers: {'Authorization': Cookies.get('token')} })
            window.location.reload();
        } catch(error) {
            if (error.response && error.response.status === 403) {
                navigate('/login')
            }
            console.log(error.response)
        } 
    };

    const handleLogout = async () => {
        try {
            await axios.post(props.url + '/authApi/blacklist', {}, {headers: {'Authorization': Cookies.get('token')}})
            navigate('/login')
        } catch (error) {
            console.log(error.response)
        }
    }

    return(
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
            <div className="account-information">
                <h2>Account Information</h2>
                <p>ID: {accountInformation.id}</p>
                <p>Email: {accountInformation.email}</p>
                {accountInformation.authority && accountInformation.authority.role && (
                <p>Role: {accountInformation.authority.role}</p>)}
                <button onClick={() => handleBecomeExecutor(accountInformation.id)} className="become-executor">Become executor</button>
            </div>

            <div className="task-list">
                <h2>TASKS</h2>
                    {task.length === 0 ? (
                        <p className="no-tasks">No tasks created</p>
                    ) : (
                    <div className="task-grid">
                        {task.map(t => (
                        <div key={t.id} className="task-details">
                            <h3>{t.title}</h3>
                            <p>Priority: {t.priority}</p>
                            <p>Status: {t.status}</p>
                            <p>Author: {t.authorId}</p>
                            <p>Executors count: {t.executorsCount}</p>
                        </div>
                        ))}
                    </div>
                )}
            </div>

            <div style={{marginBottom: '30px'}}className="task-list">
                <h2>EXECUTOR IN TASKS</h2>
                {executorInTasks.length === 0 ? (
                        <p className="no-tasks">Is not the executor of any task</p>
                    ) : (
                    <div className="task-grid">
                        {executorInTasks.map(t => (
                        <div className="task-details">
                            <h3>{t.title}</h3>
                            <p>Priority: {t.priority}</p>
                            <p>Status: {t.status}</p>
                            <p>Author: {t.authorId}</p>
                            <button onClick={() => handleDetailsButton(t.id)} className="details">View Details</button>
                        </div>
                        ))}
                    </div>
                )}
                {isModalOpen && (
                    <div className="modal-overlay">
                    <div className="modal-content">
                        <h3>{taskDetails.title}</h3>
                        <p>Task ID: {taskDetails.id}</p>
                        <p>Description: {taskDetails.description}</p>
                        <p>Priority: {taskDetails.priority}</p>
                        <p>Status: {taskDetails.status}</p>
                        <label>
                        Change Status:
                        <select style={{color:'#05368B'}} value={selectedStatus} onChange={handleStatusChange}>
                            <option style={{color:'#05368B'}} value="PENDING">PENDING</option>
                            <option style={{color:'#05368B'}} value="PROCESSING">PROCESSING</option>
                            <option style={{color:'#05368B'}} value="COMPLETED">COMPLETED</option>
                        </select>
                        </label>
                        <p>Author: {taskDetails.authorId}</p>
                        <p>Executors:</p>
                        {taskDetails.executors.length === 0 ? (
                        <p>No executors</p>
                        ) : (
                        <div>
                            <ul className="no-markers">
                            {taskDetails.executors.map((executor) => (
                                <li key={executor.id}>{executor.email}</li>
                            ))}
                            </ul>
                        </div>
                        )}
                        <div className="modal-buttons">
                            <button onClick={() => handleConfirmClick(taskDetails.id)} className="close-button">Confirm</button>
                            <button onClick={handleCloseModal} className="close-button">Close</button>
                        </div>
                    </div>
                    </div>
                )}
                </div>
            </div>
    )
}

export default MyAccount;