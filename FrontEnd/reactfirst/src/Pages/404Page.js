
function notFound(){
  generateChallenge();
  console.log('IN 404 page!')
  return (
    <div>
  <p>
  Page request is not found.
  </p>
  </div>

  );
}
export default notFound;
