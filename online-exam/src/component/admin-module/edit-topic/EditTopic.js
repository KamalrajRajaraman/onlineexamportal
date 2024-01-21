import React, { useEffect, useState } from 'react'
import { TiEdit } from 'react-icons/ti';
import { useLocation, useParams } from 'react-router-dom'
import Swal from "sweetalert2";
import { CONTROL_SERVLET, DOMAIN_NAME, GET, PORT_NO, PROTOCOL, WEB_APPLICATION } from '../../common/CommonConstant';
import MainContent from '../../common/MainContent';
import AccordinMaker from '../../common/AccordinMaker';
import useStateRef from 'react-usestateref';
import { useQuestionContext } from '../question/QuestionData';
import EditQuestion from '../question/EditQuestion';


const EditTopic = () => {
   const {topicId} = useParams();
    const topicRecord =useLocation().state;
    const  [topicDetails, settopicDetails] = useState(topicRecord);
    const [questions, setQuestions, questionRef] = useStateRef([]);
    
    const {question, setQuestion, onDelete, onEdit} = useQuestionContext();
    console.log("topicIdddd::", topicId);
    console.log('topicDetails::', topicDetails);

    const text = {
        header: topicDetails.topicName,
        btnText: "Question",
      };

      useEffect(() => {
        fetchQuestion(topicId);
        
      },[question]);

      const fetchQuestion = async (topicId) => {
       
        const res = await fetch(
          PROTOCOL +
            DOMAIN_NAME +
            PORT_NO +
            WEB_APPLICATION +
            CONTROL_SERVLET +
            `findQuestionsByTopicId?topicId=${topicId}`, {
              method: 'POST',
            
              credentials:'include'
             
         
            })


        const data = await res.json();
    
        if (data.result === "success") {
          const questionList = data.questionList;
          console.log('questionList:::', questionList);
          setQuestions(questionList);
        }
      };

      const modalEdit = async (id, question) => {
        setQuestion(question);
      };
   
 
    return (
       
        <div>
            <MainContent
        text={text}
        to="add-question-to-topic"
        back={`/admin/topic/edit-topic/${topicId}`}
      />
     {questions ? <AccordinMaker
        objects={questions}
        id={"questionId"}
        name={"questionDetail"}
        onDelete={onDelete}
        onEdit={modalEdit}
      />:"No Questions.Please Add Question"}
      {question && <EditQuestion question={question} setQuestion={setQuestion}/>}
      
        </div>
    )
}

export default EditTopic
