import React from 'react';

import './styles.css';
import logoJP from '../../assets/logoJP.png';
import {Link} from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi";

export default function NewClient(){
    return(
        <div className="new-client-container">
            <div className="content">
                <section>
                    <img src={logoJP} alt="JP"/>
                    <h1>Add New CLient</h1>
                    <p>Enter the client information and click on 'Add'!</p>
                    <Link className="back-link" to="/clients">
                        <FiArrowLeft size={16} color="#251FC5"/>
                        Home
                    </Link>
                    <form>
                        <input placeholder="Name"/>
                        <input placeholder="E-mail"/>
                        <input placeholder="Password"/>
                        <input placeholder="RG"/>
                        <input placeholder="CPF"/>
                        <input type="date"/>
                        <input placeholder="Cellphone"/>
                        <button className="buttonNewClient" type="submit">Add</button>
                    </form>
                </section>
            </div>
        </div>
    );
}