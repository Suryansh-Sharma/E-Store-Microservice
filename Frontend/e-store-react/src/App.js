import './App.css';


import { BrowserRouter as Router, Routes, Route, Switch} from "react-router-dom";

import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import NavigationBar from "./Components/NavigationBar/NavigationBar";
import Footer from "./Components/Footer/Footer";
import SearchBar from "./Components/SearchBar/SearchBar";
import Login from './Components/Security/Login';
import LoginContext from './context/loginContext';
import PrivateRouter from './Components/Security/PrivateRouter';
import PrivateLogin from './Components/Security/PrivateLogin';
import SignUp from './Components/Security/SignUp';
import PageNotFound from './Components/Exception/PageNotFound';
import Profile from './Components/Security/Profile';
import LandingPage from './Components/LandingPage/LandingPage'
import PrivateProfileRoute from './Components/Security/PrivateProfileRoute';
import { ToastContainer} from 'react-toastify';
import AllProduct from "./Components/Allproduct/AllProduct";
import Product from './Components/Product/Product';
import Cart from './Components/Cart/Cart';
function App() {
  
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
        <SearchBar/>
            <Routes>
              
                <Route path={"/"} element={<LandingPage/>}/>
                <Route path={"error404"} element={<PageNotFound/>}/>
                <Route path={"allProduct"} element={<AllProduct/>}/>
                <Route path={"cart"} element={<Cart/>}/>
                <Route path={"product"} element={<Product/>}/>  
                <Route path={"search"} element={<SearchBar/>} />
                <Route path="profile/:userName" element={
                  <PrivateProfileRoute>
                  <Profile/>
                </PrivateProfileRoute>
                }/>
                <Route path="login" element={
                  <PrivateLogin>
                  <Login/>
                </PrivateLogin>
                }/>
                <Route path="signUp" element={
                  <PrivateLogin>
                    <SignUp/>
                  </PrivateLogin>
                } />
                {/* <Route path={"/uploadShow"} element={
                <PrivateRouter>
                  <Upload/>
                </PrivateRouter>
                
                }/> */}

            </Routes>
        </Router>
        </LoginContext.Provider>
      </div>
  );
}

export default App;
