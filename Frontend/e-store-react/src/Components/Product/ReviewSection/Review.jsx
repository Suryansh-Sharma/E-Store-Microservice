import React from "react";
import { useState } from "react";
import { useEffect } from "react";
import "./Review.css";
import "./../Product.css";
import axios from "axios";
import { useAuth0 } from "@auth0/auth0-react";
const dataJson = require("./ReviewFake.json");

function Review({ productId }) {
  const [data, setData] = useState(dataJson);
  const [isPageLoading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const {user}=useAuth0();
  useEffect(() => {
    handleFetchData();
  }, [data]);
  const handleFetchData=async ()=>{
    axios.get(`http://localhost:8080/api/review/getReviewsByProductId/${productId}?pageNo=${currentPage}`)
    .then(response=>{
      setData(response.data);
      setLoading(false);
    })
    .catch(error=>{
      setLoading(true);
    })
  }
  const postReview = {
    productId: 0,
    noOfStars: 0,
    text: "",
    userName: "",
    nickname: ""
  };

  if (isPageLoading) {
    return (
      <p style={{ textAlign: "center",marginBottom:100 }}>
        Please Wait Reviews Section is Loading
      </p>
    );
  }
  const submitReview = () => {
    postReview.userName = user.email
    postReview.nickname = user.nickname;
    postReview.productId = productId;
    console.log(postReview);
  };
  return (
    <div className="reviewSection">
            <div className="allReviews review">
        <h3 className="subProductSectionTitle">Top Reviews</h3>
        {data != null
          ? data.reviews.map((val) => (
              <div className="reviewCard row" key={val.id}>
                <h6 className={"commentUsername col-4"}>{val.nickname}</h6>
                <span className={"commentDate col-"}>
                  Created on {"->   "}
                  {val.dateOfReview}
                </span>
                <span className={"commentDate col-"}>
                  Rated {val.noOfStars} Stars
                </span>
                <span className={"comment-text col-md-10"}>{val.text}</span>
              </div>
            ))
          : null}
        <nav className="reviewPagination">
          <ul className="pagination">
            <li className="page-item">
              <a className="page-link"
                onClick={()=>{
                  if(currentPage>0){
                    handleFetchData();
                    setCurrentPage(currentPage-1)
                  }
                  else alert("No previous page available");
                }}
              >Previous</a>
            </li>
            <li className="page-item">
              <a className="page-link">
                {data != null ? data.currentPage : null}
              </a>
            </li>
            <li className="page-item">
              <a className="page-link"
                onClick={()=>{
                  if(data.currentPage < data.totalPages){
                    setCurrentPage(currentPage+1);
                    handleFetchData();
                  }else alert("No next page is available");
                }}
              >Next</a>
            </li>
          </ul>
        </nav>
      </div>
      <div className="addReview">
        <h2 className="fh2">WE APPRECIATE YOUR REVIEW!</h2>
        <h6 className="fh6">
          Your review will help us to improve our web hosting quality products,
          and customer services.
        </h6>
        <div className="mb-2">
          <label className="form-label" style={{ color: "black" }}>
            No of Stars
          </label>
          <div className="dropdown show">
            <select name="cars" id="cars">
              <option>Select Option</option>
              <option onClick={() => (postReview.noOfStars = 1)}>1 Star</option>
              <option onClick={() => (postReview.noOfStars = 2)}>2 Star</option>
              <option onClick={() => (postReview.noOfStars = 3)}>3 Star</option>
              <option onClick={() => (postReview.noOfStars = 4)}>4 Star</option>
              <option onClick={() => (postReview.noOfStars = 5)}>5 Star</option>
            </select>
          </div>
        </div>
        <div className="mb-3">
          <label className="form-label" style={{ color: "black" }}>
            Your Review
          </label>
          <textarea
            style={{ caretColor: "black" }}
            placeholder="Please write your review here."
            className="form-control"
            rows="3"
            onChange={(event) => (postReview.text = event.target.value)}
          ></textarea>
        </div>
        <button className="ReviewSubmitBtn" onClick={submitReview}>
          Submit Review
        </button>
      </div>

    </div>
  );
}

export default Review;
