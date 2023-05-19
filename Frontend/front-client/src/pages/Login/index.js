import React, {useState} from 'react';
//Biblioteca para Enviar as rotas
import {useNavigate} from "react-router-dom";
//Servico para se conectar com a API
import api from '../../services/api';

import './styles.css'
import logoImage from '../../assets/logo.png';
import { ToastContainer, toast } from "react-toast";

//Propriedade com React - Definindo que o metodo vai receber Filhos (Children) e especificando dentro do H1 também
export default function Login() {

    //Notifications React Toast
    const error = () => toast.warn('Login failed! Try agains!');

    //Chamando a API para enviar os Dados
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    //Funcao navigate para Enviar a Rota
    const navigate = useNavigate();

    //funcao para nao fazer a pagina dar Refresh
    async function login(e) {
        e.preventDefault();
        const data = {
            username,
            password,
        };
        try{
            //Enviado a Requisicao Post para API
            const response = await api.post('auth/signin', data);
            //Armazenando o login no LocalStorage
            localStorage.setItem('username', username);
            localStorage.setItem('accessToken', response.data.token);

            navigate('/clients');
        }catch (err){
            error();
        }
    };

    return (
        <div className="login">
            <ToastContainer position="top-center" delay="3000" />
            <div className="login-container">

                <section className="form">

                    <img className="logo" src={logoImage} alt="Client Logo"/>
                    <form onSubmit={login}>
                        <h1>Access your Account</h1>
                        <input type="text" placeholder="Username" value={username}
                               onChange={e => setUsername(e.target.value)}/>
                        <input type="password" placeholder="Password" value={password}
                               onChange={e => setPassword(e.target.value)}/>
                        <button className="button" type="submit"> Login</button>

                    </form>

                </section>
            </div>
        </div>
    )

};
