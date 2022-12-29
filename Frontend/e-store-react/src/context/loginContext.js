import { createContext } from "react";

const LoginContext = createContext({
        name:"Login/SignUp",
        status:false,
        userName:"",
        isAdmin:false,
        link:''
    });


export default LoginContext;

    