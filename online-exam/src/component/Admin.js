import React from 'react'
import SideBar from './SideBar'
import { Outlet } from 'react-router-dom'

const Admin = () => {
  return (
    <div className="container-fluid">
    <div className="row">
      <div className="col-2 p-0">
        <SideBar />
      </div>
      <div className="col-10">
        <Outlet/>
      </div>
    </div>
  </div>
  )
}

export default Admin