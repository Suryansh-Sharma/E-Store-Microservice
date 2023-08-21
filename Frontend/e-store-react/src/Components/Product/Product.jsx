import { useAuth0 } from "@auth0/auth0-react";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import SwiperCore, { Pagination } from "swiper";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper-bundle.css";
import "swiper/swiper-bundle.min.css";
import "swiper/swiper.min.css";
import "./Product.css";
import { AiOutlineHeart } from "react-icons/ai";
import { AiFillHeart } from "react-icons/ai";
import QuesAns from "./QuestionAnswer/QuesAns";
import Review from "./ReviewSection/Review";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import parse from "html-react-parser";
SwiperCore.use([Pagination]);
const productJson = require("./Product-Full-View.json");
const relatedProductJson = require("./Product-Related.json");
const Product = () => {
  const { productId } = useParams();
  const [data, setData] = useState(productJson);
  const [relatedProduct, setRelatedProduct] = useState(relatedProductJson);

  const [isPageLoading, setLoading] = useState(true);
  const [showReview, setShowReview] = useState(false);
  const [isProductLiked, setLiked] = useState(false);
  const [isProductInCart, setInCart] = useState(false);
  const [cartId, setCartId] = useState(0);
  const { isAuthenticated, getAccessTokenSilently, isLoading, user } =
    useAuth0();
  let navigate = useNavigate();
  const fetchData = async (id) => {
    axios
      .get(`http://localhost:8080/api/products/fullView-by-id/${id}`)
      .then((response) => {
        setData(response.data);
        document.title = response.data.productName;
      })
      .catch((error) => {
        console.log(error);
      });

    if (isAuthenticated) {
      const token = await getAccessTokenSilently();
      axios
        .get(
          `http://localhost:8080/api/user/isProductLikedByUser/${user.email}/${id}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        )
        .then((response) => {
          setLiked(response.data);
        })
        .catch((error) => {
          console.log(error);
        });
      // Check Product is in cart.
      axios
        .get(
          `http://localhost:8080/api/cart/isProductInCart/${user.email}/${productId}`
        )
        .then((res) => {
          if (res.data.productInCart) {
            setInCart(true);
            setCartId(res.data.cartId);
          }
        });
    }
  };
  const handleLikedApi = async (state) => {
    if (isAuthenticated && state === "Like") {
      const token = await getAccessTokenSilently();
      const likeData = {
        userName: user.email,
        productId: productId,
        productName: data.productName,
      };
      axios
        .post(`http://localhost:8080/api/user/likeProduct`, likeData, {
          headers: { Authorization: `Bearer ${token}` },
        })
        .then((response) => {
          toast.success("Product Liked ", {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
          setLiked(true);
        })
        .catch((error) => {
          console.log(error);
        });
    } else if (isAuthenticated && state === "UnLike") {
      const token = await getAccessTokenSilently();
      const UnLikeData = {
        userName: user.email,
        productId: productId,
        productName: data.productName,
      };
      axios
        .post(`http://localhost:8080/api/user/unLikeProduct`, UnLikeData, {
          headers: { Authorization: `Bearer ${token}` },
        })
        .then((response) => {
          toast.success("Product UnLiked ", {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
          setLiked(false);
        })
        .catch((error) => {
          console.log(error);
        });
    } else {
      alert("Please login to like product.");
    }
  };
  const handleCartButton = async () => {
    if (isAuthenticated && !isProductInCart) {
      const token = await getAccessTokenSilently();
      const cartData = {
        productId: productId,
        userName: user.email,
        noOfProduct: 1,
      };
      axios
        .post(`http://localhost:8080/api/cart/addProductToCart`, cartData, {
          headers: { Authorization: `Bearer ${token}` },
        })
        .then((res) => {
          toast.success("Product added to cart ", {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
          setInCart(true);
        });
    } else if (isAuthenticated && isProductInCart === true) {
      const token = await getAccessTokenSilently();
      axios
        .post(
          `http://localhost:8080/api/cart/deleteProductFromCart/${user.email}/${cartId}`,
          {},
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        )
        .then((res) => {
          toast.success("Product removed from cart ", {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
          setInCart(false);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };
  useEffect(() => {
    // alert(productId)
    // setData(productJson);
    // setRelatedProduct(relatedProductJson)
    // window.scroll(0,0);
    // fetchData(productId);
    // document.title = "Product Page";
    setLoading(false);
  }, [productId, isLoading, setData, data]);
  // if (isPageLoading || isLoading || data===null)
  //   return (
  //     <h1 style={{ textAlign: "center" }}>Please wait page is loading.</h1>
  //   );
  return (
    <div className={"product-page"}>
      <div className='product-grid'>
        <Swiper pagination={true} modules={[Pagination]} className='p-g-left '>
          {data.productImages.map((image) => (
            <SwiperSlide key={image.imageUrl}>
              <img src={`${image.imageUrl}`} className={"pro-img-box"} alt='' />
            </SwiperSlide>
          ))}
        </Swiper>
        <div className='p-right'>
          <div className='p-right-title'>
            <span>{data.title}</span>
          </div>

          <span className={"site-url"}>visit site</span>

          {/* <h5 className={"p-g-right-"}>Details: {data.description}</h5> */}
          <div className='product-shortdesc-rating'>
            <div className={"short-desc"}>
              <span>{data.shortDescription}</span>
            </div>
            <div className='product-rating-review'>
              <div className='p-r-r-rating'>
                {data.productRatingDto.averageRating}⭐
              </div>
              <span style={{ marginLeft: "2%" }}>Average Rated by </span>
              <br />
              <p style={{ marginLeft: "1%" }}>
                ({data.productRatingDto.ratingCount} User)
              </p>
            </div>
          </div>
          <div
            className={"ProductBrand"}
            onClick={() => navigate(`/allProduct/Brand/${data.brand.name}`)}
          >
            <span>{data.brand.name}</span>
          </div>

          <div
            className={"Product-Category"}
            onClick={() =>
              navigate("/allProduct/product/" + data.productCategory)
            }
          >
            {/* PRODUCT CATEGORY:- {data.productCategory} */}
          </div>

          <div className='Product-Cost-Section'>
            <hr />
            <div className='Product-Cost-Section-Discount'>
              <span className=''>
                {data.discountPrice.currency} {data.discountPrice.value}
              </span>
              <br />
              <span>(Incl. all Taxes) </span>
            </div>
            <hr />
            <div className='Product-Cost-Section-Original'>
              <span>
                {data.price.currency} {data.price.value}
              </span>
              <p>
                (Save {data.price.currency}{" "}
                {data.price.value - data.discountPrice.value} ,
                {data.discountPrice.percentage}% off)
              </p>
            </div>
            <div>
              <span>Color:</span>
              <b style={{ marginLeft: "1%" }}>{data.color}</b>
            </div>
          </div>

          <div className='product-like-cart '>
            <div className='product-cart-btn'>
              <button className={" btn-pro-pri"} onClick={handleCartButton}>
                {isProductInCart === true ? "Remove from cart" : "Add to Cart"}
              </button>
            </div>

            <div className='product-like-section'>
              {isProductLiked ? (
                <AiFillHeart color={"Red"} size={45} cursor={"pointer"} />
              ) : (
                <AiOutlineHeart
                  size={45}
                  cursor={"pointer"}
                  onClick={() => handleLikedApi("Like")}
                />
              )}
            </div>
          </div>

          <div className='row justify-content-center product-btn'></div>
        </div>
        <div></div>
        <div
          className='related-products'
          hidden={relatedProduct === null ? true : false}
        >
          {relatedProduct !== null &&
            relatedProduct.map((p) => (
              <div
                key={p.id}
                className={
                  p.id === data.id
                    ? "related-product-card glow-border"
                    : "related-product-card"
                }
              >
                <div className='related-product-image'>
                  <img src={p.imageUrl} alt='' />
                </div>
                <div className='related-product-price'>
                  <p>{p.price.currency}</p>
                  <span>{p.price.value}</span>
                </div>
              </div>
            ))}
        </div>
      </div>

      <hr />
      <div className='product-inventory-section'>
        <div className='p-i-s-availabilty'>
          <span>
            Stock status : -{" "}
            {data.inventoryResponse.estimatedAvailability.availabilityStatus}
          </span>
          <span>
            Total item sold : -{" "}
            {data.inventoryResponse.estimatedAvailability.soldQuantity}
          </span>
        </div>
        <hr />
        <span>Shipping Options</span>
        <div className='p-i-s-shipping'>
          {data.inventoryResponse.shippingOptions.map((s) => (
            <div className='shipping-option-card' key={s.providerName}>
              <span>Service provider :- {s.providerName}</span>
              <span>Order will arrive before {s.maxEstimatedDeliveryDate}</span>
              <span>
                Shipping cost {s.shippingCost.currency} {s.shippingCost.value}
              </span>
            </div>
          ))}
        </div>

        <hr />

        <span>Return Details</span>

        <div className='Return-Details'>
          <span>
            Return Accepted :-{" "}
            {data.inventoryResponse.returnTerms.returnAccepted}
          </span>
          <span>
            Return Method :- {data.inventoryResponse.returnTerms.refundMethod}
          </span>
          <span>
            Return Period :- {data.inventoryResponse.returnTerms.returnPeriod}
          </span>
          <span>
            Return Instruction :-{" "}
            {data.inventoryResponse.returnTerms.returnInstructions}
          </span>
        </div>
      </div>

      <hr />
      <span>Similar Products</span>

      <div className='similar-products'>
        {data.similar_products.map((p) => (
          <div className='similar_product_card' key={p.id}>
            <div className='similar-product-image'>
              <img src={p.imageUrl} alt='' />
            </div>
            <div className="similar-product-title">
              <span>{p.title}</span>
            </div>
            <div className="similar-product-short-desc">
              <span>{p.shortDescription}</span>
            </div>
            <div className="similar-product-price">
              <span>{p.price.currency} {p.price.value}</span>
            </div>

          </div>
        ))}
      </div>

      <div>
        {
          // parse(data.description.data)
          // data.description
        }
      </div>

      {/* <QuesAns productId={productId} /> */}
      {/* <button
        type='button'
        className='ReviewSubmitBtn showReviewBtn'
        onClick={() => {
          if (showReview) setShowReview(false);
          else setShowReview(true);
        }}
      >
        {showReview ? "Hide reviews" : "Show reviews"}
      </button> */}
      <Review productId={data.id} productRatingDto={data.productRatingDto}/>
      {/* <AllProduct /> */}
    </div>
  );
};

export default Product;
