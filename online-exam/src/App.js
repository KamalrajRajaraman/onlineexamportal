import { Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./component/auth/Auth";
import Header from "./component/common/Header";
import Login from "./component/auth/Login";
import Admin from "./component/admin-module/admin-dashboard/Admin";
import Exam from "./component/admin-module/exam/Exam";
import AddExam from "./component/admin-module/exam/AddExam";
import EditExam from "./component/admin-module/edit-exam/EditExam";
import AddTopicToExam from "./component/admin-module/edit-exam/AddTopicToExam";
import Topics from "./component/admin-module/topic/Topics";
import Addtopic from "./component/admin-module/topic/AddTopic";
import UserRegister from "./component/admin-module/user/UserRegister";
import Questions from "./component/admin-module/question/Questions";
import AddQuestions from "./component/admin-module/question/AddQuestions";
import User from "./component/admin-module/user/User";
import NoMatch from "./component/common/NoMatch";
import EditUser from "./component/admin-module/user/EditUser";
import AddExamToUser from "./component/admin-module/user/AddExamToUser";


import ListUser from "./component/admin-module/user/ListUser";
import UserDashboard from "./component/userModule/user-dashboard/UserDashboard";
import ExamsForUser from "./component/userModule/exams/ExamsForUser";
import ExamPage from "./component/userModule/exam-page/ExamPage";

import ShowExam from "./component/admin-module/user/ShowExam";
import RequiredAuth from "./component/common/RequiredAuth";
import Error from "./component/common/Error";
import ResultPage from "./component/userModule/exam-page/ResultPage";



function App() {
  return (
    <AuthProvider>
      <Header />
      <Routes>
      <Route index element={<Login />} />
        <Route path="login" element={<Login />} />
        <Route path="error/:errorMessage" element={<Error />} />

        <Route path="admin" element={<RequiredAuth><Admin /></RequiredAuth>}>
        <Route index element={<Navigate to="exam" />}/>
          <Route path="exam" element={<Exam />}>
            <Route path="add-exam" element={<AddExam />} />
          </Route>
          <Route path="exam/edit/examId/:examId" element={<EditExam />}>
            <Route path="add-topic-to-exam" element={<AddTopicToExam />} />          
          </Route>
          <Route path="topic" element={<Topics/>}>
            <Route path="add-topic" element={<Addtopic />} />
          </Route>
          <Route path="questions" element={<Questions />}>
            <Route path="add-questions" element={<AddQuestions />} />
          </Route>
          <Route path="user" element={<User />}>
            <Route index element={<ListUser />} />
            <Route path="add-user" element={<UserRegister />} />
            <Route path="list-user" element={<ListUser />} />
          </Route>
          <Route
            path="user/list-user/edit/userId/:partyId"
            element={<EditUser />}
          > <Route index element={<ShowExam />} />
            <Route path="add-exam-to-user" element={<AddExamToUser />} />
            <Route path="view-all-exam" element={<ShowExam />} />
          </Route>
          <Route path="user/edit/userId/:partyId" element={<EditUser />}>
            <Route index element={<ShowExam />} />
            <Route path="add-exam-to-user" element={<AddExamToUser />} />
            <Route path="view-all-exam" element={<ShowExam />} />
          </Route>
        </Route>  
        <Route path="user-dashboard" element={<RequiredAuth><UserDashboard /></RequiredAuth>}>
          <Route index element={<Navigate to="exams" />}/>
          <Route path="exams" element={<ExamsForUser />} />
          <Route path="result-page" element={<ResultPage />} />
        </Route>
      
        <Route path="user-dashboard/exams/exam-page/:examId" element={<ExamPage />} />
        <Route path="exam-page" element={<ExamPage />} />
      
        <Route path="*" element={<NoMatch />} />

      </Routes>
    </AuthProvider>
    
   
  );
}

export default App;
