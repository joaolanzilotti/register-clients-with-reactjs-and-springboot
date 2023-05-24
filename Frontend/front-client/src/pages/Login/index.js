import React, {useState} from 'react';
import loadingGif from '../../assets/loadingTwoWhite.gif';
//Biblioteca para Enviar as rotas
import {useNavigate} from "react-router-dom";
//Servico para se conectar com a API
import api from '../../services/api';

import './styles.css'
import logoImage from '../../assets/logo.png';
import {ToastContainer, toast} from "react-toast";

//Propriedade com React - Definindo que o metodo vai receber Filhos (Children) e especificando dentro do H1 também
export default function Login() {

    const [showLoading, setShowLoading] = useState(false);

    //Notifications React Toast

    //Chamando a API para enviar os Dados
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    //Funcao navigate para Enviar a Rota
    const navigate = useNavigate();

    //funcao para nao fazer a pagina dar Refresh
    async function login(e) {
        e.preventDefault();
        setShowLoading(true);
        const data = {
            email: email,
            password,
        }
        try {
            //Enviado a Requisicao Post para API
            const response = await api.post('auth/signin', data);
            //Armazenando o login no LocalStorage
            localStorage.setItem('email', email);
            localStorage.setItem('accessToken', response.data.token);

            navigate('/users');
        } catch (err) {
            toast.warn('Login failed! Try agains!');
        } finally {
            setShowLoading(false);
        }
    };

    return (
        <div className="login">
            <ToastContainer position="top-center" delay="3000"/>
            <div className="login-container">

                <section className="form">

                    <img className="logo" src={logoImage} alt="User Logo"/>
                    <form onSubmit={login}>
                        <h1>Access your Account</h1>
                        <input type="text" placeholder="Email" value={email}
                               onChange={e => setEmail(e.target.value)}/>
                        <input type="password" placeholder="Password" value={password}
                               onChange={e => setPassword(e.target.value)}/>
                        <button className="button" type="submit">
                            {showLoading ? (
                                <img className="loadingGif" src={loadingGif} alt="Spinner"/>
                            ) : (
                                'Login'
                            )}
                        </button>

                    </form>

                </section>
            </div>
        </div>
    )

};
