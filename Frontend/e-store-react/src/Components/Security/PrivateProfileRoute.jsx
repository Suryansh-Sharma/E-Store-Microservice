import React from 'react'
import { useContext } from 'react'
import { Navigate } from 'react-router-dom'
import LoginContext from '../../context/loginContext'

export default function PrivateProfileRoute({children}) {

    const auth = useContext(LoginContext);
    if(auth.status){
        return children;
    }
  return (
    <div>
        <Navigate to="/"/>
    </div>
  )
}
