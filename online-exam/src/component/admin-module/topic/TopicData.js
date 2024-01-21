import { createContext, useContext, useState } from "react";
import {PUT, CONTROL_SERVLET, DELETE, DOMAIN_NAME,  GET,  PORT_NO, PROTOCOL, WEB_APPLICATION, vanishAlert ,alert, swalFireAlert} from "../../common/CommonConstants";

import { useNavigate } from "react-router-dom";
const TopicContext = createContext(null);

export const TopicProvider =({children})=>{
  const navigate = useNavigate();
    const [topics, setTopics] = useState([]);
    const [alert, setAlert] = useState(false);

    const getTopics = async () => {
      const topicList = await fetchTopic();
      setTopics(topicList);
    };

      const fetchTopic = async () => {
        const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+
          "findAllTopics",GET
        );
        if (res.status === 401) {
          navigate("/login");
        }
        const data = await res.json();
        if (data.result==="error"){
          swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
        }
        const { topicList } = data;
        return topicList;
      };

      //Deletes Topic and fetch All topics after deleting
      const onDelete=async(id)=>{
        const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+`/deleteTopic?topicId=${id}`,DELETE)
        if (res.status === 401) {
          navigate("/login");
        }
        const data = await res.json();
        const {result} = data
        if(result==="success"){
          vanishAlert("Good job!","Topic Deleted successfully!","success",2000,false);
          getTopics();
        }else if (result==="error"){
          swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
        }
      }

      //On Editing topic 
      const onEdit=async(object)=>{
        console.log(object)
        const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+"createTopic",
        {...PUT,body: JSON.stringify(object)});
        if (res.status === 401) {
          navigate("/login");
        }
        const data = await res.json();
        const {result} = data
        if(result==="success"){
          fetchTopic();
          vanishAlert("Good job!","Exam-Topic details updated successfully!","success",2000,false);
        }else if (result==="error"){
          swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
        }
      }

    return <TopicContext.Provider value={{getTopics,topics,setTopics,fetchTopic,onDelete,alert,setAlert,onEdit}}>
        {children}
    </TopicContext.Provider>

}

export const useTopicContext =()=>{
    return useContext(TopicContext);
}
