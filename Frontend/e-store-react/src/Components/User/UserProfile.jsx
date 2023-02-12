import { useAuth0 } from '@auth0/auth0-react';
import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import account from "../../icons/account.png";
import "./UserProfile.css";
function UserProfile() {
  const { user,getAccessTokenSilently,isLoading,isAuthenticated} = useAuth0();

  let navigate=useNavigate();
  useEffect(()=>{
    document.title="User Profile";

  },[user]);

  const [states, setStates] = useState({
    showAddress: false,
    showOrders: false,
  });
  
  const [isPageLoading,setLoading]=useState(false);
  const [isAddressUpdated,setUpdated] =useState(false);
  const [orders,setOrders]=useState([]); 
  const [address,setAddress]=useState({
    userName:"",
    line1: "",
    city: "",
    pinCode: 0,
    otherDetails: null
});
if(!isAuthenticated){
  return <h1
  style={{ textAlign: "center", fontStyle: "italic" }}
  >Please login to view user page</h1>
}
  const handleAddressBtn=()=>{
    setStates({...states,showOrders:false});
    if(states.showAddress===false){
      handleFetchData("getAddress");
      setStates({...states,showAddress:true});
    }else{
      setStates({...states,showAddress:false});
    }
  }
  const handleOrderButton=()=>{
    setStates({...states,showAddress:false});
    if(states.showOrders===false){
      handleFetchData("getOrders")
      setStates({...states,showOrders:true});
    }else{
      setStates({...states,showOrders:false});
    }
  }
  const handleFetchData=async(api)=>{
    document.title="User Profile";
    setAddress({...address,userName:user.email});
    setLoading(true)
    const token = await getAccessTokenSilently();
    if(api==="getAddress"){
    axios.get(`http://localhost:8080/api/user/getUserAddress/${user.email}`,
      {
        headers: {"Authorization" : `Bearer ${token}`} 
      }
    )
    .then(response=>{
      console.log(response.data)
      setAddress(response.data)
      if(response.data.city===null)setUpdated(false);
      else setUpdated(true);
    })
    .catch(error=>{
      console.log(error);
    })
    }
    else if(api==="getOrders"){
      axios.get(`http://localhost:8080/api/order/getOrder-byUser/${user.email}`,
      {
        headers: {"Authorization" : `Bearer ${token}`} 
      }
    )
    .then(response=>{
      setOrders(response.data);
    })
    .catch(error=>{
      console.log(error);
    })
    }
    setLoading(false);
  }

  const handleAddressChange=(event)=>{
    const {name,value}=event.target;
    setAddress({...address,[name]:value});
  }
  const handleAddressUpdateButton=async()=>{
    const token = await getAccessTokenSilently();
    if(!isAddressUpdated){
      axios.post(`http://localhost:8080/api/user/addUserAddress`,
      address,
      {
        headers: {"Authorization" : `Bearer ${token}`},
      },
      )
      .then(res=>{
        toast.success("Address Added  successfully!", {
          position: "top-center",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
        setUpdated(true);
      })
      .catch(error=>{
        console.log(error);
        toast.error("Some fields are missing please check", {
          position: "top-center",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      })
    }else if(isAddressUpdated){
      axios.put(`http://localhost:8080/api/user/updateUserAddress`,
      address,
      {
        headers: {"Authorization" : `Bearer ${token}`},
      },
      )
      .then(res=>{
        toast.success("Address updated  successfully!", {
          position: "top-center",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
        setUpdated(true);
      })
      .catch(error=>{
        console.log(error);
        toast.error("Some fields are missing please check", {
          position: "top-center",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      })
    }
  }
  if(isLoading || isPageLoading)return <h1 
  style={{ textAlign: "center", fontStyle: "italic" }}
  >Please wait </h1>
  return (
    <div className='userProfile'>
      <div className='userProfileTop row'>
        <div className="userProfileImage col-2">
          <img src={account} alt="" />
        </div>
        <div className="userProfileName col-6 float-right">
          <h1>Welcome Back..</h1>
          <h3>{user.email}</h3>
        </div>
        <button className='col-1 mt-4 userProfileAddressButton' onClick={()=>handleAddressBtn()}>Address</button>
        <button className='col-1 mt-4 userProfileAllOrderButton' onClick={()=>handleOrderButton()}>All Orders</button>
      </div>
      <div className="userProfileMiddle">
      {
          states.showAddress ?
            <div className='userProfileAddressForm'>
              <h1>Address Section</h1>
              <form>
                <div className="form-group">
                  <label style={{color:'black'}}>Line 1 </label>
                  <input type="text" className="form-control" name="line1" placeholder='Enter line1 here.'
                  value = {address.line1 || ''} autoComplete={"name"} onChange={handleAddressChange}/>
                </div>
                <div clasNames="form-group">
                  <label style={{color:'black'}}>City</label>
                  <input type="text" className="form-control"  placeholder="Enter City." name="city"
                  value = {address.city || ''} autoComplete={"name"} onChange={handleAddressChange}/>
                </div>
                <div clasNames="form-group">
                  <label style={{color:'black'}}>Pincode</label>
                  <input type="text" className="form-control"  placeholder="Enter Pincode." name="pinCode"
                  value = {address.pinCode || ''} autoComplete={"name"} onChange={handleAddressChange}
                  />
                </div>
                <div clasNames="form-group">
                  <label style={{color:'black'}}>Other Details</label>
                  <input type="text" className="form-control"  placeholder="Optional"  name="otherDetails"
                  value = {address.otherDetails || ''} autoComplete={"name"} onChange={handleAddressChange}/>
                </div>
                <button type="button" className="btn btn-primary mt-2" onClick={handleAddressUpdateButton}>
                  {isAddressUpdated?"Update Address":"Add Address"}
                </button>
              </form>
            </div>
            : null
        }
        {
          states.showOrders?
          orders.length>0?
          <div>
              <h1>Orders Section</h1>
            {
              orders.map((val)=>(
                <div className='orderCard row' key={val.id} 
                style={val.isProductDelivered===false?{background:'#a8feab'}:{background:''}}
                onClick={()=>navigate("/orderDetails/"+val.id)}
                >
                  <span className='col-1'>Order Id: {val.id}</span>
                  <span className='col-2'>Order Place Date: {val.orderDate}</span>
                  <span className='col-2'>Order Last Update: {val.lastUpdate}</span>
                  <span className='col-2'>Order Status: {val.status}</span>
                  <span className='col-1'>No Of Items: {val.totalItems}</span>
                  <span className='col-1'>Price: ₹{val.price}</span>
                  <span className='col-2'>Is Deliverd: {val.isProductDelivered? "YES, Order deliverd":"NO, Order is not deliverd"}</span>
                </div>
              ))
            }
          </div>
          :<h1>No orders found.</h1>
          :null
        }
      </div>

    </div>
  )
}

export default UserProfile