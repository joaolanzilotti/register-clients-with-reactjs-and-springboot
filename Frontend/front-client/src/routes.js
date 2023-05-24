import React from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Login from './pages/Login';
import Users from './pages/Users';
import NewUser from './pages/NewUser';

function AppRoutes(){
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login/>}/>
                <Route path="/users" element={<Users/>}/>
                <Route path="/user/new/:userId" element={<NewUser/>}/>
            </Routes>
        </BrowserRouter>
    );
}
export default AppRoutes;

