import { useAuth0 } from "@auth0/auth0-react";
import axios from "axios";
import React, { useEffect, useState } from "react";
import "./CheckOut.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
function CheckOut() {
  const { user, isLoading, isAuthenticated, getAccessTokenSilently } =
    useAuth0();
  const [isPageLoading, setIsLoading] = useState(false);
  useEffect(() => {
    document.title = "Payment Page";
  }, [isLoading]);

  if (!isAuthenticated)
    return <h1 className={"CartException"}>Please login to place order</h1>;
  if (isLoading || isPageLoading)
    return <h1 className={"CartException"}>Page loading please wait</h1>;
  const handlePlaceOrderApi = async () => {
    const token = await getAccessTokenSilently();
    axios.post(`http://localhost:8080/api/order/placeOrder/${user.email}`,
      {},
      {
        headers: { Authorization: `Bearer ${token}` }
      },
      )
      .then((response) => {
        toast.success("🦄 Wow so easy!", {
          position: "top-center",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "dark",
        });
        toast.info("Your order placed successfully", {
          position: "top-center",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "dark",
        });
      })
      .catch((error) => {
        console.log(error);
        toast.error("Product is out of stock or Check address location", {
          position: "top-center",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "dark",
        });
        if (error.response.status === 503 || error.response.status===403) {
          toast.error("Sorry service is down please try after sometime !!", {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "dark",
          });
        }
      });
  };
  return (
    <div className="checkOut">
      <div className="wrapper">
        <div className="card px-4">
          <div className=" my-3">
            <p className="h8">Card number</p>
            <p className="text-muted ">
              Please check address in user section before proceed
            </p>
          </div>

          <div className="debit-card mb-3">
            <div className="d-flex flex-column h-100">
              <label className="d-block">
                <div className="d-flex position-relative">
                  <div>
                    <img
                      src="https://www.freepnglogos.com/uploads/visa-inc-logo-png-11.png"
                      className="visa"
                      alt=""
                    />
                    <p className="mt-2 mb-4 text-white fw-bold">{user.email}</p>
                  </div>
                  <div className="input">
                    <input type="radio" name="card" id="check" />
                  </div>
                </div>
              </label>
              <div className="mt-auto fw-bold d-flex align-items-center justify-content-between">
                <p>4589 72XX XXXX XXXX</p>
                <p>01/27</p>
              </div>
            </div>
          </div>
          <div className="debit-card card-2 mb-4">
            <div className="d-flex flex-column h-100">
              <label className="d-block">
                <div className="d-flex position-relative">
                  <div>
                    <img
                      src="https://www.freepnglogos.com/uploads/mastercard-png/mastercard-logo-png-transparent-svg-vector-bie-supply-0.png"
                      alt="master"
                      className="master"
                    />
                    <p className="text-white fw-bold">{user.email}</p>
                  </div>
                  <div className="input">
                    <input type="radio" name="card" id="check" />
                  </div>
                </div>
              </label>
              <div className="mt-auto fw-bold d-flex align-items-center justify-content-between">
                <p className="m-0">5540 23XX XXXX XXXX</p>
                <p className="m-0">05/27</p>
              </div>
            </div>
          </div>
          <div className="btn2 mb-4" onClick={handlePlaceOrderApi}>
            Order Now
          </div>
        </div>
      </div>
    </div>
  );
}

export default CheckOut;
