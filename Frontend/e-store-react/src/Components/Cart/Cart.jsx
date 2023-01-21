import React, {useEffect, useState} from 'react';
import "./Cart.css";
import ps5 from "../../icons/sony_ps5.png";
import plus from "../../icons/plus.png"
import minus from "../../icons/Minus.png"

const data = require("./CartFakeApi.json");

function Cart() {
    const [cart, setCart] = useState(data);
    useEffect(() => {
        console.log(cart);
    }, [cart]);
    const [defaults, setDefaults] = useState({
        buttonText: "CHECK OUT",
        isCartModified: false,
        checkOut: false
    })
    const increaseProductQuantity = (id) => {
        let oldCart = cart.cartProduct;
        oldCart.map((item) => {
            if (item.id === id) {
                cart.totalPrice = cart.totalPrice + item.price
                item.noOfProduct = item.noOfProduct + 1;
                item.totalPrice = item.price + item.totalPrice;
            }
        });
        setDefaults({buttonText: "UPDATE CART", isCartModified: true})
        setCart({...cart, cartProduct: oldCart})
    }
    const decreaseProductQuantity = (id) => {
        let oldCart = [...cart.cartProduct];
        oldCart.map((item) => {
            if (item.id === id) {
                if (item.noOfProduct > 1) {
                    cart.totalPrice = cart.totalPrice - item.price
                    item.noOfProduct = item.noOfProduct - 1;
                    item.totalPrice = item.totalPrice - item.price
                    return;
                }
            } else {
                oldCart.filter((e) => (e.id !== id))
            }
        });
        setDefaults({buttonText: "UPDATE CART", isCartModified: true})
        setCart({...cart, cartProduct: oldCart});
    }

    const placeOrder = () => {
        if (defaults.isCartModified === false) {
            alert("Order Placed Successfully !!")
        }
    }

    return (
        <div className='cart'>
            <div className="cart-title">
                <h1>Woo hoo! Lets complete your order,shall we ? </h1>
            </div>

            <div className="cart-top-option">
                <span className="c-t-o-text">SHOPPING CART</span>
                <button className="c-t-o-button btn" onClick={placeOrder}>{defaults.buttonText}</button>
            </div>

            <div className="cart-items">
                {
                    cart.cartProduct.map((item) => (
                        <div className="cartItem container" key={item.id}>
                            <div className="cartImgBox ">
                                <img src={ps5} alt="" className="cartItemImg col-md-3"/>
                            </div>
                            <div className="cartItemTitleBox col-md-5">
                                <span className="cartItemTitle ">{item.productName}</span>
                            </div>
                            <div className="cartItemQuantityBox col-4">
                                <img src={minus} onClick={() => decreaseProductQuantity(item.id)}
                                     className={"cartItemMinus"} alt=""/>
                                <span type="text" className="cartItemQuantityNo">{item.noOfProduct}</span>
                                <img src={plus} onClick={() => increaseProductQuantity(item.id)}
                                     className={"cartItemPlus"} alt={""}/>
                            </div>

                            <div className="cartItemPriceBox col-3">
                                <span className="cartItemPrice ">Rs {item.totalPrice}</span>
                            </div>
                        </div>
                    ))
                }
            </div>
            <hr className={"CartPageDivider"}/>
            <div className="cart-bottom">
                <span className={"cartTotalPrice"}>SUBTOTAL:-  Rs {cart.totalPrice}</span>
                <span className={"cartTotalProduct"}>Cart Total Items:- {cart.totalProducts}</span>
                <button className={"cartCheckout"} onClick={placeOrder}>{defaults.buttonText}</button>
            </div>
        </div>
    )
}

export default Cart