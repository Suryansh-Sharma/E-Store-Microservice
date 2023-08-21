import React, { useState } from "react";
import "./ReviewRatingChart.css";
import { useEffect } from "react";
function RatingReviewChart({ productRatingDto }) {
  const [ratingChart, setRatingChart] = useState(null);
  useEffect(() => {
    const totalCount = productRatingDto.ratingsHistogram.reduce(
      (sum, rating) => sum + rating.count,
      0
    );

    // Step 2: Calculate the percentage for each star's count
    const ratingsWithPercentage = productRatingDto.ratingsHistogram.map(
      (rating) => ({
        ...rating,
        percentage: (rating.count / totalCount) * 100,
      })
    );
    setRatingChart(ratingsWithPercentage);
  }, []);

  return (
    <div class='row'>
      {ratingChart !== null &&
        ratingChart.map((r,index) => (
          <div className='rating-row' key={r.value}>
            <div class='side'>
              <div>{r.value} star</div>
            </div>
            <div class='middle'>
              <div class='bar-container'>
                <div className={`bar-${(index % 5) + 1}`} style={{ width: `${r.percentage}%` }}></div>
              </div>
            </div>
            <div class='side right'>
              <div>{r.count}</div>
            </div>
          </div>
        ))}
    </div>
  );
}

export default RatingReviewChart;
