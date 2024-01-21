import React from 'react'
import UserSideBar from '../sidebar/UserSideBar'
import {  Outlet } from 'react-router-dom'
import UserNavBar from '../sidebar/UserNavBar'



const UserDashboard = () => {
  return (
    <div className="container-fluid p-0">   
     <UserNavBar/>
      <Outlet/>       
  </div>
  )
}

export default UserDashboard
