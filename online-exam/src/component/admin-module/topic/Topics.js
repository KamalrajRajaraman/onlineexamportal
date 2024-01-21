import React, { useEffect,useState } from "react";
import MainContent from "../../common/MainContent";
import AccordinMaker from "../../common/AccordinMaker";
import { useTopicContext } from "./TopicData";
import Alert from "../../common/Alert";
import FormInput from "../../common/FormInput";
import { useNavigate } from "react-router-dom";

const Topics = () => {
  const {getTopics, topics, setTopics, fetchTopic, onDelete, alert, setAlert,onEdit } = useTopicContext();
  const navigate = useNavigate();
  const initialValue = {
    topicId: "",
    topicName: ""
  }
  const [refresh,setRefresh]= useState(true);

  const [formValues, setFormValues] = useState(initialValue);

  useEffect(() => {
    getTopics();
    return () => {
      setTopics([]);
    };
  }, [refresh]);

 
  const text = {
    header: "Topic",
    btnText: "Topic"
  }
  const modalEdit = async (id, object) => {
    setFormValues({
      topicId: id,
      topicName: object.topicName

    });
    document.getElementById("buttonId").click();
  };

  const showDetails = (id, topic)=>{
    navigate(`edit-topic/${id}`,{state:topic})
  }

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormValues({ ...formValues, [name]: value });
  };
  
  const handleSubmit = async(e) => {
    e.preventDefault();
    await onEdit(formValues);
    setRefresh(!refresh);
  };

  return (
    <div className="position-relative">
      {alert && <Alert color={"alert-success"} close={setAlert} message={"Aww yeah, you successfully Topic Exam"} />}
      <MainContent text={text} to="add-topic" back="/admin/topic" />
      {topics ? <AccordinMaker objects={topics} id={"topicId"} name={"topicName"} onDelete={onDelete} onEdit={modalEdit} showDetails={showDetails} />:"No Topics Found.Please Add Exams"}
      <button
        type="button"
        class="btn btn-primary d-none"
        data-bs-toggle="modal"
        data-bs-target="#exampleModal"
        id="buttonId"
      />
      <div
        class="modal fade"
        id="exampleModal"
        tabindex="-1"
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
      >
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="exampleModalLabel">
                Edit Topic
              </h5>
              <button
                type="button"
                id="modal-close"
                class="btn-close"
                data-bs-dismiss="modal"
                aria-label="Close"
              ></button>
            </div>
            <div class="modal-body">
              <form onSubmit={handleSubmit}>
                <FormInput
                  id="topicId"
                  name="topicId"
                  type={"text"}
                  text={"Topic ID"}
                  value={formValues.topicId}
                />

                <FormInput
                  id="topicName"
                  name="topicName"
                  type={"text"}
                  text={"Topic Id"}
                  value={formValues.topicName}
                  onChange={handleChange}
                />

                <div className="col-2 mx-auto  d-grid gap-2 ">
                  <button className="btn btn-primary" type="submit" data-bs-dismiss="modal">
                    Submit
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>

    </div>
  );
};

export default Topics;
