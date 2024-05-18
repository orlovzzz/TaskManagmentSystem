import { Helmet } from "react-helmet";
import '../styles/CreateTask.css'
import { useState } from "react";
import Cookies from "js-cookie";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function CreateTask(props) {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [priority, setPriority] = useState('LOW');
    const navigate = useNavigate();
  
    const handleSubmit = async (e) => {
        e.preventDefault();
        const taskData = { title, description, priority };
        setDescription('')
        setPriority('')
        setTitle('')
        try {
            const response = await axios.post(props.taskUrl + '/taskApi/task', taskData, {headers: {'Authorization': Cookies.get('token')}})
        } catch (error) {
            if (error.response && error.respose.status === 403) {
                navigate('/login')
            }
        }
    };

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
            <div className="create-task">
                <h2>Create Task</h2>
                <form onSubmit={handleSubmit} className="task-form">
                <div>
                    <label htmlFor="title">Title:</label>
                    <input
                    type="text"
                    id="title"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                    />
                </div>
                <div>
                    <label htmlFor="description">Description:</label>
                    <textarea
                    id="description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                    />
                </div>
                <div>
                    <label htmlFor="priority">Priority:</label>
                    <select
                    id="priority"
                    value={priority}
                    onChange={(e) => setPriority(e.target.value)}
                    required
                    >
                    <option value="LOW">LOW</option>
                    <option value="MEDIUM">MEDIUM</option>
                    <option value="HIGH">HIGH</option>
                    </select>
                </div>
                <button type="submit">Submit</button>
                </form>
            </div>
        </div>
    )

}

export default CreateTask;