import React from "react";
import "./Card.css";

export default function Card({
  id,
  heading,
  text,
  img,
  ratings,
  price,
  brand,
  title,
}) {
  return (
    <div class='Card'>
      <div class='Card-left'>
        <h5 class='Card-left-title'>Special Offer</h5>
        <h6 class='Card-left-title2'>{heading}</h6>
        <h6 class='Card-left-ratings'>{ratings}</h6>
        <h6 class='Card-left-brand'>{brand}</h6>
      </div>

      <div class='Card-middle'>
        <h6 class='Card-middle-title'>{title}</h6>
        <span class='Card-middle-text'>{text}</span>
        <button class='Card-middle-button'>Buy {price}</button>
      </div>

      <div class='Card-right'>
        <img src={img} alt='' class='Card-right-image' />
      </div>
    </div>
  );
}
