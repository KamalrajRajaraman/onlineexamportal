import { createContext, useContext, useState } from "react";

const TopicContext = createContext(null);

export const TopicProvider =({children})=>{

    const [topics, setTopics] = useState([]);
    const [alert, setAlert] = useState(false);

      const fetchTopic = async () => {
        const res = await fetch(
          "https://localhost:8443/onlineexam/control/findAllTopics",{credentials: 'include',}
        );
        const data = await res.json();
      
        const { topicList } = data;
        return topicList;
      };

      const onDelete=async(id)=>{
        const res = await fetch(`https://localhost:8443/onlineexam/control/deleteTopic?topicId=${id}`,{method:'DELETE', credentials: 'include'})
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
