import React, { useEffect, useState } from "react";
import "./../Product.css";
import "./Review.css";
import RatingReviewChart from "./RatingReviewChart";
const dataJson = require("./ReviewFake.json");

function Review({ productId, productRatingDto }) {
  const [data,setData] = useState(null);
  const handleFetchData = async () => {};
  useEffect(() => {
    setData(dataJson);
  }, [productId,data]);

  return (
    <div className='review-rating-section'>
      <span>Reviews</span>
      <div className={"rating-section"}>
        <div className='rating-section-overall'>
          <span>Overall ratings</span>
          <span>{productRatingDto.averageRating}⭐</span>
          <span>Based on {productRatingDto.ratingCount} ratings</span>
        </div>

        <div className='rating-section-histogram'>
          <RatingReviewChart productRatingDto={productRatingDto} />
        </div>
      </div>
      
      <div className="rating_section_write_btn">
          <button className={"btn-pro-pri"} style={{width:'180px',fontSize:'12px'}}>Write a review</button>
      </div>

      <hr />

      <div className="all_reviews_section">
        <div className="all_review_section_header">
          <span>
            Customer Reviews:
          </span>
        </div>
        <div className="all_reviews">

        {data!==null &&
          data.reviews.map(r=>(
            <div className="single_review" key={r.id}>
              <span className={"single_review_user"}>
                {
                  r.nickname
                }
              </span>
              <div className="single_review_middle">
                <span >{r.noOfStars} ⭐</span>
                <span>{r.dateOfReview}</span>
              </div>
              <div className={"single_review_text"}>
                <span>{r.text}</span>
              </div>
            </div>
          ))
        }
        </div>

      </div>

    </div>
  );
}

export default Review;
