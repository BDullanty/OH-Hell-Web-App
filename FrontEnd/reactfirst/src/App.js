
import './App.css';
import { BrowserRouter, Route, Routes} from 'react-router-dom';
import {OAuthHandler} from './Components/OAuthHandler';
import HomePage from './Pages/HomePage';
import NotFound from './Pages/404Page';
import Play from './Pages/Play';
const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/oauth/callback" element={<OAuthHandler/>} />
        <Route path="/oauth/callback/" element={<OAuthHandler/>} />
        
        <Route path="/Play" element={<Play/>} />
        <Route path="/" element={<HomePage/>}/>
        <Route path="*" element={<NotFound/>} />
      </Routes>
    </BrowserRouter>
  );
}
export default App;
