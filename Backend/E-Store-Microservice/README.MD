<h1>Hello , This is E-Commerce Microservice Project </h1>

<h3>Total Microservices in this project are </h3>
   
    a. Service-Registry : It is use for Netflix Discovery Server.
    
    b. Api-Gateway : It uses Spring Cloud Gateway , Okta for Oauth2.0 Security .

    c. Product-Service: For Get/Store Product including Sub-Products, Images , Brand , Description. 
    
    d. Inventory-Service: To store product stock updates.
    
    e. Order-Service: This service is used for placing order , get order updates, get order details including address. 
    
    f. User-Service: This service stores user details including orders,liked product,user cart,user Address.
    
    g. ReviewAndQuestion-Service:  This service handle rating and Question Answers.
<h3> Technologies used in this project </h3>
    
    a. Database: Postgresql,MongoDb,SpringDataJpa,Filesystem for images.
   
    b. Discovery Server: Netflix Eureka.
    
    c. Routing: Spring Cloud Api gateway.
    
    d. Circuit-Breaker: Resilience4j circuit breaker.
    
    e. Distributing Tracing: Sleuth and Zipkin.

    f. Security: Okta Oauth2.0 for Securing Microsevice.

    g. Microservice Communication: Spring WebFlux is used for microservices to communicate with each-other. 
<h3> DataBase used in service </h3>
    
    a. Product-Service,

    b. Inventory-Service.

    c. User-Service.

    d. Order-Service.

<h3>Project Port Configuration </h3>
    
    Eureka server port: http://localhost:8761/
    Api Gateway: http://localhost:8080
    
<h3> Postman Documentation <h3>
      https://documenter.getpostman.com/view/19485910/2s8Z6zzBjs
<h3> Steps </h3>
step 1 : Getting Jwt Token from Okta

use Browser: http://localhost:8080/authenticate/login .

    username: suryanshsharma1942@gmail.com
    password: suryansh@1904
step 2 : Get Okta Jwt Token from login Response.

step 3 : Add User Address.

POST: http://localhost:8080/api/user/addUserAddress
    
Body
    
    {
        "userName":"suryanshsharma1942@gmail.com",
        "line1":"(Rajesh Sharma) House no 4 Chaudhary Shikarpur Compound near D.M Residence",
        "city":"Bulandshahr",
        "pinCode":203001,
        "otherDetails":null
    }

step4: Add New Brand.

POST: http://localhost:8080/api/brand

Body
    
    {
        "name":"Samsung"
    }

step 5 : Add Product to product service. It Will also store data about stock in Inventory Service.

POST: http://localhost:8080/api/products/save

Body

    {
        "productName":"Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)",
        "text":"No Cost EMI/Additional Exchange Offers",
        "price":131999.00,
        "discount":17,
        "newPrice":109999.00,
        "productImage":"www.Samsung.com",
        "productCategory":"Mobile",
        "data":"The first Galaxy S with embedded S Pen. Write comfortably like pen on paper, turn quick notes into legible text and use Air Actions to control your phone remotely.",
        "dimensions":"9. millimeters x 78. millimeters x 163. millimeters;  228g",
        "specialFeatures":"2388-by-1668-pixel resolution at 264 pixels per inch (ppi) ",
        "brandName":"Samsung",
        "noOfStock":12
    }

step 6 : Get product by name . 

GET: http://localhost:8080/api/products/by-name/Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)

Response
     
    {
        "id": 1,
        "productName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)",
        "ratings": 0,
        "noOfRatings": 0,
        "text": "No Cost EMI/Additional Exchange Offers",
        "price": 131999,
        "discount": 17,
        "newPrice": 109999,
        "productImage": "www.Samsung.com",
        "productCategory": "Mobile",
        "description": null,
        "productIsInStock": null,
        "brand": null,
        "productImages": null,
        "subProducts": null
    }

step 7 : Add SubProduct for Product.

// Note:  Get SubProduct Parent id from Step 6 .

POST: http://localhost:8080/api/products/save-subProduct

Body
        
    {
        "productId": 1,
        "text":"with No Cost EMI/Additional Exchange Offers",
        "subProductName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 1Tb Storage)",
        "brandName": "Samsung",
        "noOfStock": 4,
        "price": 142999.00,
        "discount":17,
        "newPrice":118999,
        "productImage":"www.Samsung.com",
        "productCategory":"Mobile"
    }

Step 8 : Add Question for Product.

POST: http://localhost:8080/api/QuestionAndAnswer/addNewQuestion

BODY:

    {
        "text":"How much battery life you get on daily use ?",
        "username":"suryanshsharma1942@gmail.com",
        "productId":1
    }

Step 9 : Add Answer for Question of product.

POST: http://localhost:8080/api/QuestionAndAnswer/addAnswer

BODY:

    {
        "questionId":"63b065eacabae504eaec6797",
        "text":"Actually this year samsung added S-Pen so thick of phone is increased 👍.",
        "username":"suryanshsharma1942@gmail.com"
    }

step 10 : Get Product Full details (Brand,isInStock,Images,SubProduct)

GET: http://localhost:8080/api/products/fullView-by-name/Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)

Response:
    
    {
        "id": 1,
        "productName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)",
        "ratings": 0,
        "noOfRatings": 0,
        "text": "No Cost EMI/Additional Exchange Offers",
        "price": 131999.0,
        "discount": 17,
        "newPrice": 109999.0,
        "productImage": "s22Burgundy1.jpg",
        "imageUrl": "http://geekyprogrammer:8080/api/image/download/s22Burgundy1.jpg",
        "productCategory": "Mobile",
        "description": {
            "id": 1,
            "data": "The first Galaxy S with embedded S Pen. Write comfortably like pen on paper, turn quick notes into legible text and use Air Actions to control your phone remotely.",
            "releaseDate": "2022-12-14T05:11:28.979150Z",
            "dimensions": "9. millimeters x 78. millimeters x 163. millimeters;  228g",
            "specialFeatures": "2388-by-1668-pixel resolution at 264 pixels per inch (ppi) "
        },
        "productIsInStock": true,
        "brand": {
            "id": 1,
            "name": "Samsung",
            "noOfProducts": 2,
            "products": null
        },
        "productImages": [
            {
                "id": 1,
                "productId": 1,
                "imageName": "s22Burgundy1.jpg",
                "imageUrl": "http://localhost:8080/api/image/download/s22Burgundy1.jpg"
            },
            {
                "id": 2,
                "productId": 1,
                "imageName": "s22Burgundy2.jpg",
                "imageUrl": "http://localhost:8080/api/image/download/s22Burgundy2.jpg"
            }
        ],
        "subProducts": [
            {
                "id": 1,
                "productId": 1,
                "subProductName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 1Tb Storage)",
                "price": 142999.0
            }
        ]
    }

Step 11 : Get Questions of product by id .

GET: http://localhost:8080/api/QuestionAndAnswer/getByProductId/1?pageNo=0

Response:
    
    {
        "questions": [
        {
            "id": "63b065eacabae504eaec6797",
            "productId": 1,
            "text": "What is thickness of this product",
            "username": "suryanshsharma1942@gmail.com",
            "date": "13 minutes ago",
            "noOfAnswers": 2
        },
        {
            "id": "63b06869cabae504eaec679a",
            "productId": 1,
            "text": "How much battery life you get on daily use ?",
            "username": "suryanshsharma1942@gmail.com",
            "date": "2 minutes ago",
            "noOfAnswers": 0
        }
        ],
        "totalPage": 1,
        "currentPage": 0
    }

Step 12 : Get Answer for question by questionId.

GET: http://localhost:8080/api/QuestionAndAnswer/getAnswerByQuestionId/63b065eacabae504eaec6797

Response:
    
    [
        {
            "id": "63b066ffcabae504eaec6798",
            "questionId": "63b065eacabae504eaec6797",
            "text": "The thickness of s22 Ultra is 8.9 mm .",
            "username": "suryanshsharma1942@gmail.com",
            "date": "7 minutes ago"
        },
        {
            "id": "63b067e6cabae504eaec6799",
            "questionId": "63b065eacabae504eaec6797",
            "text": "Actually this year samsung added S-Pen so thick of phone is increased 👍.",
            "username": "suryanshsharma1942@gmail.com",
            "date": "4 minutes ago"
        }
    ]

Step 13 : Add Review for product.

POST: http://localhost:8080/api/review/addReview

BODY:

    {
        "productId":1,
        "noOfStars":4,
        "text":"Battery Is Improved ",
        "userName":"suryanshsharma1942@gmail.com"
    }

Step : Get Product Review's by productId.

GET 14 : http://localhost:8080/api/review/getReviewsByProductId/1

RESPONSE:
    
    {
        "reviews": [
            {
                "id": "63afee9cac9c997a9befd57b",
                "productId": 1,
                "noOfStars": 5,
                "text": "Fully Satisfied with new camera and display.Battery is also improved.",
                "dateOfReview": "moments ago",
                "userName": "suryanshsharma1942@gmail.com"
            },
            {
                "id": "63afeeacac9c997a9befd57c",
                "productId": 1,
                "noOfStars": 4,
                "text": "Battery Is Improved ",
                "dateOfReview": "moments ago",
                "userName": "suryanshsharma1942@gmail.com"
            }
        ],
        "currentPage": 0,
        "totalPage": 1
    }
    
    
    
    
    
Step 15 : Like a product. 

POST: http://localhost:8080/api/user/likeProduct

Body:
    
    {
        "userName":"suryanshsharma1942@gmail.com",
        "productId":1,
        "productName":"Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)"
    }

Step 16 : Check is product like by user.

/api/user/isProductLikedByUser/{userName}/{productId}

GET: http://localhost:8080/api/user/isProductLikedByUser/SuryanshSharma@1942/1

Step 17 : UnLike Product.

DELETE : http://localhost:8080/api/user/unLikeProduct

BODY:
    
    {
        "userName":"suryanshsharma1942@gmail.com",
        "productId":1,
        "productName":"Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)"
    }

Step 18 : Add product to Cart

This will add only one product to Cart . After this you can update product quantity.

POST : http://localhost:8080/api/cart/addProductToCart

BODY:
    
    {
        "productId":1,
        "userName":"suryanshsharma1942@gmail.com",
        "noOfProduct":1
    }

Step 19 : Get Cart Details by username

GET: http://localhost:8080/api/cart/getCartByUser/suryanshsharma1942@gmail.com
    
Response:
        
    {
        "cartProduct": [
            {
                "id": 1,
                "isInStock": true,
                "productId": 1,
                "productName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)",
                "price": 131999.0,
                "totalPrice": 131999.0,
                "noOfProduct": 1
            }
        ],
        "totalProducts": 1,
        "totalPrice": 131999.0
    }    

Step 20 : For Updating Cart .

It Will take whole all Cart product as List input and id of product and cartItem.
It Will update cart total product in user and cart database , totalPrice.

POST: http://localhost:8080/api/cart/updateCartOfUser/suryanshsharma1942@gmail.com

BODY:

    [
        {
            "id": 3,
            "isInStock": true,
            "noOfStock": 12,
            "productId": 1,
            "productName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)",
            "productImage": "s22Burgundy1.jpg",
            "imageUrl": "http://geekyprogrammer:8080/api/image/download/s22Burgundy1.jpg",
            "price": 131999.0,
            "totalPrice": 131999.0,
            "noOfProduct": 1
        }
    ]

Step 21 : For Placing Order.

POST: http://localhost:8080/api/order/placeOrder/suryanshsharma1942@gmail.com

Step 22 : Get Order by userName

GET: http://localhost:8080/api/order/getOrder-byUser/suryanshsharma1942@gmail.com

Response:
        
    [
        {
            "id": 1,
            "userId": 1,
            "orderDate": "8 minutes ago",
            "lastUpdate": "8 minutes ago",
            "status": "Submitted To Seller",
            "totalItems": 2,
            "price": 263998.0,
            "isProductDelivered": false
        }
    ]

Step 23 : Get Order Full details by id . 

GET: http://localhost:8080/api/order/getOrderDetails/1

Response:
    
    {
        "orderId": 1,
        "status": "Submitted To Seller",
        "totalItems": 2,
        "price": 263998.0,
        "line1": "(Rajesh Sharma) House no 4 Chaudhary Shikarpur Compound near D.M Residence",
        "city": "Bulandshahr",
        "pinCode": 203001,
        "otherDetails": null,
        "isProductDelivered": false,
        "orderItems": [
            {
                "itemId": 1,
                "orderId": 1,
                "productId": 1,
                "productName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)",
                "quantity": 2,
                "price": 131999.0
            }
        ]
    }

Step 24 : Get All Pending Order.

GET: http://localhost:8080/api/order/getAllPendingOrder

Response:
        
    [
       {
        "id": 1,
        "userId": 1,
        "orderDate": "10 minutes ago",
        "lastUpdate": "10 minutes ago",
        "status": "Submitted To Seller",
        "totalItems": 2,
        "price": 263998.0,
        "isProductDelivered": false
        }
    ]

Step 25 : Update Order details.

PUT : http://localhost:8080/api/order/updateProduct

Body:
        
    {
        "orderId":1,
        "isProductDelivered":true,
        "status":"Order is Delivered on 13 December"
    }
