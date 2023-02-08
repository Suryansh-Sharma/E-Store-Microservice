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
import QuesAns from "./QuestionAnswer/QuesAns";
import Review from "./ReviewSection/Review";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
SwiperCore.use([Pagination]);

const Product = () => {
  const { productId } = useParams();
  const [data, setData] = useState(null);
  const [isPageLoading, setLoading] = useState(true);
  const [showReview, setShowReview] = useState(false);
  const [isProductLiked, setLiked] = useState(false);
  const [isProductInCart,setInCart] = useState(false);
  const [cartId,setCartId] = useState(0);
  const { isAuthenticated, getAccessTokenSilently, isLoading, user } =
    useAuth0();
  let navigate = useNavigate();
  const fetchData = async (id) => {
    axios
      .get(`http://localhost:8080/api/products/fullView-by-id/${id}`)
      .then((response) => {
        setData(response.data);
        document.title=response.data.productName
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
      axios.get(
        `http://localhost:8080/api/cart/isProductInCart/${user.email}/${productId}`
      ).then(res=>{
        if(res.data.productInCart){
          setInCart(true);
          setCartId(res.data.cartId);
        }
      })
    }
  };
  const handleLikedApi = async (state) => {
    if(isAuthenticated && state==="Like"){
      const token = await getAccessTokenSilently();
      const likeData = {
        userName:user.email,
        productId:productId,
        productName:data.productName
      }
      axios.post(`http://localhost:8080/api/user/likeProduct`,
      likeData,{
        headers: { Authorization: `Bearer ${token}` }
      })
      .then(response=>{
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
      .catch(error=>{
        console.log(error);
      })
    }else if(isAuthenticated && state==="UnLike"){
      const token = await getAccessTokenSilently();
      const UnLikeData = {
        userName:user.email,
        productId:productId,
        productName:data.productName
      }
      axios.post(`http://localhost:8080/api/user/unLikeProduct`,
      UnLikeData,{
        headers: { Authorization: `Bearer ${token}` }
      })
      .then(response=>{
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
      .catch(error=>{
        console.log(error);
      })
    }else{
      alert("Please login to like product.")
    }
  };
  const handleCartButton=async()=>{
    if(isAuthenticated && !isProductInCart){
      const token = await getAccessTokenSilently();
      const cartData = {
        productId:productId,
        userName:user.email,
        noOfProduct:1
      }
      axios.post(`http://localhost:8080/api/cart/addProductToCart`,
        cartData,
        {
          headers: { Authorization: `Bearer ${token}` }
        }
      ).then(res=>{
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
      })
    }else if(isAuthenticated && isProductInCart===true){
      const token = await getAccessTokenSilently();
      axios.post(`http://localhost:8080/api/cart/deleteProductFromCart/${user.email}/${cartId}`,
      {},
      {
        headers: { Authorization: `Bearer ${token}` }
      },
      ).then(res=>{
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
      .catch(error=>{
        console.log(error)
      })
    }
  }
  useEffect(() => {
    window.scroll(0,0);
    fetchData(productId);
    document.title = "Product Page";
    setLoading(false);
  }, [productId, isLoading]);
  if (isPageLoading || isLoading || data===null)
    return (
      <h1 style={{ textAlign: "center" }}>Please wait page is loading.</h1>
    );
  return (
    <div className={"Product"}>
      <div className="product-grid">
        <Swiper pagination={true} modules={[Pagination]} className="p-g-left ">
          {data.productImages.map((slide) => (
            <SwiperSlide key={slide.id}>
              <img src={`${slide.imageUrl}`} className={"pro-img-box"} alt="" />
            </SwiperSlide>
          ))}
        </Swiper>
        <div className="p-g-right">
          <h2 className="p-g-right-heading">{data.productName}</h2>
          <h5 className={"p-g-right-title"}>Details:</h5>
          <div className="product-desc">
            <p>{data.text}</p>
            <p>Rated by :- {data.noOfRatings} User</p>
          </div>
          <p
            className={"ProductBrand"}
            onClick={() => navigate(`/allProduct/Brand/${data.brand.name}`)}
          >
            BRAND:- {data.brand.name}
          </p>
          <p
            className={"ProductBrand"}
            onClick={() =>
              navigate("/allProduct/product/" + data.productCategory)
            }
          >
            PRODUCT CATEGORY:- {data.productCategory}
          </p>
          <p>DISCOUNT:- {data.discount}%</p>
          <div className="container product-price row">
            <div className="row">
              <div className="col-6">₹{data.newPrice}</div>
              <div className="col-6 card-dis">₹{data.price}</div>
            </div>
          </div>
          <div className="product-rating row">
            <div className="ratings col-md-6 mt-1">
              {data.noOfRatings > 0 ? (
                new Array(data.ratings).fill(null).map((i) => (
                  <p className="star" key={i} id={i}>
                    ⭐
                  </p>
                ))
              ) : (
                <p>No ratings found.</p>
              )}
            </div>
            <div className="ProductLikeSection col-md-1">
              {isProductLiked ? (
                <p className={"heartIcon"} onClick={()=>handleLikedApi("UnLike")}>💖</p>
              ) : (
                <p className="heartIcon" onClick={()=>handleLikedApi("Like")}>♡</p>
              )}
            </div>
          </div>
          <div className="row justify-content-center product-btn">
            <button className={" btn-pro-pri"}
              onClick={handleCartButton}
            >{isProductInCart===true?"Remove from cart":"Add to Cart"}</button>
          </div>
        </div>
      </div>
      <div>
        {
          data.subProducts.length>0?
          <>
        <div className="slider subProductSection">
          <h3 className="subProductSectionTitle">Sub Products </h3>
          <Swiper
            slidesPerView={3}
            spaceBetween={60}
            speed={2000}
            autoplay={{
              delay: 1000,
              disableOnInteraction: true,
            }}
            className="subProductCardSlider"
          >
            {data.subProducts.map((subProduct) => (
              <div key={subProduct.id}>
                <SwiperSlide key={subProduct.id}>
                  <div className="row subProductCard"
                    onClick={()=>{navigate("/product/"+subProduct.id)}}
                  >
                    <div className="subProductCardTop col">
                      <img src={`${subProduct.imageUrl}`} className={"subProductCardImage"} alt={""}/>
                    </div>
                    <div className="subProductCardBottom">
                      <span>{subProduct.subProductName}</span>
                      <span className="">Rs {subProduct.price}</span>
                    </div>
                  </div>
                </SwiperSlide>
              </div>
            ))}
          </Swiper>
        </div>
          </>
          :null
        }
      </div>

      <QuesAns productId={productId} />
      <button
        type="button"
        className="ReviewSubmitBtn"
        onClick={() => {
          if (showReview) setShowReview(false);
          else setShowReview(true);
        }}
      >
        {showReview ? "Hide reviews" : "Show reviews"}
      </button>
      {showReview ? <Review productId={productId} /> : null}
      {/* <AllProduct /> */}
    </div>
  );
};

export default Product;
