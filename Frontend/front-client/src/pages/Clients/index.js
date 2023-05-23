import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiUserPlus, FiLogOut, FiEdit, FiTrash2, FiAlertTriangle, FiX } from 'react-icons/fi';
import { toast } from 'react-toast';

import api from '../../services/api';

import './styles.css';

import logoJP from '../../assets/logoJP.png';

export default function Clients() {
    const username = localStorage.getItem('username');
    const accessToken = localStorage.getItem('accessToken');

    const navigate = useNavigate();
    const [showConfirmation, setShowConfirmation] = useState(false);
    const [selectedClientId, setSelectedClientId] = useState(null);
    const [selectedClientName, setSelectedClientName] = useState(null);

    const [clients, setClients] = useState([]);
    const [page, setPage] = useState(0);

    async function logout() {
        localStorage.clear();
        navigate('/');
    }

    async function editClient(id) {
        try {
            navigate(`/client/new/${id}`);
        } catch (error) {
            toast.error('Edit Failed! Try Again.');
        }
    }

    async function deleteClient(id) {
        try {
            await api.delete(`/api/clients/${id}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            });
            setClients(clients.filter((client) => client.id !== id));
            toast.success('Client Deleted Successfully!');
        } catch (err) {
            toast.error('Delete Failed!');
        }
    }

    async function fetchMoreClients() {
        const response = await api.get('/api/clients', {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
            params: {
                page: page,
                size: 4,
            },
        });

        setClients([...clients, ...response.data._embedded.clientDTOList]);
        setPage(page + 1);
    }

    function handleDeleteClick(id, name) {
        setSelectedClientId(id);
        setSelectedClientName(name)
        setShowConfirmation(true);
    }

    function handleConfirmDelete() {
        deleteClient(selectedClientId);
        setShowConfirmation(false);
    }

    function handleCancelDelete() {
        setSelectedClientId(null);
        setSelectedClientName(null);
        setShowConfirmation(false);
    }

    // useEffect é para carregar a tela assim que carregar o HTML, os dados virão!
    useEffect(() => {
        fetchMoreClients();
    }, []);

    return (
        <div className="client-container">
            <header>
                <img src={logoJP} alt="JP" />
                <span>
          Welcome, <strong>{username.toUpperCase()}</strong>
        </span>
                <Link className="buttonClient" to="/client/new/0">
                    <div className="container-button">
                        <div className="iconUserPlus">
                            <FiUserPlus size={24} color="white" />
                        </div>
                        <div className="textButton">Add new Client</div>
                    </div>
                </Link>

                <button onClick={logout} className="buttonPower" type="button">
                    <FiLogOut size={18} color="#251FC5" />
                </button>
            </header>
            <h1>Registered Clients</h1>
            <ul>
                {clients.map((client) => (
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
                        <p>
                            {Intl.DateTimeFormat('pt-BR').format(new Date(client.birthDay))}
                        </p>
                        <strong>Cellphone:</strong>
                        <p>{client.cellphone}</p>
                        <strong>Adress:</strong>
                        {client.adress ? (
                            <p>
                                {client.adress.street +
                                    ', ' +
                                    client.adress.district +
                                    ', ' +
                                    client.adress.number}
                            </p>
                        ) : (
                            <p>Endereço não disponível</p>
                        )}

                        <button type="button">
                            <FiEdit onClick={() => editClient(client.id)} size={20} color="#251FC5" />
                        </button>
                        <button onClick={() => handleDeleteClick(client.id, client.name)} type="button">
                            <FiTrash2 size={20} color="#251FC5" />
                        </button>
                    </li>
                ))}
            </ul>
            <button className="buttonMorePage" onClick={fetchMoreClients} type="button">
                Load More
            </button>

            {showConfirmation && (
                <div className="modal">
                    <button className="buttonCloseModal" onClick={handleCancelDelete}><FiX size={20} color='gray' /></button>
                    <div className="modal-content">
                        <div className="iconContainer">
                        <FiAlertTriangle className="iconAlertModal" size="45" color='orange' />
                        </div>
                        <h2>Confirmation</h2>
                        <p>Are you sure you want to delete client?</p>
                        <h5>{selectedClientName}</h5>
                        <button className="buttonConfirmDialog" onClick={handleConfirmDelete}>Delete</button>
                        <button className="buttonCancelDialog" onClick={handleCancelDelete}>Cancel</button>
                    </div>
                </div>
            )}
        </div>
    );
}
