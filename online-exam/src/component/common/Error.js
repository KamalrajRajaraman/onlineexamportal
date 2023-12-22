import React from 'react'
import { useParams } from 'react-router-dom'
import Login from '../Login';

const Error = () => {
   const {errorMessage} = useParams();
  return (
    <div>
       <h2 className='text-danger text-center'>{`Its ${errorMessage}.
       Please Enter proper credentials to login` }</h2>
       
    </div>
  )
}

export default Error