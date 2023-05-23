import React from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Login from './pages/Login';
import Clients from './pages/Clients';
import NewClient from './pages/NewClient';

function AppRoutes(){
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login/>}/>
                <Route path="/clients" element={<Clients/>}/>
                <Route path="/client/new/:clientId" element={<NewClient/>}/>
            </Routes>
        </BrowserRouter>
    );
}
export default AppRoutes;

