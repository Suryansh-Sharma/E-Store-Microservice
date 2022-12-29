import React from 'react'
import { useState,useEffect } from 'react'
import "./Login.css"
import { useNavigate } from 'react-router-dom';
import Axios from "axios";
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
var Mockdata = require("./MockJwt.json");

function Login() {
    useEffect(() => {
        window.scroll(0, 0);
      }, []);
    let Navigate = useNavigate();
    const [data,setData]=useState({
        userName:"",
        userPassword:""
    });
    const [error,setError]=useState({
        userName:"",
        password:""
    });
    const handleToast=(value)=>{
        if(!value){
            toast.error('😕 Invalid User Name and Password.', {
                position: "top-center",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
        }else{
            toast.success('Welcome '+ data.userName,{
                position: "top-center",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                });       
            setTimeout(RefreshPage,5000);
        }
    }
    const RefreshPage=()=>{
        window.location.reload();
    }


    const submitForm=()=>{
        if(data.userName.length==0){
            alert("Not valid userName.");
            setError({...error,email:"Enter Valid User Name."});
        }
        else if(data.userPassword.length==0){
            alert("No Valid Password.");
            setError({...error,password:"Enter Valid Password."});
        }
        else{

            Axios.post(`http://localhost:8080/authenticate `,data)
            .then((response)=>{
                if(response.status===401){
                    alert("Error")
                }
                localStorage.setItem('userName',response.data.user.userName);
                localStorage.setItem('isLogin',true);

                localStorage.setItem('isAdmin',false);
                localStorage.setItem('jwtToken',response.data.jwtToken);
                localStorage.setItem('userPic',response.data.userPic);
                
                handleToast(true);
                console.log(typeof(response.status));
            })
            .catch((error)=>{
                handleToast(false);
            })
        }
        
    }
    return (
        <div className={"loginPage"}>
            <h1 className={"pageTitle"}>Welcome To Login Page.</h1>
            
            <div className={"loginForm"} >
                <div className="mb-3 emailLogin">
                    <label className="form-label">User Name</label>
                    <input type="text" className="form-control" onChange={(event)=>{
                        setData({...data,userName:event.target.value});
                    }} aria-describedby="emailHelp" />
                    <div id="emailHelp" className="formError">{error.userName}</div>
                </div>
                <div className="mb-3 passwordLogin">
                    <label 
                     className="form-label ">Password</label>
                    <input type="password" className="form-control" onChange={(event)=>{
                    setData({...data,userPassword:event.target.value});
                    }}/>
                    <div id="passwordHelp" className="formError">{error.password}</div>
                
                </div>
                <div className="mb-3 form-check redicetUrl">
                    <a onClick={()=>{alert("Sorry this service is not currently working.")}}>
                        <span>Forgot Password</span>
                    </a>
                </div>
                <div className="mb-3 form-check redicetUrl">
                    <a onClick={()=>{ Navigate("/signUp")}}>
                        <span>New User/SignUp.</span>
                    </a>
                </div>
                <button type="button" className="btn btn-primary buttonLogin"
                    onClick={submitForm}
                >Submit</button>
            </div>
        </div>
    )
}

export default Login