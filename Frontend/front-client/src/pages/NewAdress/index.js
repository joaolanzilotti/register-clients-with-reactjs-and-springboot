import React, {useEffect, useState} from 'react';
import './styles.css';
import loadingGif from '../../assets/loadingTwoWhite.gif';
import homeadress from '../../assets/homeadress.png';
import {Link, useNavigate, useParams} from "react-router-dom";
import {FiArrowLeft, FiUserPlus} from "react-icons/fi";
import {ToastContainer, toast} from "react-toast";

import api from '../../services/api';

export default function NewAddress() {

    const [id, setId] = useState(null);
    const [street, setStreet] = useState('');
    const [district, setDistrict] = useState('');
    const [number, setNumber] = useState('');
    const [city, setCity] = useState('');
    const [state, setState] = useState('');

    const accessToken = localStorage.getItem('accessToken');

    const [showLoading, setShowLoading] = useState(false);

    const {userId} = useParams();
    const {adressId} = useParams();

    //Funcao navigate para Enviar a Rota
    const navigate = useNavigate();

    //async function é uma funcao que aguarda o carregamento da pagina.
    async function loadAdress() {
        try {
            const response = await api.get(`/api/adress/${adressId}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })
            setId(response.data.id)
            setStreet(response.data.street)
            setDistrict(response.data.district)
            setNumber(response.data.number)
            setCity(response.data.city)
            setState(response.data.state)

        } catch (erro) {
            toast.error('Error recovering address!, Try again!')
            navigate('/users');
        }
    }

    useEffect(() => {
        if (adressId === '0') return;
        else {
            loadAdress().then();
        }


    }, [adressId])

    //funcao para nao fazer a pagina dar Refresh
    async function SaveOrUpdateAdress(e) {
        e.preventDefault();
        setShowLoading(true);


        const data = {
            street,
            district,
            number,
            city,
            state,

        }

        try {
            if(adressId === '0') {
                const response = await api.post('/api/adress', data, {
                    //Adicionando na resposta o Header com o Token
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                });

                await api.post(`/api/users/userWithAdress/${userId}/${response.data.id}`, null, {
                    //Adicionando na resposta o Header com o Token
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                });
                toast.success('Address added with Sucess!');

            }else{
                data.id = id
                await api.put(`/api/adress/${id}`, data, {
                    //Adicionando na resposta o Header com o Token
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                });
                toast.success('Address Updated with Success!');
            }

        } catch (err) {
            toast.error('Error while Record Address! Try Again!');
        } finally {
            setShowLoading(false);
        }

    }

    return (
        <div className="notification">
            <ToastContainer position="top-center" delay="3000"/>

            <div className="new-adress-container">

                <div className="content">
                    <section className="form">
                        <img src={homeadress} alt="JP"/>
                        <h1>{adressId === '0' ? 'Add New' : 'Update'} Address</h1>
                        <p>Enter the address information and click on {adressId === '0' ? 'Add' : 'Update'}</p>
                        <Link className="back-link" to="/users">
                            <div className="container-button">
                                <div className="iconArrowLeft"><FiArrowLeft size={16} color="blue"/></div>
                                <div className="textButton">Back to Users</div>
                            </div>
                        </Link>
                    </section>
                    <form onSubmit={SaveOrUpdateAdress}>
                        <label>Street</label>
                        <input id="street" placeholder="Street" value={street} onChange={e => setStreet(e.target.value)}/>
                        <label>District</label>
                        <input type="text" placeholder="District" value={district}
                               onChange={e => setDistrict(e.target.value)}/>
                        <label>Number</label>
                        <input type="text" placeholder="Number" value={number}
                               onChange={e => setNumber(e.target.value)}/>
                        <label>City</label>
                        <input placeholder="City" value={city} onChange={e => setCity(e.target.value)}/>
                        <label>State</label>
                        <input placeholder="State" value={state} onChange={e => setState(e.target.value)}/>
                        <button className="button" type="submit">
                            {showLoading ? (
                                <img className="loadingGif" src={loadingGif} alt="Spinner"/>
                            ) : (
                                adressId === '0' ? 'Add' : 'Update'
                            )}
                        </button>

                    </form>
                </div>
            </div>
        </div>
    )
}
