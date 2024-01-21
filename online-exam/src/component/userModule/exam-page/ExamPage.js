import React from 'react'
import AttendExam from './AttendExam'
import Head from './Head'

import "./Styles/examPage.css"
const ExamPage = () => {
  return (
    <div className='exam-body'>
      <Head/>
      <AttendExam/>
     
    </div>
  )
}

export default ExamPage
