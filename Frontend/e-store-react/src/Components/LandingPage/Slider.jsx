import React from "react";
import "./slider.css";
// import Sliders from "react-slick";
// import Swiper from 'react-slider-swiper';
import iphone from "../../icons/iphone-14-pro.png";
import fireTv from "../../icons/fireTv.png";
import appleWatchUltra from "../../icons/AppleWatchUltra.png";
import Card from "./Card";

import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper-bundle.min.css";
import "swiper/swiper.min.css";


function Slider() {
  return (
    <div className="container-slider">
      <h1 className="container-slider-title">Top Rated Products</h1>
      <Swiper className="mySwiper">
        <SwiperSlide>
          <Card heading={"New Apple IPhone 14 Pro Max."} img={iphone}
            ratings={"Rated 4.5 out of 5 by PC MAG."}
            price={"$1200"}
            brand={"Apple"}
            title={"Apple IPhone 14 Pro."}
            text={"A magical new way to interact with iPhone. A vital safety feature designed to save lives."}
          />
        </SwiperSlide>
        <SwiperSlide>
            <Card heading={"Apple Tv Gen-4"} img={appleWatchUltra}
                  ratings={"Apple"}
                  price={"125"}
            />
        </SwiperSlide>
          <SwiperSlide>
              <Card heading={"Apple Tv Gen-4"} img={fireTv}
                    ratings={"Apple"}
                    price={"125"}
              />
          </SwiperSlide>
      </Swiper>
    </div>
  );
}

export default Slider;
