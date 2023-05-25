import React, {useState, useEffect} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {FiUserPlus, FiLogOut, FiEdit, FiTrash2, FiAlertTriangle, FiX, FiPlusCircle} from 'react-icons/fi';
import {toast} from 'react-toast';

import api from '../../services/api';

import './styles.css';

import logoJP from '../../assets/logoJP.png';

export default function Users() {
    const email = localStorage.getItem('email');
    const accessToken = localStorage.getItem('accessToken');
    const username = localStorage.getItem('username');

    const navigate = useNavigate();
    const [showConfirmation, setShowConfirmation] = useState(false);
    const [selectedUserId, setSelectedUserId] = useState(null);
    const [selectedUserName, setSelectedUserName] = useState(null);

    const [users, setUsers] = useState([]);
    const [page, setPage] = useState(0);

    const valorParagrafo = 'Adress not registred';

    async function logout() {
        localStorage.clear();
        navigate('/');
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
            setUsers(users.filter((client) => client.id !== id));
            toast.success('User Deleted Successfully!');
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
        setShowConfirmation(true);
    }

    function handleConfirmDelete() {
        deleteUser(selectedUserId);
        setShowConfirmation(false);
    }

    function handleCancelDelete() {
        setSelectedUserId(null);
        setSelectedUserName(null);
        setShowConfirmation(false);
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
            <header>
                <img src={logoJP} alt="JP"/>
                <span>
          Welcome, <strong>{username}</strong>
        </span>
                <Link className="buttonUser" to="/user/new/0">
                    <div className="container-button">
                        <div className="iconUserPlus">
                            <FiUserPlus size={24} color="white"/>
                        </div>
                        <div className="textButton">Add new User</div>
                    </div>
                </Link>

                <button onClick={logout} className="buttonPower" type="button">
                    <FiLogOut size={18} color="#251FC5"/>
                </button>
            </header>
            <h1>Registered Users</h1>
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
                                <div className="buttons-Adress">

                                    <button className="buttonEditAdress" type="button"><FiEdit size={20} color="#251FC5"/>
                                    </button>
                                    <button className="buttonTrashAdress" type="button"><FiTrash2 size={20}
                                                                                                  color="#251FC5"/></button>
                                </div>
                            </p>


                        ) : (
                            <p>Adress not registred

                                <div className="buttons-Adress">
                                    <button className="buttonPlusAdress" type="button"><FiPlusCircle size={20} color="#251FC5"/></button>
                                    <button className="buttonEditAdress" type="button"><FiEdit size={20} color="#251FC5"/>
                                    </button>
                                    <button className="buttonTrashAdress" type="button"><FiTrash2 size={20}
                                                                                                  color="#251FC5"/></button>
                                </div>

                            </p>

                            //<button className="buttonPlusAdress" type="button"><FiPlusCircle size={20} color="#251FC5"/></button>

                        )}


                        <button className="buttonEditUser" type="button">
                            <FiEdit onClick={() => editUser(user.id)} size={20} color="#251FC5"/>
                        </button>
                        <button className="buttonTrashUser" onClick={() => handleDeleteClick(user.id, user.name)}
                                type="button">
                            <FiTrash2 size={20} color="#251FC5"/>
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
                        <p>Are you sure you want to delete user?</p>
                        <h5>{selectedUserName}</h5>
                        <button className="buttonConfirmDialog" onClick={handleConfirmDelete}>Delete</button>
                        <button className="buttonCancelDialog" onClick={handleCancelDelete}>Cancel</button>
                    </div>
                </div>
            )}
        </div>
    );
}
