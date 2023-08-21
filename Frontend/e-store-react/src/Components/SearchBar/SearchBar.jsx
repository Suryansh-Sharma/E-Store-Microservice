import React from "react";
import { useState, useEffect } from "react";
import "./SearchBar.css";
import axios from "axios";
import { AiOutlineSearch } from "react-icons/ai";
import { useNavigate } from "react-router-dom";
import LoadingPage from "../UtillsComponent/LoadingPage";

function SearchBar(props) {
  const [isLoading,setLoading] = useState(false);
  const [value, setValue] = useState("");
  const [data, setData] = useState(null);
  let navigate = useNavigate();
  let searchTimeout = null;
  useEffect(() => {
    document.title = "Seach Product";
  }, []);
  const onChangeInput = (event) => {

    clearTimeout(searchTimeout); // Clear any existing timeouts
    setValue(event.target.value);
    
    if (event.target.value.length === 0) {
      setValue("")
      setData(null);
    } else if (event.target.value.length > 2) {
      // Set a timeout to delay the API call by 500 milliseconds (adjust as needed)
      searchTimeout = setTimeout(() => {
        if(event.target.value.length < 2){
          return;
        }
        setLoading(true);
        console.log(event.target.value)
        axios
          .get(
            `http://localhost:8080/api/v1/elastic/product/search-with-elastic-fuzzy-and-suggestion/${event.target.value}?page_no=0&page_size=6`
          )
          .then((response) => {
            setData(response.data);
            console.log(response.data);
          })
          .catch((error) => {
            console.log(error);
          });
        setLoading(false);
      }, 2000); // Delay time in milliseconds
    }
  };
  const onSearch = (productId) => {
    setValue("");
    navigate("/search/" + productId);
    setData(null);
  };

  const handleSearchIconClick=()=>{
    if(value.length < 2){
      return;
    }
    setValue("");
    setData(null);
    navigate("/search/"+value);
  }
  if(isLoading){
    return <LoadingPage/>
  }
  return (
    <div className='Search'>
      <div className='search-container'>
        <div className='search-inner'>
          <input
            className={"input-search"}
            value={value}
            onChange={onChangeInput}
            placeholder={"Type 3 word for search"}
          />
          <div>
            <AiOutlineSearch className={"search-btn"} size={40} onClick={handleSearchIconClick}/>
          </div>
        </div>
        <div className='dropdown'>
          {data !== null
            ? data.productList.map((item) => (
                <div
                  onClick={() => onSearch(item.id)}
                  className='dropdown-row'
                  key={item.id}
                >
                  {item.name}
                  <hr />
                </div>
              ))
            : null}
        </div>
      </div>
    </div>
  );
}

export default SearchBar;
