import './App.css';


import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import { ToastContainer } from 'react-toastify';
import 'slick-carousel/slick/slick-theme.css';
import 'slick-carousel/slick/slick.css';
import AllProduct from "./Components/Allproduct/AllProduct";
import Cart from './Components/Cart/Cart';
import PageNotFound from './Components/Exception/PageNotFound';
import LandingPage from './Components/LandingPage/LandingPage';
import NavigationBar from "./Components/NavigationBar/NavigationBar";
import Product from './Components/Product/Product';
import SearchBar from "./Components/SearchBar/SearchBar";
import OrderDetails from './Components/User/OrderDetails';
import UserProfile from './Components/User/UserProfile';
import LoginContext from './context/loginContext';
import { useAuth0 } from '@auth0/auth0-react';
import { useEffect } from 'react';
import axios from 'axios';
import CheckOut from './Components/CheckOut/CheckOut';
import AddOrEditProduct from './Components/Product/AddOrEditProduct';
function App() {
  const {isAuthenticated,user} =useAuth0();
  useEffect(()=>{
    document.title="E Store";
    if(isAuthenticated){
      axios.get("http://localhost:8080/api/user/isUserPresent/"+user.email)
      .then(response=>{
      })
    }
  },[isAuthenticated])
  return (
    <div className="App">
        <LoginContext.Provider value={{name:"Login/SignUp",
        status:JSON.parse(localStorage.getItem('isLogin')),
        userName:localStorage.getItem('userName'),
        isAdmin:JSON.parse(localStorage.getItem('isAdmin')),
        link:''
      }}>
        <ToastContainer/>
        <Router>
        <NavigationBar  />
            <Routes>
                <Route path={"/"} element={<LandingPage/>}/>
                <Route path={"/search"} element={<SearchBar/>}/>
                <Route path={"error404"} element={<PageNotFound/>}/>
                <Route path={"/allProduct/:type/:api"} element={<AllProduct/>}/>
                <Route path={"/cart"} element={<Cart/>}/>
                <Route path={"product/:productId"} element={<Product/>}/>  
                <Route path={"search"} element={<SearchBar/>} />
                <Route path={"/profile"} element={<UserProfile/>}/>
                <Route path={"/orderDetails/:id"} element={<OrderDetails/>}/>
                <Route path={"/checkOut"} element={<CheckOut/>}/>
                <Route path={"/addOrEditProduct"} element={<AddOrEditProduct/>}/>
            </Routes>
        </Router>
        </LoginContext.Provider>
      </div>
  );
}

export default App;
