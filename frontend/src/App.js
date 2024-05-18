import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import Task from "./components/Task";
import Login from "./components/Login";
import Registration from "./components/Registration";
import Account from "./components/Account";
import MyAccount from "./components/MyAccount";
import CreateTask from "./components/CreateTask";

function App() {

  const authService = 'http://localhost:8080'
  const accountService = 'http://localhost:8081'
  const taskService = 'http://localhost:8082'
  const url = 'http://localhost'

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Task authUrl={authService} taskUrl={taskService}/>}></Route>
        <Route path="/login" element={<Login url={authService}/>}></Route>
        <Route path="/registration" element={<Registration url={authService}/>}></Route>
        <Route path="/accounts" element={<Account authUrl={authService} taskUrl={taskService} accountUrl={accountService}/>}></Route>
        <Route path='/myAccount' element={<MyAccount authUrl={authService} taskUrl={taskService} accountUrl={accountService}/>}></Route>
        <Route path='/createTask' element={<CreateTask taskUrl={taskService} accountUrl={accountService} authUrl={authService}/>}></Route>
      </Routes>
    </Router>
  );
}

export default App;
