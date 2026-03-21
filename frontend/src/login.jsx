import { useState } from "react";
function Login (){
    const [getUsername, setUsername] = useState();
    const [getPassword, setPassword] = useState();
    function onSubmit(e){
        e.preventDefault();
        console.log(`Username: ${getUsername}, Password: ${getPassword}`);
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