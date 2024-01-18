import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { CONTROL_SERVLET, DOMAIN_NAME, PORT_NO,  PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstant";
const ViewExam = ({ examDetails }) => {

    const navigate=useNavigate();

    const [examId, setExamId] = useState(examDetails.examId);
    const [examName, setExamName] = useState(examDetails.examName);
    const [description, setDescription] = useState(examDetails.description);
    const [creationDate, setCreationDate] = useState(examDetails.creationDate);
    const [expirationDate, setExpirationDate] = useState(examDetails.expirationDate);
    const [noOfQuestions, setNoOfQuestions] = useState(examDetails.noOfQuestions);
    const [durationMinutes, setDurationMinutes] = useState(examDetails.durationMinutes);
    const [passPercentage, setPassPercentage] = useState(examDetails.passPercentage);
    const [questionsRandomized, setQuestionsRandomized] = useState(examDetails.questionsRandomized);
    const [answersMust, setAnswersMust] = useState(examDetails.answersMust);
    const [enableNegativeMark, setEnableNegativeMark] = useState(examDetails.enableNegativeMark);
    const [negativeMarkValue, setNegativeMarkValue] = useState(examDetails.negativeMarkValue);

    async function updateExam(data) {

        await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+'editExam', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json"
            },
            credentials:"include",
            body: JSON.stringify(data),
        })
            .then((response) => { return response.json() })
            .then((res) => {
                console.log(res);
                if (res.result === "success") {
                    alert("Exam updated successfully!");

                }
                else {
                    alert("Failed updated exam!")
                }
                navigate("/admin/exam/edit/examId/111")
            })
    }


    const submitExamDetails = (e) => {
        e.preventDefault();
        updateExam({
            examId,
            examName,
            description,
            creationDate,
            expirationDate,
            noOfQuestions,
            durationMinutes,
            passPercentage,
            questionsRandomized,
            answersMust,
            enableNegativeMark,
            negativeMarkValue

        });
    }
    return (<>
        <div className="main-content  container-fluid  border-bottom border-3 border-dark    ">
            <div className="row">
                <div className="col-8 p-0">
                    <h2 className="p-2">Exam Details</h2>
                </div>
                <div className="col-4">
                    <div className="d-grid gap-2 d-md-flex justify-content-md-end m-2 me-4">

                        <button type="button"
                            className="btn btn-lg m-1  btn-primary"
                            data-bs-toggle="modal"
                            data-bs-target={`#exampleModal${examDetails.examId}`} >EDIT</button>
                    </div>
                </div>
            </div>
        </div>
        <div className='container  my-3'>
            <table className="table table-striped ">
                <tbody>
                    <tr>
                        <th>Exam Id</th>
                        <th>Exam Name</th>
                        <th>Description </th>
                        <th>Creation Date</th>
                        <th>Expiration Date</th>
                        <th>No. Of Questions</th>
                    </tr>

                    <tr>
                        <td>{examDetails.examId}</td>
                        <td>{examDetails.examName}</td>
                        <td>{examDetails.description}</td>
                        <td>{examDetails.creationDate}</td>
                        <td>{examDetails.expirationDate}</td>
                        <td>{examDetails.noOfQuestions}</td>

                    </tr>
                    <tr>
                        <th>Duration Minutes</th>
                        <th>Pass Percentage</th>
                        <th>Questions Randomized</th>
                        <th>Answers Must</th>
                        <th>Enable Negative Mark</th>
                        <th>Negative Mark Value</th>
                    </tr>
                    <tr>
                        <td>{examDetails.durationMinutes}</td>
                        <td>{examDetails.passPercentage}</td>
                        <td>{examDetails.questionsRandomized}</td>
                        <td>{examDetails.answersMust}</td>
                        <td>{examDetails.enableNegativeMark}</td>
                        <td>{examDetails.negativeMarkValue}</td>
                    </tr>
                </tbody>
            </table>
        </div>


        <div className="modal fade" id={`exampleModal${examDetails.examId}`} tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true"   >

            <div className="modal-dialog modal-dialog-centered">

                <div className="modal-content">

                    <div className="modal-header">
                        <h1 className="modal-title fs-5" id="exampleModalLabel" ><b> Edit Exam</b></h1>
                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form onSubmit={submitExamDetails}>
                        <div className="modal-body">


                            <div className="mb-3">
                                <label htmlFor="examId" className="form-label">Exam ID</label>
                                <input disabled="true" type="text" value={examId} onChange={(e) => (setExamId(e.target.value))} className="form-control" id="examId" placeholder="Enter a value" />
                            </div>

                            <div className="mb-3">
                                <label htmlFor="examName" className="form-label">Exam Name</label>
                                <input type="text" value={examName} onChange={(e) => (setExamName(e.target.value))} className="form-control" id="ExamName" placeholder="Enter the exam name" />
                            </div>

                            <div className="mb-3">
                                <label htmlFor="description" className="form-label">Description</label>
                                <input type="text" value={description} onChange={(e) => (setDescription(e.target.value))} className="form-control" id="description" placeholder="" />
                            </div>

                            <div className="mb-3">
                                <label htmlFor="creationDate" className="form-label">Creation Date</label>
                                <input type="date" value={creationDate} onChange={(e) => (setCreationDate(e.target.value))} className="form-control" id="creationDate" placeholder="" />
                            </div>

                            <div className="mb-3">
                                <label htmlFor="expirationDate" className="form-label">Expiration Date</label>
                                <input type="date" value={expirationDate} onChange={(e) => (setExpirationDate(e.target.value))} className="form-control" id="expirationDate" placeholder="" />
                            </div>

                            <div className="mb-3">
                                <label htmlFor="noOfQuestions" className="form-label">Total Questions</label>
                                <input type="text" value={noOfQuestions} onChange={(e) => (setNoOfQuestions(e.target.value))} className="form-control" id="noOfQuestions" placeholder="" />
                            </div>

                            <div className="mb-3">
                                <label htmlFor="durationMinutes" className="form-label">Duration</label>
                                <input type="text" value={durationMinutes} onChange={(e) => (setDurationMinutes(e.target.value))} className="form-control" id="durationMinutes" placeholder="Enter the Duration in minutes" />
                            </div>

                            <div className="mb-3">
                                <label htmlFor="passPercentage" className="form-label">Pass Percentage</label>
                                <input type="text" value={passPercentage} onChange={(e) => (setPassPercentage(e.target.value))} className="form-control" id="passPercentage" placeholder="Enter in %" />
                            </div>

                            <div className="mb-3">
                                <label htmlFor="questionsRandomized" className="form-label">Questions Randomized?</label>
                                <select id='questionsRandomized' value={questionsRandomized} onChange={(e) => (setQuestionsRandomized(e.target.value))} className='form-label' >
                                    <option value="Y">Yes</option>
                                    <option value="N">No</option>
                                </select>
                            </div>

                            <div className="mb-3">
                                <label htmlFor="answersMust" className="form-label">Must Attend All Answers?</label>
                                <select id='answersMust' value={answersMust} onChange={(e) => (setAnswersMust(e.target.value))} className='form-label' >
                                    <option value="Y">Yes</option>
                                    <option value="N">No</option>
                                </select>
                            </div>

                            <div className="mb-3">
                                <label htmlFor="enableNegativeMark" className="form-label">Enable Negative Mark?</label>
                                <select id='enableNegativeMark' value={enableNegativeMark} onChange={(e) => (setEnableNegativeMark(e.target.value))} className='form-label'>
                                    <option value="Y">Yes</option>
                                    <option value="N">No</option>
                                </select>
                            </div>

                            <div className="mb-3">
                                <label htmlFor="negativeMarkValue" className="form-label">Negative Mark Value</label>
                                <input type="text" value={negativeMarkValue} onChange={(e) => (setNegativeMarkValue(e.target.value))} className="form-control" id="negativeMarkValue" placeholder="" />
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" id="submit" className="btn btn-primary" data-bs-dismiss="modal">Save changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </>

    )
}

export default ViewExam
