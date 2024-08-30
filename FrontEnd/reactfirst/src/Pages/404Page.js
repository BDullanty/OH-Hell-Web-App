import {Button} from '@mui/material';
const NotFound=()=>{
  console.log('IN 404 page!')
  return  (
  <div>
    <p> Page request is not found.</p>
    <Button
      onClick={() => {
        window.location.replace('http://localhost:3000');
      }}>
        HomePage
      </Button>
  </div>
  );
}
export default NotFound;
