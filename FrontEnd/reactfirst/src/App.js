
import './App.css';
import { BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {OAuthHandler} from './Components/OAuthHandler';
import HomePage from './HomePage';
const App = () => {
  return (

      <Router>
      <Routes>
        <Route path="/oauth/callback/" Component={OAuthHandler} />
        {/* Other routes */}
        <Route path="/" Component={HomePage}>
         
        </Route>
      </Routes>
    </Router>
  );
}
export default App;
