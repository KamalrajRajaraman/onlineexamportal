import React, { useState } from 'react'
import { TiEdit } from 'react-icons/ti';
import { useLocation, useNavigate } from 'react-router-dom'
import { POST,CONTROL_SERVLET, DOMAIN_NAME, PORT_NO, PROTOCOL, WEB_APPLICATION, vanishAlert } from '../../common/CommonConstants';


const EditTopic = () => {
    const navigate = useNavigate();
    let state = useLocation().state;
    const [topicId, setTopicId] = useState(state.topicId);
    const [topicName, setTopicName] = useState(state.topicName);

    const handleSubmit = (e) => {
        e.preventDefault();
        updateTopic({ topicId, topicName });
    }

    function updateTopic(data) {
        fetch(PROTOCOL + DOMAIN_NAME + PORT_NO + WEB_APPLICATION + CONTROL_SERVLET + "createTopic", 
        {...POST,body: JSON.stringify(data)}).then((res) => {
            if (response.status === 401) {
                navigate("/login");
            }
            return res.json()
        }).then((data) => {
            if (data.result === "success") {
                vanishAlert("Good job!", "Exam-Topic details updated successfully!","success",2000,false);
            }
            else if (data.result === "error"){
                vanishAlert("Error!",data._ERROR_MESSAGE_,"error",2000,false);
                setTopicName(state.topicName)
            }
        }).catch(e=>{
            console.log(e.message);
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
