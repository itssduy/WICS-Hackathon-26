import { useState } from "react";
import '../styles/login.css';

const Signup = ()=>{
    const [getUsername, setUsername] = useState("");
    const [getPassword, setPassword] = useState("");
    const [getConfirmPassword, setConfirmPassword] = useState("");

    const onSubmit = async (e) => {
        e.preventDefault();
        if (getConfirmPassword == setPassword){
            const apiURL = import.meta.env.VITE_API_URL;
            console.log(apiURL);
            const response = await fetch(`${apiURL}/auth/login`, {
                method: 'POST',
                mode: "cors",
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ 
                    email: getUsername, 
                    password: getPassword
                }),
            });

            const data = await response.json();
            console.log("Server Response:", data);
        } else {
            console.log("passwords must match")
        }


        


    }
    return(
        <>
            <form onSubmit={onSubmit}>
                <input type="text" placeholder="Username" onChange={(e)=>{setUsername(e.target.value)}}/>
                <input type="password" placeholder="Password" onChange={(e)=>{setPassword(e.target.value)}}/>
                <input type="password" placeholder="Confirm Password" onChange={(e)=>{setConfirmPassword(e.target.value)}}/>
                <button type="submit">Create Account</button>
            </form>
        </>
    )
}

export default Signup