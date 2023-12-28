import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import AccordinMaker from '../../common/AccordinMaker';

const ListUser = () => {
    const [users, setUsers] = useState([]);
    const navigate = useNavigate();
    useEffect(() => {
        fetchUsers();
        return () => {
          setUsers([]);
        };
      }, []);
    
      const fetchUsers = async () => {
        const res = await fetch(
          "https://localhost:8443/onlineexam/control/findAllUsers",{ credentials: 'include'}
        );
    
        const data = await res.json();
    
        const { userList } = data;
    
        setUsers(userList);
      };
    
      const onEdit = (id) => {
        navigate(`edit/userId/${id}`);
      };

      const text = {
        header: "Users",
        btnText: "User",
      };

  return (
    <>  <AccordinMaker
    objects={users}
    id={"partyId"}
    name={"firstName"}
    onDelete={""}
    onEdit={onEdit}
  /></>
  )
}

export default ListUser