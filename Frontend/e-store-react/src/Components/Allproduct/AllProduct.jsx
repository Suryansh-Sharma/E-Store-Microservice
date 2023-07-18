import { useAuth0 } from "@auth0/auth0-react";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import cart from "../../icons/cart2.png";
import heart from "../../icons/heart.png";
import zoom_in from "../../icons/zoom_in.png";
import "./AllProduct.css";
function AllProduct(props) {
  const [currentPage, setCurrentPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const { user, getAccessTokenSilently, isAuthenticated } = useAuth0();
  const { type, api } = useParams();
  let navigate = useNavigate();
  const [data, setData] = useState({
    products: [],
  });

  useEffect(() => {
    handleFetchData();
  }, [api,setLoading]);

  const handleFetchData = async () => {
    setLoading(true);
    if (type === "product") {
      axios
        .get(
          `http://localhost:8080/api/products/by-category/${api}?pageNo=${currentPage}`
        )
        .then((response) => {
          setData(response.data);
          document.title="All "+api;
        })
        .catch((error) => {
          console.log(error);
          toast.error("Something went wrong please try again !!", {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        });
    } else if (type === "user") {
      const token = await getAccessTokenSilently();
      axios
        .get(
          `http://localhost:8080/api/user/likedProducts-byUser/${user.email}?pageNo=${currentPage}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        )
        .then((response) => {
          setData(response.data);
          document.title="Liked by user";
        })
        .catch((error) => {
          console.log(error);
          toast.error("Something went wrong please try again !!", {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        });
    } else if (type === "nameLike") {
      axios
        .get(`http://localhost:8080/api/products/by-nameLike/${api}?pageNo=0`)
        .then((response) => {
          setData(response.data);
        })
        .catch((error) => {
          console.log(error);
          toast.error("Something went wrong please try again !!", {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        });
    } else if (type === "Brand") {
      console.log(type);
      console.log(api);
      axios
        .get(
          `http://localhost:8080/api/brand/by-name/${api}?pageNo=${currentPage}`
        )
        .then((response) => {
          setData(response.data);
        })
        .catch((error) => {
          console.log(error);
          toast.error("Something went wrong please try again !!", {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        });
    }

    setLoading(false);
  };
  if (type === "user" && !isAuthenticated)
    return (
      <h1 style={{ textAlign: "center", fontStyle: "italic" }}>
        Please login..
      </h1>
    );

  if (loading) return <h1>Please wait page is loading !!</h1>;
  const handleRoute = (id) => {
    navigate("/product/" + id);
  };
  if (data.products.length === 0)
    return (
      <h1 style={{ textAlign: "center", fontStyle: "italic" }}>
        No Products found.
      </h1>
    );
  return (
    <div className="all-product">
      {/*Header Section*/}
      <div className="all-product-header">
        <h1 className={"a-p-h-heading"}>
          It's only the product of your imagination
        </h1>
      </div>
      {/*Start Section.*/}
      {/* <div className="all-product-start">
        <h1 className="a-p-s-title">All Products</h1>
        <p className={"a-p-s-text"}></p>
      </div> */}
      {/*Middle Section.*/}
      <div className="all-product-middle">
        {data.products.map((item) => (
          <div key={data.id} onClick={() => handleRoute(item.id)}>
            <div className="a-p-m-card" key={data.id}>
              <p className="a-p-m-c-header">{item.text.slice(0, 50)}...</p>
              <div className="a-p-m-c-img">
                <img src={`${item.imageUrl}`} alt="" className="cardImg" />
              </div>
              <div className="container">
                <div className="row">
                  <h5 className={"cardProductTitle"}>
                    {item.productName.slice(0, 100)}
                  </h5>
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
                      <img className="card-icon" src={cart} alt="" />
                    </div>
                    <div className="col">
                      <img className="card-icon" src={zoom_in} alt="" />
                    </div>
                  </div>
                  <div className="a-p-c-card-bottom">
                    <div className="row">
                      <div className="col-md-xm-1">PRICE: ${item.newPrice}</div>
                      <div className="col card-dis">$ {item.price}</div>
                      <div className="star col-9">
                        {item.ratings !== 0
                          ? new Array(item.ratings).fill(null).map((i) => (
                            <p className="star" id={i}>
                              ⭐
                            </p>
                          ))
                          : "No ratings available"}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
      {/* Pagination */}
      <div className="all-product-pagination">
        <nav aria-label="Page navigation example">
          <ul className="pagination">
            <li className="page-item">
              <span
                className="page-link"
                onClick={() => {
                  if (currentPage > 0) {
                    handleFetchData();
                    setCurrentPage(currentPage - 1);
                  } else alert("No previous page available");
                }}
              >
                Previous
              </span>
            </li>
            <li className="page-item">
              <a className="page-link">{data.currentPage}</a>
            </li>
            <li className="page-item">
              <a
                className="page-link"
                onClick={() => {
                  if (data.currentPage < data.totalPages) {
                    setCurrentPage(currentPage + 1);
                    handleFetchData();
                  } else alert("No next page is available");
                }}
              >
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
