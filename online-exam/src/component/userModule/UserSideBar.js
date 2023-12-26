import React from 'react'
import { Link } from 'react-router-dom'
const UserSideBar = () => {
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
      

      </div>
    </>
    </div>
  )
}

export default UserSideBar















