import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Auth0Provider} from "@auth0/auth0-react";
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <Auth0Provider
        domain="dev-kx0i75g5w8igcuhs.us.auth0.com"
        clientId="TSVEQHImT2uwEC4ufaTqED7w3ye2Rsm9"
        redirectUri={window.location.origin}
        audience={"http://localhost:8080"}
        cacheLocation={'localstorage'}
        useRefreshTokens={true}
    >
            <App />
        </Auth0Provider>
);

reportWebVitals();