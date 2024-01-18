import React, { useState } from 'react'
import { TiEdit } from 'react-icons/ti';
import { useLocation } from 'react-router-dom'
import Swal from "sweetalert2";
import { CONTROL_SERVLET, DOMAIN_NAME, PORT_NO, PROTOCOL, WEB_APPLICATION } from '../../common/CommonConstant';


const EditTopic = () => {

    let state = useLocation().state;
    const [topicId, setTopicId] = useState(state.topicId);
    const [topicName, setTopicName] = useState(state.topicName);

    const handleSubmit = (e) => {
        e.preventDefault();
        updateTopic({ topicId, topicName });

    }

    function updateTopic(data) {
        fetch(PROTOCOL + DOMAIN_NAME + PORT_NO + WEB_APPLICATION + CONTROL_SERVLET + "createTopic", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: 'include',
            body: JSON.stringify(data)
        }).then((res) => {
            return res.json()
        }).then((data) => {
            if (data.result === "success") {
                Swal.fire({
                    position: "top",
                    title: "Good job!",
                    text: "Exam-Topic details updated successfully!",
                    timer: 2000,
                    showConfirmButton: false,
                });
            }

            else {
                Swal.fire({
                    title: "Error!",
                    text: data._ERROR_MESSAGE_,
                    timer: 2000,
                    showConfirmButton: false,
                });
                setTopicName(state.topicName)
            }
        })
    }
    return (
        <div>
            <>
                <div className="main-content  container-fluid  border-bottom border-3 border-dark    ">
                    <div className="row">
                        <div className="col-8 p-0">
                            <h2 className="p-2">Topic Details</h2>
                        </div>
                    </div>
                </div>
                <div className='container  my-3'>
                    <table className="table table-striped ">
                        <tbody>
                            <tr>
                                <th>Topic Id</th>
                                <th>Topic Name</th>
                                <th>Edit</th>
                            </tr>

                            <tr>
                                <td>{state.topicId}</td>
                                <td>{topicName}</td>
                                <td><TiEdit type="button"
                                    data-bs-toggle="modal"
                                    data-bs-target={`#exampleModal${state.topicId}`} /></td>
                            </tr>
                        </tbody>
                    </table>
                </div>


                <div className="modal fade" id={`exampleModal${state.topicId}`} tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true"   >

                    <div className="modal-dialog modal-dialog-centered">

                        <div className="modal-content">

                            <div className="modal-header">
                                <h1 className="modal-title fs-5" id="exampleModalLabel" ><b> Edit Exam</b></h1>
                                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <form onSubmit={handleSubmit}>
                                <div className="modal-body">


                                    <div className="mb-3">
                                        <label htmlFor="topicId" className="form-label">Exam ID</label>
                                        <input disabled="true" name='topicId' type="text" value={topicId} onChange={(e) => (setTopicId(e.target.value))} className="form-control" id="topicId" />
                                    </div>

                                    <div className="mb-3">
                                        <label htmlFor="topicName" className="form-label">Exam Name</label>
                                        <input type="text" name="topicName" value={topicName} onChange={(e) => (setTopicName(e.target.value))} className="form-control" id="topicName" />
                                    </div>

                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                    <button type="submit" id="submit" className="btn btn-primary" data-bs-dismiss="modal">Save changes</button>
                                </div>
                            </form >
                        </div>
                    </div>
                </div>
            </>
        </div>
    )
}

export default EditTopic
