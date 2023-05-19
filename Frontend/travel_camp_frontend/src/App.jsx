import axios from 'axios';
import './App.css';
import React, { useEffect, useState } from 'react';
import Register from './Components/Pages/Register';

//Экспортируем контекст из реакта: {} означает, что там будет массив данных
export const AppContext = React.createContext({})

function App() {

  //hook section
  const [users, setUsers] = useState([])

  //получаем данные от БД, async + await - для асинхронной работы
//(отрисовка и выполнение кода без ожидания получения данных от сервера)
  useEffect(() => {
    async function axiosData () {
      const usersData = await axios.get('http://localhost:8080/api/users')
      setUsers(usersData.data);
    }
    axiosData();
  },[]) // ,[] - указываем для того, чтобы сервак не отправлял постоянно запросы



  return (
    <AppContext.Provider
    value={
      {
        users,
        setUsers
      }
    }
    >

    <div className="App">
      <h5>Пользователь id: {users.id}</h5>
      {
      users.map(obj => {
        return(
          <div>
            {console.log(users)}
           key={obj.id}
          <p>{obj.id}</p>
          <p>{obj.role}</p>
          </div>
        )})}
        <Register/>        
    </div>
    </AppContext.Provider>
  );
}

export default App;


// const React = require('react');
// const ReactDOM = require('react-dom');
// const client = require('./client');
// class App extends React.Component {

//   constructor(props) {
//     super(props);
//     this.state = {users.html: []};
//   }

//   componentDidMount() {
//     client({method: 'GET', path: '/api/users.html'}).done(response => {
//       this.setState({users.html: response.entity._embedded.users.html});
//     });
//   }

//   render() {
//     return (
//         <EmployeeList employees={this.state.users.html}/>
//     )
//   }
// }

// ReactDOM.render(
//     <App />,
//     document.getElementById('react')
// )
