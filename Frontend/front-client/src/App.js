import React, {useState} from 'react';
import Header from './Header';

function App() {

    //So consigo mudar valores se usar os estados com useStates
    //Array {Valor, changeValueFunction}
  const [counter, setCounter] = useState(0);


  function increment() {
   setCounter(counter + 1);
  }

  return(
    //JSX JavaScript XML
    <div>
    <Header>
     
      Counter: {counter}
      
    </Header>
    <button onClick={increment}>Add</button>
    </div>

  ) ;
}

export default App;
