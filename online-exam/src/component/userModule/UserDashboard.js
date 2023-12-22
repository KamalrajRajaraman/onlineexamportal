import React from 'react'
import UserSideBar from './UserSideBar'
import { Link, Outlet, Route, Routes } from 'react-router-dom'
import ExamsForUser from './ExamsForUser'


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
