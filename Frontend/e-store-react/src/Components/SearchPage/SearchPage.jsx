import React, { useEffect, useState } from "react";
import "./SearchPage.css";
import { AiFillHeart } from "react-icons/ai";
import { AiFillStar } from "react-icons/ai";
import { Dropdown } from "react-bootstrap";
import { useParams } from "react-router-dom";
import axios from "axios";
import LoadingPage from "../UtillsComponent/LoadingPage";
const productJson = require("./ProductsRes.json");
function SearchPage() {
  const {value} = useParams();
  const [isLoading,setIsLoading] = useState(true);
  const [searchFilter, setSearchFilter] = useState({
    sort_by: "none",
    sort_direction: "none",
  });
  const [sortValue,setSortValue] = useState("None");
  const [products, setProducts] = useState(null);

  useEffect(()=>{
    const firstTimeUrl = `http://localhost:8080/api/v1/elastic/product/full-text-search?field=name&data=${value}&page_size=6&page_no=0&sort_by=rating&sort_directionection=asc`
    handleFetchProduct(firstTimeUrl);
    handleFilterFetch();
  },[value])

  useEffect(() => {
    const queryParams = new URLSearchParams();

    // Remove empty or undefined keys from the searchFilter
    for (const [key, value] of Object.entries(searchFilter)) {
      if (Array.isArray(value) && value.length === 0) {
        // Skip empty arrays
        continue;
      }
      if (value === "" || value === undefined) {
        // Skip empty or undefined values
        continue;
      }

      // Convert the array values to a comma-separated string
      if (Array.isArray(value)) {
        const commaSeparatedValues = value.join(",");
        queryParams.append(key, commaSeparatedValues);
      } else {
        // For single values, directly add to the URLSearchParams
        queryParams.append(key, value);
      }
    }

    let baseURL =
      `http://localhost:8080/api/v1/elastic/product/full-text-search/?field=name&data=${value}&page_size=6&is_filter_applied=true&page_no=0&`;

    if(queryParams.size <=2){
      baseURL = `http://localhost:8080/api/v1/elastic/product/full-text-search/?field=name&data=${value}&page_size=6&is_filter_applied=false&page_no=0&`;
    }
    if(queryParams.size ===3 && queryParams.has('categories')){
      baseURL = `http://localhost:8080/api/v1/elastic/product/full-text-search/?field=name&data=${value}&page_size=6&is_filter_applied=false&page_no=0&`;

    }
    console.log(queryParams);
    const filterURL = `${baseURL}&${queryParams.toString()}`;
    handleFetchProduct(filterURL);
  }, [searchFilter,setSearchFilter]);

  const [filtersData, setFiltersData] = useState({});

  const handleFilterValueChange = (category, option) => {
    setSearchFilter((prevSearchFilter) => {
      const updatedSearchFilter = { ...prevSearchFilter };

      if (!updatedSearchFilter[category]) {
        updatedSearchFilter[category] = [];
      }

      const index = updatedSearchFilter[category].indexOf(option);

      if (index === -1) {
        updatedSearchFilter[category].push(option);
      } else {
        updatedSearchFilter[category].splice(index, 1);
      }

      return updatedSearchFilter;
    });
  };

  const handleSortByChange = (value) => {
    console.log(value)
    if(value==="Ratings"){
      setSortValue("Ratings");
      setSearchFilter({...{sort_by:'rating',sort_direction:'desc'}});
    }else if(value==="Price (Low to High)"){
      setSortValue("Price (Low to High)");
      setSearchFilter({...{sort_by:'price.value',sort_direction:'asc'}});
    }else if(value==="Price (High to Low)"){
      setSortValue("Price (High to Low)");
      setSearchFilter({...{sort_by:'price.value',sort_direction:'desc'}});
    }else if(value==="Discount"){
      setSortValue("Discount");
      setSearchFilter({...{sort_by:'discount',sort_direction:'desc'}});
    }else if(value==="none"){
      setSortValue("None");
      setSearchFilter({...{sort_by:'none',sort_direction:'none'}});
    }
  };

  const handleFetchProduct=(url)=>{
    console.log(url);
    setIsLoading(true);
    axios.get(url)
    .then(response=>{
      console.log(response.data);
      setProducts(response.data);
      setIsLoading(false);
    })
    .catch(error=>{
      console.log(error);
    })
  }
  const handleFilterFetch=()=>{
    const url = `http://localhost:8080/api/v1/elastic/product/full-text-search/get-filters/${value}`;
    axios.get(url)
    .then(response=>{
      console.log(response.data);
      setFiltersData(response.data)
    })
    .catch(error=>{
      console.log(error);
    })
  }
  if(isLoading){
    return <LoadingPage/>
  }
  return (
    <div className={"SearchPage"}>
      <div className='Desktop-Filters'>
        <span className={"filter-key"}>SORT BY</span>

        <Dropdown className={""}
          onSelect={(eventKey) => handleSortByChange(eventKey)}
        >
          <Dropdown.Toggle variant={"outline-secondary"} size={"sm"} >
            <span className='filter-value'>
              {sortValue.toUpperCase()}
            </span>
          </Dropdown.Toggle>

          <Dropdown.Menu >
          <Dropdown.Item className='filter-value' eventKey={"none"}>
              <span>None</span>
            </Dropdown.Item>
            <Dropdown.Item className='filter-value' eventKey={"Ratings"}>
              <span>Ratings</span>
            </Dropdown.Item>
            <Dropdown.Item className='filter-value' eventKey={"Price (Low to High)"}>
              <span>Price (Low to High)</span>
            </Dropdown.Item>
            <Dropdown.Item className='filter-value' eventKey={"Price (High to Low)"}>
              <span>Price (High to Low)</span>
            </Dropdown.Item>
            <Dropdown.Item className='filter-value' eventKey={"Discount"}>
              <span>Discount</span>
            </Dropdown.Item>
            <Dropdown.Item className='filter-value' eventKey={"Trending"}>
              <span>Trending</span>
            </Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>

        <br />
        <span className={"filter-key mt-2"}>FILTER BY</span>
        <br />
        <hr />

        <div>
          {Object.keys(filtersData).map((category) => (
            <div key={category} className={"filter-box"}>
              <span className={"filter-key"}>{category.toUpperCase()}</span>
              <div>
                {filtersData[category].map((option) => (
                  <div key={option} className={"filter-value-choice"}>
                    <div className='filter-value-choice-checkbox'>
                      <input
                        type='checkbox'
                        name=''
                        id={option}
                        checked={
                          searchFilter[category] &&
                          searchFilter[category].includes(option)
                        }
                        onChange={() =>
                          handleFilterValueChange(category, option)
                        }
                      />
                    </div>
                    <div className='filter-value-choice-title'>
                      <span>{option}</span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>
      <div className='Products'>
        <div className='Products-Heaader'>
          <span className={"p-h-search-title"}>Result for "{value}"</span>
          <div className={"p-h-total-record"}>
            <span>{products!==null&&products.total_record} Products Found</span>
          </div>
        </div>
        {products!==null && products.productList.map((product) => (
          <div key={product.id}>
            <div className={"Product"} >
              <div className='Product-Image'>
                <img
                  src={product.image}
                  alt=''
                />
              </div>

              <div className='Product-Info'>
                <div className='Product-Info-Name'>
                  <span>{product.name}</span>
                </div>

                <div className='Product-Info-Short-Desc'>
                  <span>{product.description}</span>
                </div>

                <div className='Product-Info-Ratings'>
                  <div className='Product-Info-Ratings-Star'>
                    <span>{product.rating}</span>
                    <AiFillStar size={10} />
                  </div>
                  <div className='Product-Info-Ratings-Info'>
                    <span>( {product.rating} Ratings & {product.review} Reviews )</span>
                  </div>
                </div>

                <div className="Product-Info-Price">
                  <span className={"Product-Info-Price-Value"}>{product.newPrice.currency} {product.newPrice.value}</span>
                  <span style={{fontSize:'10px'}}>(Incl. all Taxes)</span>

                  <div className="Product-Info-Price-Discount">
                    <span className={"orignal-price"}>{product.price.currency} {product.price.value}</span>
                    <span className={"save-value"}>(save {product.price.currency} {(product.price.value - product.newPrice.value).toFixed(2)} )</span>
                    <div className="save-value-string">
                      <span>{product.discount} off</span>
                    </div>
                  </div>
                </div>

              </div>

              <div className='Product-Heart'>
                <AiFillHeart className={"Product-Heart-AiFillHeart"} size={20} />
              </div>
            </div>
            <hr />
          </div>
        ))}
      </div>
      <div className='Pagination'>
        <span>Pagination Section</span>
      </div>
    </div>
  );
}

export default SearchPage;
