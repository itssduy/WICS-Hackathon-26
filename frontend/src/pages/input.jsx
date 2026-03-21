import { useState, useEffect } from "react";
import '../styles/login.css';

function Input (){
    const [getInput, setInput] = useState("");
    const [getConversation, setConversation] = useState([])

    useEffect(()=>{
       (async ()=>{
        const apiURL = import.meta.env.VITE_API_URL;
        //API URL NOT SET
        const response = await fetch(`${apiURL}`, {
            method: 'GET',
            mode: "cors",
            headers: {
                'Content-Type': 'application/json',
            },
        });

        

        const data = await response.json();
        setConversation(data);
       })
    }, [])
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
                input: getInput, 
            }),
        });

        

        const data = await response.json();
        console.log("Server Response:", data);
    }
    return(
        <>
            <div>
                <div>
                   {getConversation.map((msg, index) => (
                        <p key={index}>{msg}</p>
                    ))} 
                </div>
                <form onSubmit={onSubmit}>
                    <input type="text" placeholder="Input" onChange={(e)=>{setInput(e.target.value)}}/>
                    <button type="submit">SEND SEND SEND</button>
                </form>
            </div>
            
        </>
    )
}
export default Input;