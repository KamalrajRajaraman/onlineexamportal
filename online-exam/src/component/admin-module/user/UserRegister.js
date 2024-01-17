import React, { useEffect, useState } from 'react'
import FormInput from '../../common/FormInput';
import Swal from 'sweetalert2';
import { CONTROL_SERVLET, DOMAIN_NAME, PORT_NO, PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstant";

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


    //User Registeration 
  const onRegister =async (userRegisterationDetails)=>{

    
    const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+"createPersonAndUserLogin", {
      method: "POST",
      headers: {
        "Content-type": "application/json",
      },
      credentials: 'include',
      body: JSON.stringify(userRegisterationDetails),
    });

    const data = await res.json();

    console.log(data);
    if(data.result==="success"){
      Swal.fire({
        title: "Good job!",
        text: "User is added to exam created successfully!",
        icon: "success",
        timer:2000,
        showConfirmButton:false
      });
      setUserValues(initialValues);
    }
    if(data.result==="error"){
      const errorFields = data.ERRORED_FIELD_NAMES
      let errMsg ="";
      errorFields.forEach(errorField => {
        errMsg =errMsg+data[errorField]+".";
      })
      Swal.fire({
        icon: "error",
        title:data._ERROR_MESSAGE_,
        text: errMsg
      });


    }



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