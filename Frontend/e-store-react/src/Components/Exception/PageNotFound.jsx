import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css';
import "./PageNotFound.scss";
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

                <img src="https://assets.codepen.io/1538474/404.svg" className="logo-404" alt=''/>
                <img src="https://assets.codepen.io/1538474/meteor.svg" className="meteor" alt=''/>
                <p className="error-page-title">Oh no!!</p>
                <p className="subtitle">
	                You’re either misspelling the URL <br /> or requesting a page that's no longer here.
                </p>
                <div align="center">
	                <p className="btn-back" onClick={()=>{
                    navigate("/")    
                  }
                }>Back Home Page</p>
                </div>
                <img src="https://assets.codepen.io/1538474/astronaut.svg" className="astronaut" alt=''/>
                <img src="https://assets.codepen.io/1538474/spaceship.svg" className="spaceship" alt=''/>
        </div>
    </div>
  )
}

export default PageNotFound