import React from 'react';
import {Link} from "react-router-dom";
import {FiUserPlus, FiLogOut, FiEdit, FiTrash2} from 'react-icons/fi';

import './styles.css';

import logoJP from '../../assets/logoJP.png';

export default function Clients() {
    return (
        <div className="client-container">
            <header>
                <img src={logoJP} alt="JP"/>
                <span>Welcome, <strong>João</strong></span>
                <Link className="buttonClient" to="/client/new">
                    <div className="container-button">
                        <div className="iconUserPlus"><FiUserPlus size={24} color="white"/></div>
                        <div className="textButton">Add new Client</div>
                    </div>
                </Link>
                <button className="buttonPower" type="button">
                    <FiLogOut size={18} color="#251FC5"/>
                </button>
            </header>
            <h1>Registered Clients</h1>
            <ul>
                <li>
                    <strong>Name:</strong>
                    <p>João Pedro</p>
                    <strong>E-mail:</strong>
                    <p>teste@gmail.com</p>
                    <strong>RG:</strong>
                    <p>545589871</p>
                    <strong>CPF:</strong>
                    <p>46998565841</p>
                    <strong>Birthday:</strong>
                    <p>04/01/2003</p>
                    <strong>Cellphone:</strong>
                    <p>21994778998</p>
                    <strong>Adress:</strong>
                    <p>Rua Maranhão</p>
                    <button type="button">
                        <FiEdit size={20} color="#251FC5"/>
                    </button>
                    <button type="button">
                        <FiTrash2 size={20} color="#251FC5"/>
                    </button>
                </li>
                <li>
                    <strong>Name:</strong>
                    <p>João Pedro</p>
                    <strong>E-mail:</strong>
                    <p>teste@gmail.com</p>
                    <strong>RG:</strong>
                    <p>545589871</p>
                    <strong>CPF:</strong>
                    <p>46998565841</p>
                    <strong>Birthday:</strong>
                    <p>04/01/2003</p>
                    <strong>Cellphone:</strong>
                    <p>21994778998</p>
                    <strong>Adress:</strong>
                    <p>Rua Maranhão</p>
                    <button type="button">
                        <FiEdit size={20} color="#251FC5"/>
                    </button>
                    <button type="button">
                        <FiTrash2 size={20} color="#251FC5"/>
                    </button>
                </li>
                <li>
                    <strong>Name:</strong>
                    <p>João Pedro</p>
                    <strong>E-mail:</strong>
                    <p>teste@gmail.com</p>
                    <strong>RG:</strong>
                    <p>545589871</p>
                    <strong>CPF:</strong>
                    <p>46998565841</p>
                    <strong>Birthday:</strong>
                    <p>04/01/2003</p>
                    <strong>Cellphone:</strong>
                    <p>21994778998</p>
                    <strong>Adress:</strong>
                    <p>Rua Maranhão</p>
                    <button type="button">
                        <FiEdit size={20} color="#251FC5"/>
                    </button>
                    <button type="button">
                        <FiTrash2 size={20} color="#251FC5"/>
                    </button>
                </li>
                <li>
                    <strong>Name:</strong>
                    <p>João Pedro</p>
                    <strong>E-mail:</strong>
                    <p>teste@gmail.com</p>
                    <strong>RG:</strong>
                    <p>545589871</p>
                    <strong>CPF:</strong>
                    <p>46998565841</p>
                    <strong>Birthday:</strong>
                    <p>04/01/2003</p>
                    <strong>Cellphone:</strong>
                    <p>21994778998</p>
                    <strong>Adress:</strong>
                    <p>Rua Maranhão</p>
                    <button type="button">
                        <FiEdit size={20} color="#251FC5"/>
                    </button>
                    <button type="button">
                        <FiTrash2 size={20} color="#251FC5"/>
                    </button>
                </li>

            </ul>
        </div>
    );
}

