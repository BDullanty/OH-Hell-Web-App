
import './App.css';
import { BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {OAuthHandler} from './Components/OAuthHandler';
import HomePage from './Pages/HomePage';
import NotFound from './Pages/404Page';
const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage/>}/>
        <Route path="oauth/callback" element={<OAuthHandler/>} />
        <Route path="oauth/callback/" element={<OAuthHandler/>} />
        <Route path="*" element={<NotFound/>} />
       
      </Routes>
    </Router>
  );
}
export default App;
