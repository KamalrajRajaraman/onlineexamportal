import React from 'react'
import AttendExam from './AttendExam'
import Head from './Head'
import Foot from './Foot'
import "./Styles/examPage.css"
const ExamPage = () => {
  return (
    <div className='exam-body'>
      <Head/>
      <AttendExam/>
      <Foot/>
    </div>
  )
}

export default ExamPage
