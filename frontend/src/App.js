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
  const url = 'http://localhost:80'

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Task url={url}/>}></Route>
        <Route path="/login" element={<Login url={url}/>}></Route>
        <Route path="/registration" element={<Registration url={url}/>}></Route>
        <Route path="/accounts" element={<Account url={url}/>}></Route>
        <Route path='/myAccount' element={<MyAccount url={url}/>}></Route>
        <Route path='/createTask' element={<CreateTask url={url}/>}></Route>
      </Routes>
    </Router>
  );
}

export default App;
