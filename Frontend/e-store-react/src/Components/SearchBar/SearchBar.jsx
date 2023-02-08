import React from 'react';
import {useState, useEffect} from "react";
import "./SearchBar.css";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function SearchBar(props) {
    const [value, setValue] = useState("");
    const [data, setData] = useState([]);
    let navigate = useNavigate();
    useEffect(()=>{
    document.title="Seach Product";
    },[]);
    const onChangeInput = (event) => {
        setValue(event.target.value);
        if(event.target.value.length===0)setData([]);
        else if(event.target.value.length>2){
            axios.get(`http://localhost:8080/api/products/navSearch/${event.target.value}`)
            .then(response=>{
                setData(response.data);
            })
        }
    };

    const onSearch = (productId) => {
        setValue("");
        navigate("/product/" + productId);
        setData([]);
    };

    return (
        <div className="Search">
            <div className="search-container">
                <div className="search-inner">
                    <input type="text" value={value} onChange={onChangeInput} placeholder={"Type 3 word for search"}/>
                    <button className="button" onClick={() => {
                        navigate("/allProduct/nameLike/"+value);
                        setValue("");
                        setData([]);
                    }}> Search</button>
                </div>
                <div className="dropdown">
                    {data
                        .map((item) => (
                            <div
                                onClick={() => onSearch(item.id)}
                                className="dropdown-row"
                                key={item.id}
                            >
                                {item.productName}
                            </div>
                        ))}
                </div>
            </div>
        </div>
    );
}

export default SearchBar;