import { useState } from "react";
import './login.css';
function Login (){
    const [getUsername, setUsername] = useState();
    const [getPassword, setPassword] = useState();
    const onSubmit = async (e) => {
        e.preventDefault();
        const apiURL = 'http://localhost:8080/auth/login';
        console.log(apiURL);
        const response = await fetch(apiURL, {
            method: 'POST',
            mode: "cors",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ 
                username: getUsername, 
                password: getPassword 
            }),
        });

        

        const data = await response.json();
        console.log("Server Response:", data);
    }
    return(
        <>
            <form onSubmit={onSubmit}>
                <input type="text" placeholder="Username" onChange={(e)=>{setUsername(e.target.value)}}/>
                <input type="password" placeholder="Password" onChange={(e)=>{setPassword(e.target.value)}}/>
                <button type="submit">Submit</button>
            </form>
        </>
    )
}
export default Login;