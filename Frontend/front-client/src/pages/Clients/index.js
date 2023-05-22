import React, {useState, useEffect} from 'react';
import {Link, useNavigate} from "react-router-dom";
import {FiUserPlus, FiLogOut, FiEdit, FiTrash2} from 'react-icons/fi';

import api from '../../services/api';

import './styles.css';

import logoJP from '../../assets/logoJP.png';
import {toast} from "react-toast";

export default function Clients() {

    const erroDeleteClient = () => toast.error('Delete Failed!');
    const successfulDeleteClient = () => toast.success('Client Deleted Sucessful!');

    const username = localStorage.getItem('username');
    const accessToken = localStorage.getItem('accessToken');

    const navigate = useNavigate();

    const [clients, setClients] = useState([]);

    async function logout(){
        localStorage.clear();
        navigate('/');
    }

    async function deleteClient(id){
        try{

            await api.delete(`/api/clients/${id}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })
            setClients(clients.filter(client => client.id !== id))
            successfulDeleteClient();
        }catch (err){
            erroDeleteClient();
        }
    }

    // useEffect é pra carregar a tela assim que carregar o HTML os dados virao!
    useEffect(() => {
        api.get('/api/clients', {
            headers: {
                Authorization: `Bearer ${accessToken}`
            },
            params: {
                page: 0,
                size: 4,
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
                <button onClick={logout} className="buttonPower" type="button">
                    <FiLogOut size={18} color="#251FC5"/>
                </button>
            </header>
            <h1>Registered Clients</h1>
            <ul>
                {clients.map(client => (
                    <li key={client.id}>
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
                        <button onClick={() => deleteClient(client.id)} type="button">
                            <FiTrash2 size={20} color="#251FC5"/>
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

