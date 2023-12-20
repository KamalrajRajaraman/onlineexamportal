
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import useComponent from './useComponent';

const Introduction = () => {
    const navigate = useNavigate();
    // const {start, secondsLeft} = useComponent();
   // start(5);
    useEffect(() => {
        // Navigate to "/target-page" after 3000 milliseconds (3 seconds)
        const timeoutId = setTimeout(() => {
          navigate('/exam-page');
        }, 5000);
    
        // Clean up the timeout to avoid memory leaks
        return () => clearTimeout(timeoutId);
      }, []);
    
  return (
    <div className='container border'>
      <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Sit rem nesciunt saepe optio deleniti perferendis vero quae doloremque voluptate voluptates nisi eveniet architecto similique, aliquid animi dolores beatae incidunt fugiat!</p>
        <h4>Your exam will start within 5  seconds</h4>
    </div>
  )
}

export default Introduction
