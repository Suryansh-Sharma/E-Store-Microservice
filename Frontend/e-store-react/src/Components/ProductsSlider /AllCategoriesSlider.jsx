import React, { useState } from 'react';
import './AllCategoriesSlider.css'
import { Swiper, SwiperSlide } from "swiper/react";
import SwiperCore, { Pagination ,Navigation} from 'swiper';
import "swiper/swiper-bundle.min.css";
import "swiper/swiper.min.css";
SwiperCore.use([Pagination,Navigation]);
function AllCategoriesSlider() {
    const [data,setData] = useState([{},{},{},{},{},{},{},{},{},{}]);
  return (
    <div className={"AllCategoriesSlider"}>
        <Swiper
            className={"Swiper-AllCategoriesSlider"}
            slidesPerView={7}
            spaceBetween={20}
            navigation
            
            breakpoints={{
                375: {
                    slidesPerView: 3,
                    spaceBetween: 1,
                  },
                  810:{
                      slidesPerView:4,
                      spaceBetween:20,
                
                  },
                  1200:{
                      slidesPerView:7, 
                      spaceBetween:20
                  }
            }}
        >
            {
                data.map(c=>(
                    <SwiperSlide key={c.name}>
                    <div className="category-card">
                        <div className="caetgory-card-top">
                            <img src="https://media-ik.croma.com/prod/https://media.croma.com/image/upload/v1676968095/Croma%20Assets/CMS/LP%20Page%20Banners/2023/01_HP_BUGS_LP_BUGS/FEB/21-02-2023/Category%20Navigation%20-%20Audio%20Split/CategoryNavigation_AudioSplit_Mobile_21Feb2023_y6hsfe.png?tr=w-1000" alt="" />
                        </div>
                        <div className="caetgory-card-bottom">
                            <span>Electronics</span>
                        </div>
                    </div>
                </SwiperSlide>
                ))
            }
        </Swiper>
    </div>
  )
}

export default AllCategoriesSlider