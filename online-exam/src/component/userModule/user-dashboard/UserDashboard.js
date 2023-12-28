import React from 'react'
import UserSideBar from '../sidebar/UserSideBar'
import {  Outlet } from 'react-router-dom'



const UserDashboard = () => {
  return (
    <div className="container-fluid">
    <div className="row">
      <div className="col-2 p-0">
        <UserSideBar />
      </div>
      <div className="col-10 ">
        <Outlet/>       
      </div>
    </div>

  </div>
  )
}

export default UserDashboard
