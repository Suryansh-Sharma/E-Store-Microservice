import { React,useState,useContext , useEffect} from 'react'
import {useNavigate, useParams} from "react-router-dom";
import "./Navbar.css";
import titleLogo from "../../icons/logo.png";
import LoginContext from '../../context/loginContext';
import cart from "../../icons/cart.png";
import account from "../../icons/account.png";

const Navigation = () => {

    const auth = useContext(LoginContext);
    let navigate = useNavigate();
    const [type, settype] = useState([{
        movie:"MOVIE"
    }])

    const handleLogin=()=>{
        // If a User is Already Login then Logout.
        if(auth.status){
            localStorage.setItem('userName',"null");
            localStorage.setItem('isLogin',false);
            localStorage.setItem('isAdmin',false);
            localStorage.removeItem('jwtToken');
            localStorage.setItem('userPic',"defaultProfile");
            window.location.reload();
        }else{
            navigate("/login");
        }
    }


    return (
        <div className="Navbar" id="Navbar" >
            <nav className="navbar navbar-expand-lg navbar-default">
                {/* <a className="navbar-brand" onClick={()=>{navigate("/")}}>Movie & Shows</a> */}
                <img src={titleLogo} className={"navbar-brand"} onClick={()=>{navigate("/")}}></img>
                <button className="navbar-toggler" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav mr-auto">
                        <li className="nav-item">
                            <a className="nav-link" onClick={()=>{
                                navigate("/search")
                            }} ></a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link" onClick={()=>navigate("/") }>Home Appliances</a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link" onClick={()=>navigate("/") }>Camera</a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link" onClick={()=>navigate("/") }>All categories</a>
                        </li>
                    </ul>
                    <ul className="navbar-nav ml-auto">

                        <li className="nav-item">
                            <a className="nav-link"  onClick={()=>{navigate("/categories")}}>Search</a>
                        </li>
                        <li className="nav-item ">
                            {/* <a className="nav-link" onClick={()=>navigate("/movie/" +"getShowsByType/"+ "MOVIE"+ "/"+"MOVIE") }>Movies</a> */}
                            <img src={account} className={"nav-account"} onClick={()=>{navigate("/")}}></img>
                        
                        </li>

                        <li className="nav-item profile">
                            {/* <a className="nav-link" onClick={()=>navigate("/profile/" + auth.userName)}>{auth.userName}</a> */}
                            <img src={cart} className={"nav-cart"} onClick={()=>{navigate("/")}}></img>
                        </li> 
                        <button className={"btn"} onClick={handleLogin}>
                        {
                            (auth.status)?
                            <div>Logout</div>:
                            <div>Login/SignUp</div>
                            
                        }</button>
                    </ul>
                </div>
            </nav>
        </div>
    )
}

export default Navigation