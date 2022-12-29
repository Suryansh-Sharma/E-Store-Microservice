import React from 'react'
import "./Footer.css";
import Wave from "../../icons/footer-bg.png";
import Insta from '@iconscout/react-unicons/icons/uil-instagram';
import Facebook from '@iconscout/react-unicons/icons/uil-facebook';
import Github from '@iconscout/react-unicons/icons/uil-github';
const Footer = () => {
  return   (
    <div className="footer">
        <img src={Wave} alt="" style={{width:'100%'}}/>
        <div className="f-content">
        <div className="f-icons" >
          <a href='https://www.instagram.com/suryansh0819/' target="_blank">
          <Insta color='red' size='3rem' />
          </a>
          <a href='https://github.com/Suryansh-Sharma' target="_blank">
          <Github color='white' size='3rem' />
          </a>
          <a href='' target="_blank">
          <Facebook color='blue' size='3rem' />
          </a>
        </div>
        </div>
    </div>
  )
}

export default Footer