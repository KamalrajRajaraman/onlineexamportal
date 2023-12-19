import { Routes, Route } from "react-router-dom";
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

function App() {
  return (
    <AuthProvider>
      <Header />
      <Routes>
        <Route path="login" element={<Login />} />
        <Route path="admin" element={<Admin />}>
          <Route path="exam" element={<Exam />}>
            <Route path="add-exam" element={<AddExam />} />
          </Route>
          <Route path="exam/edit/examId/:examId" element={<EditExam />}>
          <Route index element={<ExamTopicTable/>} />
            <Route path="view-all" element={<ExamTopicTable/>} />
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
            <Route path="add-user" element={<UserRegister />} />
          </Route>
          <Route path="user/edit/userId/:partyId" element={<EditUser />}>
            <Route path="add-exam-to-user" element={<AddExamToUser />} />
          </Route>
        </Route>
        <Route path="*" element={<NoMatch />} />
      </Routes>
    </AuthProvider>
  );
}

export default App;
