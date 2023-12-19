import React, { useEffect, useState } from 'react'
import { Link, Outlet } from 'react-router-dom'
import { useNavigate } from 'react-router-dom'

    // const users=
    //     [
    //         {
    //             'partyId':'1',
    //             'name':'ganesh'
    //         },{
    //             'partyId':'2',
    //             'name':'naren'
    //         }
    //     ]
    
        
const User = () => {
    const navigate= useNavigate()
    const [users, setUsers] = useState([]);
  useEffect(()=>{
    fetch(`https://localhost:8443/onlineexam/control/findAllUsers`)
    .then(response=>{ return response.json()})
    .then(data=>{
      console.log(data.userList)
      setUsers(data.userList)
    })
  },[])

  return (
    <div>
      <button onClick={()=>navigate('/admin/add-user')}>Add User</button>
<table class="table table-striped">
  

      <thead className=''>
                    <tr>
                         <th> Party ID</th>
                         <th> First Name</th>
                          <th>LastName</th>
                          <th>User LoginId</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>

      {users&&
        users.map((user)=>
        <tr key={user.userLoginId}>
            <td>{user.partyId}</td>
           <td>{user.firstName}</td>
           <td>{user.lastName}</td>
           <td>{user.userLoginId}</td>
       
           <td>
                   {/* <Link onClick={()=>navigate(`detail/${exam.examId}`)} className='btn btn-success bg-primary '>Details</Link> */}
                     {/* <Link to={`/admin/detail/${exam.examId}`} className='btn btn-success bg-primary '>Details</Link> */}
                     {/* <Link  className='btn btn-success bg-success'>Edit</Link> */}
                     {/* <Link to={`/admin/detail/${exam.examId}`} className='btn btn-success bg-primary '>Details</Link> */}
                     <button onClick={()=>navigate(`/add-exam-to-user/${user.partyId}`)} className='btn btn-success bg-dark'>Add Exam</button>
                     <button  onClick={()=>navigate(`/show-exams/${user.partyId}`)} className='btn btn-success bg-danger'>Exams</button>
          </td>
      </tr>
        )
      }
       </tbody>
       </table>
      
      <Outlet/>
    </div>
  )
}

export default User
