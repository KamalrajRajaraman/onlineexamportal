import { Routes, Route } from "react-router-dom";
import Login from "./component/Login";
import Header from "./component/Header";
import { AuthProvider } from "./auth";
import NoMatch from "./component/NoMatch";
import Admin from "./component/Admin";
import Exam from "./component/Exam";
import Topics from "./component/Topics";
import Questions from "./component/Questions";
import RequiredAuth from "./component/RequiredAuth";
import AddExam from "./component/AddExam";
import Addtopic from "./component/Addtopic";
import AddQuestions from "./component/AddQuestions";
import UserRegister from "./component/UserRegister";
import EditExam from "./component/EditExam";
import AddTopicToExam from "./component/edit-exam/add-to-exam/AddTopicToExam";


function App() {
  return (
    <AuthProvider>  
      <Header />
      <Routes>
      <Route path="login" element={<Topics />}/>
        <Route path="login" element={<Login />} />
        <Route path="admin" element={<Admin />}>
          <Route path="exam" element={<Exam />}>
            <Route path="add-exam" element={<AddExam />} />
          
          </Route>
          <Route path="exam/edit/examId/:examId" element={<EditExam />}>
          <Route path="Topic to Exam" element={<AddTopicToExam />} />
          </Route>
          <Route path="topic" element={<Topics />}>
            <Route path="add-topic" element={<Addtopic />} />
          </Route>
          <Route path="questions" element={<Questions />}>
            <Route path="add-questions" element={<AddQuestions />} />
          </Route>
          <Route path="user" element={<UserRegister />} />
        </Route>
        <Route path="*" element={<NoMatch />} />
      </Routes>
    </AuthProvider>
  );
}

export default App;
