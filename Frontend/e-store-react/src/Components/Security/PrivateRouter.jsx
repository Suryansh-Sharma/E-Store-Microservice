import React from 'react'
import { useContext } from 'react'
import { Navigate } from 'react-router-dom';
import LoginContext from '../../context/loginContext'
export default function PrivateRouter({children}) {

    const auth = useContext(LoginContext);
    if(auth.isAdmin){
        return children;
    }

    return (
        <Navigate to="/"/>
    )
}
