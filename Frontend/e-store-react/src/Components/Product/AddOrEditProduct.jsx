import React, { useState } from "react";
import "./AddOrEditProduct.css"
import JoditEditor from "jodit-react";
import axios from "axios";
import { useAuth0 } from "@auth0/auth0-react";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
function AddOrEditProduct() {
  const {getAccessTokenSilently}=useAuth0();
  const [data, setData] = useState({
    productName: "",
    noOfStock: 0,
    text: "",
    price: 0.0,
    discount: 0,
    newPrice: 0.0,
    productCategory: "",
    description: "",
    brandName: "",
  });
  const [brandData, setBrandData] = useState([]);
  const [valInp, setValInp] = useState("");
  const [isImagePrimary,setImagePrimary] = useState(false);
  const [image,setImage]=useState([]);
  const [exception,setException] = useState({
    Exception:""
  })
  const handleBrandApi = async(event) => {
    setValInp(event.target.value)
    setData({...data,brandName:event.target.value});
    if(event.target.value===""){
      setBrandData([]);
      return;
    }
    axios.get(`http://localhost:8080/api/brand/nameLike/`+event.target.value)
    .then(response=>{
        setBrandData(response.data);
        if(response.data.length<=0)setBrandData([]);
      })
      .catch(error=>{
        console.log(error);
      })
  };
  const handleDataChange = (event) => {
    const { name, value } = event.target;
    setData({ ...data, [name]: value });
  };
  const addNewBrand=async()=>{
    const token = await getAccessTokenSilently();
    axios.post(`http://localhost:8080/api/brand`,{name:data.brandName},
    {
      headers: { Authorization: `Bearer ${token}` },
    }
    ).then(response=>{
      toast.success("Brand added successfully , Now search to add for product.", {
        position: "top-center",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
    }).catch(error=>{
      toast.error("Brand Already Exist", {
        position: "top-center",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      console.log(error);
    })
  }
  const addProductApi = async () => {
    if(data.productName===""||data.brandName===""||data.discount===""||data.noOfStock===""||data.price===""
    ||data.newPrice===""||data.productCategory===""){
      toast.error("Some fields are missing !!", {
        position: "top-center",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      return ;
    }
    if(window.confirm("Confirm")){
      const token = await getAccessTokenSilently();
      axios.post(`http://localhost:8080/api/products/save`,data,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
      )
      .then(response=>{
        toast.success("Product Added Successfully !!", {
          position: "top-center",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      }).catch(error=>{
        setException(error.response.data)
        toast.error(exception.Exception, {
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
  };
  const handleButtonClick=async()=>{
    if(image.length===0){
      alert("Please select image");
      return;
    }
    if(data.productName===""){
      alert("Please add product name first");
      return;
    }
    const token = await getAccessTokenSilently();
    axios.post(`http://localhost:8080/api/image/upload/${data.productName}`,
    {
      image:image,
      isPrimary: isImagePrimary ? "Yes" : "No"
    },
    {
      headers: { Authorization: `Bearer ${token}`,
      "Content-Type": "multipart/form-data",
      },
      
    }
    )
    .then(response=>{
      toast.success("Image added successfully", {
        position: "top-center",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
    }).catch(error=>{
      console.log(error.response.data);
    })
  }
  return (
    <div>
      <form>
        <div className="form-row">
          <div className="form-group col-md-9">
            <label>Product Name</label>
            <input
              type="text"
              className="form-control"
              name="productName"
              placeholder="Product Name"
              value={data.productName}
              autoComplete={"name"}
              style={{caretColor:'black'}}
              onChange={handleDataChange}
            />
          </div>
          <div className="form-group col-md-3">
            <label>No of stock</label>
            <input
              type="number"
              className="form-control"
              placeholder="No of stock"
              name="noOfStock"
              value={data.noOfStock}
              style={{caretColor:'black'}}
              onChange={handleDataChange}
            />
          </div>
        </div>
        <div className="form-group">
          <label>Product text not more than 50 words</label>
          <input
            type="text"
            className="form-control"
            placeholder="E.G. No Cost EMI/Additional Exchange Offers."
            name="text"
            value={data.text}
            style={{caretColor:'black'}}
            autoComplete={"name"}
            onChange={handleDataChange}
          />
        </div>
        <div className="form-group">
          <label>Design you product description page</label>
          <JoditEditor
            tabIndex={1}
            name="description"
            style={{caretColor:'black'}}
            value={""}
            onChange={(event)=>{
                setData({ ...data,description: event });
            }}
          />
        </div>
        <div className="form-row">
          <div className="form-group col-md-3">
            <label>Orignal Price</label>
            <input
              value={data.price}
              onChange={handleDataChange}
              type="number"
              className="form-control"
              name="price"
              placeholder="Enter price in rupees"
              style={{caretColor:'black'}}
            />
          </div>
          <div className="form-group col-md-3">
            <label>Discount</label>
            <input
              value={data.discount}
              onChange={handleDataChange}
              name="discount"
              placeholder="E.G. 17"
              className="form-control"
              style={{caretColor:'black'}}
              type={"number"}
            />
          </div>
          <div className="form-group col-md-3">
            <label>New Price</label>

            <input
              value={data.newPrice}
              onChange={handleDataChange}
              name="newPrice"
              placeholder="Enter new price in rupees"
              className="form-control"
              style={{caretColor:'black'}}
              type={"number"}
            />
          </div>
          <div className="form-group col-md-3">
            <label>Select Category</label>
            <select
              className="form-control"
              name={"productCategory"}
              onChange={handleDataChange}
            >
              <option>Select..</option>
              <option>Mobile</option>
              <option>Mobile Accessories</option>
              <option>Gaming</option>
              <option>Computer</option>
              <option>Laptop</option>
              <option>Computer/Laptop Accessories</option>
              <option>Music</option>
              <option>Camera</option>
              <option>Appliances</option>
            </select>
          </div>
        </div>
        <div className="form-row">

          <div className="form-group col-md-3">
            <label>Add brand for product</label>
            <input
              type="text"
              className="form-control"
              placeholder="Search Brand"
              name="text"
              value={valInp}
              style={{caretColor:'black'}}
              onChange={(val)=>handleBrandApi(val)}
            />
            <div className="form-group col-md">
              {brandData.length>0
                ? brandData.map((item) => <option key={item.id}
                    name="brandName"
                    value={item.name} 
                    onClick={()=>{
                      setData({...data,brandName:item.name});
                      setBrandData([]);
                      setValInp(item.name)
                    }}
                  >{item.name}</option>)
                : null}
            </div>
          </div>
          <div className="col-md-3">
                <label>Add new brand.</label>
                <button
                  type="button"
                  onClick={addNewBrand}
                  className="btn-pro-pri"
                >
                  Add New Brand
                </button>
        </div>
        </div>
        <button
          type="button"
          onClick={addProductApi}
          className="btn-pro-pri"
        >
          Add New Product
        </button>
        <div class="appProductImageSection row">
          <div className="col-3"> 
          <label>Add Image for product one by one </label>
          <input type="file" className="AddImageInput" accept="image/*"
            onChange={(e)=>{
              setImage(e.target.files[0]);
            }}
          />
          </div>
          <div className={"col-2"}>
            <label>Is image is main image</label>
            <input type={"checkbox"} className="AddImageCheckBox" value={isImagePrimary}
              onClick={()=>{
                if(isImagePrimary)
                  setImagePrimary(false);
                else if(!isImagePrimary)
                  setImagePrimary(true);
              }}
            />
          </div>
          <div>
            <button className="btn-pro-pri" type="button" onClick={handleButtonClick}>Add image</button>
          </div>
         </div>
      </form>
    </div>
  );
}

export default AddOrEditProduct;
