import React, {useState} from 'react';
import './styles.css';
import logoJP from '../../assets/newUser.png';
import {Link, useNavigate} from "react-router-dom";
import {FiArrowLeft, FiUserPlus} from "react-icons/fi";
import {ToastContainer, toast} from "react-toast";

import api from '../../services/api';


export default function NewClient() {

    const sucess = () => toast.success('Client added with Sucessfuly!');
    const error = () => toast.error('Error while Recorde Client! Try Again!');

    //Chamando a API para enviar os Dados
    const [id, setId] = useState(null);
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [rg, setRG] = useState('');
    const [cpf, setCpf] = useState('');
    const [birthDay, setBirthDay] = useState('');
    const [cellphone, setCellphone] = useState('');

    const username = localStorage.getItem('username');
    const accessToken = localStorage.getItem('accessToken');

    //Funcao navigate para Enviar a Rota
    const navigate = useNavigate();

    //funcao para nao fazer a pagina dar Refresh
    async function createNewClient(e) {
        e.preventDefault();

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
            await api.post('/api/clients', data, {
                //Adicionando na resposta o Header com o Token
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });
            sucess();
            //navigate('/clients');
        } catch (err) {
            error();
        }

    };

    return (
        <div className="notification">
            <ToastContainer position="top-center" delay="3000"/>

            <div className="new-client-container">

                <div className="content">
                    <section className="form">
                        <img src={logoJP} alt="JP"/>
                        <h1>Add New Client</h1>
                        <p>Enter the client information and click on 'Add'!</p>
                        <Link className="back-link" to="/clients">
                            <div className="container-button">
                                <div className="iconArrowLeft"><FiArrowLeft size={16} color="blue"/></div>
                                <div className="textButton">Home</div>
                            </div>
                        </Link>
                    </section>
                    <form onSubmit={createNewClient}>
                        <input placeholder="Name" value={name} onChange={e => setName(e.target.value)}/>
                        <input type="email" placeholder="E-mail" value={email} onChange={e => setEmail(e.target.value)}/>
                        <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)}/>
                        <input placeholder="RG" value={rg} onChange={e => setRG(e.target.value)}/>
                        <input placeholder="CPF" value={cpf} onChange={e => setCpf(e.target.value)}/>
                        <input type="date" value={birthDay} onChange={e => setBirthDay(e.target.value)}/>
                        <input placeholder="Cellphone" value={cellphone} onChange={e => setCellphone(e.target.value)}/>
                        <button className="button" type="submit">Add</button>

                    </form>
                </div>
            </div>
        </div>
    );
}