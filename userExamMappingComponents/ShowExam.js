import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom';

const ShowExam = () => {

    const [exams, setExams] =useState([]);
    const param=useParams();
    const partyId= param.partyId;
    useEffect(()=>{
        fetch(`https://localhost:8443/onlineexam/control/showExamsForPartyId`,
        {
          method:'POST', 
          headers:
          {'Content-type':'application/json'},
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
                        <th>Action</th>
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
