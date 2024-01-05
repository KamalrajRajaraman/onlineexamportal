import React from "react";
import SideBar from "../sidebar/SideBar";
import { Outlet } from "react-router-dom";
import { ExamProvider } from "../exam/ExamData";
import { TopicProvider } from "../topic/TopicData";
import { QuestionProvider } from "../question/QuestionData";

const Admin = () => {
  return (
    <ExamProvider>
      <TopicProvider>
        <QuestionProvider>
          <div className="container-fluid">
            <div className="row">
              <div className="col-2 p-0">
                <SideBar />
              </div>
              <div className="col-10">
                <Outlet />
              </div>
            </div>
          </div>
        </QuestionProvider>
      </TopicProvider>
    </ExamProvider>
  );
};

export default Admin;
