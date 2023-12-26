import React, { useState } from 'react'
import QuestionType from './QuestionType'
import Question from './Question'
import Options from './Options'
import QuestionTiles from './QuestionTiles'
import CandidateDetail from './CandidateDetail'
import QuestionNav from './QuestionNav'

const AttendExam = () => {

    const questions=[{q:"What is JAVA ?",},{q:"Why do we need JAVA"}];
    const [activeQuestion,setActiveQuestion]=useState(`${questions[0].q}`)
    const[activeQuestionType,setActiveQuestionType]=useState(`${questions[0].qt}`)
    const[activeQuestionId,setActiveQuestionId]=useState(1)

    function onChange(id){
        setActiveQuestionId(id+1)
        setActiveQuestion(questions[id].q)
        setActiveQuestionType(questions[id].qt)
        
    }

    // useEffect(()=>{
    //     const fetchTopic = fetch(`https://localhost:8443/onlineexam/control/`)
    // },[])

    return (

        <div class="container-fluid  exam-body py-3 ">
            <div class="row">
                <div class="col-9  ">
                    <ul class="nav nav-pills border border-secondary-subtle p-2 rounded">
                        <li class="nav-item">
                            <button class="nav-link active">Core Java</button>
                        </li>
                        <li class="nav-item">
                            <button class="nav-link" >Advanced Java</button>
                        </li>

                    </ul>
                </div>
                <div class="col-3 text-center">

                <CandidateDetail/>

                </div>

            </div>
            <div class="row mt-3 ">
                <div class="col-9 ">
                    <div className='border border-secondary-subtle p-3 rounded pb-5'>
                    <QuestionType questionType={activeQuestionType}/>
                    <hr />
                    <Question  question={activeQuestion}/>
                    <Options questionType={activeQuestionType}/>
                    </div>
                    <QuestionNav/>

                </div>
                <div class="col-3">
                    <QuestionTiles questions={questions} setQuestion={onChange}/>
                </div>
            </div>
        </div>
    )
}

export default AttendExam
