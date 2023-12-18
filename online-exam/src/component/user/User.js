import React, { createContext, useEffect, useState } from "react";
import MainContent from "../common/MainContent";
import AccordinMaker from "../common/AccordinMaker";
import { useNavigate } from "react-router-dom";

export const UserContext = createContext(null);
function User() {
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
      "https://localhost:8443/onlineexam/control/findAllUsers"
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
    <UserContext.Provider value={{ users, setUsers }}>
      <MainContent text={text} to="add-user" back="/admin/user" />
      <AccordinMaker
        objects={users}
        id={"partyId"}
        name={"firstName"}
        onDelete={""}
        onEdit={onEdit}
      />
    </UserContext.Provider>
  );
}

export default User;
