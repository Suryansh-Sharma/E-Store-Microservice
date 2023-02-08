import { useAuth0 } from '@auth0/auth0-react';
import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import "./OrderDetails.css"
function OrderDetails() {
    const {id} = useParams();
    let navigate=useNavigate();
    const {getAccessTokenSilently}=useAuth0();
    const[data,setData] = useState(
        {
            orderId: 0,
            status: "",
            totalItems: 0,
            price: 0,
            line1: "",
            city: "",
            pinCode: 0,
            otherDetails: "",
            isProductDelivered: false,
            orderItems: []
        }
    );
    const handleFetchData=async()=>{
        const token = await getAccessTokenSilently();
        axios.get(`http://localhost:8080/api/order/getOrderDetails/${id}`,{
            headers: {"Authorization" : `Bearer ${token}`},
          }
        )
        .then(response=>{
            setData(response.data);
        })
        .catch(error=>{
            console.log(error);
        })
    }

    useEffect(()=>{
        handleFetchData();
    },[handleFetchData]);
  return (
    <div className='orderDetails row'>
        <div className="orderDetailsLeft col-6">
            <h6>Order id: {data.orderId}</h6>
            <h6>Order Status: {data.status}</h6>
            <h6>Total Items: {data.totalItems}</h6>
            <h6>Price: {data.price}</h6>
            <h4>Order Delivery Address:</h4>
            <h6>Line 1: {data.line1}</h6>
            <h6>City: {data.city}</h6>
            <h6>Pin Code: {data.pinCode}</h6>
            <h6>Other Details: {data.otherDetails}</h6>
        </div>
        <div className="orderDetailsRight col-6">
                {
                    data.orderItems.map((items)=>(
                        <div className='orderDetailsItems row' key={items.itemId}
                            onClick={()=>navigate("/product/"+items.productId)}
                        >
                        <span className='col-5'>PRODUCT NAME: {items.productName}</span>
                        <span className='col-3'>PRODUCT QUANTITY: {items.quantity}</span>
                        <span className='col-3'>PRODUCT PRICE: {items.price}</span>
                        </div>
                    ))
                }
        </div>
    </div>
  )
}

export default OrderDetails