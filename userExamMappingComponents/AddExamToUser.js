import React, { useEffect, useState } from 'react'

import UserExamMapping from './component/UserExamMapping'
import { useParams } from 'react-router-dom'

const AddExamToUser = () => {
 
  const params=useParams();
  const partyId= params.partyId
  const initialValues={ partyId: partyId, examId:'', allowedAttempts:'', noOfAttempts:'', 
                          lastPerformanceDate:'', timeoutDays:'', passwordChangesAuto:'Y', 
                          canSplitExams:'Y', canSeeDetailedResults:'Y', maxSplitAttempts:'' }
    
    const [formValues, setFormValues] = useState(initialValues);
    const [exams, setExams] =useState(null);

    useEffect(()=>{
        fetch(`https://localhost:8443/onlineexam/control/findExams`,
        {
          method:'POST',
         }).then((response)=>{
          if(response.ok){
            return response.json();
          }
         }).then((data)=>{
          console.log('data:::',data.examList)
          setExams(data.examList);
        });
    },[] )

    const handleChange = (e)=>{
      const {name, value}=e.target;
      console.log('name:', name, 'value:', value);
      setFormValues({...formValues, [name]: value });
      console.log(formValues);
    }

    const handleSubmit= (e)=>{
      e.preventDefault();
      console.log('handlesubmit called');
      const fetchExamsForUser=()=>{
        console.log('fetch called');
        console.log('formvalues:::::', formValues);
        fetch('https://localhost:8443/onlineexam/control/add-exam-to-user',
        {
          method:'POST', 
          headers:
          {'Content-type':'application/json'},
         body: JSON.stringify(formValues)
      }).then(response => {
        if(response.ok){
          alert('Mapping created succesfully')
          console.log(response);
            // navigate('/')
        }
      }).catch(error => {
        throw(error);
    })
    }
    fetchExamsForUser();
    }

    // useEffect(()=>{
    //   fetch(`https://localhost:8443/onlineexam/control/`)
    //   .then(response=>{ return response.json()})
    //   .then(data=>{
    //     console.log(data.userList)
    //     setUsers(data.userList)
    //   })
    // },[])  

   
    //  setFormValues({...formValues, [partyId]:partyId})
  return (
    <div>
      Hai Mr. number: {partyId}

  <form onSubmit={handleSubmit}  class="row g-3">
  <div class="col-md-2">
    <label  class="form-label">PartyId</label>
    <input name='partyId' type="text" class="form-control" value={formValues.partyId}  />
  </div>
    <div>
      <label>ExamId</label>
      <select name='examId' type="text" onChange={handleChange} value={formValues.examId} class="form-control">
            {exams&&
            exams.map((exam)=>{
              <option value={exam.examId}>{exam.examId}</option>
            })

            }
      </select>
    </div>
  <div class="col-md-2">
    <label class="form-label">ExamId</label>
    <input name='examId' type="text" onChange={handleChange} value={formValues.examId} class="form-control" />
  </div>

  <div class="col-2">
    <label class="form-label">Allowed attempts</label>
    <input name='allowedAttempts' type="text"  onChange={handleChange} value={formValues.allowedAttempts} class="form-control" />
  </div>

  <div class="col-2">
    <label  class="form-label">NoOfAttempts</label>
    <input name='noOfAttempts' type="text"  onChange={handleChange} value={formValues.noOfAttempts} class="form-control" />
  </div>

  <div class="col-4">
    <label class="form-label">Last Performace Date</label>
    <input name='lastPerformanceDate' type="date"  onChange={handleChange} value={formValues.lastPerformanceDate} class="form-control"/>
  </div>

  <div class="col-3">
    <label class="form-label">Timeout Days</label>
    <input name='timeoutDays' type="date"  onChange={handleChange} value={formValues.timeoutDays} class="form-control"/>
  </div>

    <div class="col-3">
      <label class="form-label">Password Changes Auto</label>
      <select  name='passwordChangesAuto' onChange={handleChange} value={formValues.passwordChangesAuto} class="form-select" >
          <option value="Y">Yes</option>
          <option value="N">No</option>
      </select>
    </div>

    <div class="col-md-3">
    <label class="form-label">Can Split Exams</label>
    <select name='canSplitExams'  onChange={handleChange} value={formValues.canSplitExams}  class="form-select">
      <option value='Y' >Yes</option>
      <option value='N'>No</option>
    </select>
  </div>

  <div class="col-md-3">
    <label class="form-label">Can See Detailed Results</label>
    <select name='canSeeDetailedResults' onChange={handleChange} value={formValues.canSeeDetailedResults}  class="form-select">
      <option value='Y' >Yes</option>
      <option value='N'>No</option>
    </select>
  </div>
  
  <div class='col-md-3'>
    <label class="form-label">Max Split Attempts</label>
    <input name='maxSplitAttempts' type="number"  onChange={handleChange} value={formValues.maxSplitAttempts}d />
  </div>

 
  
  <div class="col-12">
  <button type="submit" onClick={handleSubmit} class="btn btn-primary">Submit</button>
  </div>

</form>





    </div>
  )
}

export default AddExamToUser
