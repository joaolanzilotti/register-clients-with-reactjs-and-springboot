import React from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Login from './pages/Login';
import Users from './pages/Users';
import NewUser from './pages/NewUser';
import NewAdress from './pages/NewAdress';
import Page404 from './pages/Page404';

function AppRoutes(){
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login/>}/>
                <Route path="/users" element={<Users/>}/>
                <Route path="/user/new/:userId" element={<NewUser/>}/>
                <Route path="/user/newadress/:userId/:adressId" element={<NewAdress/>}/>
                <Route path="/page404" element={<Page404/>}/>
            </Routes>
        </BrowserRouter>
    );
}
export default AppRoutes;

