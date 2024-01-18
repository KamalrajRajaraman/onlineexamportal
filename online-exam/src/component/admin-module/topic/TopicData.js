import { createContext, useContext, useState } from "react";
import { CONTROL_SERVLET, DOMAIN_NAME,  PORT_NO, PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstant";
import Swal from "sweetalert2";
const TopicContext = createContext(null);

export const TopicProvider =({children})=>{

    const [topics, setTopics] = useState([]);
    const [alert, setAlert] = useState(false);

      const fetchTopic = async () => {
        const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+
          "findAllTopics",{credentials: 'include',}
        );
        const data = await res.json();
      
        const { topicList } = data;
        return topicList;
      };

      const onDelete=async(id)=>{
        const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+`/deleteTopic?topicId=${id}`,{method:'DELETE', credentials: 'include'})
        const data = await res.json();
        const {result} = data
        if(result==="success"){
          setTopics(topics.filter(topic=>topic.topicId!==id));
        }
      }

      const onEdit=async(object)=>{
        console.log(object)
        const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+"createTopic",{method: "PUT",
        headers: {
          'Content-type':"application/json"
        },
         credentials: 'include',
        body: JSON.stringify(object)})
        const data = await res.json();
        const {result} = data
        if(result==="success"){
          fetchTopic();
          Swal.fire({
            position: "top",
            title: "Good job!",
            text: "Exam-Topic details updated successfully!",
            timer: 2000,
            showConfirmButton: false,
        });
        }
      }

    return <TopicContext.Provider value={{topics,setTopics,fetchTopic,onDelete,alert,setAlert,onEdit}}>
        {children}
    </TopicContext.Provider>

}

export const useTopicContext =()=>{
    return useContext(TopicContext);
}
