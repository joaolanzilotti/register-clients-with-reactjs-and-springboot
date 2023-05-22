import React, {useState, useEffect} from 'react';
import {Link, useNavigate} from "react-router-dom";
import {FiUserPlus, FiLogOut, FiEdit, FiTrash2} from 'react-icons/fi';

import api from '../../services/api';

import './styles.css';

import logoJP from '../../assets/logoJP.png';

export default function Clients() {

    const username = localStorage.getItem('username');
    const accessToken = localStorage.getItem('accessToken');

    const navigate = useNavigate();

    const [clients, setClients] = useState([]);

    // useEffect é pra carregar a tela assim que carregar o HTML os dados virao!
    useEffect(() => {
        api.get('/api/clients', {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        }).then(response => {
            setClients(response.data._embedded.clientDTOList)
        })
    })

    return (
        <div className="client-container">
            <header>
                <img src={logoJP} alt="JP"/>
                <span>Welcome, <strong>{username.toUpperCase()}</strong></span>
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
                {clients.map(client => (
                    <li>
                        <strong>Name:</strong>
                        <p>{client.name}</p>
                        <strong>E-mail:</strong>
                        <p>{client.email}</p>
                        <strong>RG:</strong>
                        <p>{client.rg}</p>
                        <strong>CPF:</strong>
                        <p>{client.cpf}</p>
                        <strong>Birthday:</strong>
                        <p>{Intl.DateTimeFormat('pt-BR').format(new Date(client.birthDay))}</p>
                        <strong>Cellphone:</strong>
                        <p>{client.cellphone}</p>
                        <strong>Adress:</strong>
                        {client.adress ? (
                            <p>{client.adress.street + ", " + client.adress.district + ", " + client.adress.number}</p>
                        ) : (
                            <p>Endereço não disponível</p>
                        )}
                        <button type="button">
                            <FiEdit size={20} color="#251FC5"/>
                        </button>
                        <button type="button">
                            <FiTrash2 size={20} color="#251FC5"/>
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

