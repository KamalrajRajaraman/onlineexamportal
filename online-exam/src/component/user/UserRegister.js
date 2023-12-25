import React, { useContext, useEffect, useState } from 'react'
import Input from '../Input';
import { UserContext } from './User';
import FormInput from '../common/FormInput';


const UserRegister = () => {
   
  const initialValues={
    firstName:'', lastName:'', userLoginId:'', currentPassword:''
, currentPasswordVerify:''  }

   const [userValues, setUserValues] = useState(initialValues);
   const [formErrors, setFormErrors] = useState({});
   const [isSubmit, setIsSubmit] = useState(false);
 
   const handleChange = (e)=>{
         const {name, value} = e.target;
         setUserValues({...userValues, [name]:value});
   }
 
   const handleSubmit = (e)=>{
     console.log("handle submit called");
       e.preventDefault();
       setFormErrors(validate(userValues));
      
       setIsSubmit(true);
   
   }
 
   useEffect(()=>{
     console.log(Object.keys(formErrors).length);
     if(Object.keys(formErrors).length === 0 && isSubmit ){
      onRegister(userValues);
     }
   },[formErrors]
   )

   const validate=(values)=>{
    const errors={}

    if(!userValues.firstName){
      errors.firstName = "First name is required!"
    }
    if(!userValues.lastName){
      errors.lastName = "Last name is required!"
    }
    if(!userValues.userLoginId){
      errors.userLoginId = "UserLogin Id is required!"
    }
    if(!userValues.currentPassword){
      errors.currentPassword = "Current password is required!"
    }
    if(!userValues.currentPasswordVerify){
      errors.currentPasswordVerify = "Current password verify is required!"
    }

    return errors;
   }


    // const onSubmit=(e)=>{
    //   e.preventDefault();
      // if(!firstName){
      //   alert("First Name is required");
      //   return;
      // }
      // if(!lastName){
      //   alert("Last Name is required");
      //   return;
      // }
      // if(!userLoginId){
      //   alert("User Login Id is required");
      //   return;
      // }
      // if(!currentPassword){
      //   alert("Current Password is required");
      //   return;
      // }
      // if(!currentPasswordVerify){
      //   alert("Current Password Verify is required");
      //   return;
      // }
      // onRegister({firstName,lastName,userLoginId,currentPassword,currentPasswordVerify})
      // setFirstName('');        
      // setLastName('');
      // setUserLoginId('');
      // setCurrentPassword('');
      // setCurrentPasswordVerify('')
   // }

    //User Registeration 
  const onRegister =async (userRegisterationDetails)=>{

    
    const res = await fetch("https://localhost:8443/onlineexam/control/createPersonAndUserLogin", {
      method: "POST",
      headers: {
        "Content-type": "application/json",
      },
      credentials: 'include',
      body: JSON.stringify(userRegisterationDetails),
    });

    const data = await res.json();

    console.log(data);



  }
  return (
    <div className="container-fluid ">
     
       <div className='row col-5 mx-auto shadow mt-2 p-2'>
        <div className='border-bottom border-3  border-dark'>
       <h2 className='text-center '> User Registeration</h2>
       </div>
       <div className='pt-1'>
      <form onSubmit={handleSubmit}>
     
      <FormInput
          name={"userLoginId"}
          value={userValues.userLoginId}
          text="User Login Id"
          onChange={handleChange}
          type={"text"}
          error={formErrors.userLoginId}
          placeholder={""}
        />
      <FormInput
          name={"firstName"}
          value={userValues.firstName}
          text="First Name"
          onChange={handleChange}
          error={formErrors.firstName}
          type={"text"}
          placeholder={""}
        />
       <FormInput
          name={"lastName"}
          text="Last Name"
          value={userValues.lastName}
          onChange={handleChange}
          error={formErrors.lastName}
          type={"text"}
          placeholder={""}
        />
        <FormInput
          name={"currentPassword"}
          text="Current Password"
          value={userValues.currentPassword}
          error={formErrors.currentPassword}
          onChange={handleChange}
          type={"password"}
          passView={true}
          placeholder={""}
        />
        <FormInput
          name={"currentPasswordVerify"}
          text="Current Password Verify"
          onChange={handleChange}
          value={userValues.currentPasswordVerify}
          error={formErrors.currentPasswordVerify}
          type={"password"}
          placeholder={""}
          passView={true}
        />
       
        <button type="submit" className="btn btn-primary">
          Submit
        </button>
      </form>
      </div>
      </div>
    </div>
  )
}

export default UserRegister