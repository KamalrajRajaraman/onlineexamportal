import { Routes, Route, Link, Navigate } from "react-router-dom";
import { AuthProvider } from "./auth";
import Header from "./component/Header";
import Login from "./component/Login";
import Admin from "./component/Admin";
import Exam from "./component/exam/Exam";
import AddExam from "./component/exam/AddExam";
import EditExam from "./component/edit-exam/EditExam";
import AddTopicToExam from "./component/edit-exam/AddTopicToExam";
import Topics from "./component/topic/Topics";
import Addtopic from "./component/topic/Addtopic";
import UserRegister from "./component/user/UserRegister";
import Questions from "./component/question/Questions";
import AddQuestions from "./component/question/AddQuestions";
import User from "./component/user/User";
import NoMatch from "./component/NoMatch";
import EditUser from "./component/user/EditUser";
import AddExamToUser from "./component/user/AddExamToUser";
import ExamTopicTable from "./component/edit-exam/ExamTopicTable";
import QuestionTable from "./component/edit-exam/QuestionTable";
import ListUser from "./component/user/ListUser";
import UserDashboard from "./component/userModule/UserDashboard";
import ExamsForUser from "./component/userModule/ExamsForUser";
import ExamPage from "./component/exam-attempt-module/ExamPage";
import Introduction from "./component/userModule/Introduction";
import ShowExam from "./component/user/ShowExam";
import RequiredAuth from "./component/common/RequiredAuth";
import Error from "./component/common/Error";
import Footer from "./component/Footer";

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
            {/* <Route index element={<ExamTopicTable />} />
            <Route path="view-all" element={<ExamTopicTable />} /> */}
            <Route path="add-topic-to-exam" element={<AddTopicToExam />} />
            <Route path="questions" element={<QuestionTable />} />
          </Route>
          <Route path="topic" element={<Topics />}>
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
       
        </Route>
        <Route path="user-dashboard/exams/exam-page/:examId" element={<ExamPage />} />
        <Route path="exam-page" element={<ExamPage />} />
        <Route path="introduction" element={<Introduction />} />
        <Route path="*" element={<NoMatch />} />
      </Routes>
     
    </AuthProvider>
   
  );
}

export default App;
