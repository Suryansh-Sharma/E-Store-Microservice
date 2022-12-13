<h1>Hello , This is E-Commerce Microservice Project </h1>

<h3>Total Microservices in this project are </h3>
   
    a. Service-Registry : It is use for Netflix Discovery Server.
    
    b. Api-Gateway : It uses Spring Cloud Gateway , Okta for Oauth2.0 Security .

    c. Product-Service: For Get/Store Product including Sub-Products, Images , Brand , Description. 
    
    d. Inventory-Service: To store product stock updates.
    
    e. Order-Service: This service is used for placing order , get order updates, get order details including address. 
    
    f. User-Service: This service stores user details including orders,liked product,user cart,user Address.
<h3> Technologies used in this project </h3>
    
    a. Database: Postgresql,SpringDataJpa,Filesystem for images.
   
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
    Api Gateway: http://geekyprogrammer:8080
<h3> Steps </h3>
step1: Getting Jwt Token from Okta

use Browser: http://localhost:8080/authenticate/login .

    username: suryanshsharma1942@gmail.com
    password: suryansh@1904
step2: Add User in Database.

POST : http://geekyprogrammer:8080/api/user/addUser/SuryanshSharma@1942

step3: Add User Address.

POST: http://geekyprogrammer:8080/api/user/addUserAddress
    
Body
    
    {
        "userName":"SuryanshSharma@1942",
        "line1":"(Rajesh Sharma) House no 4 Chaudhary Shikarpur Compound near D.M Residence",
        "city":"Bulandshahr",
        "pinCode":203001,
        "otherDetails":null
    }

step4: Add New Brand.

POST: http://geekyprogrammer:8080/api/brand

Body
    
    {
        "name":"Samsung"
    }

step5: Add Product to product service. It Will also store data about stock in Inventory Service.

POST: http://geekyprogrammer:8080/api/products/save

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

step6: Get product by name . 

GET: http://geekyprogrammer:8080/api/products/by-name/Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)

Response
     
    {
        "id": 25,
        "productName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)",
        "ratings": 0,
        "noOfRatings": 0,
        "text": "No Cost EMI/Additional Exchange Offers",
        "price": 131999.0,
        "discount": 17,
        "newPrice": 109999.0,
        "productImage": "www.Samsung.com",
        "productCategory": "Mobile",
        "description": null,
        "productIsInStock": null,
        "brand": null,
        "productImages": null,
        "subProducts": null
    }

step7: Add SubProduct for Product.

// Note:  Get SubProduct Parent id from Step7 .

POST: http://localhost:8080/api/products/save-subProduct

Body
        
    {
        "productId": 25,
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

step8: Get Product Full details (Brand,isInStock,Images,SubProduct)

GET: http://geekyprogrammer:8080/api/products/fullView-by-name/Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)

Response:
    
    {
        "id": 25,
        "productName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)",
        "ratings": 0,
        "noOfRatings": 0,
        "text": "No Cost EMI/Additional Exchange Offers",
        "price": 131999.0,
        "discount": 17,
        "newPrice": 109999.0,
        "productImage": "www.Samsung.com",
        "productCategory": "Mobile",
        "description": null,
        "productIsInStock": true,
        "brand": {
            "id": 2,
            "name": "Samsung",
            "noOfProducts": 7,
            "products": null
        },
        "productImages": [
            {
                "id": 1,
                "productId": 25,
                "imageName": "PersonalPortfolio.png",
                "imageUrl": "http://localhost:8080/api/image/download/PersonalPortfolio.png"
            }
        ],
        "subProducts": [
            {
                "id": 4,
                "productId": 25,
                "subProductName": "Samsung Galaxy S22 Ultra 5G (Black, 256GB Storage)",
                "price": 131999.0
            },
            {
                "id": 7,
                "productId": 25,
                "subProductName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 1Tb Storage)",
                "price": 142999.0
            }
        ]
    }

step9 : Like a product. 

POST: http://geekyprogrammer:8080/api/user/likeProduct

Body:
    
    {
        "userName":"SuryanshSharma@1942",
        "productId":25,
        "productName":"Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)"
    }

step10: Check is product like by user.

/api/user/isProductLikedByUser/{userName}/{productId}

GET: http://geekyprogrammer:8080/api/user/isProductLikedByUser/SuryanshSharma@1942/25

step11: UnLike Product.

DELETE : http://geekyprogrammer:8080/api/user/unLikeProduct

BODY:
    
    {
        "userName":"SuryanshSharma@1942",
        "productId":25,
        "productName":"Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)"
    }

step12: Add product to Cart

This will add only one product to Cart . After this you can update product quantity.

POST : http://geekyprogrammer:8080/api/cart/addProductToCart

BODY:
    
    {
        "productId":25,
        "userName":"SuryanshSharma@1942",
        "noOfProduct":1
    }

step13: Get Cart Details by username

GET: http://geekyprogrammer:8080/api/cart/getCartByUser/SuryanshSharma@1942
    
Response:
        
    {
        "cartProduct": [
            {
                "id": 6,
                "isInStock": true,
                "productId": 25,
                "productName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)",
                "price": 131999.0,
                "totalPrice": 131999.0,
                "noOfProduct": 1
            }
        ],
        "totalProducts": 1,
        "totalPrice": 131999.0
    }    

step14: For Updating Cart .

It Will take whole all Cart product as List input and id of product and cartItem.
It Will update cart total product in user and cart database , totalPrice.

POST: http://geekyprogrammer:8080/api/cart/updateCartOfUser/SuryanshSharma@1942

BODY:
    
    [
        {
            "id": 6,
            "productId": 25,
            "noOfProduct": 4
        }
    ]

step15 : For Placing Order. 

// Send same Response of step13 in Body of Order Place Request.  

POST: http://geekyprogrammer:8080/api/order/placeOrder/SuryanshSharma@1942

BODY:
    
    {
        "cartProduct": [
            {
                "id": 6,
                "isInStock": true,
                "productId": 25,
                "productName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)",
                "price": 131999.0,
                "totalPrice": 527996.0,
                "noOfProduct": 4
            }
        ],
        "totalProducts": 4,
        "totalPrice": 527996.0
    }

step16: Get Order by userName

GET: http://geekyprogrammer:8080/api/order/getOrder-byUser/SuryanshSharma@1942

Response:
        
    [
        {
            "id": 5,
            "userId": 2,
            "orderDate": "moments ago",
            "lastUpdate": "moments ago",
            "status": "Submitted To Seller",
            "totalItems": 4,
            "price": 527996.0,
            "isProductDelivered": false
        },
        {
            "id": 4,
            "userId": 2,
            "orderDate": "2 hours ago",
            "lastUpdate": "2 hours ago",
            "status": "Submitted To Seller",
            "totalItems": 4,
            "price": 527996.0,
            "isProductDelivered": false
        }
    ]

step17: Get Order Full details by id . 

GET: http://geekyprogrammer:8080/api/order/getOrderDetails/5

BODY:
    
    {
        "orderId": 5,
        "status": "Submitted To Seller",
        "totalItems": 4,
        "price": 527996.0,
        "line1": "(Rajesh Sharma) House no 4 Chaudhary Shikarpur Compound near D.M Residence",
        "city": "Bulandshahr",
        "pinCode": 203001,
        "otherDetails": null,
        "isProductDelivered": false,
        "orderItems": [
            {
                "itemId": 6,
                "orderId": 5,
                "productId": 25,
                "productName": "Samsung Galaxy S22 Ultra 5G (Burgundy, 12GB, 256GB Storage)",
                "quantity": 4,
                "price": 131999.0
            }
        ]
    }

step19 : Get All Pending Order.

GET: http://geekyprogrammer:8080/api/order/getAllPendingOrder

Response:
        
        [
            {
                "id": 4,
                "userId": 2,
                "orderDate": "2 hours ago",
                "lastUpdate": "2 hours ago",
                "status": "Submitted To Seller",
                "totalItems": 4,
                "price": 527996.0,
                "isProductDelivered": false
            },
            {
                "id": 5,
                "userId": 2,
                "orderDate": "5 minutes ago",
                "lastUpdate": "5 minutes ago",
                "status": "Submitted To Seller",
                "totalItems": 4,
                "price": 527996.0,
                "isProductDelivered": false
            }
        ]

step20 : Update Order details.

PUT : http://geekyprogrammer:8080/api/order/updateProduct

Body:
        
    {
        "orderId":4,
        "isProductDelivered":true,
        "status":"Order is Delivered on 13 December"
    }