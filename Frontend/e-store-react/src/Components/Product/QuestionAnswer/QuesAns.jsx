import { useAuth0 } from "@auth0/auth0-react";
import { default as axios, default as Axios } from "axios";
import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
  default as defaultPic
} from "../../../icons/defaultPic.png";
import "./QuesAns.css";

const QuesAns = ({ productId }) => {
  const {user,isAuthenticated}=useAuth0();
  const [commentsData, setCommentsData] = useState(null);
  const [ansData, setAnsData] = useState(null);
  const [showRepliedComment, setShowRepliedComment] = useState(false);
  const [questionId, setQuestionId] = useState(0);
  const [isLoading,setLoading]=useState(true);
  const postCommentReplyValues = {
    commentId: 0,
    userName: "",
    repliedTo: "",
    text: "",
    noOfReplies: 0,
  };
  const [postCommentValues, setPostCommentValues] = useState({
    postId: 0,
    text: "",
    userName: "",
  });

  useEffect(()=>{
    setTimeout(()=>{
      handleFetchData();
    },"2000");
  },[productId])
  const handleFetchData= async()=>{
    setLoading(true);
    axios.get(`http://localhost:8080/api/QuestionAndAnswer/getByProductId/${productId}?pageNo=0`)
    .then(response=>{
      setCommentsData(response.data);
      setLoading(false);
    })
    .catch(error=>{
      setCommentsData(null);
      setLoading(true);  
    })
  }
  if(isLoading)return <h5 style={{textAlign:'center'}}>Please wait untill Question Section is loading</h5>

  // To check if user is login then add comment only.
  const handleReplyButtonClick = () => {};

  const handleAddCommentToPost = () => {
    Axios.post(`http://localhost:8080/api/comments/save`, postCommentValues, {
      headers: {
        "Content-Type": "application/json",
        // Authorization: "Bearer " + Auth.jwtToken,
      },
    })
      .then((response) => {
        toast.success("🦄 Comment Added Successfully !!", {
          position: "top-center",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
        setTimeout(RefreshPage, 5000);
      })
      .catch((error) => {
      });
  };
  const RefreshPage = () => {
    window.location.reload();
  };
  // To handle comment reply.
  const handleCommentReply = () => {
  };
  const handleLoadAnswer = async (id) => {
    setAnsData();
  };


  return (
    <div
      className={"Comments"}
    >
      <div className="comments-top row">
        <img
          src={isAuthenticated ? `${user.picture}` : defaultPic}
          alt=""
          className={"col-1"}
        />
        <input
          type="text"
          placeholder={"Ask Question Regarding Product Here. "}
          className={"AddComment col-md-7"}
          onChange={(event) => {
            setPostCommentValues({
              ...postCommentValues,
              postId: productId,
              text: event.target.value,
              userName: user.nickname,
            });
          }}
        />
        <button
          className={"AddCommentButton col-md-2"}
          onClick={handleReplyButtonClick}
        >
          {isAuthenticated ? "Ask Question." : "Login/NewUser"}
        </button>
      </div>
      {commentsData.questions.map((data) => (
        <div
          className={"singleComment"}
          key={data.id}
        >
          <div className="comment row">
            <h6 className={"commentUsername col-4"}>@{data.nickname}</h6>
            <span className={"commentDate col-"}>
              Created on {"->   "}
              {data.date}
            </span>
            <span className={"comment-text col-md-10"}>{data.text}</span>
          </div>
          <div className="commentOptions">
            <span className={"Reply"}>Reply</span>
            <span
              onClick={() => {
                if (!showRepliedComment) {
                  setShowRepliedComment(true);
                  setQuestionId(data.id);
                  handleLoadAnswer(data.id);
                } else if (showRepliedComment) {
                  setShowRepliedComment(false);
                  setQuestionId(0);
                  setAnsData(null);
                }
              }}
            >
              {data.noOfAnswers > 0 ? "Show All Replies" : null}
            </span>
          </div>
          {questionId === data.id && ansData != null
            ? ansData.map((ans) => (
                <div className={"repliedCommentSection"} key={ans.id}>
                  <div className="comment row" key={ans.id}>
                    <h6 className={"commentUsername col-4"}>@{ans.nickname}</h6>
                    <span className={"commentDate col-"}>
                      Created on {"->   "}
                      {ans.date}
                    </span>
                    <span className={"comment-text col-md-10"}>{ans.text}</span>
                  </div>
                </div>
              ))
            : null}
        </div>
      ))}
        <nav aria-label="Page navigation example">
          <ul className="pagination">
            <li className="page-item">
              <a className="page-link">Previous</a>
            </li>
            <li className="page-item">
              <a className="page-link">
                Current Page: {commentsData.currentPage}
              </a>
            </li>
            <li className="page-item">
              <a className="page-link">Next</a>
            </li>
          </ul>
        </nav>
    </div>
  );
};

export default QuesAns;
