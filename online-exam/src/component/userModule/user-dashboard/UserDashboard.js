import React from 'react'
import UserSideBar from '../sidebar/UserSideBar'
import {  Outlet } from 'react-router-dom'
import UserNavBar from '../sidebar/UserNavBar'



const UserDashboard = () => {
  return (
    <div className="container-fluid p-0">
   
      {/* <div className="col-2 p-0">
        <UserSideBar />
      </div> */}
      
     <UserNavBar/>
      <Outlet/>       
     
   

  </div>
  )
}

export default UserDashboard
