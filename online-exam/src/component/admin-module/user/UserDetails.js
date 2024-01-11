import React, { useState } from "react";
import { json, useLocation } from "react-router-dom";

const UserDetails = () => {
  const state = useLocation().state;
  const [userDetails, setUserDetails] = useState(state);
  return (
    <>
      <div className="container-fluid">
        <div className="row border-bottom  border-3 border-dark">
          <div className="col-8 p-0">
            <h2 className="p-2">User Details</h2>
          </div>
          
        </div>
      </div>
      <div className="container-fluid">
        <table className="table  table-striped ">
          <tbody>
            <tr>
            <th>
                Party Id
             </th>
             <th>
                User Login Id
             </th>
             <th>
                First Name
             </th>
             <th>
                Last Name
             </th>
             
             <th>
                Role Type Id 
             </th>

           
            </tr>
            <tr>
            <td>{userDetails.partyId}</td>
            <td>{userDetails.userLoginId}</td>
                <td>{userDetails.firstName}</td>
                <td>{userDetails.lastName}</td>
              
                <td>{userDetails.roleTypeId}</td>

            </tr>
          </tbody>
        </table>
      </div>
    </>
  );
};

export default UserDetails;
