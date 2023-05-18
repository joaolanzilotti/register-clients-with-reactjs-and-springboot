import React from 'react';
import { Link } from "react-router-dom";
import { FiPower } from 'react-icons/fi';

import './styles.css';

import logoJP from '../../assets/logoJP.png';

function Client(){
    return(
        <div className="client-container">
            <header>
                <img src={logoJP} alt="JP"/>
                <span>Welcome, <strong>João</strong></span>
                <Link className="button" to="/client/new">Add new Client </Link>
                <button type="button">
                    <FiPower size={18} color="#251FC5"/>
                </button>
            </header>
        </div>
    );
}
export default Client;