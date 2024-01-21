import React, { useEffect, useState } from "react";
import { useTopicContext } from "./TopicData";
import FormInput from "../../common/FormInput";
import {POST, CONTROL_SERVLET, DOMAIN_NAME,  PORT_NO,  PROTOCOL, WEB_APPLICATION, vanishAlert, swalFireAlert } from "../../common/CommonConstants";
import { useNavigate } from "react-router-dom";

const Addtopic = () => {
  const navigate = useNavigate();
  const initialValues = { topicName:''};
  const [topicValues, setTopicValues] = useState(initialValues);
  const [formErrors, setFormErrors] =  useState({})
  const [isSubmit, setIsSubmit] = useState(false);
  const{topics,setTopics,setAlert} =useTopicContext();
  
 const handleChange = (e)=>{
  const {name, value} = e.target;
  setTopicValues({...topicValues, [name]:value});
 }

const handleSubmit = (e)=>{
e.preventDefault();
setFormErrors(validate(topicValues));

setIsSubmit(true);
}

useEffect(()=>{
  if(Object.keys(formErrors).length === 0 && isSubmit ){
    onCreateTopic(topicValues);
  }
},[formErrors]

)

const validate = (values)=>{
  const errors= {};
  if(!values.topicName){
    errors.topicName = "Topic name is required!";
  }
  return errors;
}

  const onCreateTopic =async (topicDetails)=>{
    console.log(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+ "createTopic");
    const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+ "createTopic",
    {...POST,body:JSON.stringify(topicDetails)});

    if (res.status === 401) {
      navigate("/login");
    }
    const data =  await res.json();
    if(data["result"]==="success"){
      vanishAlert( "Good job!","Topic created successfully!","success",2000,false);
      setTopicValues(initialValues)
      const {topic} =data;
      setTopics([...topics,topic]);
    } 
     else if (data.result==="error"){
      swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
    }
    
  }

 

  return (
    <div className="container-fluid border my-2 fw-bold ">
       <form onSubmit={handleSubmit}>
      
      <div className="col-4 mx-auto">
        <FormInput
          name={"topicName"}
          text="TopicName"
          value={topicValues.topicName}
          error={formErrors.topicName}
          onChange={handleChange}
          type={"text"}
          placeholder={""}
        />
      </div>

      <div className="d-grid gap-2 col-2 mx-auto mb-2 mb-2">
        <button className="btn btn-primary" type="submit">
          Submit
        </button>
      </div>
      </form>
    </div>
  );
};

export default Addtopic;
