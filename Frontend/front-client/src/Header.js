import React from 'react';

//Propriedade com React - Definindo que o metodo vai receber Filhos (Children) e especificando dentro do H1 também
function Header({children}) {
return (
    <header>
        <h1>{children}</h1>
    </header>
)
};
export default Header;