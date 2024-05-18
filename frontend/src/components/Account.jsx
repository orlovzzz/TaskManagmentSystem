import { useEffect, useState } from "react";
import Cookies from "js-cookie";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import '../styles/Task.css'
import { Helmet } from "react-helmet";

function Account(props) {
    const [account, setAccount] = useState([])
    const navigate = useNavigate();
    const [accountDetails, setAccountDetails] = useState('')
    const [accountTaskDetails, setAccountTaskDetails] = useState([])
    const [executorTask, setExecutorTask] = useState([])
    const [isModalOpen, setIsModalOpen] = useState(false);
    
    const handleDetailsButton = async (id) => {
        try {
            const response = await axios.get(props.accountUrl + '/accountApi/accounts/' + id, {
                headers: { 'Authorization': Cookies.get('token') }
            })
            setAccountDetails(response.data)

            const response1 = await axios.get(props.taskUrl + '/taskApi/task/account/' + id, {
                headers: { 'Authorization': Cookies.get('token') }
            })
            setAccountTaskDetails(response1.data);
            console.log(response1);

            const response2 = await axios.get(props.taskUrl + "/taskApi/task/executor/" + id, {
                headers: { 'Authorization': Cookies.get('token') }
            })
            setExecutorTask(response2.data)
            console.log(response2);
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
                const response = await axios.get(props.accountUrl + "/accountApi/accounts", {
                headers: { 'Authorization': Cookies.get('token') }
                });
                console.log(response)
                setAccount(response.data)
            } catch(error) {
                if (error.response && error.response.status === 403) {
                    navigate('/login')
                }
            }
        }; fetchData();
    }, []);

    const handleLogout = async () => {
        try {
            await axios.post(props.authUrl + '/authApi/blacklist', {}, {headers: {'Authorization': Cookies.get('token')}})
            navigate('/login')
        } catch (error) {
            console.log(error.response)
        }
    }

    return (
        <div className="task-container">
            <Helmet>
                <title>Accounts</title>
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
                <h2>ACCOUNTS</h2>
                <div className="task-grid">
                    {account.map(a => (
                        <div className="task-details">
                            <p>{a.email}</p>
                            <p>{a.authority.role}</p>
                            <button onClick={() => handleDetailsButton(a.id)} className="details">View Details</button>
                        </div>
                    ))}
                    {isModalOpen &&
                        <div className="modal-overlay">
                            <div className="modal-content">
                                <h3>{accountDetails.id}</h3>
                                <p>Email: {accountDetails.email}</p>
                                <p>Role: {accountDetails.authority.role}</p>
                                {accountTaskDetails.length === 0 ? (
                                    <p>No created tasks</p>
                                ) : (
                                    <div>
                                        <p>Tasks:</p>
                                        <ul>
                                            {accountTaskDetails.map(atd => (
                                                <li key={atd.id}>{atd.id}</li>
                                            ))}
                                        </ul>
                                    </div>
                                )}
                                {executorTask.length === 0 ? (
                                    <p>Doesn't complete any tasks</p>
                                ) : (
                                    <div>
                                        <p>Executor in task:</p>
                                        <ul>
                                            {executorTask.map(et => (
                                                <li key={et.id}>{et.id}</li>
                                            ))}
                                        </ul>
                                    </div>
                                )}
                                <button onClick={handleCloseModal} className="close-button">Close</button>
                            </div>
                        </div>
                    }
                </div>
            </div>
        </div>
    )

}

export default Account;