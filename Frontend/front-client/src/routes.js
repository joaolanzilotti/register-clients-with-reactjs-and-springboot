import React from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Login from './pages/Login';
import Users from './pages/Users';
import NewUser from './pages/NewUser';
import NewAdress from './pages/NewAdress';

function AppRoutes(){
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login/>}/>
                <Route path="/users" element={<Users/>}/>
                <Route path="/user/new/:userId" element={<NewUser/>}/>
                <Route path="/user/newadress/:userId/:adressId" element={<NewAdress/>}/>
            </Routes>
        </BrowserRouter>
    );
}
export default AppRoutes;

