import React, {useEffect, useState} from 'react';
import '../NewUser/styles.css';
import loadingGif from '../../assets/loadingTwoWhite.gif';
import logoJP from '../../assets/newUser.png';
import {Link, useNavigate, useParams} from "react-router-dom";
import {FiArrowLeft, FiUserPlus} from "react-icons/fi";
import {ToastContainer, toast} from "react-toast";

import api from '../../services/api';

export default function NewUser() {

    //Chamando a API para enviar os Dados
    const [id, setId] = useState(null);
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [rg, setRG] = useState('');
    const [cpf, setCpf] = useState('');
    const [birthDay, setBirthDay] = useState('');
    const [cellphone, setCellphone] = useState('');
    const [adress, setAdress] = useState('');

    const emailLogged = localStorage.getItem('email');
    const accessToken = localStorage.getItem('accessToken');

    const [showLoading, setShowLoading] = useState(false);

    const {userId} = useParams();

    //Funcao navigate para Enviar a Rota
    const navigate = useNavigate();

    //async function é uma funcao que aguarda o carregamento da pagina.
    async function loadUser() {
        try {
            const response = await api.get(`/api/users/${userId}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })
            setId(response.data.id)
            setName(response.data.name)
            setEmail(response.data.email)
            setPassword(response.data.password)
            setRG(response.data.rg)
            setCpf(response.data.cpf)
            setCellphone(response.data.cellphone)
            setBirthDay(response.data.birthDay)
            setAdress(response.data.adress)
        } catch (erro) {
            toast.error('Error recovering User!, Try again!')
            navigate('/users');
        }
    }

    useEffect(() => {
        if (userId === '0') return;
        else {
            loadUser().then();
        }


    }, [userId])

    //funcao para nao fazer a pagina dar Refresh
    async function SaveOrUpdateUser(e) {
        e.preventDefault();
        setShowLoading(true);


        const data = {
            name,
            email,
            password,
            rg,
            cpf,
            birthDay,
            cellphone,

        }

        try {
            if(userId === '0') {
                await api.post('/api/users', data, {
                    //Adicionando na resposta o Header com o Token
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                });
                toast.success('User added with Sucess!');
            }else{
                data.id = id
                await api.put(`/api/users/${id}`, data, {
                    //Adicionando na resposta o Header com o Token
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                });
                toast.success('User Updated with Success!');
            }

        } catch (err) {
            toast.error('Error while Record User! Try Again!');
        } finally {
            setShowLoading(false);
        }

    }

    return (

            <div className="new-user-container">
                <ToastContainer position="top-center" delay="3000"/>
                <div className="content">
                    <section className="form">
                        <img src={logoJP} alt="JP"/>
                        <h1>{userId === '0' ? 'Add New' : 'Update'} User</h1>
                        <p>Enter the user information and click on {userId === '0' ? 'Add' : 'Update'}</p>
                        <Link className="back-link" to="/users">
                            <div className="container-button">
                                <div className="iconArrowLeft"><FiArrowLeft size={16} color="blue"/></div>
                                <div className="textButton">Back to Users</div>
                            </div>
                        </Link>
                    </section>
                    <form onSubmit={SaveOrUpdateUser}>
                        <input id="name" placeholder="Name" value={name} onChange={e => setName(e.target.value)}/>
                        <input type="email" placeholder="E-mail" value={email}
                               onChange={e => setEmail(e.target.value)}/>
                        <input type="password" placeholder="Password" value={password}
                               onChange={e => setPassword(e.target.value)}/>
                        <input placeholder="RG" value={rg} onChange={e => setRG(e.target.value)}/>
                        <input placeholder="CPF" value={cpf} onChange={e => setCpf(e.target.value)}/>
                        <input type="date" value={birthDay} onChange={e => setBirthDay(e.target.value)}/>
                        <input placeholder="Cellphone" value={cellphone}
                               onChange={e => setCellphone(e.target.value)}/>
                        <button className="button" type="submit">
                            {showLoading ? (
                                <img className="loadingGif" src={loadingGif} alt="Spinner"/>
                            ) : (
                                userId === '0' ? 'Add' : 'Update'
                            )}
                        </button>

                    </form>
                </div>
            </div>
    )
}

