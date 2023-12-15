import React, { useEffect, useState } from 'react'
import TableRowMaker from './TableRowMaker'

const ExamTopicTable = ({examId,examTopicMap,setExamTopicMap}) => {
    
    
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
        const res =await fetch(`https://localhost:8443/onlineexam/control/findAllExamTopicMappingMasterRecordByExamId?examId=${examId}`);
        const data = await res.json();
        console.log(data);
        const{examTopicMappingRecordList}=data;
        console.log(examTopicMappingRecordList);
        return examTopicMappingRecordList;
    }

  return (
    <div className="border mt-2">
    <table className="table">
      <thead>
        <tr>
          <th scope="col">Exam Id</th>
          <th scope="col">Topic Id</th>
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