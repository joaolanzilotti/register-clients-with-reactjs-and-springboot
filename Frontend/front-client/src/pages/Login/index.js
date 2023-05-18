import React from 'react';
import './styles.css';
import logoImage from '../../assets/logo.png'
import loginLock from '../../assets/login.png'

//Propriedade com React - Definindo que o metodo vai receber Filhos (Children) e especificando dentro do H1 também
function Login() {
        return (

                <div className="login">
                        <div className="login-container">

                                <section className="form">

                                        <img className="logo" src={logoImage} alt="Client Logo" />
                                        <form>
                                                <h1>Access your Account</h1>
                                                <input type="text" placeholder="Username" />
                                                <input type="password" placeholder="Password" />
                                                <button className="button" type="submit"> Login</button>
                                        </form>

                                </section>
                        </div>
                </div>
        )

};
export default Login;