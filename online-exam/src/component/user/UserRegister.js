import React, { useContext, useState } from 'react'
import Input from '../Input';
import { UserContext } from './User';


const UserRegister = () => {
    const {users,setUsers} = useContext(UserContext);
    const [firstName,setFirstName]=useState("");
    const [lastName,setLastName]=useState("");
    const [userLoginId,setUserLoginId]=useState("");
    const [currentPassword,setCurrentPassword]=useState("");
    const [currentPasswordVerify,setCurrentPasswordVerify]=useState("");

    const onSubmit=(e)=>{
      e.preventDefault();
      if(!firstName){
        alert("First Name is required");
        return;
      }
      if(!lastName){
        alert("Last Name is required");
        return;
      }
      if(!userLoginId){
        alert("User Login Id is required");
        return;
      }
      if(!currentPassword){
        alert("Current Password is required");
        return;
      }
      if(!currentPasswordVerify){
        alert("Current Password Verify is required");
        return;
      }
      onRegister({firstName,lastName,userLoginId,currentPassword,currentPasswordVerify})
      setFirstName('');        setLastName('');
      setUserLoginId('');
      setCurrentPassword('');
      setCurrentPasswordVerify('')
    }

    //User Registeration 
  const onRegister =async (userRegisterationDetails)=>{

    
    const res = await fetch("https://localhost:8443/onlineexam/control/createPersonAndUserLogin", {
      method: "POST",
      headers: {
        "Content-type": "application/json",
      },
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
      <form onSubmit={onSubmit}>
     
      <Input
          name={"userLoginId"}
          text="User Login Id"
          state={userLoginId}
          setStateFun={setUserLoginId}
          type={"text"}
          placeholder={""}
        />
      <Input
          name={"firstName"}
          text="First Name"
          state={firstName}
          setStateFun={setFirstName}
          type={"text"}
          placeholder={""}
        />
       <Input
          name={"lastName"}
          text="Last Name"
          state={lastName}
          setStateFun={setLastName}
          type={"text"}
          placeholder={""}
        />
        <Input
          name={"currentPassword"}
          text="Current Password"
          state={currentPassword}
          setStateFun={setCurrentPassword}
          type={"password"}
          passView={true}
          placeholder={""}
        />
        <Input
          name={"currentPasswordVerify"}
          text="Current Password Verify"
          state={currentPasswordVerify}
          setStateFun={setCurrentPasswordVerify}
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