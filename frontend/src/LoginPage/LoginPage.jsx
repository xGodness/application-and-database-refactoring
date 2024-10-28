import "./LoginPage.css";
import { useState, useEffect } from "react";
import Input from "../components/Input/Input";
import Button from "../components/Button/Button";
import axios from "axios";

import { useNavigate } from "react-router-dom";

const LoginPage = () => {
    
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    
    const [error, setError] = useState(null);
    
    const navigate = useNavigate();
    
    const handleChangeUsername = (val) => {
        setUsername(val.target.value);
    };
    
    const handleChangePassword = (val) => {
        setPassword(val.target.value);
    };
    
    const login = () => {
        axios({
            method: "post",
            url: "http://localhost:8080/api/user/login",
            headers: {},
            data: {
                username: username,
                password: password,
            },
        })
      .then((r) => {
          
          console.log("LOGINED")
          console.log(r)
          localStorage.setItem("username", username);
          localStorage.setItem("password", password);
          navigate("/");
          
          
      })
      .catch((err) => setError(err.response.data.errorList[0]));
    }
    
    const register = () => {
        axios({
            method: "post",
            url: "http://localhost:8080/api/user/save",
            headers: {},
            data: {
                username: username,
                password: password,
            },
        })
      .then((r) => {
          localStorage.setItem("username", username);
          localStorage.setItem("password", password);
          navigate("/");
      })
      .catch((err) => setError(err.response.data.errorList[0]));
    }
    
    return <div>
        <h1>Login</h1>
        <h2>Username</h2>
        <Input value={username} onChange={handleChangeUsername}/>
        <h2>Password</h2>
        <Input value={password} onChange={handleChangePassword}/>
        <br/>
        <br/>
        {error && error}
        <br />
        <br />
        
        <Button onClick={login}>Login</Button>
        <Button onClick={register}>Register</Button>
    </div>
}

export default LoginPage;