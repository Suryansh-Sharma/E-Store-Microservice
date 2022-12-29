import { React, useEffect, useState } from "react";
import "./Profile.css";
import { useParams } from "react-router-dom";
import Axios from "axios";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
export default function Profile() {
  let { userName } = useParams();
  const [data, setData] = useState({
    userPic: "",
    role: [{}],
  });
  const [file, setFile] = useState(null);
  const [modifiedData, setModifiedData] = useState({
    userFirstName: "",
    userLastName: "",
    userPassword: "",
    userEmail: "",
    userPic: "",
    oldPassword: "",
    newPassword: "",
    confirmPassword: "",
  });
  const reloadWindow = () => {
    window.location.reload();
  };
  useEffect(() => {
    const token = localStorage.getItem("jwtToken");
    Axios.get(`http://localhost:8080/getAccountDetails/${userName}`, {
      headers: { Authorization: `Bearer  ${token}` },
    })
      .then((response) => {
        setData(response.data);
        console.log(response.data);
      })
      .catch((error) => {
        alert("Something Went Wrong.");
        console.log(error);
      });
  }, []);
  const SubmitForm = () => {
    if (
      modifiedData.oldPassword.length > 0 &&
      modifiedData.newPassword.length > 0
    ) {
      if (modifiedData.newPassword === modifiedData.confirmPassword) {
        const postData = {
          userName: data.userName,
          userPassword: modifiedData.oldPassword,
        };
        const token = localStorage.getItem("jwtToken");
        const api = `http://localhost:8080/checkPassword`;
        Axios.post(api, postData, {
          headers: { Authorization: `Bearer  ${token}` },
        })
          .then((response) => {
            updateUserPassword();
            toast.success("🦄 Your password is upgraded sucessfully", {
              position: "top-center",
              autoClose: 5000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
          })
          .catch((error) => {
            console.log(error);
            toast.error("Old Password is Incorrect", {
              position: "top-center",
              autoClose: 5000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
          });
      } else {
        alert("New password and Confirm password is not Matching.");
      }
    } else {
      alert("Password field is Empty.");
    }
  };
  const updateUserPassword = () => {
    const formData = {
      userName: data.userName,
      userPassword: modifiedData.confirmPassword,
    };
    const token = localStorage.getItem("jwtToken");
    Axios.post(`http://localhost:8080/updatePassword`, formData, {
      headers: { Authorization: `Bearer  ${token}` },
    }).then((response) => {
      console.log("Password Changed Sucessfully");
    });
  };
  const updateProfilePic = () => {
    if (file === null) {
      alert("File Input is Empty.");
    } else {
      const formData = new FormData();
      formData.append("file", file);
      const token = localStorage.getItem("jwtToken");
      Axios.post(
        `http://localhost:8080/updateImage/${data.userName}/${data.userPic}`,
        formData,
        {
          headers: { "Content-Type": "multipart/form-data" },
          headers: { Authorization: `Bearer  ${token}` },
        }
      )

        .then((response) => {
          toast.success("🦄 Your profile pic is updated sucessfully", {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
          setTimeout(4000, reloadWindow);
        })
        .catch((error) => {
          alert("Something Went Wrong.");
          console.log(error);
        });
    }
  };

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-12 col-lg-10 col-xl-8 mx-auto">
          <h2 className="h3 mb-4 page-title text-info ">User Profile Settings</h2>
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
              <div className="row mt-5 align-items-center">
                <div className="col-md-3 text-center mb-5">
                  <div className="avatar avatar-xl">
                    <img
                      src={`http://localhost:8080/file/download/${data.userPic}`}
                      className="avatar-img rounded-circle"
                    />
                  </div>
                </div>
                <div className="col">
                  <div className="row align-items-center">
                    <div className="col-md-7">
                      <h4 className="mb-1">{data.userName}</h4>
                      <p className="small mb-3">
                        <span className="badge badge-dark">
                          {data.role[0].roleName}
                        </span>
                      </p>
                    </div>
                  </div>
                </div>
              </div>
              <hr className="my-4" />

              <div className="form-row">
                <div className="form-group col-md-4">
                  <label>Profile Picture</label>
                  <input
                    type="file" accept="image/png, image/jpeg"
                    onChange={(e) => {
                      var x = e.target.files[0].name;
                      var f = x.substr(0, x.lastIndexOf("."));
                      setFile(e.target.files[0]);
                      modifiedData({ ...modifiedData, userPic: f });
                    }}
                    className="form-control"
                    placeholder="98232"
                  />
                </div>
                <div className="form-group col-md-3">
                  <button
                    onClick={updateProfilePic}
                    type={"button"}
                    className={"btn"}
                  >
                    Update Profile Picture.
                  </button>
                </div>
              </div>
              <hr className="my-4" />
              <div className="row mb-4">
                <div className="col-md-6">
                  <div className="form-group">
                    <label for="inputPassword4">Old Password</label>
                    <input
                      type="password"
                      onChange={(event) => {
                        setModifiedData({
                          ...modifiedData,
                          oldPassword: event.target.value,
                        });
                      }}
                      className="form-control"
                    />
                  </div>
                  <div className="form-group">
                    <label for="inputPassword5">New Password</label>
                    <input
                      type="password"
                      onChange={(event) => {
                        setModifiedData({
                          ...modifiedData,
                          newPassword: event.target.value,
                          userPassword: event.target.value,
                        });
                      }}
                      className="form-control"
                    />
                  </div>
                  <div className="form-group">
                    <label for="inputPassword6">Confirm Password</label>
                    <input
                      type="password"
                      onChange={(event) => {
                        setModifiedData({
                          ...modifiedData,
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
                    <li>Minimum 8 character</li>
                    <li>At least one special character</li>
                    <li>At least one number</li>
                    <li>Can’t be the same as a previous password</li>
                  </ul>
                </div>
              </div>
              <button
                type={"button"}
                onClick={SubmitForm}
                className="btn btn-primary"
              >
                Save Change
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
