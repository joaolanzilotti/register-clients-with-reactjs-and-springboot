import React, {useEffect, useState} from 'react';
import './styles.css';
import loadingGif from '../../assets/loadingTwoWhite.gif';
import homeadress from '../../assets/homeadress.png';
import {Link, useNavigate, useParams} from "react-router-dom";
import {FiArrowLeft, FiUserPlus} from "react-icons/fi";
import {ToastContainer, toast} from "react-toast";

import api from '../../services/api';

export default function Page404() {


    return (
        <section className="page_404">

            <div className="container">
                <div className="row">
                    <div className="col-sm-12">
                        <div className="container-text-gif">
                            <div className="four_zero_four_bg">
                                <h1 className="text-center">404</h1>
                            </div>
                            <div className="contant_box_404">
                                <h3 className="h2"> Look like you`re lost</h3>
                                <p>the page you are looking for not avaible!</p>
                                <Link to="/users" className="link_404">Go To Home</Link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    )
}

