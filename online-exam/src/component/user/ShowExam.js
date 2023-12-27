import React, { useContext, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom';
import { EditUserContext } from './EditUser';

const ShowExam = () => {
    const {partyId} =useContext(EditUserContext);
    const [exams, setExams] =useState([]);
    const param=useParams();
   
    useEffect(()=>{
        fetch(`https://localhost:8443/onlineexam/control/showExamsForPartyId`,
        {
          method:'POST', 
          headers:
          {'Content-type':'application/json'},
          credentials: 'include',
         body: JSON.stringify({partyId})
        }
        )
        .then(response=>{ return response.json()})
        .then(data=>{
          console.log('final data:::', data);
          setExams(data.examList);
           console.log(data.examList)
          // setUsers(data.userList)
        })
      },[])

  return (
    <div>
       <div className='card-body'>
            <table className='table table-bordered'>
                <thead className='bg-dark text-white'>
                    <tr>
                         <th> Exam ID</th>
                         <th> Exam Name</th>
                        {/* <th>Action</th> */}
                    </tr>
                    </thead>

      <tbody>
       {exams&&

       
        exams.map((exam)=>(
          
            <tr key={exam.examId}>
              <td>{exam.examId}</td>
              <td>{exam.examName}</td>
            </tr>
            )
        )
       }
       </tbody>
       </table>
       </div>
    </div>
  )
}

export default ShowExam
