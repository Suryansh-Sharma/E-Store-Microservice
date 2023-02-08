import React, { useEffect, useState } from "react";
import "./Cart.css";
import plus from "../../icons/plus.png";
import minus from "../../icons/Minus.png";
import { useAuth0 } from "@auth0/auth0-react";
import axios from "axios";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useNavigate } from "react-router-dom";

function Cart() {
  const [cart, setCart] = useState(null);
  let navigate = useNavigate();
  const { user, isAuthenticated, isLoading, getAccessTokenSilently } =
    useAuth0();
  const [isPageLoading, setLoading] = useState(true);
  const [isCarUpdated, setIsUpdated] = useState(false);
  useEffect(() => {
    fetchData();
  }, [isLoading,setLoading]);
  const fetchData = async () => {
    const token = await getAccessTokenSilently();
    axios
      .get(`http://localhost:8080/api/cart/getCartByUser/${user.email}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((response) => {
        if (response.data === "") {
          console.log("Empty");
          setCart(null);
        } else {
          setCart(response.data);
        }
      });
    document.title="Cart";
    setLoading(false);
  };
  if (!isAuthenticated) return <h1 className={"CartException"}>Please login to view cart</h1>;
  if (isPageLoading || isLoading)
    return <h1 className={"CartException"}>Please wait page is is loading</h1>;
  const increaseProductQuantity = (id) => {
    setIsUpdated(true);
    let oldCart = cart.cartProduct;
    oldCart.map((item) => {
      if (item.id === id) {
        if (item.noOfStock >= item.noOfProduct + 1) {
          cart.totalPrice = cart.totalPrice + item.price;
          item.id = id;
          item.noOfProduct = item.noOfProduct + 1;
          item.totalPrice = item.price + item.totalPrice;
        } else {
          alert("Out of stock");
        }
      }
    });
    setCart({ ...cart, cartProduct: oldCart });
  };
  const decreaseProductQuantity = (id) => {
    setIsUpdated(true);
    let oldCart = [...cart.cartProduct];
    oldCart.map((item) => {
      if (item.id === id) {
        if (item.noOfProduct > 1) {
          cart.totalPrice = cart.totalPrice - item.price;
          item.noOfProduct = item.noOfProduct - 1;
          item.totalPrice = item.totalPrice - item.price;
          setCart({ ...cart, cartProduct: oldCart });
          return;
        } else {
          if (
            window.confirm(
              "Do you really want to delete this product from cart."
            )
          ) {
            let updatedGroups = [...cart.cartProduct].filter(
              (i) => i.id !== id
            );
            cart.totalPrice = cart.totalPrice - item.price;
            item.noOfProduct = item.noOfProduct - 1;
            item.totalPrice = item.totalPrice - item.price;
            cart.totalProducts = cart.totalProducts - 1;
            setCart({ ...cart, cartProduct: updatedGroups });
            removeProductFromCart(item.id);
            return;
          }
        }
      }
    });
  };
  const handleCartUpdateApi = async () => {
    if (isCarUpdated) {
      const token = await getAccessTokenSilently();
      axios
        .post(
          `http://localhost:8080/api/cart/updateCartOfUser/${user.email}`,
          cart.cartProduct,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        )
        .then((response) => {
          toast.success("Cart updated successfully", {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
          console.log(response);
          setIsUpdated(false);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };
  const removeProductFromCart = async (id) => {
    const token = await getAccessTokenSilently();
    axios
      .post(
        `http://localhost:8080/api/cart/deleteProductFromCart/${user.email}/${id}`,
        cart.cartProduct,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      )
      .then((response) => {
        toast.success("Product removed from successfully", {
          position: "top-center",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
        console.log(response);
      })
      .catch((error) => {
        console.log(error);
      });
  };
  const placeOrderOrUpdate = () => {
    if (isCarUpdated) {
      handleCartUpdateApi();
      setIsUpdated(false);
    } else {
      navigate("/checkOut");
    }
  };

  return (
    <>
      {cart !== null ? (
        <div className="cart">
          <div className="cart-title">
            <h1>Woo hoo! Lets complete your order,shall we ? </h1>
          </div>

          <div className="cart-top-option">
            <span className="c-t-o-text">SHOPPING CART</span>
            {/* <button className="c-t-o-button btn" onClick={placeOrder}>Checkout</button> */}
          </div>

          <div className="cart-items">
            {cart.cartProduct.map((item) => (
              <div className="cartItem container" key={item.id}>
                <div className="cartImgBox ">
                  <img
                    src={`${item.imageUrl}`}
                    alt=""
                    className="cartItemImg col-md-3"
                  />
                </div>
                <div className="cartItemTitleBox col-md-5">
                  <span className="cartItemTitle ">{item.productName}</span>
                </div>
                <div className="cartItemQuantityBox col-4">
                  <img
                    src={minus}
                    onClick={() => decreaseProductQuantity(item.id)}
                    className={"cartItemMinus"}
                    alt=""
                  />
                  <span type="text" className="cartItemQuantityNo">
                    {item.noOfProduct}
                  </span>
                  <img
                    src={plus}
                    onClick={() => increaseProductQuantity(item.id)}
                    className={"cartItemPlus"}
                    alt={""}
                  />
                </div>

                <div className="cartItemPriceBox col-3">
                  <span className="cartItemPrice ">Rs {item.totalPrice}</span>
                </div>
              </div>
            ))}
          </div>
          <hr className={"CartPageDivider"} />
          <div className="cart-bottom row">
            <span className={"cartTotalPrice col"}>
              SUBTOTAL:- Rs {cart.totalPrice}
            </span>
            <span className={"cartTotalProduct col"}>
              Cart Total Items:- {cart.totalProducts}
            </span>
            <button className={"cartCheckout col"} onClick={placeOrderOrUpdate}>
              {isCarUpdated ? "Update Cart" : "Proceed to check out"}
            </button>
          </div>
        </div>
      ) : (
        <h1 className={"CartException"}>Your cart is empty</h1>
      )}
    </>
  );
}

export default Cart;
