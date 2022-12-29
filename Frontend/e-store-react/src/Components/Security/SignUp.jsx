import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Axios from "axios";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
function SignUp() {
  let Navigate = useNavigate();
  const [data, setData] = useState({
    userName: "",
    userFirstName: "",
    userLastName: "",
    userPassword: "",
    userEmail: "",
    userPic: "",
  });
  const [error, setError] = useState({
    password: "",
    confirmPassword: "",
  });
  const [status, setStatus] = useState(200);
  const [file, setFile] = useState(null);
  const submitForm = (event) => {
    event.preventDefault();
    if (!data.userEmail.includes("@") || data.email === "") {
      alert("Not valid email.");
    } else if (data.userName.length === 0) {
      alert("User Name is empty");
    } else if (data.userFirstName.length === 0) {
      alert("First Name is empty");
    } else if (data.userLastName.length === 0) {
      alert("Last Name is empty");
    } else if (file === null) {
      alert("Please Upload profile pic");
    } else {
      if (error.password === error.confirmPassword && error.password != "") {
        Axios.post(`http://localhost:8080/registerNewUser`, data).catch(() => {
          toast.error(" Sorry this User Name is Already Present", {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
          setStatus(404);
        });
        if (status === 200) {
          console.log("first");
          toast.success(
            "🦄 You are Sucessfully Registered.Now you can Login.",
            {
              position: "top-center",
              autoClose: 5000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            }
          );

          const formData = new FormData();
          formData.append("file", file);
          try {
            const response = Axios({
              method: "post",
              url: "http://localhost:8080/file/upload",
              data: formData,
              headers: { "Content-Type": "multipart/form-data" },
            });
          } catch (error) {
            console.log(error);
          }

          Navigate("/login");
        }
      } else {
        alert("New Password and Confirm Password is not Matching");
      }
    }
  };
  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-12 col-lg-10 col-xl-8 mx-auto">
          <h2 className="h3 mb-4 page-title text-info ">
            New User Registration.
          </h2>
          <div className="my-4">
            <ul className="nav nav-tabs mb-4" id="myTab" role="tablist">
              <li className="nav-item">
                <a
                  className="nav-link active"
                  id="home-tab"
                  data-toggle="tab"
                  href="#home"
                  role="tab"
                  aria-controls="home"
                  aria-selected="false"
                >
                  Profile
                </a>
              </li>
            </ul>
            <form>
              <hr className="my-4" />
              <div className="form-row">
                <div className="form-group col-md-4">
                  <label>Firstname</label>
                  <input
                    type="text"
                    onChange={(event) => {
                      setData({ ...data, userFirstName: event.target.value });
                    }}
                    className="form-control"
                    placeholder="Suryansh"
                  />
                </div>
                <div className="form-group col-md-4">
                  <label>Lastname</label>
                  <input
                    type="text"
                    onChange={(event) => {
                      setData({ ...data, userLastName: event.target.value });
                    }}
                    className="form-control"
                    placeholder="Sharma"
                  />
                </div>
                <div className="form-group col-md-4">
                  <label>User Name</label>
                  <input
                    type="text"
                    onChange={(event) => {
                      setData({ ...data, userName: event.target.value });
                    }}
                    className="form-control"
                    placeholder="Suryansh@1234"
                  />
                </div>
              </div>
              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  onChange={(event) => {
                    setData({ ...data, userEmail: event.target.value });
                  }}
                  className="form-control"
                  id="inputEmail4"
                  placeholder="suryansh@gmail.com"
                />
              </div>
              <div className="form-row">
                <div className="form-group col-md-4">
                  <label>Profile Pic</label>
                  <input
                    type="file"
                    accept="image/png, image/jpeg"
                    onChange={(e) => {
                      var x = e.target.files[0].name;
                      var f = x.substr(0, x.lastIndexOf("."));
                      setFile(e.target.files[0]);
                      setData({ ...data, userPic: f });
                    }}
                    className="form-control"
                  />
                </div>
              </div>
              <hr className="my-4" />
              <div className="row mb-4">
                <div className="col-md-6">
                  <div className="form-group">
                    <label>New Password</label>
                    <input
                      type="password"
                      onChange={(event) => {
                        setError({ ...error, password: event.target.value });
                        setData({ ...data, userPassword: event.target.value });
                      }}
                      className="form-control"
                    />
                  </div>
                  <div className="form-group">
                    <label>Confirm Password</label>
                    <input
                      type="password"
                      onChange={(event) => {
                        setError({
                          ...error,
                          confirmPassword: event.target.value,
                        });
                      }}
                      className="form-control"
                    />
                  </div>
                </div>
                <div className="col-md-6">
                  <p className="mb-2">Password requirements</p>
                  <p className="small text-muted mb-2">
                    To create a new password, you have to meet all of the
                    following requirements:
                  </p>
                  <ul className="small text-muted pl-4 mb-0">
                    {/* <li>Minimum 8 character</li> */}
                    <li>At least one special character</li>
                    <li>At least one number</li>
                    <li>Can’t be the same as a previous password</li>
                  </ul>
                </div>
              </div>
              <button type="button" onClick={submitForm} className="btn">
                Save Change
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default SignUp;
