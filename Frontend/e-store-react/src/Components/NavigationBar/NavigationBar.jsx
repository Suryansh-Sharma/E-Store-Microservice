import { useAuth0 } from "@auth0/auth0-react";
import { React, useState } from "react";
import { useNavigate } from "react-router-dom";
import account from "../../icons/account.png";
import cart from "../../icons/cart.png";
import titleLogo from "../../icons/logo.png";
import likeIcon from "../../icons/LikeIcon.png";
import "./Navbar.css";
const Navigation = () => {
  const {
    user,
    isAuthenticated,
    isLoading,
    loginWithRedirect,
    getAccessTokenSilently,
  } = useAuth0();
  let navigate = useNavigate();

  return (
    <div className="Navbar" id="Navbar">
      <nav className="navbar navbar-expand-lg navbar-default">
        {/* <a className="navbar-brand" onClick={()=>{navigate("/")}}>Movie & Shows</a> */}
        <img
          src={titleLogo}
          className={"navbar-brand"}
          onClick={() => {
            navigate("/");
          }}
          alt=""
        ></img>
        <button
          className="navbar-toggler"
          type="button"
          data-toggle="collapse"
          data-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarSupportedContent">
          <ul className="navbar-nav mr-auto">
            <li className="nav-item">
              <a
                className="nav-link"
                onClick={() => navigate("/allProduct/product/Mobile")}
              >
                Mobile
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link" onClick={() => navigate("/")}>
                All categories
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link" onClick={() => navigate("/search")}>
                Search
              </a>
            </li>
          </ul>
          <ul className="navbar-nav ml-auto">
            <li className="nav-item">
              <img
                src={likeIcon}
                className="nav-link"
                onClick={() => {
                  navigate("/allProduct/user/likedByUser");
                }}
              ></img>
            </li>
            <li className="nav-item ">
              {/* <a className="nav-link" onClick={()=>navigate("/movie/" +"getShowsByType/"+ "MOVIE"+ "/"+"MOVIE") }>Movies</a> */}
              <img
                src={account}
                className={"nav-account"}
                onClick={() => {
                  navigate("/profile");
                }}
              ></img>
            </li>

            <li className="nav-item profile">
              {/* <a className="nav-link" onClick={()=>navigate("/profile/" + auth.userName)}>{auth.userName}</a> */}
              <img
                src={cart}
                className={"nav-cart"}
                onClick={() => {
                  navigate("/cart");
                }}
              ></img>
            </li>
            {isAuthenticated ? (
              <button className={"nav-btn"}>Logout</button>
            ) : (
              <button className={"nav-btn"} onClick={loginWithRedirect}>
                Login / SignUp{" "}
              </button>
            )}
          </ul>
        </div>
      </nav>
    </div>
  );
};

export default Navigation;
