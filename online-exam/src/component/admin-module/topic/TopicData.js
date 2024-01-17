import { createContext, useContext, useState } from "react";
import { CONTROL_SERVLET, DOMAIN_NAME,  PORT_NO, PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstant";
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

    return <TopicContext.Provider value={{topics,setTopics,fetchTopic,onDelete,alert,setAlert}}>
        {children}
    </TopicContext.Provider>

}

export const useTopicContext =()=>{
    return useContext(TopicContext);
}
