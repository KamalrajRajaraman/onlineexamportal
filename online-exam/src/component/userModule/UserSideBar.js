import React from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../../auth';
const UserSideBar = () => {
  const {logout} = useAuth();
  const onlogout =async() =>{
    const res = await fetch("https://localhost:8443/onlineexam/control/onlineExamLogout",{credentials:"include"});
    const data = await res.json();
    console.log(data);
    if(data.result==="success"){
      logout();
    }
  }
  return (
    <div>
      <>
      <div className="list-group">
       
        <Link to="exams" className="list-group-item list-group-item-action">
          Exams
        </Link>
        <Link to="" className="list-group-item list-group-item-action">
            Performance Report
        </Link>
        <button  onClick = {onlogout}type="button" class="list-group-item list-group-item-action" >Logout</button>
      

      </div>
    </>
    </div>
  )
}

export default UserSideBar















