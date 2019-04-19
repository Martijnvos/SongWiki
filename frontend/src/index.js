import React, { useState } from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import { ThemeProvider } from 'styled-components';
import WebFont from 'webfontloader';

import { AuthenticationProvider } from './utils/authenticationContext';
import { NotificationProvider } from './utils/notificationContext';
import { getJWTToken } from './utils/localStorage';
import { initializeWebsocket } from './utils/websocket';

import AuthenticatedRoute from './components/AuthenticatedRoute';

import theme from './styles/theme';

import Home from './pages/home';
import Register from './pages/register';
import Login from './pages/login';
import TwoFactorAuth from './pages/twoFactorAuth';
import AddSong from './pages/addSong';
import User from './pages/user';

import * as serviceWorker from './serviceWorker';

import 'normalize.css';

WebFont.load({
  google: {
    families: ['Open Sans', 'Lato'],
  },
});

const AppRouter = () => {
  const [isAuthenticated, setAuthenticated] = useState(getJWTToken() != null);
  const [notificationMessage, setNotificationMessage] = useState('');

  initializeWebsocket(setNotificationMessage);

  return (
    <AuthenticationProvider
      isAuthenticated={isAuthenticated}
      setAuthenticated={setAuthenticated}
    >
      <NotificationProvider
        notificationMessage={notificationMessage}
        setNotificationMessage={setNotificationMessage}
      >
        <ThemeProvider theme={theme}>
          <Router>
            <Route exact path="/" component={Home} />
            <Route exact path="/register" component={Register} />
            <Route exact path="/login" component={Login} />
            <Route exact path="/two-factor-auth" component={TwoFactorAuth} />
            <Route exact path="/add-song" component={AddSong} />
            <AuthenticatedRoute exact path="/user" component={User} />
          </Router>
        </ThemeProvider>
      </NotificationProvider>
    </AuthenticationProvider>
  );
};

ReactDOM.render(<AppRouter />, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
