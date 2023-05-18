import React from 'react';
import { Link } from "react-router-dom";
import { FiLogOut } from 'react-icons/fi';
import { FiUserPlus } from 'react-icons/fi';

import './styles.css';

import logoJP from '../../assets/logoJP.png';

function Client(){
    return(
        <div className="client-container">
            <header>
                <img src={logoJP} alt="JP"/>
                <span>Welcome, <strong>João</strong></span>
                <Link className="buttonClient" to="/client/new">
                    <div className="container-button">
                    <div className="iconUserPlus"><FiUserPlus size={24} color="white"/> </div>
                    <div className="textButton">Add new Client </div>
                    </div>
                    </Link>


                <button className="buttonPower" type="button">
                    <FiLogOut size={18} color="#251FC5"/>
                </button>
            </header>
        </div>
    );
}
export default Client;