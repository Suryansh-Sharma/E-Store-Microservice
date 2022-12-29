import React from 'react'
import { Navigate } from 'react-router-dom'
import "./PageNotFound.scss";
import { useParams,useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
function PageNotFound() {
    let navigate = useNavigate()
    useEffect(() => {
      window.scroll(0,0);
    }, [])
    
  return (
    <div className='error'>
        <div>
            <div className="mars">
            </div>

                <img src="https://assets.codepen.io/1538474/404.svg" className="logo-404" />
                <img src="https://assets.codepen.io/1538474/meteor.svg" className="meteor" />
                <p className="error-page-title">Oh no!!</p>
                <p className="subtitle">
	                You’re either misspelling the URL <br /> or requesting a page that's no longer here.
                </p>
                <div align="center">
	                <a className="btn-back" onClick={()=>{
                    navigate("/")    
                  }
                }>Back Home Page</a>
                </div>
                <img src="https://assets.codepen.io/1538474/astronaut.svg" className="astronaut" />
                <img src="https://assets.codepen.io/1538474/spaceship.svg" className="spaceship" />
        </div>
    </div>
  )
}

export default PageNotFound