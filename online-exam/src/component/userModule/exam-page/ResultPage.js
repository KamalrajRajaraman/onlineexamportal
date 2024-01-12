import React, { useEffect, useState } from 'react'
import useStateRef from 'react-usestateref';
import { useLocation } from 'react-router-dom';
import { BsAward } from "react-icons/bs";
import { FaHashtag } from "react-icons/fa6";
import { BsClipboardCheckFill } from "react-icons/bs";
import { BsClipboard2XFill } from "react-icons/bs";
import { BsAwardFill } from "react-icons/bs";
import { HiMiniReceiptPercent } from "react-icons/hi2";
import { PiPercentFill } from "react-icons/pi";


const ResultPage = () => {
//  const {state} = useLocation();
//  const {actualUserPercentage, passPercentage} = state;
 
  const [result, setResult] = useState({
    noOfQuestions:'', score : '', totalCorrect : '', totalWrong : '', userPassed:'', passPercentage : '', actualUserPercentage : '',
  });
  const [topicResult, setTopicResult, topicResultRef] =  useStateRef([])



   useEffect(()=>{
        fetchResult();
  }
  ,[])

  const fetchResult = async () => {

  
    try {
      const res = await fetch(
        `https://localhost:8443/onlineexam/control/evaluateUserAttemptAnswerMaster`,
        { 
        credentials: "include"
      }
      );
      const data = await res.json();
      const resultDetails = data.resultMap;
      
      setTopicResult(...topicResult, resultDetails.updatedUserAttemptTopicMasterList);

      setResult({noOfQuestions:resultDetails.noOfQuestions, 
                score:resultDetails.score, 
                totalCorrect : resultDetails.totalCorrectQuestionsInExam,
                totalWrong : resultDetails.totalWrongAnswersInExam,
                userPassed : resultDetails.userPassed,
                passPercentage : resultDetails.passPercentage,
                actualUserPercentage : resultDetails.actualUserPercentage
              });

  
    } 
    catch (error) {
      console.log(error);
    } 
  };

 


  return (
    <>
  
  <div><h2>Exam Results</h2></div>
<div>
     <div class="row mt-3">
      <div class="col-xl-3 col-sm-6 col-12"> 
        <div class="card  border-info">
          <div class="card-content  ">
            <div class="card-body">
              <div class="media d-flex">
                <div class="align-self-center">
                  <i class="icon-pencil primary font-large-2 float-left"></i>
                </div>
                <div class="media-body text-right">
            
                  <h3> <FaHashtag />{result.noOfQuestions}</h3>
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
               
                </div>
                <div class="media-body text-right">
             
                  <h3> <BsAward />{result.score}</h3>
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
                <i class="bi bi-award"></i>
                  
                </div>
                <div class="media-body text-right">
                  <h3><HiMiniReceiptPercent />{result.passPercentage}</h3>
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
                  <h3><PiPercentFill />{result.actualUserPercentage}</h3>
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
                  <h3><BsClipboardCheckFill /> {result.totalCorrect}</h3>
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
                  <h3><BsClipboard2XFill />{result.totalWrong}</h3>
                  <span>Total wrong questions</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
   
    </div>
  
  </div>

<div className='mt-3'><h2>Topicwise Results</h2></div>

<table class="table table-success table-striped table-bordered  mt-3">
  <tr className='border bg-primary '>
    <th>TopicId</th>
    <th>Topic pass percentage</th>
    <th>User topic percentage</th>
    <th>User passed this topic</th>
  </tr>
{topicResult && topicResult.map((value)=>{
  return(
        <tr className='mt-4' key={value.topicId}>
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