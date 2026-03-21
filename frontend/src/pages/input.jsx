import { useState } from "react";
import '../styles/login.css';

function Input (){
    const [getUsername, setUsername] = useState("");
    const [getGender, setGender] = useState("");
    const [getCategory, setCategory] = useState("");
    const [getTopics, setTopics] = useState("");

    const onSubmit = async (e) => {
        e.preventDefault();
        const apiURL = import.meta.env.VITE_API_URL;
        //API URL NOT SET
        const response = await fetch(`${apiURL}`, {
            method: 'POST',
            mode: "cors",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ 
                username: getUsername,
                gender: getGender,
                category: getCategory,
                topics: getTopics
            }),
        });

        const data = await response.json();
        console.log("Server Response:", data);
    }
    return(
        <>
            <form onSubmit={onSubmit}>
                <input type="text" placeholder="Username" onChange={(e)=>{setUsername(e.target.value)}}/>
                <input type="text" placeholder="Gender" onChange={(e)=>{setGender(e.target.value)}}/>
                <input type="text" placeholder="Category" onChange={(e)=>{setCategory(e.target.value)}}/>
                <input type="text" placeholder="Topics" onChange={(e)=>{setTopics(e.target.value)}}/>

                <button type="submit">Submit</button>
            </form>  
        </>
    )
}
export default Input;