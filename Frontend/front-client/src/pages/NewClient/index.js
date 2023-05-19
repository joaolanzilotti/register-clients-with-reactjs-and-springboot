import React from 'react';

import './styles.css';
import logoJP from '../../assets/newUser.png';
import {Link} from "react-router-dom";
import {FiArrowLeft, FiUserPlus} from "react-icons/fi";

export default function NewClient(){
    return(
        <div className="new-client-container">
            <div className="content">
                <section className="form">
                    <img src={logoJP} alt="JP"/>
                    <h1>Add New Client</h1>
                    <p>Enter the client information and click on 'Add'!</p>
                    <Link className="back-link" to="/clients">
                        <div className="container-button">
                            <div className="iconArrowLeft"><FiArrowLeft size={16} color="blue"/></div>
                            <div className="textButton">Home</div>
                        </div>
                    </Link>
                </section>
                    <form>
                        <input placeholder="Name"/>
                        <input placeholder="E-mail"/>
                        <input placeholder="Password"/>
                        <input placeholder="RG"/>
                        <input placeholder="CPF"/>
                        <input type="date"/>
                        <input placeholder="Cellphone"/>
                        <button className="button" type="submit">Add</button>
                    </form>
            </div>
        </div>
    );
}