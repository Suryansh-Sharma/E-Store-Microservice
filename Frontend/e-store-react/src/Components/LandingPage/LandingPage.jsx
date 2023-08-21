import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import iphoneGif from "../../icons/apple-apple-iphone.gif";
import appleTv from "../../icons/appleTv.png";
import bose from "../../icons/bose.webp";
import bose_earbuds from "../../icons/bose_earbuds.png";
import camera from "../../icons/camera.jpeg";
import powerbank from "../../icons/powerbank.webp";
import ps5 from "../../icons/sony_ps5.png";
import sony_speaker from "../../icons/sony_speaker.png";
import tv from "../../icons/tv.png";
import "./LandingPage.css";
import Slider from "./Slider.jsx";
import AllCategoriesSlider from "../ProductsSlider /AllCategoriesSlider"
import SwiperCore, { Autoplay, Mousewheel, Pagination } from 'swiper';
import 'swiper/components/pagination/pagination.min.css';
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper-bundle.min.css";
import "swiper/swiper.min.css";
import TopProductSlider from "../ProductsSlider /TopProductSlider";
SwiperCore.use([Mousewheel, Pagination,Autoplay]);
const LandingPage = () => {
  let navigate = useNavigate();
  const [slideData, setSlideData] = useState({
    products: [
      {
        id:0,
        name: "Apple i Phone 14 pro max",
        description: "This is new amazing i phone",
        price: { value: 344.99 },
        image:
          "https://th.bing.com/th/id/R.31fac6dd7bd7cd485206982b20392ecc?rik=k9eIcv6H%2fc7hgQ&riu=http%3a%2f%2fimeinow.com%2fimg%2fiphone14better.png&ehk=uFTDXUApujYyCrotd2zAfgmbFPmsH8TL%2f8qvCyBFkKA%3d&risl=&pid=ImgRaw&r=0",
      },
      {
        id:1,
        name: "Apple i Phone 14 pro max",
        description: "This is new amazing i phone",
        price: { value: 34.99 },
      },
      {
        id:2,
        name: "Apple i Phone 14 pro max",
        description: "This is new amazing i phone",
        price: { value: 34.99 },
      },
      {
        id:3,
        name: "Apple i Phone 14 pro max",
        description: "This is new amazing i phone",
        price: { value: 34.99 },
      },
      {
        id:4,
        name: "Apple i Phone 14 pro max",
        description: "This is new amazing i phone",
        price: { value: 34.99 },
      },
    ],
  });
  useEffect(() => {
    document.title = "Home Page";
    window.scrollTo(0, 0);
  }, []);


  return (
    <>
      <div className='product-card-container'>
        <Swiper spaceBetween={30}
          autoplay={{
            delay:5000
          }}
        >
          {slideData.products.map((product) => (
            <SwiperSlide key={product.id}>
              <div className={"product-card"}>
                <div className='product-card-image'>
                  <img src={product.image} alt='' />
                </div>
                <div className={"product-card-info"}>
                  <h3>{product.name}</h3>
                  <span>{product.description}</span>

                  <div className='product-card-button'>
                    <button>Buy now at {product.price.value}</button>
                  </div>
                </div>
              </div>
            </SwiperSlide>
          ))}
        </Swiper>
      </div>

      {/* Top categories */}

      <div className='top-categories'>
        <div
          className='top-cat-item tct1'
          onClick={() => {
            navigate("/allProduct/product/Travelling");
          }}
        >
          <h2 className='tct-title'>Travelling</h2>
          <p className='tct-title'> From $30.00</p>
          {/* <img src={right_icon} alt='' /> */}
          <img className='tct1-img' src={camera} alt='' />
        </div>

        <div
          className='top-cat-item tct2'
          onClick={() => navigate("/allProduct/product/Electronics")}
        >
          <h2 className='tct-title'>Electronics</h2>
          <p className='tct-title'>From $30.00</p>
          {/* <img className='right-icon' src={right_icon} alt='' /> */}
          <img className='tct2-img' src={powerbank} alt='' />
        </div>

        <div
          className='top-cat-item tct3'
          onClick={() => navigate("/allProduct/product/Gaming")}
        >
          <h2 className='tct-title'>I am the Gammer !!</h2>
          <p className='tct-title'>From $20.00</p>
          {/* <img className='right-icon' src={right_icon} alt='' /> */}
          <img className='tct3-img' src={ps5} alt='' />
        </div>
        <div
          className='top-cat-item tct4'
          onClick={() => navigate("/allProduct/product/Music")}
        >
          <h2 className='tct-title' style={{ color: "black" }}>
            Music & Sound
          </h2>
          <p className='tct-title' style={{ color: "black" }}>
            From $30.00
          </p>
          {/* <img className='right-icon' src={right_icon} alt='' /> */}
          <img className='tct4-img' src={bose} alt='' />
        </div>
      </div>
      <h1>Featured Products</h1>
      <div
        className='top-categories'
        onClick={() => navigate("/allProduct/product/Mobile")}
      >
        <div className='tct-item1' onClick={() => alert("All Phones.")}>
          <h2 className='tct-title'>All Phones</h2>
          <img className={"tct-item-img-1"} src={iphoneGif} alt='' />
        </div>
        <div className='tct-item2'>
          <h2 className={"tct-title"} style={{ color: "black" }}>
            All Categories
          </h2>
          <img className={"tct-item-img-2"} src={tv} alt='' />
        </div>

        <div className='tct-item3'>
          <img src={sony_speaker} className={"tct-item-img-3"} alt='' />
          <p className='tct-item-text'>Sony Srs-Xb31</p>
          {/*<p className='tct-item-text' style={{color:'gray'}} */}
          {/*onClick={()=>alert("Music & Sound")} >Music & Sound</p>*/}
          <p className='tct-item-text'>$125.00</p>
        </div>
        <div className='tct-item3'>
          <img src={appleTv} className={"tct-item-img-4"} alt='' />
          <p className='tct-item-text'>Apple TV Gen-4</p>
          {/*<p className='tct-item-text' style={{color:'gray'}}>Music & Sound</p>*/}
          <p className='tct-item-text'>$180.25</p>
        </div>
        <div className='tct-item4'>
          <img src={bose_earbuds} className={"tct-item-img-5"} alt='' />
          <p className='tct-item-text' style={{ color: "white" }}>
            Bose sports earbuds
          </p>
          {/*<p className='tct-item-text' style={{color:'gray'}}>Music & Sound</p>*/}
          <p className='tct-item-text' style={{ color: "white" }}>
            $187.95
          </p>
        </div>
        <div className='tct-item4'>
          <img src={bose_earbuds} className={"tct-item-img-6"} alt='' />
          <p className='tct-item-text' style={{ color: "white" }}>
            Bose sports earbuds
          </p>
          {/*<p className='tct-item-text' style={{color:'gray'}}>Music & Sound</p>*/}
          <p className='tct-item-text' style={{ color: "white" }}>
            $187.95
          </p>
        </div>
      </div>

      <Slider />
      <AllCategoriesSlider/>
      <TopProductSlider title={"Deals on Audio"} api={"/top-audio"}/>
      <TopProductSlider title={"Deals on Audio"} api={"/top-audio"}/>
      <TopProductSlider title={"Deals on Audio"} api={"/top-audio"}/>

    </>
  );
};

export default LandingPage;
