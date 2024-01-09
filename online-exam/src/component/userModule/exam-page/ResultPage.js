import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom';
import useStateRef from 'react-usestateref';

const ResultPage = () => {
  const [result, setResult] = useState({
    noOfQuestions:'', score : '', totalCorrect : '', totalWrong : '', userPassed:''
  });
  const [topicResult, setTopicResult, topicResultRef] =  useStateRef([])



   useEffect(()=>{
      fetchTopicResults();
      fetchResults();
  }
  ,[])

  const fetchTopicResults = async () => {
    
    try {
      const res = await fetch(
        `https://localhost:8443/onlineexam/control/findUserAttemptTopicMasterRecordsForReport`,
        { credentials: "include" }
      );
      const data = await res.json();
      console.log(data);

      const { topicWiseResultDetails } = data;
      setTopicResult(topicWiseResultDetails);
      if(data.result==="error"){
      
        
      }
        if(data.result==="success"){
        console.log(data);
      
        
        // const setFirstQuestion =questionsRef.current[0] 
        // setActiveQuestion({ ...setFirstQuestion ,isViewed:true});
        // setShowExam(true);
       
      }
    } catch (error) {
      console.log(error);
    }
    console.log('topicResultRef.topicPassPercentage', topicResultRef.topicPassPercentage);
  }

  const fetchResults = async () => {
    
    try {
      const res = await fetch(
        `https://localhost:8443/onlineexam/control/findUserAttemptMasterRecordsForReport`,
        { credentials: "include" }
      );
      const data = await res.json();
   console.log('data', data.AttemptMasterResultDetails);
   const values = data.AttemptMasterResultDetails;
   result.noOfQuestions = values.noOfQuestions;
  result.score = values.score;
  result.totalCorrect =values.totalCorrect;
  result.totalWrong = values.totalWrong;
  result.userPassed = values.userPassed;

  console.log( result.noOfQuestions, result.score,  result.totalCorrect ,result.totalWrong,result.userPassed);
   
      if(data.result==="error"){
      
        
      }
        if(data.result==="success"){
        console.log(data);
       
      }
    } catch (error) {
      console.log(error);
    }
  }



  return (
    <>
  
  
<div>
     <div class="row mt-3">
      <div class="col-xl-3 col-sm-6 col-12"> 
        <div class="card  border-info  ">
          <div class="card-content  ">
            <div class="card-body">
              <div class="media d-flex">
                <div class="align-self-center">
                  <i class="icon-pencil primary font-large-2 float-left"></i>
                </div>
                <div class="media-body text-right">
                  <h3>{result.noOfQuestions}</h3>
                  <span>No of Questions</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-xl-3 col-sm-6 col-12">
        <div class="card border-primary">
          <div class="card-content">
            <div class="card-body">
              <div class="media d-flex">
                <div class="align-self-center">
                  <i class="icon-speech warning font-large-2 float-left"></i>
                </div>
                <div class="media-body text-right">
                  <h3>{result.score}</h3>
                  <span>Score</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-xl-3 col-sm-6 col-12">
        <div class="card border-secondary">
          <div class="card-content">
            <div class="card-body">
              <div class="media d-flex">
                <div class="align-self-center">
                  <i class="icon-graph success font-large-2 float-left"></i>
                </div>
                <div class="media-body text-right">
                  <h3>64.89 %</h3>
                  <span>Pass percentage</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-xl-3 col-sm-6 col-12">
        <div class="card border-warning">
          <div class="card-content">
            <div class="card-body">
              <div class="media d-flex">
                <div class="align-self-center">
                  <i class="icon-pointer danger font-large-2 float-left"></i>
                </div>
                <div class="media-body text-right">
                  <h3>45</h3>
                  <span>User percentage</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row mt-3">
      <div class="col-xl-3 col-sm-6 col-12"> 
        <div class="card border-secondary">
          <div class="card-content">
            <div class="card-body">
              <div class="media d-flex">
                <div class="align-self-center">
                  <i class="icon-pencil primary font-large-2 float-left"></i>
                </div>
                <div class="media-body text-right">
                  <h3>{result.totalCorrect}</h3>
                  <span>Total correct Questions</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-xl-3 col-sm-6 col-12">
        <div class="card border-secondary">
          <div class="card-content">
            <div class="card-body">
              <div class="media d-flex">
                <div class="align-self-center">
                  <i class="icon-speech warning font-large-2 float-left"></i>
                </div>
                <div class="media-body text-right">
                  <h3>{result.totalWrong}</h3>
                  <span>Total wrong questions</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
   
    </div>
  
  </div>

 {/* <div className="grid ">
    <div className='col-span-1'>
        <div className='mx-auto bg-sky-50 rounded-xl shadow-lg '>
           
            <div className='flex justify-between'>
                <div className='ml-5 mt-5'>
                Icon
                </div>
            </div>

            <div className='pl-7 py-5'>
                <div className='text-blue-600 font-semibold'>
                      No of Questions
                </div>
                <div className='text-3xl font-semibold'>
                  60
                </div>

            </div>

        </div>

    </div>

 </div> */}




<table class="table table-success table-striped  table-bordered  mt-3">
  <tr className='border'>
    <th>TopicId</th>
    <th>Topic pass percentage</th>
    <th>User topic percentage</th>
    <th>User passed this topic</th>
  </tr>
{topicResult && topicResult.map((value)=>{
  return(
        <tr key={value.topicId}>
          <td>{value.topicId}</td>
          <td>{value.topicPassPercentage}</td>
          <td>{value.userTopicPercentage}</td>
          <td>{value.userPassedThisTopic}</td>
           </tr>
  )
})}

</table>



    </>
  )
}


export default ResultPage