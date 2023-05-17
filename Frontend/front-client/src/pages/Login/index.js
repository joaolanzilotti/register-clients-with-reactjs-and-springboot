import React from 'react';
import './styles.css';
import logoImage from '../../assets/logo.png'
import loginLock from '../../assets/login.png'

//Propriedade com React - Definindo que o metodo vai receber Filhos (Children) e especificando dentro do H1 também
function Login() {
return (

        <div className="login-container">

                <section className="form">
                <img src={logoImage} alt="Client Logo" />
                <form>
                        <h1>Acess your Account</h1>
                        <input type="text" placeholder="Username"/>
                        <input type="password" placeholder="Password"/>
                        <button type="submit"> Login</button>
                </form>
                        
                </section>
                
                <img src={loginLock} alt="Login" />
        </div>
        )

};
export default Login;