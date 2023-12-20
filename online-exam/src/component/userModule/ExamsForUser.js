import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';

const ExamsForUser = () => {
    const [exams, setExams] = useState([]);
    const navigate = useNavigate();
    const partyId='10025';
    useEffect(()=>{
        fetch(`https://localhost:8443/onlineexam/control/showExamsForPartyId`,
        {
            method:'POST', 
            headers:
            {'Content-type':'application/json'},
           body: JSON.stringify({partyId})
          }
        )
        .then((response)=> {return response.json()})
        .then((data)=> 
        {
            console.log('data.examlist::::',data.examList);
            setExams(data.examList);
        })
    },[])

  return (
    <div>
        <h1 className='container col-12'>Scheduled Exams</h1>
     {
        exams.map((exam)=>(
          
           
            <div className='container col-6 border rounded bg-secondary '>  
            <h4>{exam.examName}<span className='offset-7 '><button className='mt-3 btn bg-primary' onClick={()=>navigate('/introduction')}>Take Exam</button></span></h4>
            </div>
          
        )
        )
     }
    </div>
  )
}

export default ExamsForUser
