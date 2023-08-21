import React, { useState } from 'react';
import { BsHeart } from "react-icons/bs";
import ReactStars from "react-rating-stars-component";
import SwiperCore, { Pagination } from 'swiper';
import "swiper/components/navigation/navigation.min.css";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper-bundle.min.css";
import "swiper/swiper.min.css";
import watch from "../../icons/AppleWatch7.jpg";
import './TopProductSlider.css';
SwiperCore.use([Pagination])
function TopProductSlider({title,api}) {
    const [data,setData] = useState({products:[{id:0},{id:1},{id:2},{id:3},{id:4}]});
  return (
    <div className={"TopProductSlider"}>
        <h5 className={"t-p-s-title"}>{title}</h5>
        <Swiper
      breakpoints={{
        375: {
          slidesPerView: 2,
          spaceBetween: 1,
        },
        810:{
            slidesPerView:3,
            spaceBetween:10
        },
        1200:{
            slidesPerView:4, 
            spaceBetween:50
        }
        // Add more breakpoints as needed
      }}
      slidesPerView={4}
      spaceBetween={50}
        >
            {
                data.products.map(product=>(
                    <SwiperSlide key={product.id}>
                        <div className="t-p-s-card">
                            <div className="t-p-s-c-top">
                                <div className={"heart-box"}>
                                    <BsHeart className={"t-p-s-c-heart"} size={20}/>
                                </div>
                                <div className="t-p-s-c-image">
                                    <img src={watch} alt="" />
                                </div>
                            </div>
                            <div className="t-p-s-c-bottom">
                                <div className="t-p-s-c-name">
                                    <span>
                                        Lorem ipsum dolor sit amet consectetur adipisicing elit. Nisi asperiores corporis provident, reprehenderit iure amet esse, sint consequatur doloremque quia non. Quia veniam sunt suscipit quae aperiam nemo doloribus animi?
                                    </span>
                                </div>    
                                <div className="t-p-s-c-price">
                                    <div className="t-p-s-c-p-new">
                                        <span>$1230</span>
                                    </div>
                                    <div className="t-p-s-c-p-old">
                                        <span>$1430</span>
                                    </div>
                                    <div className="t-p-s-c-p-disc">
                                        <span>25% off</span>
                                    </div>
                                </div>
                                <div className="t-p-s-c-b-rating">
                                    <ReactStars 
                                        value={3.6}
                                        edit={false}
                                    />
                                </div>
                            </div>  
                        </div>
                    </SwiperSlide>
                ))
            }

        </Swiper>
    </div>
  )
}

export default TopProductSlider