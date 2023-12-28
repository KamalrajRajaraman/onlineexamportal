import React, { useContext, useEffect } from 'react'
import TableRowMaker from './TableRowMaker'
import { EditExamContext } from './EditExam';

const ExamTopicTable = () => {
    //Consuming data from EditExamContext
    const {examId,examTopicMap,setExamTopicMap} = useContext(EditExamContext);

    //fetch data on Mount
    useEffect(()=>{
        getExamTopicMappingRecords();
    },[])

    
    const getExamTopicMappingRecords =async()=>{
        const list = await fetchExamTopicMappingRecords();
        if(list){
         setExamTopicMap([...list])
        }
    }
    
    const fetchExamTopicMappingRecords=async ()=>{
        const res =await fetch(`https://localhost:8443/onlineexam/control/findAllExamTopicMappingMasterRecordByExamId?examId=${examId}`,{  credentials: 'include'});
        const data = await res.json();
        const{_ERROR_MESSAGE_}=data;
        if(_ERROR_MESSAGE_){
          console.log(_ERROR_MESSAGE_);
        }
        else{
          const{examTopicMappingRecordList}=data;
          return examTopicMappingRecordList;
        }
    }

  return (
    <div className="mt-2">
    <table className="table table-bordered">
      <thead>
        <tr>
          <th scope="col">Exam Id</th>
          <th scope="col">Exam Name</th>
          <th scope="col">Topic Id</th>
          <th scope="col">Topic Name</th>
          <th scope="col">Percentage</th>
          <th scope="col">Topic Pass Percentage</th>
          <th scope="col">Question Per Exam</th>
        </tr>
      </thead>
      <tbody>
        <TableRowMaker array={examTopicMap}/>
      </tbody>
    </table>
    </div>
  )
}

export default ExamTopicTable