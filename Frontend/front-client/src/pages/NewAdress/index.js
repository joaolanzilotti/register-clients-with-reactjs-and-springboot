import React, { useEffect, useState } from 'react';
import './styles.css';
import loadingGif from '../../assets/loadingTwoWhite.gif';
import homeadress from '../../assets/homeadress.png';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { FiArrowLeft, FiUserPlus } from 'react-icons/fi';
import { ToastContainer, toast } from 'react-toast';
import InputMask from 'react-input-mask';

import api from '../../services/api';

export default function NewAddress() {
    const [id, setId] = useState(null);
    const [street, setStreet] = useState('');
    const [district, setDistrict] = useState('');
    const [number, setNumber] = useState('');
    const [city, setCity] = useState('');
    const [state, setState] = useState('');
    const [cep, setCep] = useState('');
    const [ufs, setUfs] = useState([]);

    const accessToken = localStorage.getItem('accessToken');

    const [showLoading, setShowLoading] = useState(false);

    const { userId } = useParams();
    const { adressId } = useParams();

    const navigate = useNavigate();

    async function loadAdress() {
        try {
            const response = await api.get(`/api/adress/${adressId}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            });
            setId(response.data.id);
            setStreet(response.data.street);
            setDistrict(response.data.district);
            setNumber(response.data.number);
            setCity(response.data.city);
            setCep(response.data.cep);
            setState(response.data.state);
        } catch (error) {
            toast.error('Error recovering address! Try again!');
            navigate('/users');
        }
    }

    async function fetchUfs() {
        const response = await api.get('https://servicodados.ibge.gov.br/api/v1/localidades/estados', {
            params: {
                orderBy: 'nome',
            },
        });

        setUfs([...ufs, ...response.data]);
    }

    useEffect(() => {
        fetchUfs();
    }, []);

    useEffect(() => {
        if (adressId === '0') return;
        else {
            loadAdress();
        }
    }, [adressId]);

    async function SaveOrUpdateAdress(e) {
        e.preventDefault();
        setShowLoading(true);

        if (street.trim() === '' || district.trim() === '' || number.trim() === '' || city.trim() === '' || state.trim() === '') {
            toast.error('Please fill in all the required fields!');
            setShowLoading(false);
            return;
        }

        const data = {
            cep,
            street,
            district,
            number,
            city,
            state,
        };

        try {
            if (adressId === '0') {
                const response = await api.post('/api/adress', data, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    },
                });

                await api.post(`/api/users/userWithAdress/${userId}/${response.data.id}`, null, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    },
                });
                toast.success('Address added with success!');
            } else {
                data.id = id;
                await api.put(`/api/adress/${id}`, data, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    },
                });
                toast.success('Address updated with success!');
            }
        } catch (error) {
            toast.error('Error while recording address! Try again!');
        } finally {
            setShowLoading(false);
        }
    }

    async function fetchAddressData() {
        try {
            const response = await api.get(`https://viacep.com.br/ws/${cep}/json/`);

            if (response.data.erro) {
                toast.error('Invalid CEP!');
            } else {
                setStreet(response.data.logradouro);
                setDistrict(response.data.bairro);
                setCity(response.data.localidade);
                setState(response.data.uf);
            }
        } catch (error) {
            toast.error('Error searching CEP data!');
        }
    }

    useEffect(() => {
        if (cep.length === 8) {
            fetchAddressData().then();
        }
    }, [cep]);

    return (
        <div className="notification">
            <ToastContainer position="top-center" autoClose={3000} />

            <div className="new-adress-container">
                <div className="content">
                    <section className="form">
                        <img src={homeadress} alt="JP" />
                        <h1>{adressId === '0' ? 'Add New' : 'Update'} Address</h1>
                        <p>Enter the address information and click on {adressId === '0' ? 'Add' : 'Update'}</p>
                        <Link className="back-link" to="/users">
                            <div className="container-button">
                                <div className="iconArrowLeft">
                                    <FiArrowLeft size={16} color="blue" />
                                </div>
                                <div className="textButton">Back to Users</div>
                            </div>
                        </Link>
                    </section>
                    <form onSubmit={SaveOrUpdateAdress}>
                        <label>CEP*</label>
                        <InputMask
                            id="cep"
                            placeholder="CEP"
                            mask="99999-999"
                            value={cep}
                            onChange={(e) => setCep(e.target.value.replace('_', '').replace('-', ''))}
                        />
                        <label>Street*</label>
                        <input id="street" placeholder="Street" value={street} onChange={(e) => setStreet(e.target.value)} />
                        <label>District*</label>
                        <input type="text" placeholder="District" value={district} onChange={(e) => setDistrict(e.target.value)} />
                        <label>Number*</label>
                        <input type="number" placeholder="Number" value={number} onChange={(e) => setNumber(e.target.value)} />
                        <label>City*</label>
                        <input placeholder="City" value={city} onChange={(e) => setCity(e.target.value)} />
                        <label>State*</label>
                        <select onChange={(e) => setState(e.target.value)} value={state} className="select">
                            {ufs.map((uf) => (
                                <option key={uf.sigla} value={uf.sigla}>
                                    {uf.nome}
                                </option>
                            ))}
                        </select>
                        <button className="button" type="submit">
                            {showLoading ? (
                                <img className="loadingGif" src={loadingGif} alt="Spinner" />
                            ) : (
                                adressId === '0' ? 'Add' : 'Update'
                            )}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}
