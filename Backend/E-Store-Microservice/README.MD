<h1>Hello , This is E-Commerce Microservice Project </h1>

<h3>Microservices in this project are </h3>
   
    a. Service-Registry : It is use for Netflix Discovery Server.
    
    b. Api-Gateway : It uses Spring Cloud Gateway , Auth0 for OAuth2.0 Security .

    c. Product-Service: It is used to Store or Get Product including Sub-Products, Images , Brand , Description in rich text etc. 
    
    d. Inventory-Service: This service is used to store inventory data for products..
    
    e. Order-Service: This service is used for placing order , get order updates, get order details including address. 
    
    f. User-Service: This service stores user details including orders,liked product,user cart,user Address.
    
    g. ReviewAndQuestion-Service:  This service handle rating and Question Answers.

<h3> Technologies used in this project </h3>
    
    a. Data: Postgresql,MongoDb,SpringDataJpa,Filesystem for images.
   
    b. Discovery Server: Netflix Eureka.
    
    c. Routing: Spring Cloud Api gateway.
    
    d. Circuit-Breaker: Resilience4j circuit breaker.
    
    e. Distributing Tracing: Sleuth and Zipkin.

    f. Security: Auth0 OAuth2.0 for Securing Microsevice.

    g. Microservice Communication: Spring WebFlux is used for microservices to communicate with each-other. 
    
    h. RabbitMq is used for event-driven architecture. 

<h3> DataBase used in service </h3>
    
    a. Product-Service :- Postgresql,

    b. Inventory-Service :- Postgresql.

    c. User-Service :- Postgresql.

    d. Order-Service :- Postgresql.

    e. ReviewAndQuestion-Service :- MongoDb.

<h3> Postman Documentation </h3>
      https://documenter.getpostman.com/view/19485910/2s8Z6zzBjs
<h3> Steps </h3>

<h3>Project Port Configuration </h3>
    
    Eureka server port: http://localhost:8761/
    Api Gateway: http://localhost:8080

Step 1 : Getting Jwt Token from Auth0.

POST: https://dev-kx0i75g5w8igcuhs.us.auth0.com/oauth/token .

Body
    
    {
        "client_id": "Duck07bx3nlKUom54Ms1imnUytzm8yIY",
        "client_secret": "",
        "audience": "https://dev-kx0i75g5w8igcuhs.us.auth0.com/api/v2/",
        "grant_type": "client_credentials"
    }

Step 2 : Store user into User-Service.

GET : http://localhost:8080/api/user/isUserPresent/suryanshsharmami@gmail.com

Step 3 : Add User Address.

POST: http://localhost:8080/api/user/addUserAddress
    
Body
    
    {
        "userName":"suryanshsharmami@gmail.com",
        "line1":"(Rajesh Sharma) House no 4 Chaudhary Shikarpur Compound near D.M Residence",
        "city":"Bulandshahr",
        "pinCode":203001,
        "otherDetails":null
    }

Step4: Add New Brand.

POST: http://localhost:8080/api/brand

Body

    {
        "name":"Apple"
    }

Step 5 : Add Product to product service. It Will also store data about stock in Inventory Service.

NOTE : * For product description use rich text or leave empty. 

POST: http://localhost:8080/api/products/save

Body

    {
        "productName":"Sony PS5 Digital Edition Gaming Console (50668613, White)",
        "noOfStock":20,
        "text":"No Cost EMI/Additional Exchange Offers",
        "price":132999,
        "discount":5,
        "newPrice":139990,
        "productCategory":"Mobile",
        "description":"",
        "brandName":"Apple"
    }

sStep 6 : Add images for product. 

Note : * Use isPrimary as "Yes" for product primary image.

POST : http://localhost:8080/api/image/upload/Sony PS5 Digital Edition Gaming Console (50668613, White)

form-data 
    
    {
        image: pass image as file.
        isPrimary: pass Yes / No as text.
    }

Step 7 : Get Product Full details (Brand,isInStock,Images,SimilarProduct).

GET : http://localhost:8080/api/products/fullView-by-name/Sony PS5 Digital Edition Gaming Console (50668613, White)

Response
    
    {
        "id": 1,
        "productName": "Sony PS5 Digital Edition Gaming Console (50668613, White)",
        "ratings": 0,
        "noOfRatings": 0,
        "text": "Low cost EMI As low as ₹ 9,165 Per Month",
        "price": 54990.0,
        "discount": 2,
        "newPrice": 53890.2,
        "productImage": "sony_ps5.png",
        "imageUrl": "http://localhost:8080/api/image/download/sony_ps5.png",
        "productCategory": "Gaming",
        "description": {
            "id": null,
            "data": ""    
        },
        "brand": {
            "id": 1,
            "name": "Sony",
            "noOfProducts": 1
        },
        "productImages": [
            {
                "id": 1,
                "productId": 1,
                "imageName": "sony_ps5.png",
                "imageUrl": "http://localhost:8080/api/image/download/sony_ps5.png"
            },
            {
                "id": 2,
                "productId": 1,
                "imageName": "ps5.png",
                "imageUrl": "http://localhost:8080/api/image/download/ps5.png"
            }
        ],
        "productIsInStock": true,
        "inventoryData": {
            "productName": Sony PS5 Digital Edition Gaming Console (50668613, White),
            "productId": 1,
            "isInStock": true,
            "noOfStock": 20,
            "totalSold": 1
        },
        "similarProducts": [
            {
                "id": 1,
                "productName": "Sony PS5 Digital Edition Gaming Console (50668613, White)",
                "ratings": 0,
                "noOfRatings": 0,
                "text": "Low cost EMI As low as ₹ 9,165 Per Month",
                "price": 54990.0,
                "discount": 2,
                "newPrice": 53890.2,
                "productImage": "sony_ps5.png",
                "imageUrl": "http://localhost:8080/api/image/download/sony_ps5.png",
                "productCategory": "Gaming",
                "description": null,
                "brand": null,
                "productImages": null,
                "productIsInStock": null,
                "inventoryData": null,
                "similarProducts": null
            }
        ]
    }

Step 8 : Get product by name . This is use for Product card's.  

GET: http://localhost:8080/api/products/by-name/Sony PS5 Digital Edition Gaming Console (50668613, White)

Response
     
    {
        "id": 1,
        "productName": "Sony PS5 Digital Edition Gaming Console (50668613, White)",
        "ratings": 0,
        "noOfRatings": 0,
        "text": "Low cost EMI As low as ₹ 9,165 Per Month",
        "price": 54990.0,
        "discount": 2,
        "newPrice": 53890.2,
        "productImage": "sony_ps5.png",
        "imageUrl": "http://localhost:8080/api/image/download/sony_ps5.png",
        "productCategory": "Gaming",
        "description": null,
        "brand": null,
        "productImages": null,
        "productIsInStock": null,
    }

Step 9 : Get Product by brand.

GET : http://localhost:8080/api/brand/by-name/Sony?pageNo=0

Response :
    
    {
        "products": [
            {
                "id": 1,
                "productName": "Sony PS5 Digital Edition Gaming Console (50668613, White)",
                "ratings": 0,
                "noOfRatings": 0,
                "text": "Low cost EMI As low as ₹ 9,165 Per Month",
                "price": 54990,
                "discount": 2,
                "newPrice": 53890.2,
                "productImage": "sony_ps5.png",
                "imageUrl": "http://localhost:8080/api/image/download/sony_ps5.png",
                "productCategory": "Gaming",
                "description": null,
                "brand": null,
                "productImages": null,
                "productIsInStock": null,
                "inventoryData": null,
                "similarProducts": null
            }
        ],
        "currentPage": 1,
        "totalPages": 1,
        "totalData": 1
    }
    
Step 10 : Get products by their category.

GET : http://localhost:8080/api/products/by-category/Gaming?pageNo=0

Response:
    
    {
        "products": [
            {
                "id": 1,
                "productName": "Sony PS5 Digital Edition Gaming Console (50668613, White)",
                "ratings": 0,
                "noOfRatings": 0,
                "text": "Low cost EMI As low as ₹ 9,165 Per Month",
                "price": 54990.0,
                "discount": 2,
                "newPrice": 53890.2,
                "productImage": "sony_ps5.png",
                "imageUrl": "http://localhost:8080/api/image/download/sony_ps5.png",
                "productCategory": "Gaming",
                "description": null,
                "brand": null,
                "productImages": null,
                "productIsInStock": null,
                "inventoryData": null,
                "similarProducts": null
            }
        ],
        "currentPage": 1,
        "totalPages": 1,
        "totalData": 1
    }

Step 10 : Get Product by name like.

GET : http://localhost:8080/api/products/by-nameLike/Sony?pageNo=0

Response:

    {
        "products": [
            {
                "id": 9,
                "productName": "Apple iPhone 14 Pro Max 128GB Gold.",
                "ratings": 0,
                "noOfRatings": 0,
                "text": "EMI starts at ₹6,512. No Cost EMI available",
                "price": 139990.0,
                "discount": 5,
                "newPrice": 132999.0,
                "productImage": "iPhone14proGold.jpg",
                "imageUrl": "http://localhost:8080/api/image/download/iPhone14proGold.jpg",
                "productCategory": "Mobile",
                "description": null,
                "brand": null,
                "productImages": null,
                "productIsInStock": null,
                "inventoryData": null,
                "similarProducts": null
            },
            {
                "id": 10,
                "productName": "Apple Airpods Pro (2nd Genration)",
                "ratings": 0,
                "noOfRatings": 0,
                "text": "EMI starts at ₹1,285. No Cost EMI available.",
                "price": 26900.0,
                "discount": 0,
                "newPrice": 26900.0,
                "productImage": "airpodspro2.jpg",
                "imageUrl": "http://localhost:8080/api/image/download/airpodspro2.jpg",
                "productCategory": "Mobile Accessories",
                "description": null,
                "brand": null,
                "productImages": null,
                "productIsInStock": null,
                "inventoryData": null,
                "similarProducts": null
            }
        ],
        "currentPage": 1,
        "totalPages": 1,
        "totalData": 2
    }


Step 11 : Add Question for Product.

POST: http://localhost:8080/api/QuestionAndAnswer/addNewQuestion

BODY:

    {
        "text":"How much battery life you get on daily use ?",
        "username":"suryanshsharmami@gmail.com",
        "productId":1,
        "nickname":"Suryansh Sharma"
    }

Step 12 : Add Answer for Question of product.

POST: http://localhost:8080/api/QuestionAndAnswer/addAnswer

BODY:

    {
        "questionId":"63b065eacabae504eaec6797",
        "text":"Actually this year samsung added S-Pen so thick of phone is increased 👍.",
        "username":"suryanshsharmami@gmail.com"
    }

Step 13 : Get Questions of product by id .

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

Step 14 : Get Answer for question by questionId.

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

Step 15 : Add Review for product.

POST: http://localhost:8080/api/review/addReview

BODY:

    {
        "productId":1,
        "noOfStars":4,
        "text":"Battery Is Improved ",
        "userName":"suryanshsharma1942@gmail.com",
        "name":"Suryansh Sharma"
    }

Step : Get Product Review's by productId.

GET 16 : http://localhost:8080/api/review/getReviewsByProductId/1

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
    
    
    
    
    
Step 17 : Like a product. 

POST: http://localhost:8080/api/user/likeProduct

Body:
    
    {
        "userName":"suryanshsharma1942@gmail.com",
        "productId":1,
        "productName":"Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)"
    }

Step 18 : Check is product like by user.

api/user/isProductLikedByUser/{userName}/{productId}

GET: http://localhost:8080/api/user/isProductLikedByUser/suryanshsharmami@gmail.com/1

Step 19 : UnLike Product.

DELETE : http://localhost:8080/api/user/unLikeProduct

BODY:
    
    {
        "userName":"suryanshsharmami@gmail.com",
        "productId":1,
        "productName":"Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)"
    }

Step 20 : Get all liked product by user.

GET : http://localhost:8080/api/user/likedProducts-byUser/suryanshsharmami@gmail.com

Response:

    {
        "products": [
            {
                "id": 1,
                "productName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)",
                "ratings": 5,
                "noOfRatings": 2,
                "text": "No Cost EMI/Additional Exchange Offers",
                "price": 131999,
                "discount": 17,
                "newPrice": 109999,
                "productImage": "s22Burgundy1.jpg",
                "productCategory": "Mobile",
                "productIsInStock": null
            }
        ],
        "currentPage": 1,
        "totalPages": 1
    }

Step 21 : Add product to Cart

This will add only one product to Cart . After this you can update product quantity.

POST : http://localhost:8080/api/cart/addProductToCart

BODY:
    
    {
        "productId":1,
        "userName":"suryanshsharmami@gmail.com,
        "noOfProduct":1
    }

Step 22 : Get Cart Details by username

GET: http://localhost:8080/api/cart/getCartByUser/suryanshsharmami@gmail.com
    
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

Step 23 : For Updating Cart .

It Will take whole all Cart product as List input and id of product and cartItem.
It Will update cart total product in user and cart database , totalPrice.

POST: http://localhost:8080/api/cart/updateCartOfUser/suryanshsharmami@gmail.com

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

Step 24 : For Placing Order.

POST: http://localhost:8080/api/order/placeOrder/suryanshsharmami@gmail.com

Step 22 : Get Order by userName

GET: http://localhost:8080/api/order/getOrder-byUser/suryanshsharmami@gmail.com

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

Step 25 : Get Order Full details by id . 

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

Step 26 : Get All Pending Order.

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

Step 27 : Update Order details.

PUT : http://localhost:8080/api/order/updateProduct

Body:
        
    {
        "orderId":1,
        "isProductDelivered":true,
        "status":"Order is Delivered on 13 December"
    }
