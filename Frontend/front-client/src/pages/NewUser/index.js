import React, {useEffect, useState} from 'react';
import '../NewUser/styles.css';
import loadingGif from '../../assets/loadingTwoWhite.gif';
import logoJP from '../../assets/newUser.png';
import {Link, useNavigate, useParams} from "react-router-dom";
import {FiArrowLeft, FiUserPlus} from "react-icons/fi";
import {ToastContainer, toast} from "react-toast";
import InputMask from 'react-input-mask';

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
    const [sexo, setSexo] = useState('');
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
            setSexo(response.data.sexo)
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
        if (name.trim() === '' || email.trim() === '' || password.trim() === '' || rg.trim() === '' || cpf.trim() === ''|| birthDay.trim() === '' || cellphone.trim() === '' || sexo.trim() === '') {
            toast.error('Please fill in all the required fields!');
            setShowLoading(false);
            return;
        }



            const data = {
                name,
                email,
                password,
                rg,
                cpf,
                birthDay,
                cellphone,
                sexo,

            }

            try {
                if (userId === '0') {
                    const response = await api.post('/api/users', data, {
                        //Adicionando na resposta o Header com o Token
                        headers: {
                            Authorization: `Bearer ${accessToken}`
                        }
                    });
                    if (sexo === 'Select' || sexo === '') {
                        toast.warn('Please, Select a option Valid.');
                    }else {
                        toast.success('User added with Sucess!');
                        navigate(`/user/newadress/${response.data.id}/0`)
                    }
                } else {
                    data.id = id
                    const response = await api.put(`/api/users/${id}`, data, {
                        //Adicionando na resposta o Header com o Token
                        headers: {
                            Authorization: `Bearer ${accessToken}`
                        }
                    });
                    if (sexo === 'Select' || sexo === '') {
                        toast.warn('Please, Select a option Valid.');
                    }else {
                        if (response.data.adress === null) {
                            navigate(`/user/newadress/${response.data.id}/0`)
                        }
                        navigate(`/user/newadress/${response.data.id}/${response.data.adress.id}`)
                        toast.success('User Updated with Success!');
                    }
                }

            } catch (err) {
                if (err.response) {
                    if (err.response.data.message === "Validation failed for classes [com.corporation.apiclient.entities.User] during persist time for groups [jakarta.validation.groups.Default, ]\nList of constraint violations:[\n\tConstraintViolationImpl{interpolatedMessage='Invalid CPF', propertyPath=cpf, rootBeanClass=class com.corporation.apiclient.entities.User, messageTemplate='Invalid CPF'}\n]") {
                        toast.error('Invalid CPF');
                    }
                    if (err.response.data.message) {
                        toast.error(err.response.data.message);
                    }

                } else if (err.request) {
                    toast.error('Request Failed. Verify your Connection.');
                } else {
                    toast.error('An unexpected error occurred. Please, Try Again Later.');
                }
            } finally {
                setShowLoading(false);
            }

        }


    return (
        <div className="container">
            <ToastContainer position="top-center" delay="3000"/>
            <div className="new-user-container">

                <div className="content">
                    <section className="form">
                        <img src={logoJP} alt="JP"/>
                        <h1>{userId === '0' ? 'Add New' : 'Update'} User</h1>
                        <p>Enter the user information and click on {userId === '0' ? 'Next' : 'Next'}</p>
                        <Link className="back-link" to="/users">
                            <div className="container-button">
                                <div className="iconArrowLeft"><FiArrowLeft size={16} color="blue"/></div>
                                <div className="textButton">Back to Users</div>
                            </div>
                        </Link>
                    </section>
                    <form onSubmit={SaveOrUpdateUser}>
                        <label>Name*</label>
                        <input id="name" placeholder="Name" value={name} onChange={e => setName(e.target.value)}/>
                        <label>Email*</label>
                        <input type="email" placeholder="E-mail" value={email}
                               onChange={e => setEmail(e.target.value)}/>
                        <label>Password*</label>
                        <input type="password" placeholder="Password" value={password}
                               onChange={e => setPassword(e.target.value)}/>
                        <label>RG*</label>
                        <InputMask placeholder="RG" mask="99.999.999-9" value={rg}
                                   onChange={e => setRG(e.target.value.replace(/\D/g, ''))}/>
                        <label>CPF*</label>
                        <InputMask placeholder="CPF" mask="999.999.999-99" value={cpf}
                                   onChange={e => setCpf(e.target.value.replace(/\D/g, ''))}/>
                        <label>Gender*</label>
                        <select onChange={e => setSexo(e.target.value)} value={sexo} className="select">
                            <option value="Select" >Select</option>
                            <option value="Male">Male</option>
                            <option value="Female"> Female</option>
                            <option value="Do Not Inform">Do not inform</option>
                        </select>
                        <label>Birthday*</label>
                        <input type="date" value={birthDay} onChange={e => () => setBirthDay(e.target.value)}/>
                        <label>Cellphone*</label>
                        <InputMask placeholder="Cellphone" mask="(99) 99999-9999" value={cellphone}
                                   onChange={e => setCellphone(e.target.value.replace(/\D/g, ''))}/>
                        <button className="button" type="submit">
                            {showLoading ? (
                                <img className="loadingGif" src={loadingGif} alt="Spinner"/>
                            ) : (
                                userId === '0' ? 'Next' : 'Next'
                            )}
                        </button>

                    </form>
                </div>
            </div>
        </div>
    )
}
