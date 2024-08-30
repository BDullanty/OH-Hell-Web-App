
import '../App.css';
import {generateChallenge} from '../Components/OAuthHandler';
const HomePage = () =>{
  generateChallenge();
  console.log('IN HomePage')
  console.log('AuthURL:'+localStorage.getItem('authUrl'))
  return (
    <div className="App">    

    <header className="App-header">
  <p>
  Click Below to Sign In.
  </p>
  <a
  className="App-link"
  href={localStorage.getItem('authUrl')}
  target=""
  rel="noopener noreferrer"
  >
  Login test
  </a>
    </header>
  </div>

  );
}
export default HomePage;
