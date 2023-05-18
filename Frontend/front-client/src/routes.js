import React from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Login from './pages/Login';
import Client from './pages/Client';

function AppRoutes(){
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login/>}/>
                <Route path="/client" element={<Client/>}/>
            </Routes>
        </BrowserRouter>
    );
}
export default AppRoutes;

