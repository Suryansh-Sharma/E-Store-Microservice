import React, {useState} from 'react'
import "./Product.css";
import AllProduct from "../Allproduct/AllProduct";
import iphone from "../../icons/iphone13.png";
import iphone2 from "../../icons/iphone.png";
import {Swiper, SwiperSlide} from "swiper/react";
import "swiper/swiper-bundle.min.css";
import "swiper/swiper.min.css";
import SwiperCore, {Pagination} from 'swiper';
import 'swiper/swiper-bundle.css';

SwiperCore.use([Pagination])

const Product = () => {
    const [image, setImage] = useState([
        {
            id: 1,
            image: iphone
        }, {
            id: 2,
            image: iphone2
        }
    ]);
    return (
        <div className={"Product"}>
            <div className="product-grid">
                <Swiper pagination={true} modules={[Pagination]} className="p-g-left ">
                    {
                        image.map((slide) => (
                            <SwiperSlide key={slide.id}>
                                <img src={slide.image} className={"pro-img-box"} alt=""/>
                            </SwiperSlide>

                        ))
                    }
                </Swiper>
                <div className="p-g-right">
                    <h2 className="p-g-right-heading">Apple iPhone 14 Pro Max </h2>
                    <h5 className={"p-g-right-title"}>Details:</h5>
                    <div className="product-desc">
                        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
                            Accusamus aliquam culpa, doloribus nostrum obcaecati reiciendis,
                            rem sequi similique sit tempora voluptas voluptates. Cupiditate
                            deleniti earum incidunt molestias ratione sint veniam?
                        </p>


                    </div>
                    <div className="container product-price">
                        <div className="row">
                            <div className="col-6">$ 249.99</div>
                            <div className="col-6 card-dis">$ 249.99</div>
                        </div>
                    </div>
                    <div className="product-rating">⭐⭐⭐⭐</div>
                    <div className="container product-btn">
                        <div className="row">
                            <div className="col-6">
                                <button className={"btn-outline-primary btn-pro-pri"}>Add to Cart</button>
                            </div>
                            <div className="col-6">
                                <button className={"btn-primary btn-pro-sec"}>Buy Now</button>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
            <AllProduct/>
        </div>
    )
}

export default Product