import React, {useState} from "react";
import "./AllProduct.css";
import heart from "../../icons/heart.png";
import cart from "../../icons/cart2.png";
import zoom_in from "../../icons/zoom_in.png";
import apple_mouse from "../../icons/apple_mouse.png";
import fire_tv from "../../icons/fireTv.png";

function AllProduct(props) {
    const [hover, setHover] = useState(true);
    const star = new Array(5);
    const [data, setData] = useState([
        {
            id:1,
            heading: "Get up tp 50% off Today Only !",
            title: "Apple Magic Mouse",
            price: "270",
            newPrice: "250",
            star: 3,
            img: apple_mouse
        },
        {
            id:2,
            heading: "Get up tp 50% off Today Only !",
            title: "Amazon Fire Stick",
            price: "20",
            newPrice: "30",
            star: 4,
            img: fire_tv,
        },
        {
            id:3,
            heading: "Get up tp 50% off Today Only !",
            title: "Apple Magic Mouse",
            price: "270",
            newPrice: "250",
            star: 3,
            img: apple_mouse
        },
        {
            id:4,
            heading: "Get up tp 50% off Today Only !",
            title: "Apple Magic Mouse",
            price: "270",
            newPrice: "250",
            star: 5,
            img: apple_mouse
        },
        {
            id:5,
            heading: "Get up tp 50% off Today Only !",
            title: "Apple Magic Mouse",
            price: "270",
            newPrice: "250",
            star: 3,
            img: apple_mouse
        },
        {
            id:6,
            heading: "Get up tp 50% off Today Only !",
            title: "Apple Magic Mouse",
            price: "270",
            newPrice: "250",
            star: 3,
            img: apple_mouse
        },
    ]);

    return (
        <div className="all-product">
            {/*Header Section*/}
            <div className="all-product-header">
                <h1 className={"a-p-h-heading"}>
                    It's only the product of your imagination
                </h1>
            </div>
            {/*Start Section.*/}
            <div className="all-product-start">
                <h1 className="a-p-s-title">All Products</h1>
                <p className={"a-p-s-text"}>
                    Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad aliquid,
                    dignissimos facere hic id itaque libero magni, modi officia optio
                    perspiciatis quaerat quam quasi repudiandae saepe, vero voluptas
                    voluptate? Nulla.
                </p>
            </div>
            {/*Middle Section.*/}
            <div className="all-product-middle">
                {data.map((item) => (
                    <>
                        <div
                            className="a-p-m-card"
                            key={data.id}
                        >
                            <p className="a-p-m-c-header">{item.heading}</p>
                            <img src={item.img} alt="" className="a-p-m-c-img"/>
                            <div className="container">
                                <div className="row">
                                        <h5 className={"cardProductTitle"}>{item.title}</h5>
                                        <div className={"row cardExtraOptions"}>
                                            <div className="col">
                                                <img
                                                    className="card-icon"
                                                    src={heart}
                                                    alt=""
                                                    onClick={() => alert("Glad you like ")}
                                                />
                                            </div>
                                            <div className="col">
                                                <img className="card-icon" src={cart} alt=""/>
                                            </div>
                                            <div className="col">
                                                <img className="card-icon" src={zoom_in} alt=""/>
                                            </div>
                                        </div>
                                    <div className="a-p-c-card-bottom">
                                        <div className="row">
                                            <div className="col-md-xm-1">$ {item.newPrice}</div>
                                            <div className="col-md-3 card-dis">$ {item.price}</div>
                                            <div className="star col-6">
                                                {new Array(item.star).fill(null).map((i, ele) => (
                                                    <p className="star">⭐</p>
                                                ))}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </>
                ))}
            </div>
            {/* Pagination */}
            <div className="all-product-pagination">
                <nav aria-label="Page navigation example">
                    <ul className="pagination">
                        <li className="page-item">
                            <a className="page-link">
                                Previous
                            </a>
                        </li>
                        <li className="page-item">
                            <a className="page-link">
                                1
                            </a>
                        </li>
                        <li className="page-item">
                            <a className="page-link">
                                2
                            </a>
                        </li>
                        <li className="page-item">
                            <a className="page-link">
                                3
                            </a>
                        </li>
                        <li className="page-item">
                            <a className="page-link">
                                Next
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    );
}

export default AllProduct;
