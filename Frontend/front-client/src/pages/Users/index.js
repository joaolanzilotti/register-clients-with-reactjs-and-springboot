import React, {useState, useEffect} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {FiUserPlus, FiLogOut, FiEdit, FiTrash2, FiAlertTriangle, FiX, FiPlusCircle} from 'react-icons/fi';
import {toast, ToastContainer} from 'react-toast';

import api from '../../services/api';

import './styles.css';

import logoJP from '../../assets/logoJP.png';

export default function Users() {
    const email = localStorage.getItem('email');
    const accessToken = localStorage.getItem('accessToken');
    const username = localStorage.getItem('username');

    const navigate = useNavigate();
    const [showConfirmation, setShowConfirmation] = useState(false);
    const [showConfirmationAdress, setShowConfirmationAdress] = useState(false);
    const [selectedUserId, setSelectedUserId] = useState(null);
    const [selectedUserName, setSelectedUserName] = useState(null);
    const [selectedAdress, setSelectedAdress] = useState(null);

    const [users, setUsers] = useState([]);
    const [page, setPage] = useState(0);

    async function editAdress(idUser, idadress) {
        try {
            navigate(`/user/newadress/${idUser}/${idadress}`);
        } catch (error) {
            toast.error('Edit Failed! Try Again.');
        }
    }

    async function editUser(id) {
        try {
            navigate(`/user/new/${id}`);
        } catch (error) {
            toast.error('Edit Failed! Try Again.');
        }
    }

    async function deleteUser(id) {
        try {
            await api.delete(`/api/users/${id}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            });
            toast.success('User Deleted Successfully!');
            setUsers(users.filter((user) => user.id !== id));
        } catch (err) {
            toast.error('Delete Failed!');
        }
    }

    async function deleteAdress(id) {
        try {
            await api.delete(`/api/adress/${id}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            });
            toast.success('Adress Deleted Successfully!');
            setUsers(users.filter((user) => user.adress.id !== id));

        } catch (err) {
            toast.error('Delete Failed!');
        }
    }

    async function fetchMoreUsers() {
        const response = await api.get('/api/users', {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
            params: {
                page: page,
                size: 4,
            },
        });

        setUsers([...users, ...response.data._embedded.userDTOList]);
        setPage(page + 1);
    }

    async function dataUsername() {
        try {
            const response = await api.get(`/api/users/findUserByEmail/${email}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                }
            });
            localStorage.setItem("username", response.data.name);
        } catch (error) {
            // Lida com erros na chamada à API
            console.error(error);
        }
    }


    function handleDeleteClick(id, name) {
        setSelectedUserId(id);
        setSelectedUserName(name)
        setSelectedAdress(name);
        setShowConfirmation(true);
    }

    function handleDeleteClickAdress(id, name) {
        setSelectedUserId(id);
        setSelectedAdress(name);
        setShowConfirmationAdress(true);
    }

    function handleConfirmDelete() {
        deleteUser(selectedUserId).then();
        setShowConfirmation(false);
    }

    function handleConfirmDeleteAdress() {
        deleteAdress(selectedUserId).then();
        setShowConfirmationAdress(false);
    }

    function handleCancelDelete() {
        setSelectedUserId(null);
        setSelectedUserName(null);
        setShowConfirmation(false);
    }

    function handleCancelDeleteAdress() {
        setSelectedUserId(null);
        setSelectedAdress(null);
        setShowConfirmationAdress(false);
    }

    useEffect(() => {
        dataUsername().then();
    });

    // useEffect é para carregar a tela assim que carregar o HTML, os dados virão!
    useEffect(() => {

        fetchMoreUsers().then();
    }, []);

    return (

        <div className="user-container">

            <ToastContainer position="top-center" delay="3000"/>


            <div className="titleTable">
                <div className="Title">
                    <p>Registered Users</p>
                </div>
                <div className="buttonUser">
                    <FiUserPlus className="iconUserPlus" size={20} color="white"/>
                    <Link className="LinkNewUser" to="/user/new/0">
                        Add new User
                    </Link>

                </div>
            </div>
            <ul>
                {users.map((user) => (
                    <li key={user.id}>
                        <strong>Name:</strong>
                        <p>{user.name}</p>
                        <strong>E-mail:</strong>
                        <p>{user.email}</p>
                        <strong>RG:</strong>
                        <p>{user.rg}</p>
                        <strong>CPF:</strong>
                        <p>{user.cpf}</p>
                        <strong>Birthday:</strong>
                        <p>
                            {Intl.DateTimeFormat('pt-BR').format(new Date(user.birthDay))}
                        </p>
                        <strong>Cellphone:</strong>
                        <p>{user.cellphone}</p>
                        <strong>Adress:</strong>
                        {user.adress ? (

                            <p>
                                {user.adress.street +
                                    ', ' +
                                    user.adress.district +
                                    ', ' +
                                    user.adress.number}
                            </p>
                        ) : (
                            <p>Adress not registred</p>

                        )}


                        <button className="buttonEditUser" type="button">
                            <FiEdit onClick={() => editUser(user.id)} size={20} color="#11009E"/>
                        </button>
                        <button className="buttonTrashUser" onClick={() => handleDeleteClick(user.id, user.name)}
                                type="button">
                            <FiTrash2 size={20} color="#CD1818"/>
                        </button>
                    </li>
                ))}
            </ul>
            <button className="buttonMorePage" onClick={fetchMoreUsers} type="button">
                Load More
            </button>

            {showConfirmation && (
                <div className="modal">
                    <button className="buttonCloseModal" onClick={handleCancelDelete}><FiX size={20} color='gray'/>
                    </button>
                    <div className="modal-content">
                        <div className="iconContainer">
                            <FiAlertTriangle className="iconAlertModal" size="45" color='orange'/>
                        </div>
                        <h2>Confirmation</h2>
                        <p>Are you sure you want to delete?</p>
                        <h5>{selectedUserName}</h5>
                        <button className="buttonConfirmDialog" onClick={handleConfirmDelete}>Delete</button>
                        <button className="buttonCancelDialog" onClick={handleCancelDelete}>Cancel</button>
                    </div>
                </div>
            )}

            {showConfirmationAdress && (
                <div className="modal">
                    <button className="buttonCloseModal" onClick={handleCancelDeleteAdress}><FiX size={20}
                                                                                                 color='gray'/>
                    </button>
                    <div className="modal-content">
                        <div className="iconContainer">
                            <FiAlertTriangle className="iconAlertModal" size="45" color='orange'/>
                        </div>
                        <h2>Confirmation</h2>
                        <p>Are you sure you want to delete adress?</p>
                        <h5>{selectedAdress}</h5>
                        <button className="buttonConfirmDialog" onClick={handleConfirmDeleteAdress}>Delete</button>
                        <button className="buttonCancelDialog" onClick={handleCancelDeleteAdress}>Cancel</button>
                    </div>
                </div>
            )}
        </div>

);
}
