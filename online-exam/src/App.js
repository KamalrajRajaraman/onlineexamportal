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
import { DataProvider } from "./component/data";
import Footer from "./component/Footer";

function App() {
  return (
    <AuthProvider>
      <DataProvider>
      <Header />
      <Routes>
        <Route path="login" element={<Login />} />
        <Route path="admin" element={<Admin />}>
          <Route path="exam" element={<Exam />}>
            <Route path="add-exam" element={<AddExam />} />
          </Route>
          <Route path="topic" element={<Topics />}>
            <Route path="add-topic" element={<Addtopic />} />
          </Route>
          <Route path="questions" element={<Questions />}>
            <Route path="add-questions" element={<AddExam />} />
          </Route>
        </Route>
        <Route path="*" element={<NoMatch />} />
      </Routes>
      <Footer/>
      </DataProvider>
    </AuthProvider>
  );
}

export default App;
