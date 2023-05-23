import React, {useEffect, useState} from 'react';
import './styles.css';
import loadingGif from '../../assets/loadingTwoWhite.gif';
import logoJP from '../../assets/newUser.png';
import {Link, useNavigate, useParams} from "react-router-dom";
import {FiArrowLeft, FiUserPlus} from "react-icons/fi";
import {ToastContainer, toast} from "react-toast";

import api from '../../services/api';

export default function NewClient() {

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

    const username = localStorage.getItem('username');
    const accessToken = localStorage.getItem('accessToken');

    const [showLoading, setShowLoading] = useState(false);

    const {clientId} = useParams();

    //Funcao navigate para Enviar a Rota
    const navigate = useNavigate();

    //async function é uma funcao que aguarda o carregamento da pagina.
    async function loadClient() {
        try {
            const response = await api.get(`/api/clients/${clientId}`, {
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
            toast.error('Error recovering Client!, Try again!')
            navigate('/clients');
        }
    }

    useEffect(() => {
        if (clientId === '0') return;
        else {
            loadClient().then();
        }


    }, [clientId])

    //funcao para nao fazer a pagina dar Refresh
    async function createNewClient(e) {
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
            await api.post('/api/clients', data, {
                //Adicionando na resposta o Header com o Token
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });
            toast.success('Client added with Sucessfuly!');
            //navigate('/clients');
        } catch (err) {
            toast.error('Error while Recorde Client! Try Again!');
        } finally {
            setShowLoading(false);
        }

    }

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
                                'Add'
                            )}
                        </button>

                    </form>
                </div>
            </div>
        </div>
    )
}

