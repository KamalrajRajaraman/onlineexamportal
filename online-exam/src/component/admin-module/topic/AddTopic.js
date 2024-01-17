import React, { useEffect, useState } from "react";
import { useTopicContext } from "./TopicData";
import FormInput from "../../common/FormInput";
import Swal from "sweetalert2";
import { CONTROL_SERVLET, DOMAIN_NAME,  PORT_NO,  PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstant";

const Addtopic = () => {
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
    const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+ "createTopic",{
      method:"POST",
      headers:{
        'Content-type':"application/json"
      },
       credentials: 'include',
      body:JSON.stringify(topicDetails)
    });
    const data =  await res.json();
   console.log(data);
    if(data["result"]==="success"){
      Swal.fire({
        title: "Good job!",
        text: "Topic created successfully!",
        icon: "success",
        timer:2000,
        showConfirmButton:false
      });
      setTopicValues(initialValues)
      const {topic} =data;
      setTopics([...topics,topic]);
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
