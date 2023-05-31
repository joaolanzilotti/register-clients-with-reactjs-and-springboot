import React, {useEffect, useState} from 'react';
import './styles.css';
import loadingGif from '../../assets/loadingTwoWhite.gif';
import logoJP from '../../assets/logoJP.png';
import {Link, useNavigate, useParams} from "react-router-dom";
import {FiLogOut, FiList, FiX} from "react-icons/fi";
import {ToastContainer, toast} from "react-toast";

import api from '../../services/api';

export default function NavBar() {

    const [isMenuOpen, setMenuOpen] = useState(false);
    const username = localStorage.getItem('username');
    const navigate = useNavigate();

    async function logout() {
        localStorage.clear();
        navigate('/');
    }

    const handleToggleMenu = () => {
        setMenuOpen(!isMenuOpen);
    };

    return (
        <header>
            <nav className="navbar">
                <a href="#" className="logo">
                    <img src={logoJP} alt="logoJP"/>
                    <span>
          Welcome, <strong>{username}</strong>
        </span>
                </a>

                <input type="checkbox" id="toggler" checked={isMenuOpen} onChange={handleToggleMenu}/>
                <label htmlFor="toggler">
                    {isMenuOpen ? <FiX size={26} color="white"/> : <FiList size={26} color="white"/>}
                </label>
                <div className={`menu ${isMenuOpen ? 'open' : ''}`}>
                    <ul className="list">
                        <li><a href="#">Home</a></li>
                        <li><a href="#">Users</a></li>
                        <li className="liLogout"><button className="buttonLogout" type="button"><FiLogOut className="liIconLogout" onClick={() => logout()} size={20}/></button></li>
                    </ul>
                </div>
                <button type="button" className="logout"><FiLogOut className="iconLogout" onClick={() => logout()} size={24}/></button>
            </nav>
        </header>
    );
}









