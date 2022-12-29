import React from "react";
import "./Card.css";

export default function Card({ id,heading, text, img, ratings, price ,brand,title}) {

  return (
    <div className="Card row">
        <div className="Card-left col-md-4">
            <h5 className={"Card-left-title"}>Special Offer</h5>
            <h6 className="Card-left-title2">{heading}</h6>
            <h6 className="Card-left-ratings">{ratings}</h6>
            <h6 className={"Card-left-brand"}>{brand}</h6>
        </div>

        <div className="Card-right col-md-8 row">
            <div className="Card-right-details col-5">
                <h6 className={"Card-right-title"}>{title}</h6>
                <span className={"Card-right-text"}>{text}</span>
                <button className="Card-right-button btn-primary">Buy {price}</button>
            </div>
            <img src={img} alt="" className="Card-right-image col-md-7"/>
        </div>

    </div>
  );
}
