import './LandingPage.css';
import Slider from "./Slider.jsx";
import React from 'react';
import watch from "../../icons/AppleWatch7.jpg"
import camera from "../../icons/camera.jpeg";
import right_icon from "../../icons/right_icon.png";
import powerbank from "../../icons/powerbank.webp";
import bose from "../../icons/bose.webp";
import ps5 from "../../icons/sony_ps5.png";
import iphoneGif from "../../icons/apple-apple-iphone.gif";
import tv from "../../icons/tv.png";
import sony_speaker from "../../icons/sony_speaker.png";
import bose_earbuds from "../../icons/bose_earbuds.png";
import appleTv from "../../icons/appleTv.png";
import { useEffect } from 'react';
const  LandingPage=()=> {
  
  useEffect(() => {
    window.scrollTo(0, 0);
  }, [])
  

  const settings = {
    infinite: true,
    fade: true,
    cssEase: 'linear',
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    speed: 2000,
    autoplaySpeed: 5500,
};
  
  return (
    <>
    
    <div className="grid-container ">
        <img className="image" src={watch} alt=""/>
        <div className="item content">
            <h1 className="page-title">Make life diffrent.Your life - a ride </h1>    
            <p className="page-text"> Series 7 gives it everything. And thanks to the larger display, you’ll see all your progress and metrics more quickly and clearly, in and out of the water. Let’s go. There’s more in there. </p>
            <button className="page-btn">Buy $562</button>
        </div>
    </div>

    {/* Top categories */}

    <div className="top-categories">
      <div className="top-cat-item tct1" >
        <h2 className='tct-title'>Travelling</h2>
        <p className='tct-title'> From $30.00</p>
        <img src={right_icon} alt="" />
        <img className="tct1-img"src={camera} alt="" />
      </div>

      <div className="top-cat-item tct2">
        <h2 className='tct-title'>Electronics</h2>
        <p className='tct-title'>From $30.00</p>
        <img className='right-icon' src={right_icon} alt="" />
        <img className="tct2-img"src={powerbank} alt="" />
      </div>

      <div className="top-cat-item tct3">
      <h2 className='tct-title'>I am the Gammer !!</h2>
        <p className='tct-title'>From $20.00</p>
        <img className='right-icon' src={right_icon} alt="" />
        <img className="tct3-img" src={ps5} alt="" />
      </div>
      <div className="top-cat-item tct4">
        <h2 className='tct-title' style={{color:'black'}}>Music & Sound</h2>
        <p className='tct-title' style={{color:'black'}}>From $30.00</p>
        <img className='right-icon' src={right_icon} alt="" />
        <img className="tct4-img"  src={bose} alt="" />
      </div>

    </div>
    <h1>Featured Products</h1>
    <div className="top-categories">
      <div className="tct-item1" onClick={()=>alert("All Phones.")}>
        <h2 className='tct-title'>All Phones</h2>
        <img className={"tct-item-img-1"} src={iphoneGif} alt="" />
      </div>
      <div className="tct-item2">
        <h2 className={"tct-title"} style={{color:'black'}}>All Categories</h2>
        <img className={"tct-item-img-2"}  src={tv} alt="" />
      </div>

      <div className="tct-item3">
        <img src={sony_speaker} className={"tct-item-img-3"} alt="" />
        <p className='tct-item-text'>Sony Srs-Xb31</p>
        {/*<p className='tct-item-text' style={{color:'gray'}} */}
        {/*onClick={()=>alert("Music & Sound")} >Music & Sound</p>*/}
        <p className='tct-item-text'>$125.00</p>
      </div> 
      <div className="tct-item3">
        <img src={appleTv} className={"tct-item-img-4"} alt="" />
        <p className='tct-item-text'>Apple TV Gen-4</p>
        {/*<p className='tct-item-text' style={{color:'gray'}}>Music & Sound</p>*/}
        <p className='tct-item-text'>$180.25</p>
      </div>
      <div className="tct-item4">
      <img src={bose_earbuds} className={"tct-item-img-5"} alt="" />
        <p className='tct-item-text' style={{color:'white'}}>Bose sports earbuds</p>
        {/*<p className='tct-item-text' style={{color:'gray'}}>Music & Sound</p>*/}
        <p className='tct-item-text' style={{color:'white'}}>$187.95</p>
      </div>
      <div className="tct-item4">
      <img src={bose_earbuds} className={"tct-item-img-6"} alt="" />
        <p className='tct-item-text' style={{color:'white'}}>Bose sports earbuds</p>
        {/*<p className='tct-item-text' style={{color:'gray'}}>Music & Sound</p>*/}
        <p className='tct-item-text' style={{color:'white'}}>$187.95</p>
      </div>

    </div>

    <Slider/>
    

    </>
  
  );
}

export default LandingPage